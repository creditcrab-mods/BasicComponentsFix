package universalelectricity.core.block;

import mekanism.api.energy.IStrictEnergyStorage;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.energy.IEnergyContainer;

@Deprecated
public interface IElectricityStorage extends IEnergyContainer, IStrictEnergyStorage {

   double getJoules();

   void setJoules(double var1);

   double getMaxJoules();

   @Override
   default double getEnergy(ForgeDirection from) {
       return getJoules();
   }

   @Override
   default void setEnergy(ForgeDirection from, double energy) {
       setJoules(energy);
   }

   @Override
   default double getEnergyCapacity(ForgeDirection from) {
       return getMaxEnergy();
   }

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
