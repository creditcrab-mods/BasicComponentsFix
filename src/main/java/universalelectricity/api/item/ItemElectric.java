package universalelectricity.api.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.world.World;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.api.energy.UnitDisplay.Unit;

/** Extend from this class if your item requires electricity or to be charged. Optionally, you can
 * implement IItemElectric instead.
 * 
 * @author Calclavia */
public abstract class ItemElectric extends Item implements IEnergyItem, IVoltageItem
{
    private static final String ENERGY_NBT = "electricity";

    public ItemElectric()
    {
        super();
        setMaxStackSize(1);
        setMaxDamage(100);
        setNoRepair();
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4)
    {
        String color = "";
        double joules = getEnergy(itemStack);

        if (joules <= getEnergyCapacity(itemStack) / 3)
        {
            color = "\u00a74";
        }
        else if (joules > getEnergyCapacity(itemStack) * 2 / 3)
        {
            color = "\u00a72";
        }
        else
        {
            color = "\u00a76";
        }

        list.add(color + UnitDisplay.getDisplayShort(joules, Unit.JOULES) + "/" + UnitDisplay.getDisplayShort(getEnergyCapacity(itemStack), Unit.JOULES));
    }

    /** Makes sure the item is uncharged when it is crafted and not charged. Change this if you do
     * not want this to happen! */
    @Override
    public void onCreated(ItemStack itemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        setEnergy(itemStack, 0);
    }

    @Override
    public double recharge(ItemStack itemStack, double energy, boolean doReceive)
    {
        double energyReceived = Math.min(getEnergyCapacity(itemStack) - getEnergy(itemStack), Math.min(getTransferRate(itemStack), energy));

        if (doReceive)
        {
            setEnergy(itemStack, getEnergy(itemStack) + energyReceived);
        }

        return energyReceived;
    }

    public double getTransferRate(ItemStack itemStack)
    {
        return getEnergyCapacity(itemStack) / 100;
    }

    @Override
    public double discharge(ItemStack itemStack, double energy, boolean doTransfer)
    {
        double energyExtracted = Math.min(getEnergy(itemStack), Math.min(getTransferRate(itemStack), energy));

        if (doTransfer)
        {
            setEnergy(itemStack, getEnergy(itemStack) - energyExtracted);
        }

        return energyExtracted;
    }

    @Override
    public double getVoltage(ItemStack itemStack)
    {
        return 120.0;
    }

    @Override
    public void setEnergy(ItemStack itemStack, double joules)
    {
        if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        double electricityStored = Math.max(Math.min(joules, getEnergyCapacity(itemStack)), 0);
        itemStack.getTagCompound().setDouble(ENERGY_NBT, electricityStored);
        itemStack.setItemDamage((int) (100 - ((double) electricityStored / (double) getEnergyCapacity(itemStack)) * 100));
    }

    public double getTransfer(ItemStack itemStack)
    {
        return getEnergyCapacity(itemStack) - getEnergy(itemStack);
    }

    @Override
    public double getEnergy(ItemStack itemStack)
    {
        if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        double energyStored = 0;

        if (itemStack.getTagCompound().hasKey(ENERGY_NBT))
        {
            // Backwards compatibility
            NBTBase obj = itemStack.getTagCompound().getTag(ENERGY_NBT);

            if (obj instanceof NBTTagFloat)
            {
                energyStored = ((NBTTagFloat) obj).func_150286_g();
            }
            else if (obj instanceof NBTTagDouble)
            {
                energyStored = ((NBTTagDouble) obj).func_150286_g();
            }
        }

        itemStack.setItemDamage((int) (100 - ((double) energyStored / (double) getEnergyCapacity(itemStack)) * 100));
        return energyStored;
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(CompatibilityModule.getItemWithCharge(new ItemStack(this), 0));
        par3List.add(CompatibilityModule.getItemWithCharge(new ItemStack(this), getEnergyCapacity(new ItemStack(this))));
    }
}
