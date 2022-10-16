package universalelectricity.prefab;

import cpw.mods.fml.common.registry.GameRegistry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Configuration;

public class RecipeHelper {

   public static List getRecipesByOutput(ItemStack output) {
      ArrayList list = new ArrayList();
      Iterator i$ = CraftingManager.getInstance().getRecipeList().iterator();

      while(i$.hasNext()) {
         Object obj = i$.next();
         if(obj instanceof IRecipe && ((IRecipe)obj).getRecipeOutput() == output) {
            list.add((IRecipe)obj);
         }
      }

      return list;
   }

   public static boolean replaceRecipe(IRecipe recipe, IRecipe newRecipe) {
      Iterator i$ = CraftingManager.getInstance().getRecipeList().iterator();

      Object obj;
      do {
         do {
            if(!i$.hasNext()) {
               return false;
            }

            obj = i$.next();
         } while(!(obj instanceof IRecipe));
      } while(!((IRecipe)obj).equals(recipe) && obj != recipe);

      CraftingManager.getInstance().getRecipeList().remove(obj);
      CraftingManager.getInstance().getRecipeList().add(newRecipe);
      return true;
   }

   public static boolean replaceRecipe(ItemStack recipe, IRecipe newRecipe) {
      if(removeRecipe(recipe)) {
         CraftingManager.getInstance().getRecipeList().add(newRecipe);
         return true;
      } else {
         return false;
      }
   }

   public static boolean removeRecipe(IRecipe recipe) {
      Iterator i$ = CraftingManager.getInstance().getRecipeList().iterator();

      Object obj;
      do {
         do {
            do {
               if(!i$.hasNext()) {
                  return false;
               }

               obj = i$.next();
            } while(obj == null);
         } while(!(obj instanceof IRecipe));
      } while(!((IRecipe)obj).equals(recipe) && obj != recipe);

      CraftingManager.getInstance().getRecipeList().remove(obj);
      return true;
   }

   public static boolean removeRecipe(ItemStack stack) {
      Iterator i$ = CraftingManager.getInstance().getRecipeList().iterator();

      Object obj;
      do {
         if(!i$.hasNext()) {
            return false;
         }

         obj = i$.next();
      } while(obj == null || !(obj instanceof IRecipe) || ((IRecipe)obj).getRecipeOutput() == null || !((IRecipe)obj).getRecipeOutput().isItemEqual(stack));

      CraftingManager.getInstance().getRecipeList().remove(obj);
      return true;
   }

   public static boolean removeRecipes(ItemStack ... itemStacks) {
      boolean didRemove = false;
      Iterator itr = CraftingManager.getInstance().getRecipeList().iterator();

      while(itr.hasNext()) {
         Object obj = itr.next();
         if(obj != null && obj instanceof IRecipe && ((IRecipe)obj).getRecipeOutput() != null) {
            ItemStack[] arr$ = itemStacks;
            int len$ = itemStacks.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               ItemStack itemStack = arr$[i$];
               if(((IRecipe)obj).getRecipeOutput().isItemEqual(itemStack)) {
                  itr.remove();
                  didRemove = true;
                  break;
               }
            }
         }
      }

      return didRemove;
   }

   public static void addRecipe(IRecipe recipe, String name, Configuration configuration, boolean defaultBoolean) {
      if(configuration != null) {
         configuration.load();
         if(configuration.get("Crafting", "Allow " + name + " Crafting", defaultBoolean).getBoolean(defaultBoolean)) {
            GameRegistry.addRecipe(recipe);
         }

         configuration.save();
      }

   }

   public static void addRecipe(IRecipe recipe, Configuration config, boolean defaultBoolean) {
      addRecipe(recipe, recipe.getRecipeOutput().getUnlocalizedName(), config, defaultBoolean);
   }
}
