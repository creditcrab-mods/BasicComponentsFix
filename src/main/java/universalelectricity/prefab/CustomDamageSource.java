package universalelectricity.prefab;

import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.util.DamageSource;

public class CustomDamageSource extends DamageSource {

   public static final CustomDamageSource electrocution = ((CustomDamageSource)(new CustomDamageSource("electrocution")).setDamageBypassesArmor()).setDeathMessage("%1$s got electrocuted!");


   public CustomDamageSource(String damageType) {
      super(damageType);
   }

   public CustomDamageSource setDeathMessage(String deathMessage) {
      LanguageRegistry.instance().addStringLocalization("death.attack." + super.damageType, deathMessage);
      return this;
   }

   public DamageSource setDamageBypassesArmor() {
      return super.setDamageBypassesArmor();
   }

   public DamageSource setDamageAllowedInCreativeMode() {
      return super.setDamageAllowedInCreativeMode();
   }

   public DamageSource setFireDamage() {
      return super.setFireDamage();
   }

}
