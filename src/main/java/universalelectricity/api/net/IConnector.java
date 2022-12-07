package universalelectricity.api.net;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Applied to TileEntities that has an instance of an electricity network.
 * 
 * @author Calclavia, tilera
 * 
 */
public interface IConnector<N> extends INetworkProvider<N>, IConnectable
{
	/**
	 * Gets an array of all the connected IConnecables that this connector is connected to. This
	 * should correspond to the ForgeDirection index.
	 * 
	 * @return An array of length "6".
	 */
	public TileEntity[] getAdjacentConnections();

	/**
	 * Gets this connector instance. Used specially for MultiPart connections.
	 * 
	 * @return The instance, in most cases, just return "this".
	 */
	public IConnector<N> getInstance(ForgeDirection dir);

}
