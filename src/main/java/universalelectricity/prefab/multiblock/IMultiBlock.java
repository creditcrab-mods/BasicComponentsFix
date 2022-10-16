package universalelectricity.prefab.multiblock;

import net.minecraft.tileentity.TileEntity;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.multiblock.IBlockActivate;

public interface IMultiBlock extends IBlockActivate {

   void onCreate(Vector3 var1);

   void onDestroy(TileEntity var1);
}
