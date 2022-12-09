package universalelectricity.core;

import cpw.mods.fml.common.Loader;
import java.io.File;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.config.Configuration;
import universalelectricity.api.CompatibilityType;

public class UniversalElectricity {

   public static final String MAJOR_VERSION = "0";
   public static final String MINOR_VERSION = "6";
   public static final String REVISION_VERSION = "2";
   public static final String BUILD_VERSION = "117";
   public static final String VERSION = "0.6.2";
   public static final Configuration CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(), "UniversalElectricity.cfg"));
   public static double UE_IC2_RATIO = CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio;
   public static double UE_RF_RATIO = CompatibilityType.REDSTONE_FLUX.reciprocal_ratio;
   public static boolean isVoltageSensitive = true;
   public static boolean isNetworkActive = false;
   public static final Material machine = new Material(MapColor.ironColor);


   static {
      CONFIGURATION.load();
      isVoltageSensitive = CONFIGURATION.get("Compatiblity", "Is Voltage Sensitive", isVoltageSensitive).getBoolean(isVoltageSensitive);
      isNetworkActive = CONFIGURATION.get("Compatiblity", "Is Network Active", isNetworkActive).getBoolean(isNetworkActive);
      CONFIGURATION.save();
   }
}
