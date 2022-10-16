package universalelectricity.core;

import cpw.mods.fml.common.Loader;
import java.io.File;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.config.Configuration;

public class UniversalElectricity {

   public static final String MAJOR_VERSION = "0";
   public static final String MINOR_VERSION = "6";
   public static final String REVISION_VERSION = "2";
   public static final String BUILD_VERSION = "117";
   public static final String VERSION = "0.6.2";
   public static final Configuration CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(), "UniversalElectricity.cfg"));
   public static double IC2_RATIO = 40.0D;
   public static double BC3_RATIO = 100.0D;
   public static double TO_IC2_RATIO = 1.0D / IC2_RATIO;
   public static double TO_BC_RATIO = 1.0D / BC3_RATIO;
   public static boolean isVoltageSensitive = false;
   public static boolean isNetworkActive = false;
   public static final Material machine = new Material(MapColor.ironColor);


   static {
      CONFIGURATION.load();
      IC2_RATIO = CONFIGURATION.get("Compatiblity", "IndustrialCraft Conversion Ratio", IC2_RATIO).getDouble(IC2_RATIO);
      BC3_RATIO = CONFIGURATION.get("Compatiblity", "BuildCraft Conversion Ratio", BC3_RATIO).getDouble(BC3_RATIO);
      TO_IC2_RATIO = 1.0D / IC2_RATIO;
      TO_BC_RATIO = 1.0D / BC3_RATIO;
      isVoltageSensitive = CONFIGURATION.get("Compatiblity", "Is Voltage Sensitive", isVoltageSensitive).getBoolean(isVoltageSensitive);
      isNetworkActive = CONFIGURATION.get("Compatiblity", "Is Network Active", isNetworkActive).getBoolean(isNetworkActive);
      CONFIGURATION.save();
   }
}
