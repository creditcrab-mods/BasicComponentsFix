package basiccomponents.common.tileentity;

import basiccomponents.common.BasicComponents;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.registry.LanguageRegistry;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.block.IElectricityStorage;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.tile.TileEntityElectricityStorage;

public class TileEntityBatteryBox extends TileEntityElectricityStorage implements IElectricityStorage, ISidedInventory {

   private ItemStack[] containingItems = new ItemStack[2];
   public final Set<EntityPlayer> playersUsing = new HashSet();


   @Override
   public void updateEntity() {
      super.updateEntity();
      if(!this.isDisabled() && !this.worldObj.isRemote) {
         this.setJoules(this.getJoules() - ElectricItemHelper.chargeItem(this.containingItems[0], this.getJoules(), this.getVoltage()));
         this.setJoules(this.getJoules() + ElectricItemHelper.dechargeItem(this.containingItems[1], this.getMaxJoules() - this.getJoules(), this.getVoltage()));
         ForgeDirection i$ = ForgeDirection.getOrientation(this.getBlockMetadata() - 4 + 2);
         TileEntity player = VectorHelper.getConnectorFromSide(this.worldObj, new Vector3(this), i$.getOpposite());
         TileEntity outputTile = VectorHelper.getConnectorFromSide(this.worldObj, new Vector3(this), i$);
         IElectricityNetwork inputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(player, i$.getOpposite());
         IElectricityNetwork outputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(outputTile, i$);
         if(outputNetwork != null && inputNetwork != outputNetwork) {
            double outputWatts = Math.min(outputNetwork.getRequest(new TileEntity[]{this}).getWatts(), Math.min(this.getJoules(), 10000.0D));
            if(this.getJoules() > 0.0D && outputWatts > 0.0D) {
               outputNetwork.startProducing(this, outputWatts / this.getVoltage(), this.getVoltage());
               this.setJoules(this.getJoules() - outputWatts);
            } else {
               outputNetwork.stopProducing(this);
            }
         }
      }

      this.setJoules(this.getJoules() - 5.0E-5D);
      if(!this.worldObj.isRemote && super.ticks % 3L == 0L) {
         for (EntityPlayer player : this.playersUsing) {
            if (player instanceof EntityPlayerMP) {
               ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(getDescriptionPacket());
            }
         }
      }

   }

   public boolean canConnect(ForgeDirection direction) {
      return direction == ForgeDirection.getOrientation(this.getBlockMetadata() - 4 + 2) || direction == ForgeDirection.getOrientation(this.getBlockMetadata() - 4 + 2).getOpposite();
   }

   protected EnumSet getConsumingSides() {
      return EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() - 4 + 2).getOpposite());
   }

   @Override
   public Packet getDescriptionPacket() {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setDouble("joules", this.getJoules());
      nbt.setInteger("disabledTicks", super.disabledTicks);
      //return PacketManager.getPacket(BasicComponents.CHANNEL, this, new Object[]{Double.valueOf(this.getJoules()), Integer.valueOf(super.disabledTicks)});
      return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, getBlockMetadata(), nbt);
   }

   @Override
   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      if (this.worldObj.isRemote) {
         NBTTagCompound nbt = pkt.func_148857_g();
         this.setJoules(nbt.getDouble("joules"));
         super.disabledTicks = nbt.getInteger("disabledTicks");
      }
   }

   /*public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) {
      try {
         this.setJoules(dataStream.readDouble());
         super.disabledTicks = dataStream.readInt();
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }*/



   @Override
   public void openInventory() {}

   @Override
   public void closeInventory() {}

   @Override
   public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readFromNBT(par1NBTTagCompound);
      NBTTagList var2 = par1NBTTagCompound.getTagList("Items", 10);
      this.containingItems = new ItemStack[this.getSizeInventory()];

      for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
         NBTTagCompound var4 = (NBTTagCompound)var2.getCompoundTagAt(var3);
         byte var5 = var4.getByte("Slot");
         if(var5 >= 0 && var5 < this.containingItems.length) {
            this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
         }
      }

   }

   @Override
   public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeToNBT(par1NBTTagCompound);
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.containingItems.length; ++var3) {
         if(this.containingItems[var3] != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("Slot", (byte)var3);
            this.containingItems[var3].writeToNBT(var4);
            var2.appendTag(var4);
         }
      }

      par1NBTTagCompound.setTag("Items", var2);
   }

   public int getSizeInventory() {
      return this.containingItems.length;
   }

   public ItemStack getStackInSlot(int par1) {
      return this.containingItems[par1];
   }

   public ItemStack decrStackSize(int par1, int par2) {
      if(this.containingItems[par1] != null) {
         ItemStack var3;
         if(this.containingItems[par1].stackSize <= par2) {
            var3 = this.containingItems[par1];
            this.containingItems[par1] = null;
            return var3;
         } else {
            var3 = this.containingItems[par1].splitStack(par2);
            if(this.containingItems[par1].stackSize == 0) {
               this.containingItems[par1] = null;
            }

            return var3;
         }
      } else {
         return null;
      }
   }

   public ItemStack getStackInSlotOnClosing(int par1) {
      if(this.containingItems[par1] != null) {
         ItemStack var2 = this.containingItems[par1];
         this.containingItems[par1] = null;
         return var2;
      } else {
         return null;
      }
   }

   public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
      this.containingItems[par1] = par2ItemStack;
      if(par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
         par2ItemStack.stackSize = this.getInventoryStackLimit();
      }

   }

   @Override
   public String getInventoryName() {
      return LanguageRegistry.instance().getStringLocalization("tile.basiccomponents:bcMachine.1.name");
   }

   public int getInventoryStackLimit() {
      return 1;
   }

   public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
      return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this?false:par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
   }

   public double getMaxJoules() {
      return 5000000.0D;
   }

   @Override
   public boolean hasCustomInventoryName() {
      return true;
   }

   public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {
      return itemstack.getItem() instanceof IItemElectric;
   }

   public int[] getAccessibleSlotsFromSide(int slotID) {
      return new int[]{0, 1};
   }

   public boolean canInsertItem(int slotID, ItemStack itemstack, int side) {
      if(this.isItemValidForSlot(slotID, itemstack)) {
         if(slotID == 0) {
            return ((IItemElectric)itemstack.getItem()).getReceiveRequest(itemstack).getWatts() > 0.0D;
         }

         if(slotID == 1) {
            return ((IItemElectric)itemstack.getItem()).getProvideRequest(itemstack).getWatts() > 0.0D;
         }
      }

      return false;
   }

   public boolean canExtractItem(int slotID, ItemStack itemstack, int side) {
      if(this.isItemValidForSlot(slotID, itemstack)) {
         if(slotID == 0) {
            return ((IItemElectric)itemstack.getItem()).getReceiveRequest(itemstack).getWatts() <= 0.0D;
         }

         if(slotID == 1) {
            return ((IItemElectric)itemstack.getItem()).getProvideRequest(itemstack).getWatts() <= 0.0D;
         }
      }

      return false;
   }
}
