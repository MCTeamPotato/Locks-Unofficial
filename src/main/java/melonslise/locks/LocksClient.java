package melonslise.locks;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import melonslise.locks.client.init.LocksItemModelsProperties;
import melonslise.locks.client.init.LocksScreens;
import melonslise.locks.common.config.LocksClientConfig;
import net.fabricmc.api.ClientModInitializer;
import net.minecraftforge.fml.config.ModConfig;

public class LocksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        LocksScreens.register();
        LocksItemModelsProperties.register();
        ForgeConfigRegistry.INSTANCE.register(Locks.ID, ModConfig.Type.CLIENT, LocksClientConfig.SPEC);
    }
}
