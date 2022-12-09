package universalelectricity.prefab.tile;

import java.util.Arrays;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.compat.CompatHandler;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.INetworkProvider;
import universalelectricity.core.block.ISelfDriven;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;

public abstract class TileEntityConductor extends TileEntityAdvanced implements IConductor {

   private IElectricityNetwork network;
   public boolean[] visuallyConnected = new boolean[]{false, false, false, false, false, false};
   public TileEntity[] connectedBlocks = new TileEntity[]{null, null, null, null, null, null};
   protected String channel = "";


   public void updateConnection(TileEntity tileEntity, ForgeDirection side) {
      if(!this.worldObj.isRemote) {
         if(ElectricityNetworkHelper.canConnect(tileEntity, side.getOpposite(), this)) {
            this.connectedBlocks[side.ordinal()] = tileEntity;
            this.visuallyConnected[side.ordinal()] = true;
            if(tileEntity.getClass() == this.getClass() && tileEntity instanceof INetworkProvider) {
               this.getNetwork().mergeConnection(((INetworkProvider)tileEntity).getNetwork());
            } else if (!(tileEntity instanceof ISelfDriven)) {
               CompatHandler.registerTile(tileEntity);
            }

            return;
         }

         if(this.connectedBlocks[side.ordinal()] != null) {
            this.getNetwork().stopProducing(this.connectedBlocks[side.ordinal()]);
            this.getNetwork().stopRequesting(this.connectedBlocks[side.ordinal()]);
         }

         this.connectedBlocks[side.ordinal()] = null;
         this.visuallyConnected[side.ordinal()] = false;
      }

   }

   @Override
   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      if(this.worldObj.isRemote) {
         NBTTagCompound nbt = pkt.func_148857_g();
         this.visuallyConnected[0] = nbt.getBoolean("bottom");
         this.visuallyConnected[1] = nbt.getBoolean("top");
         this.visuallyConnected[2] = nbt.getBoolean("back");
         this.visuallyConnected[3] = nbt.getBoolean("front");
         this.visuallyConnected[4] = nbt.getBoolean("left");
         this.visuallyConnected[5] = nbt.getBoolean("right");
      }
   }

   public void initiate() {
      this.updateAdjacentConnections();
   }

   @Override
   public void invalidate() {
      if(!this.worldObj.isRemote) {
         this.getNetwork().splitNetwork(this);
      }

      super.invalidate();
   }

   @Override
   public void updateEntity() {
      super.updateEntity();
      if(!this.worldObj.isRemote && super.ticks % 300L == 0L) {
         this.updateAdjacentConnections();
      }

   }

   public void updateAdjacentConnections() {
      if(this.worldObj != null && !this.worldObj.isRemote) {
         boolean[] previousConnections = (boolean[])this.visuallyConnected.clone();

         for(byte i = 0; i < 6; ++i) {
            this.updateConnection(VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), ForgeDirection.getOrientation(i)), ForgeDirection.getOrientation(i));
         }

         if(!Arrays.equals(previousConnections, this.visuallyConnected)) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         }
      }

   }

   @Override
   public Packet getDescriptionPacket() {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setBoolean("bottom", this.visuallyConnected[0]);
      nbt.setBoolean("top", this.visuallyConnected[1]);
      nbt.setBoolean("back", this.visuallyConnected[2]);
      nbt.setBoolean("front", this.visuallyConnected[3]);
      nbt.setBoolean("left", this.visuallyConnected[4]);
      nbt.setBoolean("right", this.visuallyConnected[5]);
      return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt);
   }

   public IElectricityNetwork getNetwork() {
      if(this.network == null) {
         this.setNetwork(new ElectricityNetwork(new IConductor[]{this}));
      }

      return this.network;
   }

   public void setNetwork(IElectricityNetwork network) {
      this.network = network;
   }

   public TileEntity[] getAdjacentConnections() {
      return this.connectedBlocks;
   }

   public boolean canConnect(ForgeDirection direction) {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return AxisAlignedBB.getBoundingBox((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 1), (double)(this.zCoord + 1));
   }
}
