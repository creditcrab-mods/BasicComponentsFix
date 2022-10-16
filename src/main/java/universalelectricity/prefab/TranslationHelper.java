package universalelectricity.prefab;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class TranslationHelper {

   public static int loadLanguages(String languagePath, String[] languageSupported) {
      int languages = 0;
      String[] arr$ = languageSupported;
      int len$ = languageSupported.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String language = arr$[i$];
         LanguageRegistry.instance().loadLocalization(languagePath + language + ".properties", language, false);
         if(LanguageRegistry.instance().getStringLocalization("children", language) != "") {
            try {
               String[] e = LanguageRegistry.instance().getStringLocalization("children", language).split(",");
               String[] arr$1 = e;
               int len$1 = e.length;

               for(int i$1 = 0; i$1 < len$1; ++i$1) {
                  String child = arr$1[i$1];
                  if(child != "" || child != null) {
                     LanguageRegistry.instance().loadLocalization(languagePath + language + ".properties", child, false);
                     ++languages;
                  }
               }
            } catch (Exception var12) {
               FMLLog.severe("Failed to load a child language file.", new Object[0]);
               var12.printStackTrace();
            }
         }

         ++languages;
      }

      return languages;
   }

   public static String getLocal(String key) {
      String text = LanguageRegistry.instance().getStringLocalization(key);
      if(text == null || text == "") {
         text = LanguageRegistry.instance().getStringLocalization(key, "en_US");
      }

      return text;
   }
}
