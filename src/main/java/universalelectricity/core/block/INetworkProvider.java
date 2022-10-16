package universalelectricity.core.block;

import universalelectricity.core.electricity.IElectricityNetwork;

public interface INetworkProvider {

   IElectricityNetwork getNetwork();

   void setNetwork(IElectricityNetwork var1);
}
