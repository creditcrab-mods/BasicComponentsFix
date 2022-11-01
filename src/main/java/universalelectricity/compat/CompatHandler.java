package universalelectricity.compat;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.compat.ic2.EventHandler;
import universalelectricity.compat.ic2.IC2CompatModule;
import universalelectricity.compat.ic2.TickHandler;
import universalelectricity.core.block.IConnector;

public class CompatHandler {

    private static List<CompatiblityModule> modules;

    public static void initCompatHandlers() {
        modules = new ArrayList<>();
        if (Loader.isModLoaded("IC2")) {
            modules.add(new IC2CompatModule());
            MinecraftForge.EVENT_BUS.register(new EventHandler());
            FMLCommonHandler.instance().bus().register(new TickHandler());
        }
    }

    public static void registerModule(CompatiblityModule module) {
        if (modules != null)
            modules.add(module);
    }

    public static boolean canConnect(TileEntity tileEntity, ForgeDirection side) {
        if (tileEntity == null) return false;
        if (tileEntity instanceof IConnector) return ((IConnector)tileEntity).canConnect(side);
        for (CompatiblityModule module : modules) {
            if (module.canConnect(tileEntity, side)) return true;
        }
        return false;
    }

    public static IElectricityTileHandler getHandler(TileEntity tileEntity) {
        if (tileEntity instanceof IConnector || tileEntity == null) return null;
        for (CompatiblityModule module : modules) {
            if (module.isHandledBy(tileEntity)) {
                return module.getHandler(tileEntity);
            }
        }
        return null;
    }
    
}
