package universalelectricity.prefab.tile;

import universalelectricity.prefab.implement.IDisableable;

public abstract class TileEntityDisableable extends TileEntityAdvanced implements IDisableable {

   protected int disabledTicks = 0;

   @Override
   public void updateEntity() {
      super.updateEntity();
      if(this.disabledTicks > 0) {
         --this.disabledTicks;
         this.whileDisable();
      }
   }

   protected void whileDisable() {}

   public void onDisable(int duration) {
      this.disabledTicks = duration;
   }

   public boolean isDisabled() {
      return this.disabledTicks > 0;
   }
}
