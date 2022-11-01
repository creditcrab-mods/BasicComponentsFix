package basiccomponents.common.block;

import basiccomponents.common.BCTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockBase extends Block {

   public BlockBase(String name) {
      super(Material.rock);
      this.setCreativeTab(BCTab.INSTANCE);
      this.setBlockName("basiccomponents:" + name);
      this.setHardness(2.0F);
      this.setBlockTextureName("basiccomponents:" + name);
      
   }
}
