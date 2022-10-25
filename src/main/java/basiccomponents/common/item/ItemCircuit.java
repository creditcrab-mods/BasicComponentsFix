package basiccomponents.common.item;

import basiccomponents.common.item.ItemBase;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemCircuit extends ItemBase {

   public static final String[] TYPES = new String[]{"circuitBasic", "circuitAdvanced", "circuitElite"};


   public ItemCircuit(int texture) {
      super("circuit");
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public int getMetadata(int damage) {
      return damage;
   }

   public String getUnlocalizedName(ItemStack itemStack) {
      return "item.basiccomponents:" + TYPES[itemStack.getItemDamage()];
   }

   @Override
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List list) {
      for(int i = 0; i < TYPES.length; ++i) {
         list.add(new ItemStack(this, 1, i));
      }

   }

}
