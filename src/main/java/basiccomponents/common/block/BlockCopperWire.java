package basiccomponents.common.block;

import basiccomponents.common.BCTab;
import basiccomponents.common.BasicComponents;
import basiccomponents.common.tileentity.TileEntityCopperWire;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.prefab.block.BlockConductor;

public class BlockCopperWire extends BlockConductor {

   public BlockCopperWire() {
      super(Material.cloth);
      this.setBlockName("basiccomponents:copperWire");
      this.setStepSound(soundTypeCloth);
      this.setResistance(0.2F);
      this.setHardness(0.1F);
      this.setBlockBounds(0.3F, 0.3F, 0.3F, 0.7F, 0.7F, 0.7F);
      this.setCreativeTab(BCTab.INSTANCE);
      //TODO this
      //Block.setBurnProperties(this.field_71990_ca, 30, 60);
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public int getRenderType() {
      return -1;
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int meta) {
      return new TileEntityCopperWire();
   }
}
