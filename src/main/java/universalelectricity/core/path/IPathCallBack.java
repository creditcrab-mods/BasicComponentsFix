package universalelectricity.core.path;

import java.util.Set;
import universalelectricity.core.path.Pathfinder;
import universalelectricity.core.vector.Vector3;

public interface IPathCallBack {

   Set getConnectedNodes(Pathfinder var1, Vector3 var2);

   boolean onSearch(Pathfinder var1, Vector3 var2);
}
