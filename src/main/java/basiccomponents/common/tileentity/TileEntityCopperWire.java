package basiccomponents.common.tileentity;

import basiccomponents.common.BasicComponents;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.prefab.tile.TileEntityConductor;

public class TileEntityCopperWire extends TileEntityConductor {

   public static double RESISTANCE = 0.05D;
   public static double MAX_AMPS = 200.0D;


   public TileEntityCopperWire() {
      super.channel = BasicComponents.CHANNEL;
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
      if(this.getNetwork() != null && super.ticks % 20L == 0L && this.getNetwork().getProduced(new TileEntity[0]).amperes > this.getCurrentCapcity() && !this.worldObj.isRemote) {
         this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, Blocks.fire);
      }

   }

}
