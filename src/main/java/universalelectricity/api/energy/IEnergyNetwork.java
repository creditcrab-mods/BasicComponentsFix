package universalelectricity.api.energy;

import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.net.INodeNetwork;
import universalelectricity.api.net.IUpdate;

/**
 * The Energy Network for energy items and blocks.
 * 
 * @author Calclavia
 */
public interface IEnergyNetwork extends INodeNetwork<IEnergyNetwork, IConductor, Object>, IUpdate
{
	/**
	 * Produces power to the energy network.
	 * 
	 * @param conductor - The conductor that is producing into the energy.
	 * @param side - The direction the source is producing out towards.
	 * @param receive - The amount that is produced.
	 * @return The amount that was accepted by the network.
	 */
	public double produce(IConductor conductor, ForgeDirection from, double amount, boolean doProduce);

	/**
	 * @return The current buffer in the network that is going sent to all energy handlers.
	 */
	public double getBuffer();

	/**
	 * @return The last buffer in the network that was sent to all energy handlers.
	 */
	public double getLastBuffer();

	/**
	 * Gets an estimated value of what the network wants for energy
	 */
	public double getRequest();

	/**
	 * Gets a value that represents the amount of energy lost in the network
	 */
	public double getResistance();

	/**
	 * Used by conductors to load their internal buffers to the network. This should be called when
	 * reading NBT data.
	 * 
	 * @param conductor
	 */
	public double getBufferOf(IConductor conductor);

	/**
	 * Used by conductors to load their internal buffers to the network. This should be called when
	 * writing NBT data.
	 * 
	 * @param conductor
	 */
	public void setBufferFor(IConductor conductor, double buffer);

	/**
	 * Sets the buffer of the network.
	 * 
	 * @param newBuffer
	 */
	public void setBuffer(double newBuffer);

}
