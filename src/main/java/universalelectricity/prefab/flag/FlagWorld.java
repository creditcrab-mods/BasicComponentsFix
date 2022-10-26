package universalelectricity.prefab.flag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.vector.Region3;

public class FlagWorld extends FlagBase {

   public static final String GLOBAL_REGION = "dimension";
   public World world;
   private final List<FlagRegion> regions = new ArrayList();


   public FlagWorld(World world) {
      this.world = world;
   }

   public void readFromNBT(NBTTagCompound nbt) {
      
      for(String key : (Set<String>) nbt.func_150296_c()) {
         NBTTagCompound childCompound = nbt.getCompoundTag(key);
         FlagRegion e = new FlagRegion(this);
         e.readFromNBT(childCompound);
         this.regions.add(e);
      }

   }

   public void writeToNBT(NBTTagCompound nbt) {
      Iterator i$ = this.regions.iterator();

      while(i$.hasNext()) {
         FlagRegion region = (FlagRegion)i$.next();

         try {
            NBTTagCompound e = new NBTTagCompound();
            region.writeToNBT(e);
            nbt.setTag(region.name, e);
         } catch (Exception var5) {
            System.out.println("Failed to save world flag data: " + region.name);
            var5.printStackTrace();
         }
      }

   }

   public List getFlagsInPosition(Vector3 position) {
      ArrayList returnFlags = new ArrayList();
      Iterator i$ = this.regions.iterator();

      while(i$.hasNext()) {
         FlagRegion flagRegion = (FlagRegion)i$.next();
         if(flagRegion.region.isIn(position) || flagRegion.name.equalsIgnoreCase("dimension")) {
            Iterator i$1 = flagRegion.getFlags().iterator();

            while(i$1.hasNext()) {
               Flag flag = (Flag)i$1.next();
               returnFlags.add(flag);
            }
         }
      }

      return returnFlags;
   }

   public List getValues(String flagName, Vector3 position) {
      ArrayList values = new ArrayList();
      Iterator i$ = this.getFlagsInPosition(position).iterator();

      while(i$.hasNext()) {
         Flag flag = (Flag)i$.next();
         values.add(flag.value);
      }

      return values;
   }

   public boolean containsValue(String flagName, String checkValue, Vector3 position) {
      Iterator i$ = this.getFlagsInPosition(position).iterator();

      Flag flag;
      do {
         if(!i$.hasNext()) {
            return false;
         }

         flag = (Flag)i$.next();
      } while(!flag.name.equalsIgnoreCase(flagName) || !flag.value.equalsIgnoreCase(checkValue));

      return true;
   }

   public boolean addRegion(String name, Vector3 position, int radius) {
      Vector3 minVec = new Vector3((double)(position.intX() - radius), 0.0D, (double)(position.intZ() - radius));
      Vector3 maxVec = new Vector3((double)(position.intX() + radius), (double)this.world.getHeight(), (double)(position.intZ() + radius));
      return this.regions.add(new FlagRegion(this, name, new Region3(minVec, maxVec)));
   }

   public FlagRegion getRegion(String name) {
      Iterator i$ = this.regions.iterator();

      FlagRegion region;
      do {
         if(!i$.hasNext()) {
            return null;
         }

         region = (FlagRegion)i$.next();
      } while(!region.name.equals(name));

      return region;
   }

   public List getRegions(Vector3 position) {
      ArrayList returnRegions = new ArrayList();
      Iterator i$ = this.regions.iterator();

      while(i$.hasNext()) {
         FlagRegion region = (FlagRegion)i$.next();
         if(region.region.isIn(position)) {
            returnRegions.add(region);
         }
      }

      return returnRegions;
   }

   public boolean removeRegion(String name) {
      Iterator i$ = this.regions.iterator();

      FlagRegion region;
      do {
         if(!i$.hasNext()) {
            return false;
         }

         region = (FlagRegion)i$.next();
      } while(!region.name.equals(name));

      this.regions.remove(region);
      return true;
   }

   public List<FlagRegion> getRegions() {
      Iterator it = this.regions.iterator();

      while(it.hasNext()) {
         FlagRegion region = (FlagRegion)it.next();
         if(region == null) {
            it.remove();
         } else if(region.name == null || region.name == "") {
            it.remove();
         }
      }

      return this.regions;
   }
}
