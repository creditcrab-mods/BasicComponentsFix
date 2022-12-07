package universalelectricity.core.path;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.net.IConnector;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IConnectionProvider;
import universalelectricity.core.path.IPathCallBack;
import universalelectricity.core.path.Pathfinder;
import universalelectricity.core.vector.Vector3;

public class PathfinderChecker extends Pathfinder {

   public PathfinderChecker(final World world, final IConnectionProvider targetConnector, final IConnectionProvider ... ignoreConnector) {
      super(new IPathCallBack() {
         public Set getConnectedNodes(Pathfinder finder, Vector3 currentNode) {
            HashSet neighbors = new HashSet();

            for(int i = 0; i < 6; ++i) {
               ForgeDirection direction = ForgeDirection.getOrientation(i);
               Vector3 position = currentNode.clone().modifyPositionFromSide(direction);
               TileEntity connectedBlock = position.getTileEntity(world);
               if(connectedBlock instanceof IConductor && !Arrays.asList(ignoreConnector).contains(connectedBlock) && ((IConductor)connectedBlock).canConnect(direction.getOpposite())) {
                  neighbors.add(position);
               }
            }

            return neighbors;
         }
         public boolean onSearch(Pathfinder finder, Vector3 node) {
            if(node.getTileEntity(world) == targetConnector) {
               finder.results.add(node);
               return true;
            } else {
               return false;
            }
         }
      });
   }

   public PathfinderChecker(final World world, final IConnector targetConnector, final IConnector[] ignoreConnector) {
      super(new IPathCallBack() {
         public Set getConnectedNodes(Pathfinder finder, Vector3 currentNode) {
            HashSet neighbors = new HashSet();

            for(int i = 0; i < 6; ++i) {
               ForgeDirection direction = ForgeDirection.getOrientation(i);
               Vector3 position = currentNode.clone().modifyPositionFromSide(direction);
               TileEntity connectedBlock = position.getTileEntity(world);
               if(connectedBlock instanceof IConductor && !Arrays.asList(ignoreConnector).contains(connectedBlock) && ((IConductor)connectedBlock).canConnect(direction.getOpposite())) {
                  neighbors.add(position);
               }
            }

            return neighbors;
         }
         public boolean onSearch(Pathfinder finder, Vector3 node) {
            if(node.getTileEntity(world) == targetConnector) {
               finder.results.add(node);
               return true;
            } else {
               return false;
            }
         }
      });
   }
}
