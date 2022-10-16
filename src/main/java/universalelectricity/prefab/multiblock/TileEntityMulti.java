package universalelectricity.prefab.multiblock;

import com.google.common.io.ByteArrayDataInput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.multiblock.BlockMulti;
import universalelectricity.prefab.multiblock.IMultiBlock;

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

   /*@Override
   public Packet getDescriptionPacket() {
      if(this.mainBlockPosition == null) {
         return null;
      } else {
         if(this.channel == null || this.channel == "" && this.getBlockType() instanceof BlockMulti) {
            this.channel = ((BlockMulti)this.getBlockType()).channel;
         }

         return PacketManager.getPacket(this.channel, this, new Object[]{Integer.valueOf(this.mainBlockPosition.intX()), Integer.valueOf(this.mainBlockPosition.intY()), Integer.valueOf(this.mainBlockPosition.intZ())});
      }
   }*/

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

   /*@Override
   public void handlePacketData(NetworkManager network, int packetType, S3FPacketCustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) {
      try {
         this.mainBlockPosition = new Vector3((double)dataStream.readInt(), (double)dataStream.readInt(), (double)dataStream.readInt());
      } catch (Exception var7) {
         var7.printStackTrace();
      }
   }*/

   @Override
   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {

   }

}
