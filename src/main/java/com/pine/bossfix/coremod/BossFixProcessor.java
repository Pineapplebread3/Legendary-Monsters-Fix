package com.pine.bossfix.coremod;

import cpw.mods.modlauncher.serviceapi.ILaunchPluginService.Phase;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import java.util.concurrent.atomic.AtomicBoolean;

public class BossFixProcessor {

    public static final BossFixProcessor INSTANCE = new BossFixProcessor();

    public void processClass(Phase phase, ClassNode classNode, Type classType, AtomicBoolean shouldWrite) {

        // 添加这行 - 不管什么类都打印
        System.out.println("[BossFix] processClass called! phase=" + phase + ", class=" + classNode.name);

        if (phase != Phase.BEFORE) return;

        String className = classNode.name;
        boolean modified = false;

        // 1. IAnimatedBoss（父类接口）
        if (className.equals("net/miauczel/legendary_monsters/entity/AnimatedMonster/OriginClasses/IAnimatedBoss")) {
            modified = IAnimatedBossFixer.fix(classNode);
        }

        // 2. IAnimatedMiniBoss（小Boss父类，移除限伤）
        else if (className.equals("net/miauczel/legendary_monsters/entity/AnimatedMonster/OriginClasses/IAnimatedMiniBoss")) {
            modified = IAnimatedMiniBossFixer.fix(classNode);
        }

        // 3. Cloud_GolemEntity（云傀儡）
        else if (className.equals("net/miauczel/legendary_monsters/entity/AnimatedMonster/IAnimatedBoss/CloudGolem/Cloud_GolemEntity")) {
            modified = CloudGolemFixer.fix(classNode);
        }

        // 4. TheWarpedOneOld（扭曲者）
        else if (className.equals("net/miauczel/legendary_monsters/entity/AnimatedMonster/Chorusling/TheWarpedOne/TheWarpedOneOld")) {
            modified = TheWarpedOneFixer.fix(classNode);
        }

        // 5. AnnihilationPursuerEntity（湮灭追逐者）
        else if (className.equals("net/miauczel/legendary_monsters/entity/AnimatedMonster/Mobs/SpaceStation/Flameborn/AnnihilationPursuer/AnnihilationPursuerEntity")) {
            modified = AnnihilationPursuerFixer.fix(classNode);
        }

        // 添加 Overgrown_colossusEntity 的处理
        else if (className.equals("net/miauczel/legendary_monsters/entity/AnimatedMonster/Mobs/Overgrown_colossusEntity")) {
            modified = OvergrownColossusFixer.fix(classNode);
        }

        // 添加 Shulker_MimicEntity 的处理
        else if (className.equals("net/miauczel/legendary_monsters/entity/AnimatedMonster/Mobs/ShulkerTower/Shulker_MimicEntity")) {
            modified = ShulkerMimicFixer.fix(classNode);
        }

        // 添加 Withered_AbominationEntity 的处理
        else if (className.equals("net/miauczel/legendary_monsters/entity/AnimatedMonster/Mobs/Withered_AbominationEntity")) {
            modified = WitheredAbominationFixer.fix(classNode);
        }

        // 添加 Lava_eaterEntity 的处理
        else if (className.equals("net/miauczel/legendary_monsters/entity/AnimatedMonster/Mobs/Lava_eaterEntity")) {
            modified = LavaEaterFixer.fix(classNode);
        }

        // 添加 EndersentEntity 的处理
        else if (className.equals("net/miauczel/legendary_monsters/entity/AnimatedMonster/Mobs/Chorusling/EndersentEntity")) {
            modified = EndersentFixer.fix(classNode);
        }

        // 添加 Ancient_GuardianEntity 的处理
        else if (className.equals("net/miauczel/legendary_monsters/entity/AnimatedMonster/Mobs/AncientStronghold/Ancient_GuardianEntity")) {
            modified = AncientGuardianFixer.fix(classNode);
        }

        // 添加 Frostbitten_GolemEntity 的处理
        else if (className.equals("net/miauczel/legendary_monsters/entity/AnimatedMonster/Mobs/Frostbitten_GolemEntity")) {
            modified = FrostbittenGolemFixer.fix(classNode);
        }

        // 添加 SkeletosaurusEntity 的处理
        else if (className.equals("net/miauczel/legendary_monsters/entity/AnimatedMonster/Mobs/SkeletosaurusEntity")) {
            modified = SkeletosaurusFixer.fix(classNode);
        }

        // 添加 PosessedPaladinEntity 的处理
        else if (className.equals("net/miauczel/legendary_monsters/entity/AnimatedMonster/Mobs/AbandonedCrypt/PosessedPaladinEntity")) {
            modified = PosessedPaladinFixer.fix(classNode);
        }

        // 添加 TheObliteratorEntity 的限伤处理
        else if (className.equals("net/miauczel/legendary_monsters/entity/AnimatedMonster/IAnimatedBoss/TheObliterator/TheObliteratorEntity")) {
            modified = TheObliteratorFixer.fix(classNode);
        }

        if (modified) {
            shouldWrite.set(true);
        }
    }
}




