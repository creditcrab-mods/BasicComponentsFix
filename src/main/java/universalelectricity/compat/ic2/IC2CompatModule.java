package universalelectricity.compat.ic2;

import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.CompatibilityType;

public class IC2CompatModule extends CompatibilityModule {

    private TileEntity getENetTile(Object handler) {
        if (handler instanceof TileEntity) {
            TileEntity te = (TileEntity) handler;
            if (te instanceof IEnergyTile) {
                return te;
            }
            return EnergyNet.instance.getTileEntity(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord);
        }
        return null;
    }

    @Override
    public double doReceiveEnergy(Object handler, ForgeDirection direction, double energy, boolean doReceive) {
        TileEntity tile = getENetTile(handler);

        if (tile instanceof IEnergySink) {
            IEnergySink sink = (IEnergySink) tile;
            double demand = sink.getDemandedEnergy();
            double toReceive = Math.min(demand, CompatibilityType.INDUSTRIALCRAFT.fromJoules(energy));

            if (doReceive) {
                double leftover = sink.injectEnergy(direction, toReceive, 1);
                return CompatibilityType.INDUSTRIALCRAFT.toJoules(Math.max(0, toReceive - leftover));
            }

            return CompatibilityType.INDUSTRIALCRAFT.toJoules(toReceive);
        }
        return 0;
    }

    @Override
    public double doExtractEnergy(Object handler, ForgeDirection direction, double energy, boolean doExtract) {
        TileEntity tile = getENetTile(handler);
        if (tile instanceof IEnergySource)
        {
            double demand = Math.min(((IEnergySource) tile).getOfferedEnergy(), energy * CompatibilityType.INDUSTRIALCRAFT.ratio);

            if (doExtract)
            {
                ((IEnergySource) tile).drawEnergy(demand);
            }

            return demand * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio;
        }

        return 0;
    }

    @Override
    public double doChargeItem(ItemStack itemStack, double joules, boolean docharge) {
        if (itemStack.getItem() instanceof IElectricItem)
        {
            return (ElectricItem.manager.charge(itemStack, joules * CompatibilityType.INDUSTRIALCRAFT.ratio, 4, true, !docharge) * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio);
        }
        return 0;
    }

    @Override
    public double doDischargeItem(ItemStack itemStack, double joules, boolean doDischarge) {
        if (itemStack.getItem() instanceof IElectricItem)
        {
            IElectricItem item = (IElectricItem) itemStack.getItem();

            if (item.canProvideEnergy(itemStack))
            {
                return (long) (ElectricItem.manager.discharge(itemStack, joules * CompatibilityType.INDUSTRIALCRAFT.ratio, 4, true, false, !doDischarge) * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio);
            }
        }
        return 0;
    }

    @Override
    public boolean doIsHandler(Object obj) {
        return getENetTile(obj) != null || obj instanceof IElectricItem;
    }

    @Override
    public boolean doIsEnergyContainer(Object obj) {
        TileEntity tile = getENetTile(obj);
        return tile instanceof IEnergyStorage;
    }

    @Override
    public double doGetEnergy(Object obj, ForgeDirection direction) {
        TileEntity tile = getENetTile(obj);

        if (tile instanceof IEnergyStorage) {
            return CompatibilityType.INDUSTRIALCRAFT.toJoules(((IEnergyStorage)tile).getStored());
        }
        
        return 0;
    }

    @Override
    public boolean doCanConnect(Object obj, ForgeDirection direction, Object source) {
        TileEntity tile = getENetTile(obj);

        if (tile instanceof IEnergySink)
        {
            if (((IEnergySink) tile).acceptsEnergyFrom((TileEntity) source, direction))
                return true;
        }

        if (tile instanceof IEnergySource)
        {
            if (((IEnergySource) tile).emitsEnergyTo((TileEntity) source, direction))
                return true;
        }
        return false;
    }

    @Override
    public ItemStack doGetItemWithCharge(ItemStack itemStack, double energy) {
        ItemStack is = itemStack.copy();

        ElectricItem.manager.discharge(is, Integer.MAX_VALUE, 1, true, false, false);
        ElectricItem.manager.charge(is, (int) (energy * CompatibilityType.INDUSTRIALCRAFT.ratio), 1, true, false);

        return is;
    }

    @Override
    public double doGetMaxEnergy(Object handler, ForgeDirection direction) {
        TileEntity tile = getENetTile(handler);

        if (tile instanceof IEnergyStorage) {
            return CompatibilityType.INDUSTRIALCRAFT.toJoules(((IEnergyStorage)tile).getCapacity());
        }
        
        return 0;
    }

    @Override
    public double doGetEnergyItem(ItemStack is) {
        return ElectricItem.manager.getCharge(is) * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio;
    }

    @Override
    public double doGetMaxEnergyItem(ItemStack is) {
        return ((IElectricItem) is.getItem()).getMaxCharge(is) * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio;
    }

    @Override
    public double doGetInputVoltage(Object handler) {
        TileEntity tile = getENetTile(handler);

        if (tile instanceof IEnergySink) {
            return tierToVolt(((IEnergySink)tile).getSinkTier());
        }

        return 0;
    }

    @Override
    public double doGetOutputVoltage(Object handler) {
        TileEntity tile = getENetTile(handler);

        if (tile instanceof IEnergySource) {
            return tierToVolt(((IEnergySource)tile).getSourceTier());
        }

        return 0;
    }

    @Override
    public boolean doCanReceive(Object handler, ForgeDirection side) {
        TileEntity tile = getENetTile(handler);
        if (!(tile instanceof IEnergySink)) return false;

        if (side != ForgeDirection.UNKNOWN) {
            return ((IEnergySink)tile).acceptsEnergyFrom(null, side);
        }
        
        return true;
    }

    @Override
    public boolean doCanExtract(Object handler, ForgeDirection side) {
        TileEntity tile = getENetTile(handler);
        if (!(tile instanceof IEnergySource)) return false;

        if (side != ForgeDirection.UNKNOWN) {
            return ((IEnergySource)tile).emitsEnergyTo(null, side);
        }
        
        return true;
    }

    @Override
    public double doGetDemandedJoules(Object handler) {
        TileEntity tile = getENetTile(handler);
        if (tile instanceof IEnergySink) {
            IEnergySink sink = (IEnergySink) tile;
            return CompatibilityType.INDUSTRIALCRAFT.toJoules(sink.getDemandedEnergy());
        }
        return 0;
    }

    @Override
    public double doGetProvidedJoules(Object handler) {
        TileEntity tile = getENetTile(handler);
        if (tile instanceof IEnergySource) {
            IEnergySource source = (IEnergySource) tile;
            return CompatibilityType.INDUSTRIALCRAFT.toJoules(source.getOfferedEnergy());
        }
        return 0;
    }

    public static int voltToTier(double volt) {
        if (volt <= 120.0) return 1;
        if (volt <= 240.0) return 2;
        if (volt <= 480.0) return 3;
        return 4;
    }

    public static double tierToVolt(int tier) {
        switch (tier) {
            case 1: return 120.0;
            case 2: return 240.0;
            case 3: return 480.0;
            default: return 960.0;
        }
    }
    
}
