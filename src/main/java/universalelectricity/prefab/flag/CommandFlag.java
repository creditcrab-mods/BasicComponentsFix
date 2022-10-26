package universalelectricity.prefab.flag;

import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import universalelectricity.core.vector.Vector3;

public class CommandFlag extends CommandBase {

   public static final String[] COMMANDS = new String[]{"list", "setregion", "removeregion", "set"};
   public String commandName;
   public ModFlag modFlagData;


   public CommandFlag(ModFlag modFlag) {
      this.commandName = "modflag";
      this.modFlagData = modFlag;
   }

   public CommandFlag(ModFlag modFlag, String commandName) {
      this(modFlag);
      this.commandName = commandName;
   }

   public String getCommandName() {
      return this.commandName;
   }

   public String getCommandUsage(ICommandSender par1ICommandSender) {
      String returnString = "";
      String[] arr$ = COMMANDS;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String command = arr$[i$];
         returnString = returnString + "\n/" + this.getCommandName() + " " + command;
      }

      return returnString;
   }

   public void processCommand(ICommandSender sender, String[] args) {
      if(args.length > 0) {
         EntityPlayer entityPlayer = (EntityPlayer)sender;
         FlagWorld flagWorld = this.modFlagData.getFlagWorld(entityPlayer.worldObj);
         String commandName = args[0].toLowerCase();
         String regionName;
         String flagName;
         FlagRegion flagRegion;
         if(commandName.equalsIgnoreCase("list")) {
            if(args.length > 1) {
               regionName = args[1];
               Iterator flagRegion1;
               if(regionName.equalsIgnoreCase("all")) {
                  flagName = "";
                  flagRegion1 = this.modFlagData.getFlagWorlds().iterator();

                  FlagRegion i$1;
                  while(flagRegion1.hasNext()) {
                     for(Iterator flags1 = ((FlagWorld)flagRegion1.next()).getRegions().iterator(); flags1.hasNext(); flagName = flagName + " " + i$1.name + " (" + i$1.region.min.x + "," + i$1.region.min.z + ")" + ",") {
                        i$1 = (FlagRegion)flags1.next();
                     }
                  }

                  if(flagName != "") {
                     flagName = "List of regions in world:\n" + flagName;
                  } else {
                     flagName = "No regions in this world.";
                  }

                  sender.addChatMessage(new ChatComponentText(flagName));
               } else {
                  Flag flags2;
                  if(flagWorld.getRegion(regionName) != null) {
                     flagName = "";

                     for(flagRegion1 = flagWorld.getRegion(regionName).getFlags().iterator(); flagRegion1.hasNext(); flagName = flagName + " " + flags2.name + " => " + flags2.value + ",") {
                        flags2 = (Flag)flagRegion1.next();
                     }

                     if(flagName != "") {
                        flagName = "List of flags in region " + regionName + ":\n" + flagName;
                     } else {
                        flagName = "No flags in this region.";
                     }

                     sender.addChatMessage(new ChatComponentText(flagName));
                  } else {
                     flagName = "Region does not exist, but here are existing flags in the position you are standing on:\n";

                     for(flagRegion1 = flagWorld.getFlagsInPosition(new Vector3(entityPlayer)).iterator(); flagRegion1.hasNext(); flagName = flagName + " " + flags2.name + "=>" + flags2.value + ",") {
                        flags2 = (Flag)flagRegion1.next();
                     }

                     sender.addChatMessage(new ChatComponentText(flagName));
                  }
               }
            } else {
               regionName = "";

               for(Iterator flagName3 = flagWorld.getRegions().iterator(); flagName3.hasNext(); regionName = regionName + " " + flagRegion.name + " (" + flagRegion.region.min.x + "," + flagRegion.region.min.z + ")" + ",") {
                  flagRegion = (FlagRegion)flagName3.next();
               }

               if(regionName != "") {
                  regionName = "List of regions in this dimension:\n" + regionName;
               } else {
                  regionName = "No regions in this dimension.";
               }

               sender.addChatMessage(new ChatComponentText(regionName));
            }

            return;
         }

         if(commandName.equalsIgnoreCase("setregion")) {
            if(args.length > 1) {
               regionName = args[1];
               if(regionName.equalsIgnoreCase("dimension")) {
                  if(flagWorld.addRegion(regionName, new Vector3(entityPlayer), 1)) {
                     sender.addChatMessage(new ChatComponentText("Created global dimension region setting."));
                     return;
                  }
               } else {
                  if(args.length <= 2) {
                     throw new WrongUsageException("/" + this.getCommandName() + " addregion <name> <radius>", new Object[0]);
                  }

                  boolean flagName1 = false;

                  int flagName2;
                  try {
                     flagName2 = Integer.parseInt(args[2]);
                  } catch (Exception var12) {
                     throw new WrongUsageException("Radius not a number!", new Object[0]);
                  }

                  if(flagName2 <= 0) {
                     throw new WrongUsageException("Radius has to be greater than zero!", new Object[0]);
                  }

                  flagRegion = flagWorld.getRegion(regionName);
                  if(flagRegion == null) {
                     if(flagWorld.addRegion(regionName, new Vector3(entityPlayer), flagName2)) {
                        sender.addChatMessage(new ChatComponentText("Region " + regionName + " added."));
                     }
                  } else {
                     flagRegion.edit(new Vector3(entityPlayer), flagName2);
                     sender.addChatMessage(new ChatComponentText("Region " + regionName + " already exists. Modified region to have a radius of: " + flagName2));
                  }
               }

               return;
            }

            throw new WrongUsageException("Please specify the region name.", new Object[0]);
         }

         if(commandName.equalsIgnoreCase("removeregion")) {
            if(args.length > 1) {
               regionName = args[1];
               if(flagWorld.removeRegion(regionName)) {
                  sender.addChatMessage(new ChatComponentText("Region with name " + regionName + " is removed."));
                  return;
               }

               throw new WrongUsageException("The specified region does not exist in this world.", new Object[0]);
            }

            throw new WrongUsageException("Please specify the region name.", new Object[0]);
         }

         if(commandName.equalsIgnoreCase("set")) {
            if(args.length <= 2) {
               throw new WrongUsageException("/" + this.getCommandName() + " set <regionName> <flagName> <value>", new Object[0]);
            }

            regionName = args[1];
            flagName = args[2];
            flagRegion = flagWorld.getRegion(regionName);
            if(flagRegion == null) {
               throw new WrongUsageException("The specified region \'" + regionName + "\' does not exist.", new Object[0]);
            }

            String flags;
            if(FlagRegistry.flags.contains(flagName)) {
               if(args.length > 3) {
                  flags = args[3];
                  flagRegion.setFlag(flagName, flags);
                  sender.addChatMessage(new ChatComponentText("Flag \'" + flagName + "\' has been set to \'" + flags + "\' in " + regionName + "."));
               } else {
                  flagRegion.removeFlag(flagName);
                  sender.addChatMessage(new ChatComponentText("Removed flag \'" + flagName + "\'."));
               }

               return;
            }

            flags = "Flag does not exist. Existing flags:\n";

            String registeredFlag;
            for(Iterator i$ = FlagRegistry.flags.iterator(); i$.hasNext(); flags = flags + registeredFlag + ", ") {
               registeredFlag = (String)i$.next();
            }

            throw new WrongUsageException(flags, new Object[0]);
         }
      }

      throw new WrongUsageException(this.getCommandUsage(sender), new Object[0]);
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public List addTabCompletionOptions(ICommandSender sender, String[] args) {
      return args.length == 1?getListOfStringsMatchingLastWord(args, COMMANDS):null;
   }

}
