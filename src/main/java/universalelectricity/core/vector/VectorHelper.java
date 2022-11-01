package universalelectricity.core.vector;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.block.IConnector;

public class VectorHelper {

   public static final int[][] RELATIVE_MATRIX = new int[][]{{3, 2, 1, 0, 5, 4}, {4, 5, 0, 1, 2, 3}, {0, 1, 3, 2, 4, 5}, {0, 1, 2, 3, 5, 4}, {0, 1, 5, 4, 3, 2}, {0, 1, 4, 5, 2, 3}};


   public static ForgeDirection getOrientationFromSide(ForgeDirection front, ForgeDirection side) {
      return front != ForgeDirection.UNKNOWN && side != ForgeDirection.UNKNOWN?ForgeDirection.getOrientation(RELATIVE_MATRIX[front.ordinal()][side.ordinal()]):ForgeDirection.UNKNOWN;
   }

   @Deprecated
   public static TileEntity getConnectorFromSide(World world, Vector3 position, ForgeDirection side) {
      TileEntity tileEntity = getTileEntityFromSide(world, position, side);
      return tileEntity instanceof IConnector && ((IConnector)tileEntity).canConnect(getOrientationFromSide(side, ForgeDirection.NORTH))?tileEntity:null;
   }

   public static TileEntity getTileEntityFromSide(World world, Vector3 position, ForgeDirection side) {
      return position.clone().modifyPositionFromSide(side).getTileEntity(world);
   }

}
