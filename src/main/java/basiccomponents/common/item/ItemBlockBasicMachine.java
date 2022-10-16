package basiccomponents.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockBasicMachine extends ItemBlock {

   public ItemBlockBasicMachine(Block block) {
      super(block);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public int getMetadata(int damage) {
      return damage;
   }

   public String getUnlocalizedName(ItemStack itemstack) {
      byte metadata = 0;
      if(itemstack.getItemDamage() >= 8) {
         metadata = 2;
      } else if(itemstack.getItemDamage() >= 4) {
         metadata = 1;
      }

      return this.field_150939_a.getUnlocalizedName() + "." + metadata;
   }

   public String getUnlocalizedName() {
      return this.field_150939_a.getUnlocalizedName() + ".0";
   }
}
