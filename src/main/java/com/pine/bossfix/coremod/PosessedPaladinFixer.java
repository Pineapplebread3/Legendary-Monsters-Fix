package com.pine.bossfix.coremod;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class PosessedPaladinFixer {

    private static final String ADD_EFFECT_DESC = "(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z";

    public static boolean fix(ClassNode classNode) {
        System.out.println("[BossFix] Found PosessedPaladinEntity!");

        for (MethodNode method : classNode.methods) {
            if (isAddEffectMethod(method)) {
                System.out.println("[BossFix] Modifying PosessedPaladinEntity.addEffect...");
                modifyAddEffectMethod(method);
                return true;
            }
        }
        return false;
    }

    private static boolean isAddEffectMethod(MethodNode method) {
        boolean isDev = method.name.equals("addEffect") && ADD_EFFECT_DESC.equals(method.desc);
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
        System.out.println("[BossFix] PosessedPaladinEntity.addEffect modified!");
    }
}