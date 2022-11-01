package universalelectricity.compat.ic2;

import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.compat.IElectricityTileHandler;
import universalelectricity.core.electricity.ElectricityPack;

public class ElectricityTileWrapper implements IElectricityTileHandler {

    TileEntity baseTile;

    public ElectricityTileWrapper(TileEntity baseTile) {
        this.baseTile = baseTile;
    }

    @Override
    public boolean canInsert() {
        return baseTile instanceof IEnergySink;
    }

    @Override
    public boolean canExtract() {
        return baseTile instanceof IEnergySource;
    }

    @Override
    public boolean canInsertOn(ForgeDirection side) {
        return baseTile instanceof IEnergySink && ((IEnergySink) baseTile).acceptsEnergyFrom(null, side);
    }

    @Override
    public boolean canExtractOn(ForgeDirection side) {
        return baseTile instanceof IEnergySource && ((IEnergySource) baseTile).emitsEnergyTo(null, side);
    }

    @Override
    public void insert(ElectricityPack pack, ForgeDirection side) {
        if (baseTile instanceof IEnergySink) {
            IEnergySink sink = (IEnergySink) baseTile;
            sink.injectEnergy(side, IC2CompatHelper.joulesToEU(pack.getWatts()), IC2CompatHelper.voltToTier(pack.voltage));
        }
    }

    @Override
    public void extract(ElectricityPack pack, ForgeDirection side) {
        if (baseTile instanceof IEnergySource) {
            IEnergySource source = (IEnergySource) baseTile;
            source.drawEnergy(IC2CompatHelper.joulesToEU(pack.getWatts()));
        }
    }

    @Override
    public ElectricityPack getDemandedJoules() {
        if (baseTile instanceof IEnergySink) {
            IEnergySink sink = (IEnergySink) baseTile;
            double voltage = IC2CompatHelper.tierToVolt(sink.getSinkTier());
            return new ElectricityPack(IC2CompatHelper.EUToJoules(sink.getDemandedEnergy()) / voltage, voltage);
        }
        return new ElectricityPack();
    }

    @Override
    public ElectricityPack getProvidedJoules() {
        if (baseTile instanceof IEnergySource) {
            IEnergySource source = (IEnergySource) baseTile;
            double voltage = IC2CompatHelper.tierToVolt(source.getSourceTier());
            return new ElectricityPack(IC2CompatHelper.EUToJoules(source.getOfferedEnergy()) / voltage, voltage);
        }
        return new ElectricityPack();
    }

    @Override
    public TileEntity getTile() {
        return baseTile;
    }

    @Override
    public double getVoltage() {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
