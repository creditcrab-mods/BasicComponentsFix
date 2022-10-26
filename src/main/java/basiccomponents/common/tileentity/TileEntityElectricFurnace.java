package basiccomponents.common.tileentity;

import basiccomponents.common.BasicComponents;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.registry.LanguageRegistry;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.prefab.tile.TileEntityElectricityRunnable;

public class TileEntityElectricFurnace extends TileEntityElectricityRunnable implements IInventory, ISidedInventory {

   public static final double WATTS_PER_TICK = 500.0D;
   public static final int PROCESS_TIME_REQUIRED = 130;
   public int processTicks = 0;
   private ItemStack[] containingItems = new ItemStack[3];
   public final Set<EntityPlayer> playersUsing = new HashSet();

   @Override
   public void updateEntity() {
      super.updateEntity();
      super.wattsReceived += ElectricItemHelper.dechargeItem(this.containingItems[0], 500.0D, this.getVoltage());
      if(!this.worldObj.isRemote) {
         if(this.canProcess()) {
            if(super.wattsReceived >= 500.0D) {
               if(this.processTicks == 0) {
                  this.processTicks = 130;
               } else if(this.processTicks > 0) {
                  --this.processTicks;
                  if(this.processTicks < 1) {
                     this.smeltItem();
                     this.processTicks = 0;
                  }
               } else {
                  this.processTicks = 0;
               }
            } else {
               this.processTicks = 0;
            }

            super.wattsReceived = Math.max(super.wattsReceived - 125.0D, 0.0D);
         } else {
            this.processTicks = 0;
         }

         if(super.ticks % 3L == 0L) {
            for (EntityPlayer player : this.playersUsing) {
               if (player instanceof EntityPlayerMP) {
                  ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(getDescriptionPacket());
               }
            }
            this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
         }
      }

   }

   public boolean canConnect(ForgeDirection direction) {
      return direction == ForgeDirection.getOrientation(this.getBlockMetadata() - 8 + 2);
   }

   public ElectricityPack getRequest() {
      return this.canProcess()?new ElectricityPack(500.0D / this.getVoltage(), this.getVoltage()):new ElectricityPack();
   }

   @Override
   public Packet getDescriptionPacket() {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setInteger("processTicks", this.processTicks);
      nbt.setInteger("disabledTicks", super.disabledTicks);
      return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, getBlockMetadata(), nbt);
      //return PacketManager.getPacket(BasicComponents.CHANNEL, this, new Object[]{Integer.valueOf(this.processTicks), Integer.valueOf(super.disabledTicks)});
   }

   @Override
   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      if (this.worldObj.isRemote) {
         NBTTagCompound nbt = pkt.func_148857_g();
         this.processTicks = nbt.getInteger("processTicks");
         super.disabledTicks = nbt.getInteger("disabledTicks");
      }
   }

   /*public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) {
      try {
         this.processTicks = dataStream.readInt();
         super.disabledTicks = dataStream.readInt();
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }*/

   public void openInventory() {}

   public void closeInventory() {}

   public boolean canProcess() {
      if (this.containingItems[1] == null) {
         return false;
      } else if(FurnaceRecipes.smelting().getSmeltingResult(this.containingItems[1]) == null) {
         return false;
      } else if(this.containingItems[1] == null) {
         return false;
      } else {
         if(this.containingItems[2] != null) {
            if(!this.containingItems[2].isItemEqual(FurnaceRecipes.smelting().getSmeltingResult(this.containingItems[1]))) {
               return false;
            }

            if(this.containingItems[2].stackSize + 1 > 64) {
               return false;
            }
         }

         return true;
      }
   }

   public void smeltItem() {
      if(this.canProcess()) {
         ItemStack resultItemStack = FurnaceRecipes.smelting().getSmeltingResult(this.containingItems[1]);
         if(this.containingItems[2] == null) {
            this.containingItems[2] = resultItemStack.copy();
         } else if(this.containingItems[2].isItemEqual(resultItemStack)) {
            ++this.containingItems[2].stackSize;
         }

         --this.containingItems[1].stackSize;
         if(this.containingItems[1].stackSize <= 0) {
            this.containingItems[1] = null;
         }
      }

   }

   @Override
   public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readFromNBT(par1NBTTagCompound);
      this.processTicks = par1NBTTagCompound.getInteger("smeltingTicks");
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
      par1NBTTagCompound.setInteger("smeltingTicks", this.processTicks);
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
      return LanguageRegistry.instance().getStringLocalization("tile.basiccomponents:bcMachine.2.name");
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

   public boolean isItemValidForSlot(int slotID, ItemStack itemStack) {
      return slotID == 1?FurnaceRecipes.smelting().getSmeltingResult(itemStack) != null:(slotID == 0?itemStack.getItem() instanceof IItemElectric:false);
   }

   public int[] getAccessibleSlotsFromSide(int side) {
      return side == 0?new int[]{2}:(side == 1?new int[]{0, 1}:new int[]{0});
   }

   public boolean canInsertItem(int slotID, ItemStack par2ItemStack, int par3) {
      return this.isItemValidForSlot(slotID, par2ItemStack);
   }

   public boolean canExtractItem(int slotID, ItemStack par2ItemStack, int par3) {
      return slotID == 2;
   }
}
