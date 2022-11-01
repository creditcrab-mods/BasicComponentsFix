package universalelectricity.prefab.tile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import scala.languageFeature.reflectiveCalls;
import universalelectricity.compat.IElectricityTileHandler;
import universalelectricity.core.block.IConnector;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;

public class ElectricTileDriver implements IConnector {

    IElectricityTileHandler handler;

    public ElectricTileDriver(IElectricityTileHandler handler) {
        this.handler = handler;
    }
    
    @Override
    public boolean canConnect(ForgeDirection side) {
        return handler.canInsertOn(side) || handler.canExtractOn(side);
    }

    public void invalidate() {
        ElectricityNetworkHelper.invalidate(handler.getTile());
    }

    public void tick() {
        Map<ForgeDirection, IElectricityNetwork> networks = getNetworks();
        Set<ForgeDirection> inputSides = new HashSet<>();
        if (handler.canInsert()) {
            inputSides = consume(networks);
        }
        if (handler.canExtract()) {
            produce(networks, inputSides);
        }
    }

    public Set<ForgeDirection> consume(Map<ForgeDirection, IElectricityNetwork> networks) {
        Set<ForgeDirection> inputSides = new HashSet<>();

        if (networks.size() > 0) {
            ElectricityPack demand = handler.getDemandedJoules();
            double voltage = demand.voltage;
            double wattsPerSide = demand.getWatts() / networks.size();
            for (ForgeDirection side : networks.keySet()) {
                IElectricityNetwork net = networks.get(side);
                if (handler.canInsertOn(side) && wattsPerSide > 0 && demand.getWatts() > 0) {
                    inputSides.add(side);
                    net.startRequesting(handler.getTile(), wattsPerSide / voltage, voltage);
                    ElectricityPack receivedPack = net.consumeElectricity(handler.getTile());
                    handler.insert(receivedPack, side);
                } else {
                    net.stopRequesting(handler.getTile());
                }
            }

        }

        return inputSides;
    }

    public void produce(Map<ForgeDirection, IElectricityNetwork> networks, Set<ForgeDirection> inputSides) {
        if ((networks.size() - inputSides.size()) > 0) {
            ElectricityPack provided = handler.getProvidedJoules();
            double voltage = provided.voltage;
            double wattsPerSide = provided.getWatts() / (networks.size() - inputSides.size());
            for (ForgeDirection side : networks.keySet()) {
                IElectricityNetwork net = networks.get(side);
                if (!inputSides.contains(side) && handler.canExtractOn(side) && wattsPerSide > 0 && provided.getWatts() > 0) {
                    double amperes = Math.min(wattsPerSide / voltage, net.getRequest(new TileEntity[]{handler.getTile()}).amperes);
                    net.startProducing(handler.getTile(), amperes, voltage);
                    handler.extract(new ElectricityPack(amperes, voltage), side);
                } else {
                    net.stopProducing(handler.getTile());
                }
            }
        }
    }

    public Map<ForgeDirection, IElectricityNetwork> getNetworks() {
        Map<ForgeDirection, IElectricityNetwork> networks = new HashMap<>();
        
        for(ForgeDirection dir : ForgeDirection.values()) {
            if (canConnect(dir)) {
                Vector3 position = new Vector3(handler.getTile());
                position.modifyPositionFromSide(dir);
                TileEntity outputConductor = position.getTileEntity(handler.getTile().getWorldObj());
                IElectricityNetwork electricityNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(outputConductor, dir);
                if(electricityNetwork != null && !networks.containsValue(electricityNetwork)) {
                    networks.put(dir, electricityNetwork);
                }
            }
        }

        return networks;
    }

}
