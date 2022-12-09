package universalelectricity.core.block;

import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.net.IConnectable;

public interface IConnector extends IConnectable, ISelfDriven {

   boolean canConnect(ForgeDirection from);

   @Override
   default boolean canConnect(ForgeDirection from, Object source) {
        if (source instanceof IConnector) {
            return canConnect(from);
        } 
        return false;
   }
}
