package basiccomponents.common.item;

import basiccomponents.common.item.ItemBase;
import java.util.List;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.IItemElectric;

public class ItemInfiniteBattery extends ItemBase implements IEnergyContainerItem {

   public ItemInfiniteBattery(String name) {
      super(name);
      this.setMaxStackSize(1);
      this.setNoRepair();
   }

   public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
      par3List.add("§2Infinite");
   }


   @Override
   public int receiveEnergy(ItemStack itemStack, int i, boolean b) {
      return Integer.MAX_VALUE;
   }

   @Override
   public int extractEnergy(ItemStack itemStack, int i, boolean b) {
      return Integer.MAX_VALUE;
   }

   @Override
   public int getEnergyStored(ItemStack itemStack) {
      return -1;
   }

   @Override
   public int getMaxEnergyStored(ItemStack itemStack) {
      return -1;
   }
}
