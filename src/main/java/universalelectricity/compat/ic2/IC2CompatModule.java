package universalelectricity.compat.ic2;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.compat.CompatiblityModule;
import universalelectricity.compat.IElectricityTileHandler;

public class IC2CompatModule extends CompatiblityModule {

    @Override
    public boolean isHandledBy(TileEntity tile) {
        return EnergyNetCache.contains(tile);
    }

    @Override
    public boolean canConnect(TileEntity tile, ForgeDirection side) {
        return EnergyNetCache.canConnect(EnergyNetCache.toKey(tile), side);
    }

    @Override
    public IElectricityTileHandler getHandler(TileEntity tileEntity) {
        return null;
    }
    
}
