package com.pine.bossfix.coremod;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class ShulkerMimicFixer {

    private static final String ADD_EFFECT_DESC = "(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z";
    private static final String HURT_DESC = "(Lnet/minecraft/world/damagesource/DamageSource;F)Z";

    public static boolean fix(ClassNode classNode) {
        boolean modified = false;
        System.out.println("[BossFix] Found Shulker_MimicEntity!");

        // 1. 修复 addEffect 方法
        for (MethodNode method : classNode.methods) {
            if (isAddEffectMethod(method)) {
                System.out.println("[BossFix] Modifying Shulker_MimicEntity.addEffect...");
                modifyAddEffectMethod(method);
                modified = true;
                break;
            }
        }

        // 2. 替换 hurt 方法
        for (MethodNode method : classNode.methods) {
            if (isHurtMethod(method)) {
                System.out.println("[BossFix] Replacing Shulker_MimicEntity.hurt...");
                replaceHurtMethod(method);
                modified = true;
                break;
            }
        }

        return modified;
    }

    private static boolean isAddEffectMethod(MethodNode method) {
        boolean isDev = method.name.equals("addEffect") && ADD_EFFECT_DESC.equals(method.desc);
        boolean isProd = method.name.equals("m_147207_") && ADD_EFFECT_DESC.equals(method.desc);
        return isDev || isProd;
    }

    private static boolean isHurtMethod(MethodNode method) {
        boolean isDev = method.name.equals("hurt") && HURT_DESC.equals(method.desc);
        boolean isProd = method.name.equals("m_6469_") && HURT_DESC.equals(method.desc);
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
        System.out.println("[BossFix] Shulker_MimicEntity.addEffect modified!");
    }

    private static void replaceHurtMethod(MethodNode method) {
        method.instructions.clear();
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        method.instructions.add(new VarInsnNode(Opcodes.FLOAD, 2));
        method.instructions.add(new MethodInsnNode(
                Opcodes.INVOKESPECIAL,
                "net/minecraft/world/entity/LivingEntity",
                "m_6469_",
                HURT_DESC,
                false
        ));
        method.instructions.add(new InsnNode(Opcodes.IRETURN));
        System.out.println("[BossFix] Shulker_MimicEntity.hurt modified!");
    }
}