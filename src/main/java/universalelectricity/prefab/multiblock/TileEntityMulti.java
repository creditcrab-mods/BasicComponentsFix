package universalelectricity.prefab.multiblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class TileEntityMulti extends TileEntity {

   public Vector3 mainBlockPosition;
   public String channel;


   public TileEntityMulti() {}

   public TileEntityMulti(String channel) {
      this.channel = channel;
   }

   public void setMainBlock(Vector3 mainBlock) {
      this.mainBlockPosition = mainBlock;
      if(!this.worldObj.isRemote) {
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
      }

   }

   @Override
   public Packet getDescriptionPacket() {
      if(this.mainBlockPosition == null) {
         return null;
      } else {
         NBTTagCompound nbt = this.mainBlockPosition.writeToNBT(new NBTTagCompound());
         return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, getBlockMetadata(),nbt);
      }
   }

   public void onBlockRemoval() {
      if(this.mainBlockPosition != null) {
         TileEntity tileEntity = this.worldObj.getTileEntity(this.mainBlockPosition.intX(), this.mainBlockPosition.intY(), this.mainBlockPosition.intZ());
         if(tileEntity != null && tileEntity instanceof IMultiBlock) {
            IMultiBlock mainBlock = (IMultiBlock)tileEntity;
            if(mainBlock != null) {
               mainBlock.onDestroy(this);
            }
         }
      }

   }

   public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer) {
      if(this.mainBlockPosition != null) {
         TileEntity tileEntity = this.worldObj.getTileEntity(this.mainBlockPosition.intX(), this.mainBlockPosition.intY(), this.mainBlockPosition.intZ());
         if(tileEntity != null && tileEntity instanceof IMultiBlock) {
            return ((IMultiBlock)tileEntity).onActivated(par5EntityPlayer);
         }
      }

      return false;
   }

   @Override
   public void readFromNBT(NBTTagCompound nbt) {
      super.readFromNBT(nbt);
      this.mainBlockPosition = Vector3.readFromNBT(nbt.getCompoundTag("mainBlockPosition"));
   }

   @Override
   public void writeToNBT(NBTTagCompound nbt) {
      super.writeToNBT(nbt);
      if(this.mainBlockPosition != null) {
         nbt.setTag("mainBlockPosition", this.mainBlockPosition.writeToNBT(new NBTTagCompound()));
      }

   }

   public boolean canUpdate() {
      return false;
   }

   @Override
   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      NBTTagCompound nbt = pkt.func_148857_g();
      this.mainBlockPosition = Vector3.readFromNBT(nbt);
   }

}
