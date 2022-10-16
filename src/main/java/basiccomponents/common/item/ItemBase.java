package basiccomponents.common.item;

import basiccomponents.common.BasicComponents;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class ItemBase extends Item {

   protected final IIcon[] icons = new IIcon[256];

   public ItemBase(String name) {
      super();
      this.setUnlocalizedName("basiccomponents:" + name);
      this.setTextureName("basiccomponents:" + name);
      this.setNoRepair();
   }
}
