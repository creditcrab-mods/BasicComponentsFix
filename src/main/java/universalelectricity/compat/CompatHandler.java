package universalelectricity.compat;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.compat.ic2.IC2CompatModule;
import universalelectricity.compat.ic2.EventHandler;
import universalelectricity.compat.ic2.TickHandler;

public class CompatHandler {

    public static void initCompatHandlers() {
        if (Loader.isModLoaded("IC2")) {
            CompatibilityModule.register(new IC2CompatModule());
            MinecraftForge.EVENT_BUS.register(new EventHandler());
            FMLCommonHandler.instance().bus().register(new TickHandler());
        }
    }
    
}
