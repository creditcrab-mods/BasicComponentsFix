package universalelectricity.prefab;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotSpecific extends Slot {

   public ItemStack[] validItemStacks = new ItemStack[0];
   public Class[] validClasses = new Class[0];
   public boolean isInverted = false;
   public boolean isMetadataSensitive = false;


   public SlotSpecific(IInventory par2IInventory, int par3, int par4, int par5, ItemStack ... itemStacks) {
      super(par2IInventory, par3, par4, par5);
      this.setItemStacks(itemStacks);
   }

   public SlotSpecific(IInventory par2IInventory, int par3, int par4, int par5, Class ... validClasses) {
      super(par2IInventory, par3, par4, par5);
      this.setClasses(validClasses);
   }

   public SlotSpecific setMetadataSensitive() {
      this.isMetadataSensitive = true;
      return this;
   }

   public SlotSpecific setItemStacks(ItemStack ... validItemStacks) {
      this.validItemStacks = validItemStacks;
      return this;
   }

   public SlotSpecific setClasses(Class ... validClasses) {
      this.validClasses = validClasses;
      return this;
   }

   public SlotSpecific toggleInverted() {
      this.isInverted = !this.isInverted;
      return this;
   }

   public boolean isItemValid(ItemStack compareStack) {
      boolean returnValue = false;
      ItemStack[] arr$ = this.validItemStacks;
      int len$ = arr$.length;

      int i$;
      for(i$ = 0; i$ < len$; ++i$) {
         ItemStack clazz = arr$[i$];
         if(compareStack.isItemEqual(clazz) || !this.isMetadataSensitive && compareStack.getItem() == clazz.getItem()) {
            returnValue = true;
            break;
         }
      }

      if(!returnValue) {
         Class[] var7 = this.validClasses;
         len$ = var7.length;

         for(i$ = 0; i$ < len$; ++i$) {
            Class var8 = var7[i$];
            if(var8.equals(compareStack.getItem().getClass()) || var8.isInstance(compareStack.getItem())) {
               returnValue = true;
               break;
            }
         }
      }

      return this.isInverted?!returnValue:returnValue;
   }
}
