package universalelectricity.prefab.flag;

import net.minecraft.nbt.NBTTagCompound;

public abstract class FlagBase {

   public abstract void readFromNBT(NBTTagCompound var1);

   public abstract void writeToNBT(NBTTagCompound var1);

   public NBTTagCompound getNBT() {
      NBTTagCompound nbt = new NBTTagCompound();

      try {
         this.writeToNBT(nbt);
      } catch (Exception var3) {
         System.out.println("Failed to read flag");
         var3.printStackTrace();
      }

      return nbt;
   }
}
