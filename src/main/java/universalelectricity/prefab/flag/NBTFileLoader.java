package universalelectricity.prefab.flag;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class NBTFileLoader {

   public static boolean saveData(File saveDirectory, String filename, NBTTagCompound data) {
      try {
         File e = new File(saveDirectory, filename + "_tmp.dat");
         File file = new File(saveDirectory, filename + ".dat");
         CompressedStreamTools.writeCompressed(data, new FileOutputStream(e));
         if(file.exists()) {
            file.delete();
         }

         e.renameTo(file);
         FMLLog.fine("Saved " + filename + " NBT data file successfully.", new Object[0]);
         return true;
      } catch (Exception var5) {
         System.out.println("Failed to save " + filename + ".dat!");
         var5.printStackTrace();
         return false;
      }
   }

   public static boolean saveData(String filename, NBTTagCompound data) {
      return saveData(getSaveDirectory(MinecraftServer.getServer().getFolderName()), filename, data);
   }

   public static NBTTagCompound loadData(File saveDirectory, String filename) {
      try {
         File e = new File(saveDirectory, filename + ".dat");
         if(e.exists()) {
            FMLLog.fine("Loaded " + filename + " data.", new Object[0]);
            return CompressedStreamTools.readCompressed(new FileInputStream(e));
         } else {
            FMLLog.fine("Created new " + filename + " data.", new Object[0]);
            return new NBTTagCompound();
         }
      } catch (Exception var3) {
         System.out.println("Failed to load " + filename + ".dat!");
         var3.printStackTrace();
         return null;
      }
   }

   public static NBTTagCompound loadData(String filename) {
      return loadData(getSaveDirectory(MinecraftServer.getServer().getFolderName()), filename);
   }

   public static File getSaveDirectory(String worldName) {
      File parent = getBaseDirectory();
      if(FMLCommonHandler.instance().getSide().isClient()) {
         parent = new File(getBaseDirectory(), "saves" + File.separator);
      }

      return new File(parent, worldName + File.separator);
   }

   public static File getBaseDirectory() {
      if(FMLCommonHandler.instance().getSide().isClient()) {
         FMLClientHandler.instance().getClient();
         return Minecraft.getMinecraft().mcDataDir;
      } else {
         return new File(".");
      }
   }
}
