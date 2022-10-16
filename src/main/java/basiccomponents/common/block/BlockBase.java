package basiccomponents.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockBase extends Block {

   public BlockBase(String name) {
      super(Material.rock);
      this.setCreativeTab(CreativeTabs.tabBlock);
      this.setBlockName("basiccomponents:" + name);
      this.setHardness(2.0F);
      this.setBlockTextureName("basiccomponents:" + name);
      
   }
}
