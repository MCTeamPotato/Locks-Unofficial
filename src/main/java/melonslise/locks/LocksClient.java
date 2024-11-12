package melonslise.locks;

import fuzs.forgeconfigapiport.fabric.api.forge.v4.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import melonslise.locks.client.init.LocksItemModelsProperties;
import melonslise.locks.client.init.LocksNetworkClient;
import melonslise.locks.client.init.LocksScreens;
import melonslise.locks.common.config.LocksClientConfig;
import melonslise.locks.common.init.LocksContainerTypes;
import net.fabricmc.api.ClientModInitializer;
import net.neoforged.fml.config.ModConfig;

public class LocksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        LocksScreens.register();
        LocksItemModelsProperties.register();
        LocksNetworkClient.register();
        NeoForgeConfigRegistry.INSTANCE.register(Locks.ID, ModConfig.Type.CLIENT, LocksClientConfig.SPEC);
    }
}
