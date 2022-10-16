package universalelectricity.core.block;

import universalelectricity.core.block.IConnectionProvider;
import universalelectricity.core.block.INetworkProvider;

public interface IConductor extends INetworkProvider, IConnectionProvider {

   double getResistance();

   double getCurrentCapcity();
}
