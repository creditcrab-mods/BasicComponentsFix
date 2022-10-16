package universalelectricity.core.item;

import net.minecraft.item.ItemStack;

public interface IItemElectricityStorage {

   double getJoules(ItemStack var1);

   void setJoules(double var1, ItemStack var3);

   double getMaxJoules(ItemStack var1);
}
