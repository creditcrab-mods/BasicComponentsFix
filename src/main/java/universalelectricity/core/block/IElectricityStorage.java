package universalelectricity.core.block;

import mekanism.api.energy.IStrictEnergyStorage;

public interface IElectricityStorage extends IStrictEnergyStorage {

   double getJoules();

   void setJoules(double var1);

   double getMaxJoules();

   default double getEnergy() {
      return getJoules();
   }

	default void setEnergy(double energy) {
      setJoules(energy);
   }

	default double getMaxEnergy() {
      return getMaxJoules();
   }
   
}
