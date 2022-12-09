package universalelectricity.api;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/** A module to extend for compatibility with other energy systems. */
public abstract class CompatibilityModule
{
	public static final Set<CompatibilityModule> loadedModules = new LinkedHashSet<>();

	/** A cache to know which module to use with when facing objects with a specific class. */
	public static final HashMap<Class<?>, CompatibilityModule> energyHandlerCache = new HashMap<>();
	public static final HashMap<Class<?>, CompatibilityModule> energyStorageCache = new HashMap<>();

	public static void register(CompatibilityModule module)
	{
		loadedModules.add(module);
	}

	/** Can the handler connect to this specific direction? */
	public static boolean canConnect(Object handler, ForgeDirection direction, Object source)
	{
		if (isHandler(handler))
		{
			return energyHandlerCache.get(handler.getClass()).doCanConnect(handler, direction, source);
		}

		return false;
	}

	/**
	 * Make the handler receive energy.
	 * 
	 * @return The actual energy that was used.
	 */
	public static double receiveEnergy(Object handler, ForgeDirection direction, double energy, boolean doReceive)
	{
		if (isHandler(handler))
		{
			return energyHandlerCache.get(handler.getClass()).doReceiveEnergy(handler, direction, energy, doReceive);
		}

		return 0;
	}

	/**
	 * Make the handler extract energy.
	 * 
	 * @return The actual energy that was extract.
	 */
	public static double extractEnergy(Object handler, ForgeDirection direction, double energy, boolean doExtract)
	{
		if (isHandler(handler))
		{
			return energyHandlerCache.get(handler.getClass()).doExtractEnergy(handler, direction, energy, doExtract);
		}

		return 0;
	}

	/**
	 * Gets the energy stored in the handler.
	 */
	public static double getEnergy(Object handler, ForgeDirection direction)
	{
		if (isEnergyContainer(handler))
		{
			return energyStorageCache.get(handler.getClass()).doGetEnergy(handler, direction);
		}

		return 0;
	}

	/**
	 * Charges an item
	 * 
	 * @return The actual energy that was accepted.
	 */
	public static double chargeItem(ItemStack itemStack, double energy, boolean doCharge)
	{
		if (itemStack != null && isHandler(itemStack.getItem()))
		{
			return energyHandlerCache.get(itemStack.getItem().getClass()).doChargeItem(itemStack, energy, doCharge);
		}

		return 0;
	}

	/**
	 * Discharges an item
	 * 
	 * @return The actual energy that was removed.
	 */
	public static double dischargeItem(ItemStack itemStack, double energy, boolean doCharge)
	{
		if (itemStack != null && isHandler(itemStack.getItem()))
		{
			return energyHandlerCache.get(itemStack.getItem().getClass()).doDischargeItem(itemStack, energy, doCharge);
		}

		return 0;
	}

	/**
	 * Gets the itemStack with a specific charge.
	 * 
	 * @return ItemStack of electrical/energy item.
	 */
	public static ItemStack getItemWithCharge(ItemStack itemStack, double energy)
	{
		if (itemStack != null && isHandler(itemStack.getItem()))
		{
			return energyHandlerCache.get(itemStack.getItem().getClass()).doGetItemWithCharge(itemStack, energy);
		}

		return null;
	}

	/**
	 * Is this object a valid energy handler?
	 * 
	 * @param True if the handler can store energy. This can be for items and blocks.
	 */
	public static boolean isHandler(Object handler)
	{
		if (handler != null)
		{
			Class clazz = handler.getClass();

			if (energyHandlerCache.containsKey(clazz))
			{
				return true;
			}

			for (CompatibilityModule module : CompatibilityModule.loadedModules)
			{
				if (module.doIsHandler(handler))
				{
					energyHandlerCache.put(clazz, module);
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Is this object able to store energy?
	 * 
	 * @param handler
	 * @return True if the handler can store energy. The handler MUST be a block.
	 */
	public static boolean isEnergyContainer(Object handler)
	{
		if (handler != null)
		{
			Class clazz = handler.getClass();

			if (energyStorageCache.containsKey(clazz))
			{
				return true;
			}

			for (CompatibilityModule module : CompatibilityModule.loadedModules)
			{
				if (module.doIsEnergyContainer(handler))
				{
					energyStorageCache.put(clazz, module);
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Blocks only
	 */
	public static double getMaxEnergy(Object handler, ForgeDirection direction)
	{
		if (isEnergyContainer(handler))
		{
			return energyStorageCache.get(handler.getClass()).doGetMaxEnergy(handler, direction);
		}

		return 0;
	}

	public static double getInputVoltage(Object handler)
	{
		if (isHandler(handler))
		{
			return energyHandlerCache.get(handler.getClass()).doGetInputVoltage(handler);
		}

		return 0;
	}

	public static double getOutputVoltage(Object handler)
	{
		if (isHandler(handler))
		{
			return energyHandlerCache.get(handler.getClass()).doGetOutputVoltage(handler);
		}

		return 0;
	}

	public static boolean canReceive(Object handler, ForgeDirection side)
	{
		if (isHandler(handler))
		{
			return energyHandlerCache.get(handler.getClass()).doCanReceive(handler, side);
		}

		return false;
	}

	public static boolean canExtract(Object handler, ForgeDirection side)
	{
		if (isHandler(handler))
		{
			return energyHandlerCache.get(handler.getClass()).doCanExtract(handler, side);
		}

		return false;
	}

	public static double getDemandedJoules(Object handler)
	{
		if (isHandler(handler))
		{
			return energyHandlerCache.get(handler.getClass()).doGetDemandedJoules(handler);
		}

		return 0;
	}

	public static double getProvidedJoules(Object handler)
	{
		if (isHandler(handler))
		{
			return energyHandlerCache.get(handler.getClass()).doGetProvidedJoules(handler);
		}

		return 0;
	}

	public static double getEnergyItem(ItemStack itemStack)
	{
		if (itemStack != null && isHandler(itemStack.getItem()))
		{
			return energyHandlerCache.get(itemStack.getItem().getClass()).doGetEnergyItem(itemStack);
		}

		return 0;
	}

	public static double getMaxEnergyItem(ItemStack itemStack)
	{
		if (itemStack != null && isHandler(itemStack.getItem()))
		{
			return energyHandlerCache.get(itemStack.getItem().getClass()).doGetMaxEnergyItem(itemStack);
		}

		return 0;
	}

	public abstract double doReceiveEnergy(Object handler, ForgeDirection direction, double energy, boolean doReceive);

	public abstract double doExtractEnergy(Object handler, ForgeDirection direction, double energy, boolean doExtract);

	/**
	 * Charges an item with the given energy
	 * 
	 * @param itemStack - item stack that is the item
	 * @param joules - input energy
	 * @param docharge - do the action
	 * @return amount of energy accepted
	 */
	public abstract double doChargeItem(ItemStack itemStack, double joules, boolean docharge);

	/**
	 * discharges an item with the given energy
	 * 
	 * @param itemStack - item stack that is the item
	 * @param joules - input energy
	 * @param docharge - do the action
	 * @return amount of energy that was removed
	 */
	public abstract double doDischargeItem(ItemStack itemStack, double joules, boolean doDischarge);

	public abstract boolean doIsHandler(Object obj);

	public abstract boolean doIsEnergyContainer(Object obj);

	public abstract double doGetEnergy(Object obj, ForgeDirection direction);

	public abstract boolean doCanConnect(Object obj, ForgeDirection direction, Object source);

	public abstract ItemStack doGetItemWithCharge(ItemStack itemStack, double energy);

	public abstract double doGetMaxEnergy(Object handler, ForgeDirection direction);

	public abstract double doGetEnergyItem(ItemStack is);

	public abstract double doGetMaxEnergyItem(ItemStack is);

	public abstract double doGetInputVoltage(Object handler);

	public abstract double doGetOutputVoltage(Object handler);

	public abstract boolean doCanReceive(Object handler, ForgeDirection side);

	public abstract boolean doCanExtract(Object handler, ForgeDirection side);

	public abstract double doGetDemandedJoules(Object handler);

	public abstract double doGetProvidedJoules(Object handler);
}
