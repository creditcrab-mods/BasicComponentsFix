package universalelectricity.core.electricity;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.core.block.IConnectionProvider;
import universalelectricity.core.electricity.ElectricityPack;

public interface IElectricityNetwork {

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

   HashMap getProducers();

   List getProviders();

   HashMap getConsumers();

   List getReceivers();

   Set getConductors();

   double getTotalResistance();

   double getLowestCurrentCapacity();

   void cleanUpConductors();

   void refreshConductors();

   void mergeConnection(IElectricityNetwork var1);

   void splitNetwork(IConnectionProvider var1);
}
