package universalelectricity.prefab.tile;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;

public class ElectricTileDriver {

    TileEntity handler;

    public ElectricTileDriver(TileEntity handler) {
        this.handler = handler;
    }

    public void invalidate() {
        ElectricityNetworkHelper.invalidate(handler);
    }

    public boolean tick() {
        if (handler.isInvalid()) return false;
        Map<ForgeDirection, IElectricityNetwork> networks = getNetworks();
        Set<ForgeDirection> inputSides = new HashSet<>();
        if (CompatibilityModule.canReceive(handler, ForgeDirection.UNKNOWN)) {
            inputSides = consume(networks);
        }
        if (CompatibilityModule.canExtract(handler, ForgeDirection.UNKNOWN)) {
            produce(networks, inputSides);
        }
        return networks.size() > 0;
    }

    public Set<ForgeDirection> consume(Map<ForgeDirection, IElectricityNetwork> networks) {
        Set<ForgeDirection> inputSides = new HashSet<>();

        if (networks.size() > 0) {
            double demand = CompatibilityModule.getDemandedJoules(handler);
            double voltage = CompatibilityModule.getInputVoltage(handler);
            double wattsPerSide = demand / networks.size();
            for (ForgeDirection side : networks.keySet()) {
                IElectricityNetwork net = networks.get(side);
                if (CompatibilityModule.canReceive(handler, side) && wattsPerSide > 0 && demand > 0) {
                    inputSides.add(side);
                    net.startRequesting(handler, wattsPerSide / voltage, voltage);
                    ElectricityPack receivedPack = net.consumeElectricity(handler);
                    if (receivedPack.voltage > voltage && UniversalElectricity.isVoltageSensitive) {
                        handler.getWorldObj().createExplosion(null, handler.xCoord, handler.yCoord, handler.zCoord, 1, true);
                        return EnumSet.allOf(ForgeDirection.class);
                    }
                    CompatibilityModule.receiveEnergy(handler, side, receivedPack.getWatts(), true);
                } else {
                    net.stopRequesting(handler);
                }
            }

        }

        return inputSides;
    }

    public void produce(Map<ForgeDirection, IElectricityNetwork> networks, Set<ForgeDirection> inputSides) {
        if ((networks.size() - inputSides.size()) > 0) {
            double provided = CompatibilityModule.getProvidedJoules(handler);
            double voltage = CompatibilityModule.getOutputVoltage(handler);
            double wattsPerSide = provided / (networks.size() - inputSides.size());
            for (ForgeDirection side : networks.keySet()) {
                IElectricityNetwork net = networks.get(side);
                if (!inputSides.contains(side) && CompatibilityModule.canExtract(handler, side) && wattsPerSide > 0 && provided > 0) {
                    double amperes = Math.min(wattsPerSide / voltage, net.getRequest(new TileEntity[]{handler}).amperes);
                    net.startProducing(handler, amperes, voltage);
                    CompatibilityModule.extractEnergy(handler, side, new ElectricityPack(amperes, voltage).getWatts(), true);
                } else {
                    net.stopProducing(handler);
                }
            }
        }
    }

    public Map<ForgeDirection, IElectricityNetwork> getNetworks() {
        Map<ForgeDirection, IElectricityNetwork> networks = new HashMap<>();
        
        for(ForgeDirection dir : ForgeDirection.values()) {
            if (CompatibilityModule.canReceive(handler, dir) || CompatibilityModule.canExtract(handler, dir)) {
                Vector3 position = new Vector3(handler);
                position.modifyPositionFromSide(dir);
                TileEntity outputConductor = position.getTileEntity(handler.getWorldObj());
                IElectricityNetwork electricityNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(outputConductor, dir);
                if(electricityNetwork != null && !networks.containsValue(electricityNetwork)) {
                    networks.put(dir, electricityNetwork);
                }
            }
        }

        return networks;
    }

}
