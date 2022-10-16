package universalelectricity.core.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.IItemElectric;

public class ElectricItemHelper {

   public static double chargeItem(ItemStack itemStack, double joules, double voltage) {
      if(itemStack != null && itemStack.getItem() instanceof IItemElectric) {
         IItemElectric electricItem = (IItemElectric)itemStack.getItem();
         double providingWatts = Math.min(joules, electricItem.getReceiveRequest(itemStack).getWatts());
         if(providingWatts > 0.0D) {
            ElectricityPack providedElectricity = electricItem.onReceive(ElectricityPack.getFromWatts(providingWatts, voltage), itemStack);
            return providedElectricity.getWatts();
         }
      }

      return 0.0D;
   }

   public static double dechargeItem(ItemStack itemStack, double joules, double voltage) {
      if(itemStack != null && itemStack.getItem() instanceof IItemElectric) {
         IItemElectric electricItem = (IItemElectric)itemStack.getItem();
         double requestingWatts = Math.min(joules, electricItem.getProvideRequest(itemStack).getWatts());
         if(requestingWatts > 0.0D) {
            ElectricityPack receivedElectricity = electricItem.onProvide(ElectricityPack.getFromWatts(requestingWatts, voltage), itemStack);
            return receivedElectricity.getWatts();
         }
      }

      return 0.0D;
   }

   public static ItemStack getWithCharge(ItemStack itemStack, double joules) {
      if(itemStack != null && itemStack.getItem() instanceof IItemElectric) {
         ((IItemElectric)itemStack.getItem()).setJoules(joules, itemStack);
         return itemStack;
      } else {
         return itemStack;
      }
   }

   public static ItemStack getWithCharge(Item item, double joules) {
      return getWithCharge(new ItemStack(item), joules);
   }

   public static ItemStack getCloneWithCharge(ItemStack itemStack, double joules) {
      return getWithCharge(itemStack.copy(), joules);
   }

   public static ItemStack getUncharged(ItemStack itemStack) {
      return getWithCharge(itemStack, 0.0D);
   }

   public static ItemStack getUncharged(Item item) {
      return getUncharged(new ItemStack(item));
   }
}
