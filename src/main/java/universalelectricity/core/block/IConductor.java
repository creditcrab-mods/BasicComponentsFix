package universalelectricity.core.block;

import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.net.IConnector;
import universalelectricity.core.electricity.IElectricityNetwork;

public interface IConductor extends INetworkProvider, IConnectionProvider, IConnector<IElectricityNetwork> {

   double getResistance();

   double getCurrentCapcity();

   @Override
   default IConnector<IElectricityNetwork> getInstance(ForgeDirection dir) {
      return this;
   }
}
