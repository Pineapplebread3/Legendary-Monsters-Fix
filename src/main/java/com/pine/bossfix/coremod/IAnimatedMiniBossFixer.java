package com.pine.bossfix.coremod;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class IAnimatedMiniBossFixer {

    private static final String HURT_DESC = "(Lnet/minecraft/world/damagesource/DamageSource;F)Z";

    public static boolean fix(ClassNode classNode) {
        boolean modified = false;
        System.out.println("[BossFix] Found IAnimatedMiniBoss!");

        // 打印所有方法名，确认混淆名
        for (MethodNode method : classNode.methods) {
            System.out.println("[BossFix] IAnimatedMiniBoss method: " + method.name + " -> " + method.desc);
        }

        // 修复 hurt 方法 - 同时匹配开发名和混淆名
        for (MethodNode method : classNode.methods) {
            if (isHurtMethod(method)) {
                System.out.println("[BossFix] Replacing IAnimatedMiniBoss.hurt method...");
                replaceHurtMethod(method);
                modified = true;
                break;
            }
        }

        return modified;
    }

    private static boolean isHurtMethod(MethodNode method) {
        // 开发环境：方法名 hurt
        boolean isDev = method.name.equals("hurt") && HURT_DESC.equals(method.desc);
        // 生产环境：方法名 m_6469_
        boolean isProd = method.name.equals("m_6469_") && HURT_DESC.equals(method.desc);
        return isDev || isProd;
    }

    private static void replaceHurtMethod(MethodNode method) {
        method.instructions.clear();

        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        method.instructions.add(new VarInsnNode(Opcodes.FLOAD, 2));
        method.instructions.add(new MethodInsnNode(
                Opcodes.INVOKESPECIAL,
                "net/minecraft/world/entity/LivingEntity",
                "m_6469_",  // hurt 混淆名
                HURT_DESC,
                false
        ));
        method.instructions.add(new InsnNode(Opcodes.IRETURN));

        System.out.println("[BossFix] IAnimatedMiniBoss.hurt now directly calls LivingEntity.m_6469_!");
    }
}