package basiccomponents.common.tileentity;

import basiccomponents.common.BasicComponents;
import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.registry.LanguageRegistry;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.tile.TileEntityElectrical;

public class TileEntityCoalGenerator extends TileEntityElectrical implements IInventory, ISidedInventory {

   public static final int MAX_GENERATE_WATTS = 10000;
   public static final int MIN_GENERATE_WATTS = 100;
   private static final float BASE_ACCELERATION = 0.3F;
   public double prevGenerateWatts;
   public double generateWatts = 0.0D;
   public IConductor connectedElectricUnit = null;
   public int itemCookTime = 0;
   private ItemStack[] containingItems = new ItemStack[1];
   public final Set playersUsing = new HashSet();


   public boolean canConnect(ForgeDirection direction) {
      return direction == ForgeDirection.getOrientation(this.getBlockMetadata() - 0 + 2);
   }

   @Override
   public void updateEntity() {
      super.updateEntity();
      if(!this.worldObj.isRemote) {
         this.prevGenerateWatts = this.generateWatts;
         ForgeDirection outputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() - 0 + 2);
         TileEntity outputTile = VectorHelper.getConnectorFromSide(this.worldObj, new Vector3((double)this.xCoord, (double)this.yCoord, (double)this.zCoord), outputDirection);
         IElectricityNetwork network = ElectricityNetworkHelper.getNetworkFromTileEntity(outputTile, outputDirection);
         if(network != null) {
            if(network.getRequest(new TileEntity[0]).getWatts() > 0.0D) {
               this.connectedElectricUnit = (IConductor)outputTile;
            } else {
               this.connectedElectricUnit = null;
            }
         } else {
            this.connectedElectricUnit = null;
         }

         if(!this.isDisabled()) {
            if(this.itemCookTime > 0) {
               --this.itemCookTime;
               if(this.connectedElectricUnit != null) {
                  this.generateWatts = Math.min(this.generateWatts + Math.min(this.generateWatts * 0.005D + 0.30000001192092896D, 5.0D), 10000.0D);
               }
            }

            if(this.containingItems[0] != null && this.connectedElectricUnit != null && this.containingItems[0].getItem() == Items.coal && this.itemCookTime <= 0) { //TODO DON'T HARDCOE FUEL!!!
               this.itemCookTime = 320;
               this.decrStackSize(0, 1);
            }

            if(this.connectedElectricUnit == null || this.itemCookTime <= 0) {
               this.generateWatts = Math.max(this.generateWatts - 8.0D, 0.0D);
            }

            if(this.connectedElectricUnit != null) {
               if(this.generateWatts > 100.0D) {
                  this.connectedElectricUnit.getNetwork().startProducing(this, this.generateWatts / this.getVoltage() / 20.0D, this.getVoltage());
               } else {
                  this.connectedElectricUnit.getNetwork().stopProducing(this);
               }
            }
         }

         if(super.ticks % 3L == 0L) {
            /*Iterator i$ = this.playersUsing.iterator();

            while(i$.hasNext()) {
               EntityPlayer player = (EntityPlayer)i$.next();
               PacketDispatcher.sendPacketToPlayer(this.func_70319_e(), (Player)player);
            }*/
            this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
         }

         if(this.prevGenerateWatts <= 0.0D && this.generateWatts > 0.0D || this.prevGenerateWatts > 0.0D && this.generateWatts <= 0.0D) {
            //PacketManager.sendPacketToClients(this.func_70319_e(), this.field_70331_k);
            this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
         }
      }

   }

   @Override
   public Packet getDescriptionPacket() {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setDouble("generateWatts", this.generateWatts);
      nbt.setInteger("itemCookTime", this.itemCookTime);
      nbt.setInteger("disabledTicks", super.disabledTicks);
      return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, getBlockMetadata(), nbt);
      //return PacketManager.getPacket(BasicComponents.CHANNEL, this, new Object[]{Double.valueOf(this.generateWatts), Integer.valueOf(this.itemCookTime), Integer.valueOf(super.disabledTicks)});
   }

   @Override
   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      if (this.worldObj.isRemote) {
         NBTTagCompound nbt = pkt.func_148857_g();
         this.generateWatts = nbt.getDouble("generateWatts");
         this.itemCookTime = nbt.getInteger("itemCookTime");
         super.disabledTicks = nbt.getInteger("disabledTicks");
      }
   }

   /*public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) {
      try {
         if(this.field_70331_k.isRemote) {
            this.generateWatts = dataStream.readDouble();
            this.itemCookTime = dataStream.readInt();
            super.disabledTicks = dataStream.readInt();
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }*/

   public void openInventory() {}

   public void closeInventory() {}

   @Override
   public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readFromNBT(par1NBTTagCompound);
      this.itemCookTime = par1NBTTagCompound.getInteger("itemCookTime");
      this.generateWatts = par1NBTTagCompound.getDouble("generateRate");
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
      par1NBTTagCompound.setInteger("itemCookTime", this.itemCookTime);
      par1NBTTagCompound.setDouble("generateRate", this.generateWatts);
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
      return LanguageRegistry.instance().getStringLocalization("tile.basiccomponents:bcMachine.0.name");
   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
      return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this?false:par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
   }

   @Override
   public boolean hasCustomInventoryName() {
      return true;
   }

   public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {
      return itemstack.getItem() == Items.coal; //TODO WTF
   }

   public int[] getAccessibleSlotsFromSide(int var1) {
      return new int[]{0};
   }

   public boolean canInsertItem(int slotID, ItemStack itemstack, int j) {
      return this.isItemValidForSlot(slotID, itemstack);
   }

   public boolean canExtractItem(int slotID, ItemStack itemstack, int j) {
      return slotID == 0;
   }
}
