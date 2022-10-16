package universalelectricity.core.block;

import net.minecraft.tileentity.TileEntity;
import universalelectricity.core.block.IConnector;

public interface IConnectionProvider extends IConnector {

   TileEntity[] getAdjacentConnections();

   void updateAdjacentConnections();
}
