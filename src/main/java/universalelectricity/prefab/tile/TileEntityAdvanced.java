package universalelectricity.prefab.tile;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityAdvanced extends TileEntity {

   protected long ticks = 0L;

   @Override
   public void updateEntity() {
      if(this.ticks == 0L) {
         this.initiate();
      }

      if(this.ticks >= Long.MAX_VALUE) {
         this.ticks = 1L;
      }

      ++this.ticks;
   }

   public void initiate() {}

   @Override
   public int getBlockMetadata() {
      if(this.blockMetadata == -1) {
         this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
      }

      return this.blockMetadata;
   }

   @Override
   public Block getBlockType() {
      if(this.blockType == null) {
         this.blockType = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
      }

      return this.blockType;
   }
}
