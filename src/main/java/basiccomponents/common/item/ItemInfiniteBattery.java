package basiccomponents.common.item;

import basiccomponents.common.item.ItemBase;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.IItemElectric;

public class ItemInfiniteBattery extends ItemBase implements IItemElectric {

   public ItemInfiniteBattery(String name) {
      super(name);
      this.setMaxStackSize(1);
      this.setNoRepair();
      this.setCreativeTab(CreativeTabs.tabRedstone);
   }

   public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
      par3List.add("§2Infinite");
   }

   public double getJoules(ItemStack itemStack) {
      return this.getMaxJoules(itemStack);
   }

   public void setJoules(double joules, ItemStack itemStack) {}

   public double getMaxJoules(ItemStack itemStack) {
      return Double.POSITIVE_INFINITY;
   }

   public double getVoltage(ItemStack itemStack) {
      return 25.0D;
   }

   public ElectricityPack onReceive(ElectricityPack electricityPack, ItemStack itemStack) {
      return electricityPack;
   }

   public ElectricityPack onProvide(ElectricityPack electricityPack, ItemStack itemStack) {
      return electricityPack;
   }

   public ElectricityPack getReceiveRequest(ItemStack itemStack) {
      return new ElectricityPack(Double.POSITIVE_INFINITY, this.getVoltage(itemStack));
   }

   public ElectricityPack getProvideRequest(ItemStack itemStack) {
      return new ElectricityPack(Double.POSITIVE_INFINITY, this.getVoltage(itemStack));
   }
}
