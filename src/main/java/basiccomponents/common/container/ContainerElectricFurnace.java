package basiccomponents.common.container;

import basiccomponents.common.tileentity.TileEntityElectricFurnace;
import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.prefab.SlotSpecific;

public class ContainerElectricFurnace extends Container {

   private TileEntityElectricFurnace tileEntity;


   public ContainerElectricFurnace(InventoryPlayer par1InventoryPlayer, TileEntityElectricFurnace tileEntity) {
      this.tileEntity = tileEntity;
      this.addSlotToContainer(new SlotSpecific(tileEntity, 0, 55, 49, new Class[]{IEnergyContainerItem.class}));
      this.addSlotToContainer(new Slot(tileEntity, 1, 55, 25));
      this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, tileEntity, 2, 108, 25));

      int var3;
      for(var3 = 0; var3 < 3; ++var3) {
         for(int var4 = 0; var4 < 9; ++var4) {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
         }
      }

      for(var3 = 0; var3 < 9; ++var3) {
         this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 142));
      }

      tileEntity.playersUsing.add(par1InventoryPlayer.player);
   }

   public void onContainerClosed(EntityPlayer entityplayer) {
      super.onContainerClosed(entityplayer);
      this.tileEntity.playersUsing.remove(entityplayer);
   }

   public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
      return this.tileEntity.isUseableByPlayer(par1EntityPlayer);
   }

   public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1) {
      ItemStack var2 = null;
      Slot var3 = (Slot)super.inventorySlots.get(par1);
      if(var3 != null && var3.getHasStack()) {
         ItemStack var4 = var3.getStack();
         var2 = var4.copy();
         if(par1 == 2) {
            if(!this.mergeItemStack(var4, 3, 39, true)) {
               return null;
            }

            var3.onSlotChange(var4, var2);
         } else if(par1 != 1 && par1 != 0) {
            if(var4.getItem() instanceof IEnergyContainerItem) {
               if(!this.mergeItemStack(var4, 0, 1, false)) {
                  return null;
               }
            } else if(FurnaceRecipes.smelting().getSmeltingResult(var4) != null) {
               if(!this.mergeItemStack(var4, 1, 2, false)) {
                  return null;
               }
            } else if(par1 >= 3 && par1 < 30) {
               if(!this.mergeItemStack(var4, 30, 39, false)) {
                  return null;
               }
            } else if(par1 >= 30 && par1 < 39 && !this.mergeItemStack(var4, 3, 30, false)) {
               return null;
            }
         } else if(!this.mergeItemStack(var4, 3, 39, false)) {
            return null;
         }

         if(var4.stackSize == 0) {
            var3.putStack((ItemStack)null);
         } else {
            var3.onSlotChanged();
         }

         if(var4.stackSize == var2.stackSize) {
            return null;
         }

         var3.onPickupFromSlot(par1EntityPlayer, var4);
      }

      return var2;
   }
}
