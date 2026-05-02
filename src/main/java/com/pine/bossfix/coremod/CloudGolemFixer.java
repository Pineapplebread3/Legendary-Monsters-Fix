package com.pine.bossfix.coremod;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class CloudGolemFixer {

    private static final String ADD_EFFECT_DESC = "(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z";

    public static boolean fix(ClassNode classNode) {
        System.out.println("[BossFix] Found Cloud_GolemEntity!");
        boolean modified = false;

        // 1. 修改 addEffect 方法
        for (MethodNode method : classNode.methods) {
            if (isAddEffectMethod(method)) {
                System.out.println("[BossFix] Modifying Cloud_GolemEntity.addEffect...");
                modifyAddEffectMethod(method);
                modified = true;
                break;
            }
        }

        // 2. 移除 tick 方法中的 getActiveEffects().clear()
        for (MethodNode method : classNode.methods) {
            if (isTickMethod(method)) {
                System.out.println("[BossFix] Removing getActiveEffects().clear() from tick...");
                if (removeClearEffectCall(method)) {
                    modified = true;
                }
                break;
            }
        }

        // 3. 修改 damageCap 方法
        for (MethodNode method : classNode.methods) {
            if (method.name.equals("damageCap") && method.desc.equals("()D")) {
                System.out.println("[BossFix] Removing damage cap in Cloud_GolemEntity...");
                method.instructions.clear();
                method.instructions.add(new LdcInsnNode(999999.0));
                method.instructions.add(new InsnNode(Opcodes.DRETURN));
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

    private static boolean isTickMethod(MethodNode method) {
        return method.name.equals("tick") && method.desc.equals("()V");
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
        System.out.println("[BossFix] Cloud_GolemEntity.addEffect modified!");
    }

    private static boolean removeClearEffectCall(MethodNode method) {
        InsnList instructions = method.instructions;
        boolean removed = false;

        for (int i = 0; i < instructions.size(); i++) {
            AbstractInsnNode insn = instructions.get(i);
            if (insn instanceof MethodInsnNode mInsn) {
                if (mInsn.name.equals("getActiveEffects") &&
                        mInsn.owner.equals("net/minecraft/world/entity/LivingEntity")) {
                    if (i + 1 < instructions.size()) {
                        AbstractInsnNode nextInsn = instructions.get(i + 1);
                        if (nextInsn instanceof MethodInsnNode clearInsn &&
                                clearInsn.name.equals("clear")) {
                            instructions.remove(insn);
                            instructions.remove(clearInsn);
                            removed = true;
                            System.out.println("[BossFix] Removed getActiveEffects().clear()!");
                            i--;
                        }
                    }
                }
            }
        }
        return removed;
    }
}