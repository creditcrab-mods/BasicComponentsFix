package universalelectricity.prefab.implement;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IRotatable {

   ForgeDirection getDirection(IBlockAccess var1, int var2, int var3, int var4);

   void setDirection(World var1, int var2, int var3, int var4, ForgeDirection var5);
}
