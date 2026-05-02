package com.pine.bossfix.coremod;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class IAnimatedBossFixer {

    private static final String ADD_EFFECT_DESC = "(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z";

    public static boolean fix(ClassNode classNode) {
        boolean modified = false;
        System.out.println("[BossFix] Found IAnimatedBoss!");

        // 打印所有方法名，确认混淆名
        for (MethodNode method : classNode.methods) {
            System.out.println("[BossFix] IAnimatedBoss method: " + method.name + " -> " + method.desc);
        }

        // 1. 修复 addEffect 方法（药水免疫）- 同时匹配开发名和混淆名
        for (MethodNode method : classNode.methods) {
            if (isAddEffectMethod(method)) {
                System.out.println("[BossFix] Modifying IAnimatedBoss.addEffect...");
                modifyAddEffectMethod(method);
                modified = true;
                break;
            }
        }

        // 2. 修改 damageCap 方法
        for (MethodNode method : classNode.methods) {
            if (method.name.equals("damageCap") && method.desc.equals("()D")) {
                System.out.println("[BossFix] Removing damage cap in IAnimatedBoss...");
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
        // 开发环境：方法名 addEffect
        boolean isDev = method.name.equals("addEffect") && ADD_EFFECT_DESC.equals(method.desc);
        // 生产环境：方法名 m_147207_（Recaf 里看到的）
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
                "m_147207_",  // ← 直接写混淆名，不要用 "addEffect"
                "(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
                false
        ));
        method.instructions.add(new InsnNode(Opcodes.IRETURN));
    }
}


//package com.pine.bossfix.coremod;



//
//import org.objectweb.asm.Opcodes;
//import org.objectweb.asm.tree.*;
//
//public class IAnimatedBossFixer {
//
//    public static boolean fix(ClassNode classNode) {
//        boolean modified = false;
//        System.out.println("[BossFix] Found IAnimatedBoss!");
//
//        // 1. 修复 addEffect 方法（药水免疫）
//        for (MethodNode method : classNode.methods) {
//            if (isAddEffectMethod(method)) {
//                System.out.println("[BossFix] Modifying IAnimatedBoss.addEffect...");
//                modifyAddEffectMethod(method);
//                modified = true;
//                break;
//            }
//        }
//
//        // 2. 修改 damageCap 方法，返回很大的值（移除限伤）
//        for (MethodNode method : classNode.methods) {
//            if (method.name.equals("damageCap") && method.desc.equals("()D")) {
//                System.out.println("[BossFix] Removing damage cap in IAnimatedBoss...");
//                method.instructions.clear();
//                method.instructions.add(new LdcInsnNode(999999.0));
//                method.instructions.add(new InsnNode(Opcodes.DRETURN));
//                modified = true;
//                break;
//            }
//        }
//
//        return modified;
//    }
//
//    private static boolean isAddEffectMethod(MethodNode method) {
//        return method.name.equals("addEffect") &&
//                method.desc.equals("(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z");
//    }
//
//    private static void modifyAddEffectMethod(MethodNode method) {
//        method.instructions.clear();
//
//        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
//        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
//        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
//        method.instructions.add(new MethodInsnNode(
//                Opcodes.INVOKESPECIAL,
//                "net/minecraft/world/entity/LivingEntity",
//                "addEffect",
//                "(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
//                false
//        ));
//        method.instructions.add(new InsnNode(Opcodes.IRETURN));
//
//        System.out.println("[BossFix] IAnimatedBoss.addEffect modified!");
//    }
//}