package universalelectricity.core.electricity;

import cpw.mods.fml.common.FMLLog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IConnectionProvider;
import universalelectricity.core.block.INetworkProvider;
import universalelectricity.core.path.PathfinderChecker;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;

public class ElectricityNetwork implements IElectricityNetwork {

   private final HashMap<TileEntity, ElectricityPack> producers = new HashMap<>();
   private final HashMap<TileEntity, ElectricityPack> consumers = new HashMap<>();
   private final Set<IConductor> conductors = new HashSet<>();


   public ElectricityNetwork() {}

   public ElectricityNetwork(IConductor ... conductors) {
      this.conductors.addAll(Arrays.asList(conductors));
   }

   public void startProducing(TileEntity tileEntity, ElectricityPack electricityPack) {
      if(tileEntity != null && electricityPack.getWatts() > 0.0D) {
         this.producers.put(tileEntity, electricityPack);
      }

   }

   public void startProducing(TileEntity tileEntity, double amperes, double voltage) {
      this.startProducing(tileEntity, new ElectricityPack(amperes, voltage));
   }

   public boolean isProducing(TileEntity tileEntity) {
      return this.producers.containsKey(tileEntity);
   }

   public void stopProducing(TileEntity tileEntity) {
      this.producers.remove(tileEntity);
   }

   public void startRequesting(TileEntity tileEntity, ElectricityPack electricityPack) {
      if(tileEntity != null && electricityPack.getWatts() > 0.0D) {
         this.consumers.put(tileEntity, electricityPack);
      }

   }

   public void startRequesting(TileEntity tileEntity, double amperes, double voltage) {
      this.startRequesting(tileEntity, new ElectricityPack(amperes, voltage));
   }

   public boolean isRequesting(TileEntity tileEntity) {
      return this.consumers.containsKey(tileEntity);
   }

   public void stopRequesting(TileEntity tileEntity) {
      this.consumers.remove(tileEntity);
   }

   public ElectricityPack getProduced(TileEntity ... ignoreTiles) {
      ElectricityPack totalElectricity = new ElectricityPack(0.0D, 0.0D);
      Iterator it = this.producers.entrySet().iterator();

      label47:
      while(it.hasNext()) {
         Entry pairs = (Entry)it.next();
         if(pairs != null) {
            TileEntity tileEntity = (TileEntity)pairs.getKey();
            if(tileEntity == null) {
               it.remove();
            } else if(tileEntity.isInvalid()) {
               it.remove();
            } else if(tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) != tileEntity) {
               it.remove();
            } else {
               if(ignoreTiles != null) {
                  TileEntity[] pack = ignoreTiles;
                  int newWatts = ignoreTiles.length;

                  for(int i$ = 0; i$ < newWatts; ++i$) {
                     TileEntity newVoltage = pack[i$];
                     if(tileEntity == newVoltage) {
                        continue label47;
                     }
                  }
               }

               ElectricityPack var11 = (ElectricityPack)pairs.getValue();
               if(pairs.getKey() != null && pairs.getValue() != null && var11 != null) {
                  double var12 = totalElectricity.getWatts() + var11.getWatts();
                  double var13 = Math.max(totalElectricity.voltage, var11.voltage);
                  totalElectricity.amperes = var12 / var13;
                  totalElectricity.voltage = var13;
               }
            }
         }
      }

      return totalElectricity;
   }

   public ElectricityPack getRequest(TileEntity ... ignoreTiles) {
      ElectricityPack totalElectricity = this.getRequestWithoutReduction();
      totalElectricity.amperes = Math.max(totalElectricity.amperes - this.getProduced(ignoreTiles).amperes, 0.0D);
      return totalElectricity;
   }

   public ElectricityPack getRequestWithoutReduction() {
      ElectricityPack totalElectricity = new ElectricityPack(0.0D, 0.0D);
      Iterator it = this.consumers.entrySet().iterator();

      while(it.hasNext()) {
         Entry pairs = (Entry)it.next();
         if(pairs != null) {
            TileEntity tileEntity = (TileEntity)pairs.getKey();
            if(tileEntity == null) {
               it.remove();
            } else if(tileEntity.isInvalid()) {
               it.remove();
            } else if(tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) != tileEntity) {
               it.remove();
            } else {
               ElectricityPack pack = (ElectricityPack)pairs.getValue();
               if(pack != null) {
                  totalElectricity.amperes += pack.amperes;
                  totalElectricity.voltage = Math.max(totalElectricity.voltage, pack.voltage);
               }
            }
         }
      }

      return totalElectricity;
   }

   public ElectricityPack consumeElectricity(TileEntity tileEntity) {
      ElectricityPack totalElectricity = new ElectricityPack(0.0D, 0.0D);

      try {
         ElectricityPack e = (ElectricityPack)this.consumers.get(tileEntity);
         if(this.consumers.containsKey(tileEntity) && e != null) {
            totalElectricity = this.getProduced(new TileEntity[0]);
            if(totalElectricity.getWatts() > 0.0D) {
               ElectricityPack totalRequest = this.getRequestWithoutReduction();
               totalElectricity.amperes *= e.amperes / totalRequest.amperes;
               int distance = this.conductors.size();
               double ampsReceived = totalElectricity.amperes - totalElectricity.amperes * totalElectricity.amperes * this.getTotalResistance() / totalElectricity.voltage;
               double voltsReceived = totalElectricity.voltage - totalElectricity.amperes * this.getTotalResistance();
               totalElectricity.amperes = ampsReceived;
               totalElectricity.voltage = voltsReceived;
               return totalElectricity;
            }
         }
      } catch (Exception var10) {
         FMLLog.severe("Failed to consume electricity!", new Object[0]);
         var10.printStackTrace();
      }

      return totalElectricity;
   }

   public HashMap<TileEntity, ElectricityPack> getProducers() {
      return this.producers;
   }

   public List<TileEntity> getProviders() {
      ArrayList<TileEntity> providers = new ArrayList<>();
      providers.addAll(this.producers.keySet());
      return providers;
   }

   public HashMap<TileEntity, ElectricityPack> getConsumers() {
      return this.consumers;
   }

   public List<TileEntity> getReceivers() {
      ArrayList<TileEntity> receivers = new ArrayList<>();
      receivers.addAll(this.consumers.keySet());
      return receivers;
   }

   public void cleanUpConductors() {
      Iterator<IConductor> it = this.conductors.iterator();

      while(it.hasNext()) {
         IConductor conductor = (IConductor)it.next();
         if(conductor == null) {
            it.remove();
         } else if(((TileEntity)conductor).isInvalid()) {
            it.remove();
         } else {
            conductor.setNetwork(this);
         }
      }

   }

   public void refreshConductors() {
      this.cleanUpConductors();

      try {
         Iterator e = this.conductors.iterator();

         while(e.hasNext()) {
            IConductor conductor = (IConductor)e.next();
            conductor.updateAdjacentConnections();
         }
      } catch (Exception var3) {
         FMLLog.severe("Universal Electricity: Failed to refresh conductor.", new Object[0]);
         var3.printStackTrace();
      }

   }

   public double getTotalResistance() {
      double resistance = 0.0D;

      IConductor conductor;
      for(Iterator i$ = this.conductors.iterator(); i$.hasNext(); resistance += conductor.getResistance()) {
         conductor = (IConductor)i$.next();
      }

      return resistance;
   }

   public double getLowestCurrentCapacity() {
      double lowestAmp = 0.0D;
      Iterator i$ = this.conductors.iterator();

      while(i$.hasNext()) {
         IConductor conductor = (IConductor)i$.next();
         if(lowestAmp == 0.0D || conductor.getCurrentCapcity() < lowestAmp) {
            lowestAmp = conductor.getCurrentCapcity();
         }
      }

      return lowestAmp;
   }

   public Set<IConductor> getConductors() {
      return this.conductors;
   }

   public void mergeConnection(IElectricityNetwork network) {
      if(network != null && network != this) {
         ElectricityNetwork newNetwork = new ElectricityNetwork();
         newNetwork.getConductors().addAll(this.getConductors());
         newNetwork.getConductors().addAll(network.getConductors());
         newNetwork.cleanUpConductors();
      }

   }

   public void splitNetwork(IConnectionProvider splitPoint) {
      if(splitPoint instanceof TileEntity) {
         this.getConductors().remove(splitPoint);
         ForgeDirection[] connectedBlocks = ForgeDirection.values();
         int i = connectedBlocks.length;

         for(int connectedBlockA = 0; connectedBlockA < i; ++connectedBlockA) {
            ForgeDirection ii = connectedBlocks[connectedBlockA];
            if(ii != ForgeDirection.UNKNOWN) {
               Vector3 connectedBlockB = new Vector3((TileEntity)splitPoint);
               TileEntity finder = VectorHelper.getTileEntityFromSide(((TileEntity)splitPoint).getWorldObj(), connectedBlockB, ii);
               if(this.producers.containsKey(finder)) {
                  this.stopProducing(finder);
                  this.stopRequesting(finder);
               }
            }
         }

         TileEntity[] var12 = splitPoint.getAdjacentConnections();

         for(i = 0; i < var12.length; ++i) {
            TileEntity var13 = var12[i];
            if(var13 instanceof IConnectionProvider) {
               for(int var14 = 0; var14 < var12.length; ++var14) {
                  TileEntity var15 = var12[var14];
                  if(var13 != var15 && var15 instanceof IConnectionProvider) {
                     PathfinderChecker var16 = new PathfinderChecker(((TileEntity)splitPoint).getWorldObj(), (IConnectionProvider)var15, new IConnectionProvider[]{splitPoint});
                     var16.init(new Vector3(var13));
                     if(var16.results.size() > 0) {
                        Iterator newNetwork = var16.closedSet.iterator();

                        while(newNetwork.hasNext()) {
                           Vector3 i$ = (Vector3)newNetwork.next();
                           TileEntity node = i$.getTileEntity(((TileEntity)splitPoint).getWorldObj());
                           if(node instanceof INetworkProvider && node != splitPoint) {
                              ((INetworkProvider)node).setNetwork(this);
                           }
                        }
                     } else {
                        ElectricityNetwork var17 = new ElectricityNetwork();
                        Iterator var18 = var16.closedSet.iterator();

                        while(var18.hasNext()) {
                           Vector3 var19 = (Vector3)var18.next();
                           TileEntity nodeTile = var19.getTileEntity(((TileEntity)splitPoint).getWorldObj());
                           if(nodeTile instanceof INetworkProvider && nodeTile != splitPoint) {
                              var17.getConductors().add((IConductor)nodeTile);
                           }
                        }

                        var17.cleanUpConductors();
                     }
                  }
               }
            }
         }
      }

   }

   public String toString() {
      return "ElectricityNetwork[" + this.hashCode() + "|Wires:" + this.conductors.size() + "]";
   }

   @Override
   public void addConnector(IConductor connector) {
      this.conductors.add(connector);
   }

   @Override
   public void removeConnector(IConductor connector) {
      this.conductors.remove(connector);
   }

   @Override
   public Set<IConductor> getConnectors() {
      return this.conductors;
   }

   @Override
   public void reconstruct() {
      refreshConductors();
   }

   @Override
   public IElectricityNetwork merge(IElectricityNetwork network) {
      this.mergeConnection(network);
      return this;
   }

   @Override
   public void split(IConductor connection) {
      splitNetwork(connection);
   }

   @Override
   public void split(IConductor connectorA, IConductor connectorB) {
      // TODO: implement this
   }

}
