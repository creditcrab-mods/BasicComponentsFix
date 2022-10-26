package universalelectricity.prefab.flag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import universalelectricity.prefab.flag.ModFlag;

public class FlagRegistry {

   public static final String DEFAULT_NAME = "ModFlags";
   private static final HashMap MOD_FLAGS = new HashMap();
   public static final List flags = new ArrayList();
   public static boolean isInitiated = false;


   public static void registerModFlag(String name, ModFlag flagData) {
      MOD_FLAGS.put(name, flagData);
   }

   public static ModFlag getModFlag(String name) {
      return (ModFlag)MOD_FLAGS.get(name);
   }

   public static String registerFlag(String name) {
      if(!isInitiated) {
         isInitiated = true;
      }

      name = name.toLowerCase();
      if(!flags.contains(name)) {
         flags.add(name);
      }

      return name;
   }

}
