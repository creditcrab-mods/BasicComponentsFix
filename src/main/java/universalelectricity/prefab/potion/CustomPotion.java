package universalelectricity.prefab.potion;

import net.minecraft.potion.Potion;

public abstract class CustomPotion extends Potion {

   public CustomPotion(int id, boolean isBadEffect, int color, String name) {
      super(id, isBadEffect, color);
      this.setPotionName("potion." + name);
      Potion.potionTypes[this.getId()] = this;
   }

   public Potion setIconIndex(int par1, int par2) {
      super.setIconIndex(par1, par2);
      return this;
   }

   protected Potion setEffectiveness(double par1) {
      super.setEffectiveness(par1);
      return this;
   }
}
