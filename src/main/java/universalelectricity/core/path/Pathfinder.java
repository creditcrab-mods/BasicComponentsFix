package universalelectricity.core.path;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import universalelectricity.core.path.IPathCallBack;
import universalelectricity.core.vector.Vector3;

public class Pathfinder {

   public IPathCallBack callBackCheck;
   public Set closedSet;
   public Set results;


   public Pathfinder(IPathCallBack callBack) {
      this.callBackCheck = callBack;
      this.reset();
   }

   public boolean findNodes(Vector3 currentNode) {
      this.closedSet.add(currentNode);
      if(this.callBackCheck.onSearch(this, currentNode)) {
         return false;
      } else {
         Iterator i$ = this.callBackCheck.getConnectedNodes(this, currentNode).iterator();

         Vector3 node;
         do {
            if(!i$.hasNext()) {
               return false;
            }

            node = (Vector3)i$.next();
         } while(this.closedSet.contains(node) || !this.findNodes(node));

         return true;
      }
   }

   public Pathfinder init(Vector3 startNode) {
      this.findNodes(startNode);
      return this;
   }

   public Pathfinder reset() {
      this.closedSet = new HashSet();
      this.results = new HashSet();
      return this;
   }
}
