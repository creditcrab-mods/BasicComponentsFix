package universalelectricity.prefab.tile;

import java.util.EnumSet;

import mekanism.api.energy.IStrictEnergyAcceptor;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.block.IElectricityStorage;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.prefab.tile.TileEntityElectrical;

public abstract class TileEntityElectricityStorage extends TileEntityElectrical implements IElectricityStorage, IStrictEnergyAcceptor {

   private double joules = 0.0D;
   public double prevJoules = 0.0D;

   @Override
   public void updateEntity() {
      super.updateEntity();
      this.prevJoules = this.joules;
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
      return new ElectricityPack((this.getMaxJoules() - this.getJoules()) / this.getVoltage(), this.getVoltage());
   }

   public void onReceive(ElectricityPack electricityPack) {
      if(UniversalElectricity.isVoltageSensitive && electricityPack.voltage > this.getVoltage()) {
         this.worldObj.createExplosion((Entity)null, (double)this.xCoord, (double)this.yCoord, (double)this.zCoord, 1.5F, true);
      } else {
         this.setJoules(this.getJoules() + electricityPack.getWatts());
      }
   }

   @Override
   public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readFromNBT(par1NBTTagCompound);
      this.joules = par1NBTTagCompound.getDouble("joules");
   }

   @Override
   public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setDouble("joules", this.joules);
   }

   public double getJoules() {
      return this.joules;
   }

   public void setJoules(double joules) {
      this.joules = Math.max(Math.min(joules, this.getMaxJoules()), 0.0D);
   }

	public double transferEnergyToAcceptor(ForgeDirection side, double amount) {
      if (!canReceiveEnergy(side)) return 0;
      double toUse = Math.min(getMaxEnergy()-getEnergy(), amount);
      setEnergy(toUse + getEnergy());
      return toUse;
   }

	public boolean canReceiveEnergy(ForgeDirection side) {
      return getConsumingSides().contains(side);
   }

}
