package universalelectricity.prefab.tile;

import universalelectricity.core.block.IConnector;
import universalelectricity.core.block.IVoltage;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.prefab.tile.TileEntityDisableable;

public abstract class TileEntityElectrical extends TileEntityDisableable implements IConnector, IVoltage {

   public double getVoltage() {
      return 120.0D;
   }

   @Override
   public void invalidate() {
      ElectricityNetworkHelper.invalidate(this);
      super.invalidate();
   }
}
