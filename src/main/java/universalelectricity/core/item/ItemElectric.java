package universalelectricity.core.item;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.item.IItemElectric;

public abstract class ItemElectric extends Item implements IItemElectric {

   public ItemElectric() {
      super();
      this.setMaxStackSize(1);
      this.setMaxDamage(100);
      this.setNoRepair();
   }

   public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
      String color = "";
      double joules = this.getJoules(itemStack);
      if(joules <= this.getMaxJoules(itemStack) / 3.0D) {
         color = "§4";
      } else if(joules > this.getMaxJoules(itemStack) * 2.0D / 3.0D) {
         color = "§2";
      } else {
         color = "§6";
      }

      list.add(color + UnitDisplay.getDisplay(joules, UnitDisplay.Unit.JOULES) + "/" + UnitDisplay.getDisplay(this.getMaxJoules(itemStack), UnitDisplay.Unit.JOULES));
   }

   public void onCreated(ItemStack itemStack, World par2World, EntityPlayer par3EntityPlayer) {
      this.setJoules(0.0D, itemStack);
   }

   public ElectricityPack onReceive(ElectricityPack electricityPack, ItemStack itemStack) {
      double rejectedElectricity = Math.max(this.getJoules(itemStack) + electricityPack.getWatts() - this.getMaxJoules(itemStack), 0.0D);
      double joulesToStore = electricityPack.getWatts() - rejectedElectricity;
      this.setJoules(this.getJoules(itemStack) + joulesToStore, itemStack);
      return ElectricityPack.getFromWatts(joulesToStore, this.getVoltage(itemStack));
   }

   public ElectricityPack onProvide(ElectricityPack electricityPack, ItemStack itemStack) {
      double electricityToUse = Math.min(this.getJoules(itemStack), electricityPack.getWatts());
      this.setJoules(this.getJoules(itemStack) - electricityToUse, itemStack);
      return ElectricityPack.getFromWatts(electricityToUse, this.getVoltage(itemStack));
   }

   public ElectricityPack getReceiveRequest(ItemStack itemStack) {
      return ElectricityPack.getFromWatts(Math.min(this.getMaxJoules(itemStack) - this.getJoules(itemStack), this.getTransferRate(itemStack)), this.getVoltage(itemStack));
   }

   public ElectricityPack getProvideRequest(ItemStack itemStack) {
      return ElectricityPack.getFromWatts(Math.min(this.getJoules(itemStack), this.getTransferRate(itemStack)), this.getVoltage(itemStack));
   }

   public double getTransferRate(ItemStack itemStack) {
      return this.getMaxJoules(itemStack) * 0.01D;
   }

   public void setJoules(double joules, ItemStack itemStack) {
      if(itemStack.getTagCompound() == null) {
         itemStack.setTagCompound(new NBTTagCompound());
      }

      double electricityStored = Math.max(Math.min(joules, this.getMaxJoules(itemStack)), 0.0D);
      itemStack.getTagCompound().setDouble("electricity", electricityStored);
      itemStack.setItemDamage((int)(100.0D - electricityStored / this.getMaxJoules(itemStack) * 100.0D));
   }

   public double getJoules(ItemStack itemStack) {
      if(itemStack.getTagCompound() == null) {
         return 0.0D;
      } else {
         double electricityStored = itemStack.getTagCompound().getDouble("electricity");
         itemStack.setItemDamage((int)(100.0D - electricityStored / this.getMaxJoules(itemStack) * 100.0D));
         return electricityStored;
      }
   }

   @Override
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(ElectricItemHelper.getUncharged(new ItemStack(this)));
      ItemStack chargedItem = new ItemStack(this);
      par3List.add(ElectricItemHelper.getWithCharge(chargedItem, this.getMaxJoules(chargedItem)));
   }
}
