package basiccomponents.common;

import basiccomponents.client.RenderCopperWire;
import basiccomponents.common.BCGuiHandler;
import basiccomponents.common.CommonProxy;
import basiccomponents.common.block.BlockBase;
import basiccomponents.common.block.BlockBasicMachine;
import basiccomponents.common.block.BlockCopperWire;
import basiccomponents.common.item.ItemBase;
import basiccomponents.common.item.ItemBattery;
import basiccomponents.common.item.ItemBlockBasicMachine;
import basiccomponents.common.item.ItemBlockCopperWire;
import basiccomponents.common.item.ItemInfiniteBattery;
import basiccomponents.common.item.ItemIngot;
import basiccomponents.common.item.ItemPlate;
import basiccomponents.common.item.ItemWrench;
import basiccomponents.common.tileentity.TileEntityBatteryBox;
import basiccomponents.common.tileentity.TileEntityCoalGenerator;
import basiccomponents.common.tileentity.TileEntityCopperWire;
import basiccomponents.common.tileentity.TileEntityElectricFurnace;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.prefab.RecipeHelper;
import universalelectricity.prefab.TranslationHelper;
import universalelectricity.prefab.ore.OreGenBase;
import universalelectricity.prefab.ore.OreGenReplaceStone;
import universalelectricity.prefab.ore.OreGenerator;

@Mod(modid = BasicComponents.MODID, name = BasicComponents.NAME, version = BasicComponents.VERSION)
public class BasicComponents {

   public static final String NAME = "Basic Components";
   public static final String MODID = "basiccomponents";
   public static final String VERSION = "1.0.0-dirty";
   public static String CHANNEL = "";
   public static final String RESOURCE_PATH = "/mods/basiccomponents/";
   public static CommonProxy proxy;
   public static final Configuration CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(), "BasicComponents.cfg"));
   public static final String TEXTURE_DIRECTORY = "/mods/basiccomponents/textures/";
   public static final String GUI_DIRECTORY = "/mods/basiccomponents/textures/gui/";
   public static final String BLOCK_TEXTURE_DIRECTORY = "/mods/basiccomponents/textures/blocks/";
   public static final String ITEM_TEXTURE_DIRECTORY = "/mods/basiccomponents/textures/items/";
   public static final String MODEL_TEXTURE_DIRECTORY = "/mods/basiccomponents/textures/models/";
   public static final String TEXTURE_NAME_PREFIX = "basiccomponents:";
   public static final String LANGUAGE_PATH = "/mods/basiccomponents/languages/";
   private static final String[] LANGUAGES_SUPPORTED = new String[]{"en_US", "zh_CN", "es_ES", "it_IT", "nl_NL", "de_DE"};
   public static Block blockOreCopper;
   public static Block blockOreTin;
   public static Block blockCopperWire;
   public static Block blockMachine;
   public static Item itemBattery;
   public static Item itemInfiniteBattery;
   public static Item itemWrench;
   public static Item itemMotor;
   public static Item itemCircuitBasic;
   public static Item itemCircuitAdvanced;
   public static Item itemCircuitElite;
   public static Item itemPlateCopper;
   public static Item itemPlateTin;
   public static Item itemPlateBronze;
   public static Item itemPlateSteel;
   public static Item itemPlateIron;
   public static Item itemPlateGold;
   public static Item itemIngotCopper;
   public static Item itemIngotTin;
   public static Item itemIngotSteel;
   public static Item itemIngotBronze;
   public static Item itemDustSteel;
   public static Item itemDustBronze;
   public static OreGenBase generationOreCopper;
   public static OreGenBase generationOreTin;
   public static final ArrayList bcDependants = new ArrayList();
   public static SimpleNetworkWrapper NETWRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel("basiccomponents");
   @Mod.Instance
   public static BasicComponents INSTANCE;

   @Mod.EventHandler
   public void preInit(FMLPreInitializationEvent e) {
   
   }

   @Mod.EventHandler
   public void init(FMLInitializationEvent e) {
      System.out.println("Basic Components Loaded: " + TranslationHelper.loadLanguages("/assets/basiccomponents/languages/", LANGUAGES_SUPPORTED) + " Languages.");
      NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new BCGuiHandler());
      CONFIGURATION.load();
      registerIngots();
      registerPlates();
      registerDusts();

      registerBattery();
      registerWrench();

      registerTileEntities();
      registerOres();
      registerCopperWire();
      registerCircuits();
      registerMachines();
      CONFIGURATION.save();
   }

   @Mod.EventHandler
   public void postInit(FMLPostInitializationEvent e) {
       
   }

   public static void registerDusts() {
      if (OreDictionary.getOres("dustBronze").isEmpty()) {
         itemDustBronze = new ItemBase("dustBronze");
         itemDustBronze.setCreativeTab(CreativeTabs.tabMaterials);
         GameRegistry.registerItem(itemDustBronze, "dustBronze");
         RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(itemDustBronze), new Object[]{"!#!", Character.valueOf('!'), "ingotCopper", Character.valueOf('#'), "ingotTin"}), CONFIGURATION, true);
         GameRegistry.addSmelting(itemDustBronze, (ItemStack)OreDictionary.getOres("ingotBronze").get(0), 0.6F);
      }
      if (OreDictionary.getOres("dustSteel").isEmpty()) {
         itemDustSteel = new ItemBase("dustSteel");
         itemDustSteel.setCreativeTab(CreativeTabs.tabMaterials);
         GameRegistry.registerItem(itemDustSteel, "dustSteel");
         RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(itemDustSteel), new Object[]{" C ", "CIC", " C ", Character.valueOf('I'), Items.iron_ingot, Character.valueOf('C'), Items.coal}), CONFIGURATION, true);
         GameRegistry.addSmelting(itemDustSteel, (ItemStack)OreDictionary.getOres("ingotSteel").get(0), 0.6F);
      }
   }

   public static void registerPlates() {
      if (OreDictionary.getOres("plateCopper").isEmpty()) {
         itemPlateCopper = new ItemPlate("plateCopper");
         GameRegistry.registerItem(itemPlateCopper, "plateCopper");
         OreDictionary.registerOre("plateCopper", itemPlateCopper);
         RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(itemPlateCopper), new Object[]{"!!", "!!", Character.valueOf('!'), "ingotCopper"}), CONFIGURATION, true);
      }
      if (OreDictionary.getOres("plateTin").isEmpty()) {
         itemPlateTin = new ItemPlate("plateTin");
         GameRegistry.registerItem(itemPlateTin, "plateTin");
         OreDictionary.registerOre("plateTin", itemPlateTin);
         RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(itemPlateTin), new Object[]{"!!", "!!", Character.valueOf('!'), "ingotTin"}), CONFIGURATION, true);
      }
      if (OreDictionary.getOres("plateIron").isEmpty()) {
         itemPlateIron = new ItemPlate("plateIron");
         GameRegistry.registerItem(itemPlateIron, "plateIron");
         OreDictionary.registerOre("plateIron", itemPlateIron);
         RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(itemPlateIron), new Object[]{"!!", "!!", Character.valueOf('!'), "ingotIron"}), CONFIGURATION, true);
      }
      if (OreDictionary.getOres("plateGold").isEmpty()) {
         itemPlateGold = new ItemPlate("plateGold");
         GameRegistry.registerItem(itemPlateGold, "plateGold");
         OreDictionary.registerOre("plateGold", itemPlateGold);
         RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(itemPlateGold), new Object[]{"!!", "!!", Character.valueOf('!'), "ingotGold"}), CONFIGURATION, true);
      }
      if (OreDictionary.getOres("plateBronze").isEmpty()) {
         itemPlateBronze = new ItemPlate("plateBronze");
         GameRegistry.registerItem(itemPlateBronze, "plateBronze");
         OreDictionary.registerOre("plateBronze", itemPlateBronze);
         RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(itemPlateBronze), new Object[]{"!!", "!!", Character.valueOf('!'), "ingotBronze"}), CONFIGURATION, true);
      }
      if (OreDictionary.getOres("plateSteel").isEmpty()) {
         itemPlateSteel = new ItemPlate("plateSteel");
         GameRegistry.registerItem(itemPlateSteel, "plateSteel");
         OreDictionary.registerOre("plateSteel", itemPlateSteel);
         RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(itemPlateSteel), new Object[]{"!!", "!!", Character.valueOf('!'), "ingotSteel"}), CONFIGURATION, true);
      }
   }

   public static void registerIngots() {
      if (OreDictionary.getOres("ingotCopper").isEmpty()) {
         itemIngotCopper = new ItemIngot("ingotCopper");
         GameRegistry.registerItem(itemIngotCopper, "ingotCopper");
         OreDictionary.registerOre("ingotCopper", itemIngotCopper);
      }
      if (OreDictionary.getOres("ingotTin").isEmpty()) {
         itemIngotTin = new ItemIngot("ingotTin");
         GameRegistry.registerItem(itemIngotTin, "ingotTin");
         OreDictionary.registerOre("ingotTin", itemIngotTin);
      }
      if (OreDictionary.getOres("ingotBronze").isEmpty()) {
         itemIngotBronze = new ItemIngot("ingotBronze");
         GameRegistry.registerItem(itemIngotBronze, "ingotBronze");
         OreDictionary.registerOre("ingotBronze", itemIngotBronze);
      }
      if (OreDictionary.getOres("ingotSteel").isEmpty()) {
         itemIngotSteel = new ItemIngot("ingotSteel");
         GameRegistry.registerItem(itemIngotSteel, "ingotSteel");
         OreDictionary.registerOre("ingotSteel", itemIngotSteel);
      }
   }

   public static void registerCircuits() {
      itemCircuitBasic = new ItemBase("circuitBasic");
      itemCircuitBasic.setCreativeTab(CreativeTabs.tabMaterials);
      GameRegistry.registerItem(itemCircuitBasic, "circuitBasic");
      RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(itemCircuitBasic), new Object[]{"!#!", "#@#", "!#!", Character.valueOf('@'), "plateBronze", Character.valueOf('#'), Items.redstone, Character.valueOf('!'), "copperWire"}), CONFIGURATION, true);
      RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(itemCircuitBasic), new Object[]{"!#!", "#@#", "!#!", Character.valueOf('@'), "plateSteel", Character.valueOf('#'), Items.redstone, Character.valueOf('!'), "copperWire"}), CONFIGURATION, true);
      OreDictionary.registerOre("circuitBasic", itemCircuitBasic);
      itemCircuitAdvanced = new ItemBase("circuitAdvanced");
      itemCircuitAdvanced.setCreativeTab(CreativeTabs.tabMaterials);
      GameRegistry.registerItem(itemCircuitAdvanced, "circuitAdvanced");
      RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(itemCircuitAdvanced), new Object[]{"@@@", "#?#", "@@@", Character.valueOf('@'), Items.redstone, Character.valueOf('?'), Items.diamond, Character.valueOf('#'), "circuitBasic"}), CONFIGURATION, true);
      OreDictionary.registerOre("circuitAdvanced", itemCircuitAdvanced);
      itemCircuitElite = new ItemBase("circuitElite");
      itemCircuitElite.setCreativeTab(CreativeTabs.tabMaterials);
      GameRegistry.registerItem(itemCircuitElite, "circuitElite");
      RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(itemCircuitAdvanced), new Object[]{"@@@", "?#?", "@@@", Character.valueOf('@'), Items.gold_ingot, Character.valueOf('?'), "circuitAdvanced", Character.valueOf('#'), Blocks.lapis_block}), CONFIGURATION, true);
      OreDictionary.registerOre("circuitElite", itemCircuitElite);
      itemMotor = new ItemBase("motor");
      itemMotor.setCreativeTab(CreativeTabs.tabMaterials);
      GameRegistry.registerItem(itemMotor, "motor");
      RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(itemMotor), new Object[]{"@!@", "!#!", "@!@", Character.valueOf('!'), "ingotSteel", Character.valueOf('#'), Items.iron_ingot, Character.valueOf('@'), "copperWire"}), CONFIGURATION, true);
      OreDictionary.registerOre("motor", itemMotor);
   }

   public static void registerWrench() {
      itemWrench = new ItemWrench();
      GameRegistry.registerItem(itemWrench, "wrench");
      RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(itemWrench), new Object[]{" S ", " SS", "S  ", Character.valueOf('S'), Items.iron_ingot}), CONFIGURATION, true);
   }

   public static void registerBattery() {
      itemInfiniteBattery = new ItemInfiniteBattery("infiniteBattery");
      GameRegistry.registerItem(itemInfiniteBattery, "infiniteBattery");
      itemBattery = new ItemBattery("battery");
      GameRegistry.registerItem(itemBattery, "battery");
      RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(itemBattery), new Object[]{" T ", "TRT", "TCT", Character.valueOf('T'), "ingotTin", Character.valueOf('R'), Items.redstone, Character.valueOf('C'), Items.coal}), CONFIGURATION, true);
      OreDictionary.registerOre("battery", ElectricItemHelper.getUncharged(itemBattery));

   }

   public static void registerOres() {
      for (ItemStack stack : OreDictionary.getOres("oreCopper")) {
         if (stack.getItem() instanceof ItemBlock) {
            blockOreCopper = Block.getBlockFromItem(stack.getItem());
            break;
         }
      }
      for (ItemStack stack : OreDictionary.getOres("oreTin")) {
         if (stack.getItem() instanceof ItemBlock) {
            blockOreTin = Block.getBlockFromItem(stack.getItem());
            break;
         }
      }
      if (blockOreCopper == null) {
         blockOreCopper = new BlockBase("oreCopper");
         GameRegistry.registerBlock(blockOreCopper, "oreCopper");
         GameRegistry.addSmelting(blockOreCopper, OreDictionary.getOres("ingotCopper").get(0), 0.6F);
         generationOreCopper = new OreGenReplaceStone("oreCopper", "oreCopper", new ItemStack(blockOreCopper), 60, 22, 4);
         OreGenerator.addOre(generationOreCopper);
      }
      if (blockOreTin == null) {
         blockOreTin = new BlockBase("oreTin");
         GameRegistry.registerBlock(blockOreTin, "oreTin");
         GameRegistry.addSmelting(blockOreTin, OreDictionary.getOres("ingotTin").get(0), 0.6F);
         generationOreTin = new OreGenReplaceStone("oreTin", "oreTin", new ItemStack(blockOreTin), 60, 22, 4);
         OreGenerator.addOre(generationOreTin);
      }
   }

   public static void registerCopperWire() {
      blockCopperWire = new BlockCopperWire();
      GameRegistry.registerBlock(blockCopperWire, ItemBlockCopperWire.class, "copperWire");
      if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
         try {
            registerCopperWireRenderer();
         } catch (Exception var8) {
            FMLLog.severe("Basic Components copper wire registry error!", new Object[0]);
            var8.printStackTrace();
         }
      }
      RecipeHelper.addRecipe(new ShapedOreRecipe(new ItemStack(blockCopperWire, 6), new Object[]{"WWW", "CCC", "WWW", Character.valueOf('W'), Blocks.wool, Character.valueOf('C'), "ingotCopper"}), CONFIGURATION, true);
      UniversalElectricity.isNetworkActive = true;
      OreDictionary.registerOre("copperWire", blockCopperWire);
   }

   @SideOnly(Side.CLIENT)
   private static void registerCopperWireRenderer() throws Exception {
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCopperWire.class, new RenderCopperWire());
   }

   public static ItemStack registerMachines() {
      blockMachine = new BlockBasicMachine(0);
      GameRegistry.registerBlock(blockMachine, ItemBlockBasicMachine.class, "basicMachine");
      ItemStack generator = ((BlockBasicMachine)blockMachine).getCoalGenerator();
      ItemStack bbox = ((BlockBasicMachine)blockMachine).getBatteryBox();
      ItemStack efurnace = ((BlockBasicMachine)blockMachine).getElectricFurnace();
      OreDictionary.registerOre("coalGenerator", generator.copy());
      OreDictionary.registerOre("batteryBox", bbox.copy());
      OreDictionary.registerOre("electricFurnace", efurnace.copy());
      RecipeHelper.addRecipe(new ShapedOreRecipe(bbox.copy(), new Object[]{"SSS", "BBB", "SSS", Character.valueOf('B'), "battery", Character.valueOf('S'), "ingotSteel"}), CONFIGURATION, true);
      RecipeHelper.addRecipe(new ShapedOreRecipe(generator.copy(), new Object[]{"MMM", "MOM", "MCM", Character.valueOf('M'), "ingotSteel", Character.valueOf('C'), "motor", Character.valueOf('O'), Blocks.furnace}), CONFIGURATION, true);
      RecipeHelper.addRecipe(new ShapedOreRecipe(generator.copy(), new Object[]{"MMM", "MOM", "MCM", Character.valueOf('M'), "ingotBronze", Character.valueOf('C'), "motor", Character.valueOf('O'), Blocks.furnace}), CONFIGURATION, true);
      RecipeHelper.addRecipe(new ShapedOreRecipe(efurnace.copy(), new Object[]{"SSS", "SCS", "SMS", Character.valueOf('S'), "ingotSteel", Character.valueOf('C'), "circuitAdvanced", Character.valueOf('M'), "motor"}), CONFIGURATION, true);

      return new ItemStack(blockMachine);
   }

   public static void registerTileEntities() {
      GameRegistry.registerTileEntity(TileEntityBatteryBox.class, "UEBatteryBox");
      GameRegistry.registerTileEntity(TileEntityCoalGenerator.class, "UECoalGenerator");
      GameRegistry.registerTileEntity(TileEntityElectricFurnace.class, "UEElectricFurnace");
      GameRegistry.registerTileEntity(TileEntityCopperWire.class, "copperWire");
   }

}
