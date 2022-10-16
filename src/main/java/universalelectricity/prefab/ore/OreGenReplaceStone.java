package universalelectricity.prefab.ore;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import universalelectricity.prefab.ore.OreGenReplace;

public class OreGenReplaceStone extends OreGenReplace {

   public OreGenReplaceStone(String name, String oreDiectionaryName, ItemStack stack, int minGenerateLevel, int maxGenerateLevel, int amountPerChunk, int amountPerBranch, String harvestTool, int harvestLevel) {
      super(name, oreDiectionaryName, stack, Blocks.stone, minGenerateLevel, maxGenerateLevel, amountPerChunk, amountPerBranch, harvestTool, harvestLevel);
   }

   public OreGenReplaceStone(String name, String oreDiectionaryName, ItemStack stack, int maxGenerateLevel, int amountPerChunk, int amountPerBranch) {
      this(name, oreDiectionaryName, stack, 0, maxGenerateLevel, amountPerChunk, amountPerBranch, "pickaxe", 1);
   }
}
