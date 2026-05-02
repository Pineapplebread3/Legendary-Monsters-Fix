package com.pine.bossfix.coremod;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class AncientGuardianFixer {

    private static final String ADD_EFFECT_DESC = "(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z";

    public static boolean fix(ClassNode classNode) {
        boolean modified = false;
        System.out.println("[BossFix] Found Ancient_GuardianEntity!");

        // 打印所有方法名，确认混淆名
        for (MethodNode method : classNode.methods) {
            System.out.println("[BossFix] Ancient_GuardianEntity method: " + method.name + " -> " + method.desc);
        }

        // 1. 修复 addEffect 方法（药水免疫）- 同时匹配开发名和混淆名
        for (MethodNode method : classNode.methods) {
            if (isAddEffectMethod(method)) {
                System.out.println("[BossFix] Modifying Ancient_GuardianEntity.addEffect...");
                modifyAddEffectMethod(method);
                modified = true;
                break;
            }
        }

        return modified;
    }

    private static boolean isAddEffectMethod(MethodNode method) {
        // 开发环境：方法名 addEffect
        boolean isDev = method.name.equals("addEffect") && ADD_EFFECT_DESC.equals(method.desc);
        // 生产环境：方法名 m_147207_
        boolean isProd = method.name.equals("m_147207_") && ADD_EFFECT_DESC.equals(method.desc);
        return isDev || isProd;
    }

    private static void modifyAddEffectMethod(MethodNode method) {
        method.instructions.clear();

        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
        method.instructions.add(new MethodInsnNode(
                Opcodes.INVOKESPECIAL,
                "net/minecraft/world/entity/LivingEntity",
                "m_147207_",
                ADD_EFFECT_DESC,
                false
        ));
        method.instructions.add(new InsnNode(Opcodes.IRETURN));

        System.out.println("[BossFix] Ancient_GuardianEntity.addEffect modified!");
    }
}