package basiccomponents.common.item;

import basiccomponents.common.item.ItemBase;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.prefab.implement.IToolConfigurator;

public class ItemWrench extends ItemBase implements IToolConfigurator {

   public ItemWrench() {
      super("wrench");
      this.setMaxStackSize(1);
   }

   public boolean canWrench(EntityPlayer entityPlayer, int x, int y, int z) {
      return true;
   }

   public void wrenchUsed(EntityPlayer entityPlayer, int x, int y, int z) {}

   @Override
   public boolean onItemUseFirst(ItemStack stack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
      Block block = world.getBlock(x, y, z);
      if(block != Blocks.furnace && block != Blocks.lit_furnace && block != Blocks.dropper && block != Blocks.hopper && block != Blocks.dispenser && block != Blocks.piston && block != Blocks.sticky_piston) {
         return false;
      } else {
         int metadata = world.getBlockMetadata(x, y, z);
         int[] rotationMatrix = new int[]{1, 2, 3, 4, 5, 0};
         if(block == Blocks.furnace || block == Blocks.lit_furnace) {
            rotationMatrix = ForgeDirection.ROTATION_MATRIX[0];
         }

         world.setBlockMetadataWithNotify(x, y, z, ForgeDirection.getOrientation(rotationMatrix[metadata]).ordinal(), 3);
         this.wrenchUsed(entityPlayer, x, y, z);
         return true;
      }
   }

   @Override
   public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
      return false;
   }

   @Override
   public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
      return true;
   }
}
