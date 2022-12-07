package universalelectricity.api.electricity;

import net.minecraft.tileentity.TileEntity;
import universalelectricity.api.energy.IEnergyNetwork;
import universalelectricity.core.electricity.ElectricityPack;

/**
 * Extended version of the energy network that properly implements amperage and voltage.
 * If you want amps get last buffer and divide it by the voltage
 * 
 * @author DarkGuardsman
 */
public interface IElectricalNetwork extends IEnergyNetwork
{
	/** Gets the current voltage of the network at this point. */
	public double getVoltage();

}
