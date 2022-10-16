package universalelectricity.core.path;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.path.IPathCallBack;
import universalelectricity.core.path.Pathfinder;
import universalelectricity.core.vector.Vector3;

public class PathfinderAStar extends Pathfinder {

   public IPathCallBack callBackCheck;
   public Set openSet;
   public HashMap navigationMap;
   public HashMap gScore;
   public HashMap fScore;
   public Vector3 goal;


   public PathfinderAStar(IPathCallBack callBack, Vector3 goal) {
      super(callBack);
      this.goal = goal;
   }

   public boolean findNodes(Vector3 start) {
      this.openSet.add(start);
      this.gScore.put(start, Double.valueOf(0.0D));
      this.fScore.put(start, Double.valueOf(((Double)this.gScore.get(start)).doubleValue() + this.getHeuristicEstimatedCost(start, this.goal)));

      while(!this.openSet.isEmpty()) {
         Vector3 currentNode = null;
         double lowestFScore = 0.0D;
         Iterator i$ = this.openSet.iterator();

         Vector3 neighbor;
         while(i$.hasNext()) {
            neighbor = (Vector3)i$.next();
            if(currentNode == null || ((Double)this.fScore.get(neighbor)).doubleValue() < lowestFScore) {
               currentNode = neighbor;
               lowestFScore = ((Double)this.fScore.get(neighbor)).doubleValue();
            }
         }

         if(currentNode == null) {
            break;
         }

         if(this.callBackCheck.onSearch(this, currentNode)) {
            return false;
         }

         if(currentNode.equals(this.goal)) {
            super.results = this.reconstructPath(this.navigationMap, this.goal);
            return true;
         }

         this.openSet.remove(currentNode);
         super.closedSet.add(currentNode);
         i$ = this.getNeighborNodes(currentNode).iterator();

         while(i$.hasNext()) {
            neighbor = (Vector3)i$.next();
            double tentativeGScore = ((Double)this.gScore.get(currentNode)).doubleValue() + currentNode.distanceTo(neighbor);
            if((!super.closedSet.contains(neighbor) || tentativeGScore < ((Double)this.gScore.get(neighbor)).doubleValue()) && (!this.openSet.contains(neighbor) || tentativeGScore < ((Double)this.gScore.get(neighbor)).doubleValue())) {
               this.navigationMap.put(neighbor, currentNode);
               this.gScore.put(neighbor, Double.valueOf(tentativeGScore));
               this.fScore.put(neighbor, Double.valueOf(((Double)this.gScore.get(neighbor)).doubleValue() + this.getHeuristicEstimatedCost(neighbor, this.goal)));
               this.openSet.add(neighbor);
            }
         }
      }

      return false;
   }

   public Pathfinder reset() {
      this.openSet = new HashSet();
      this.navigationMap = new HashMap();
      return super.reset();
   }

   public Set reconstructPath(HashMap nagivationMap, Vector3 current_node) {
      HashSet path = new HashSet();
      path.add(current_node);
      if(nagivationMap.containsKey(current_node)) {
         path.addAll(this.reconstructPath(nagivationMap, (Vector3)nagivationMap.get(current_node)));
         return path;
      } else {
         return path;
      }
   }

   public double getHeuristicEstimatedCost(Vector3 start, Vector3 goal) {
      return start.distanceTo(goal);
   }

   public Set getNeighborNodes(Vector3 vector) {
      if(this.callBackCheck != null) {
         return this.callBackCheck.getConnectedNodes(this, vector);
      } else {
         HashSet neighbors = new HashSet();

         for(int i = 0; i < 6; ++i) {
            neighbors.add(vector.clone().modifyPositionFromSide(ForgeDirection.getOrientation(i)));
         }

         return neighbors;
      }
   }
}
