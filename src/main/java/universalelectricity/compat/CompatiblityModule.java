package universalelectricity.compat;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class CompatiblityModule {

    public abstract boolean isHandledBy(TileEntity tile);

    public abstract boolean canConnect(TileEntity tile, ForgeDirection side);

    public abstract IElectricityTileHandler getHandler(TileEntity tileEntity);
    
}
