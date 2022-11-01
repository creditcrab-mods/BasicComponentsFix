package universalelectricity.compat.ic2;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.relauncher.Side;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import mekanism.api.energy.ICableOutputter;
import mekanism.api.energy.IStrictEnergyAcceptor;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.core.block.IConnector;

public class EventHandler {
    
    @SubscribeEvent
    public void onLoadTile(EnergyTileLoadEvent event) {
        TileEntity te = (TileEntity) event.energyTile;
        if (te instanceof IConnector || te instanceof IStrictEnergyAcceptor || te instanceof ICableOutputter) return;
        if (te instanceof IEnergySink || te instanceof IEnergySource) EnergyNetCache.load(te);
    }

    @SubscribeEvent
    public void onUnloadTile(EnergyTileUnloadEvent event) {
        TileEntity te = (TileEntity) event.energyTile;
        EnergyNetCache.unload(te);
    }

}
