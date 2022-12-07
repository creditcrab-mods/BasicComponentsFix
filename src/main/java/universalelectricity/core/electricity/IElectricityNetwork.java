package universalelectricity.core.electricity;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.api.net.INetwork;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IConnectionProvider;

public interface IElectricityNetwork extends INetwork<IElectricityNetwork, IConductor> {

   void startProducing(TileEntity var1, ElectricityPack var2);

   void startProducing(TileEntity var1, double var2, double var4);

   boolean isProducing(TileEntity var1);

   void stopProducing(TileEntity var1);

   void startRequesting(TileEntity var1, ElectricityPack var2);

   void startRequesting(TileEntity var1, double var2, double var4);

   boolean isRequesting(TileEntity var1);

   void stopRequesting(TileEntity var1);

   ElectricityPack getProduced(TileEntity ... var1);

   ElectricityPack getRequest(TileEntity ... var1);

   ElectricityPack getRequestWithoutReduction();

   ElectricityPack consumeElectricity(TileEntity var1);

   HashMap<TileEntity, ElectricityPack> getProducers();

   List<TileEntity> getProviders();

   HashMap<TileEntity, ElectricityPack> getConsumers();

   List<TileEntity> getReceivers();

   Set<IConductor> getConductors();

   double getTotalResistance();

   double getLowestCurrentCapacity();

   void cleanUpConductors();

   void refreshConductors();

   void mergeConnection(IElectricityNetwork var1);

   void splitNetwork(IConnectionProvider var1);
   
}
