package universalelectricity.prefab.tile;

import java.util.EnumSet;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;

public abstract class TileEntityElectricityRunnable extends TileEntityElectrical {

   public double prevWatts;
   public double wattsReceived = 0.0D;

   /*@Override                                                                                                                                                                                 
    public void initiate() {                                                                                                                                                                  
        super.initiate();                                                                                                                                                                     
        MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));                                                                                                                         
    }                                                                                                                                                                                         
                                                                                                                                                                                              
    @Override                                                                                                                                                                                 
    public void invalidate() {                                                                                                                                                                
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));                                                                                                                       
        super.invalidate();                                                                                                                                                                   
    }*/

   @Override
   public void updateEntity() {
      super.updateEntity();
      this.prevWatts = this.wattsReceived;
      if(!this.worldObj.isRemote) {
         if(!this.isDisabled()) {
            ElectricityPack electricityPack = ElectricityNetworkHelper.consumeFromMultipleSides(this, this.getConsumingSides(), this.getRequest());
            this.onReceive(electricityPack);
         } else {
            ElectricityNetworkHelper.consumeFromMultipleSides(this, new ElectricityPack());
         }
      }

   }

   protected EnumSet<ForgeDirection> getConsumingSides() {
      return ElectricityNetworkHelper.getDirections(this);
   }

   public ElectricityPack getRequest() {
      return new ElectricityPack();
   }

   public void onReceive(ElectricityPack electricityPack) {
      if(UniversalElectricity.isVoltageSensitive && electricityPack.voltage > this.getVoltage()) {
         this.worldObj.createExplosion((Entity)null, (double)this.xCoord, (double)this.yCoord, (double)this.zCoord, 1.5F, true);
      } else {
         this.wattsReceived = Math.min(this.wattsReceived + electricityPack.getWatts(), this.getWattBuffer());
      }
   }

   public double getWattBuffer() {
      return this.getRequest().getWatts() * 2.0D;
   }

   //IC2 START

  /* @Override
   public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
      return getConsumingSides().contains(direction);
   }

   @Override
   public double getDemandedEnergy() {
      return Math.ceil(this.getRequest().getWatts() * UniversalElectricity.TO_IC2_RATIO);
   }

   @Override
    public int getSinkTier() {
        return 32;
    }

    @Override
    public double injectEnergy(ForgeDirection direction, double i, double voltage) {
        double givenElectricity = (double)i * UniversalElectricity.IC2_RATIO;
        double rejects = 0.0;
        if (givenElectricity > this.getWattBuffer()) {
            rejects = givenElectricity - this.getRequest().getWatts();
        }
        this.onReceive(new ElectricityPack(givenElectricity / this.getVoltage(), this.getVoltage()));
        return (rejects * UniversalElectricity.TO_IC2_RATIO);
    }*/

}
