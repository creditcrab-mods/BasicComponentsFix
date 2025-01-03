package basiccomponents.common.tileentity;

import basiccomponents.common.BasicComponents;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.prefab.tile.TileEntityConductor;

public class TileEntityCopperWire extends TileEntity {

   public static double RESISTANCE = 0.05D;
   public static double MAX_AMPS = 200.0D;


   public TileEntityCopperWire() {

   }

   public double getResistance() {
      return RESISTANCE;
   }

   public double getCurrentCapcity() {
      return MAX_AMPS;
   }

   @Override
   public void updateEntity() {
      super.updateEntity();


   }

}
