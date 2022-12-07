package basiccomponents.common.item;

import basiccomponents.common.tileentity.TileEntityCopperWire;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import universalelectricity.api.energy.UnitDisplay;

public class ItemBlockCopperWire extends ItemBlock {

   IIcon icon;

   public ItemBlockCopperWire(Block block) {
      super(block);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
      this.setTextureName("basiccomponents:copperWire");
   }

   @SideOnly(Side.CLIENT)
   @Override
   public IIcon getIconFromDamage(int meta) {
      return icon;
   }

   @SideOnly(Side.CLIENT)
   @Override
   public void registerIcons(IIconRegister reg) {
      icon = reg.registerIcon("basiccomponents:copperWire");
   }

   public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
      par3List.add("Resistance: " + UnitDisplay.getDisplay(TileEntityCopperWire.RESISTANCE, UnitDisplay.Unit.RESISTANCE));
      par3List.add("Max Amps: " + UnitDisplay.getDisplay(TileEntityCopperWire.MAX_AMPS, UnitDisplay.Unit.AMPERE));
   }
}
