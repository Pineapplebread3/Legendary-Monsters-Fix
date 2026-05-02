package com.pine.bossfix.util;

import com.pine.bossfix.coremod.BossFixProcessor;
import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class BossFixMixinPlugin implements IMixinConfigPlugin {

    private static boolean registered = false;

    @Override
    public void onLoad(String mixinPackage) {
        if (!registered) {
            registerCoremod();
            registered = true;
        }
    }

    private void registerCoremod() {
        try {
            System.out.println("[BossFix] Registering Coremod from MixinPlugin...");

            Field f1 = Launcher.class.getDeclaredField("launchPlugins");
            f1.setAccessible(true);
            LaunchPluginHandler launchPluginHandler = (LaunchPluginHandler) f1.get(Launcher.INSTANCE);

            Field f2 = LaunchPluginHandler.class.getDeclaredField("plugins");
            f2.setAccessible(true);
            Map<String, ILaunchPluginService> plugins = (Map<String, ILaunchPluginService>) f2.get(launchPluginHandler);

            if (!plugins.containsKey("BossFixPlugin")) {
                plugins.put("BossFixPlugin", new ILaunchPluginService() {
                    @Override
                    public String name() {
                        return "BossFixPlugin";
                    }

                    @Override
                    public EnumSet<Phase> handlesClass(Type classType, boolean isEmpty) {
                        if (classType.getClassName().startsWith("net.miauczel.legendary_monsters.")) {
                            return EnumSet.of(Phase.BEFORE);
                        }
                        return EnumSet.noneOf(Phase.class);
                    }

                    @Override
                    public boolean processClass(Phase phase, ClassNode classNode, Type classType) {
                        AtomicBoolean shouldWrite = new AtomicBoolean(false);
                        BossFixProcessor.INSTANCE.processClass(phase, classNode, classType, shouldWrite);
                        return shouldWrite.get();
                    }
                });
            }

            System.out.println("[BossFix] Coremod registered!");
        } catch (Throwable t) {
            System.err.println("[BossFix] Failed to register!");
            t.printStackTrace();
        }
    }

    // IMixinConfigPlugin 必需的空实现
    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}