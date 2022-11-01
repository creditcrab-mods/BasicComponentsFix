package basiccomponents.common.item;

import basiccomponents.common.BCTab;
import basiccomponents.common.BasicComponents;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.ItemElectric;

public class ItemBattery extends ItemElectric {

   public ItemBattery(String name) {
      super();
      this.setUnlocalizedName("basiccomponents:" + name);
      this.setCreativeTab(BCTab.INSTANCE);
   }

   @SideOnly(Side.CLIENT)
   @Override
   public void registerIcons(IIconRegister iconRegister) {
      this.itemIcon = iconRegister.registerIcon(this.getUnlocalizedName().replace("item.", ""));
   }

   public double getMaxJoules(ItemStack itemStack) {
      return 1000000.0D;
   }

   public double getVoltage(ItemStack itemStack) {
      return 25.0D;
   }
}
