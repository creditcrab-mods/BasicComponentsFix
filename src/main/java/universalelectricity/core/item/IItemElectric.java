package universalelectricity.core.item;

import net.minecraft.item.ItemStack;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.IItemElectricityStorage;
import universalelectricity.core.item.IItemVoltage;

public interface IItemElectric extends IItemElectricityStorage, IItemVoltage {

   ElectricityPack onReceive(ElectricityPack var1, ItemStack var2);

   ElectricityPack onProvide(ElectricityPack var1, ItemStack var2);

   ElectricityPack getReceiveRequest(ItemStack var1);

   ElectricityPack getProvideRequest(ItemStack var1);
}
