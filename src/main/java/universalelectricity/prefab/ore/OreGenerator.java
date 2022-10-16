package universalelectricity.prefab.ore;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import universalelectricity.prefab.ore.OreGenBase;

public class OreGenerator implements IWorldGenerator {

   public static boolean isInitiated = false;
   private static final List ORES_TO_GENERATE = new ArrayList();


   public static void addOre(OreGenBase data) {
      if(!isInitiated) {
         GameRegistry.registerWorldGenerator(new OreGenerator(), 10); //TODO figure out the right value instead of 10
      }

      ORES_TO_GENERATE.add(data);
   }

   public static boolean oreExists(String oreName) {
      Iterator i$ = ORES_TO_GENERATE.iterator();

      OreGenBase ore;
      do {
         if(!i$.hasNext()) {
            return false;
         }

         ore = (OreGenBase)i$.next();
      } while(ore.oreDictionaryName != oreName);

      return true;
   }

   public static void removeOre(OreGenBase data) {
      ORES_TO_GENERATE.remove(data);
   }

   public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
      chunkX <<= 4;
      chunkZ <<= 4;
      Iterator i$ = ORES_TO_GENERATE.iterator();

      while(i$.hasNext()) {
         OreGenBase oreData = (OreGenBase)i$.next();
         if(oreData.shouldGenerate && oreData.isOreGeneratedInWorld(world, chunkGenerator)) {
            oreData.generate(world, rand, chunkX, chunkZ);
         }
      }

   }

}
