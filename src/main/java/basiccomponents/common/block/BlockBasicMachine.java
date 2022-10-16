package basiccomponents.common.block;

import basiccomponents.common.BasicComponents;
import basiccomponents.common.tileentity.TileEntityBatteryBox;
import basiccomponents.common.tileentity.TileEntityCoalGenerator;
import basiccomponents.common.tileentity.TileEntityElectricFurnace;
import java.util.List;
import java.util.Random;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.block.BlockAdvanced;

public class BlockBasicMachine extends BlockAdvanced {

   public static final int COAL_GENERATOR_METADATA = 0;
   public static final int BATTERY_BOX_METADATA = 4;
   public static final int ELECTRIC_FURNACE_METADATA = 8;
   private IIcon iconMachineSide;
   private IIcon iconInput;
   private IIcon iconOutput;
   private IIcon iconCoalGenerator;
   private IIcon iconBatteryBox;
   private IIcon iconElectricFurnace;


   public BlockBasicMachine(int textureIndex) {
      super(UniversalElectricity.machine);
      this.setBlockName("basiccomponents:bcMachine");
      this.setCreativeTab(CreativeTabs.tabDecorations);
      this.setStepSound(soundTypeMetal); //TODO Metal Footstep
   }

   @Override
   public void registerBlockIcons(IIconRegister par1IconRegister) {
      this.blockIcon = par1IconRegister.registerIcon("basiccomponents:machine");
      this.iconInput = par1IconRegister.registerIcon("basiccomponents:machine_input");
      this.iconOutput = par1IconRegister.registerIcon("basiccomponents:machine_output");
      this.iconMachineSide = par1IconRegister.registerIcon("basiccomponents:machine_side");
      this.iconCoalGenerator = par1IconRegister.registerIcon("basiccomponents:coalGenerator");
      this.iconBatteryBox = par1IconRegister.registerIcon("basiccomponents:batteryBox");
      this.iconElectricFurnace = par1IconRegister.registerIcon("basiccomponents:electricFurnace");
   }

   @Override
   public void randomDisplayTick(World par1World, int x, int y, int z, Random par5Random) {
      TileEntity tile = par1World.getTileEntity(x, y, z);
      if(tile instanceof TileEntityCoalGenerator) {
         TileEntityCoalGenerator tileEntity = (TileEntityCoalGenerator)tile;
         if(tileEntity.generateWatts > 0.0D) {
            int metadata = par1World.getBlockMetadata(x, y, z);
            float var7 = (float)x + 0.5F;
            float var8 = (float)y + 0.0F + par5Random.nextFloat() * 6.0F / 16.0F;
            float var9 = (float)z + 0.5F;
            float var10 = 0.52F;
            float var11 = par5Random.nextFloat() * 0.6F - 0.3F;
            if(metadata == 3) {
               par1World.spawnParticle("smoke", (double)(var7 - var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
               par1World.spawnParticle("flame", (double)(var7 - var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
            } else if(metadata == 2) {
               par1World.spawnParticle("smoke", (double)(var7 + var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
               par1World.spawnParticle("flame", (double)(var7 + var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
            } else if(metadata == 1) {
               par1World.spawnParticle("smoke", (double)(var7 + var11), (double)var8, (double)(var9 - var10), 0.0D, 0.0D, 0.0D);
               par1World.spawnParticle("flame", (double)(var7 + var11), (double)var8, (double)(var9 - var10), 0.0D, 0.0D, 0.0D);
            } else if(metadata == 0) {
               par1World.spawnParticle("smoke", (double)(var7 + var11), (double)var8, (double)(var9 + var10), 0.0D, 0.0D, 0.0D);
               par1World.spawnParticle("flame", (double)(var7 + var11), (double)var8, (double)(var9 + var10), 0.0D, 0.0D, 0.0D);
            }
         }
      }

   }

   @Override
   public IIcon getIcon(int side, int metadata) {
      if(side != 0 && side != 1) {
         if(metadata >= 8) {
            metadata -= 8;
            if(side == metadata + 2) {
               return this.iconInput;
            }

            if(side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal()) {
               return this.iconElectricFurnace;
            }
         } else {
            if(metadata >= 4) {
               metadata -= 4;
               if(side == metadata + 2) {
                  return this.iconOutput;
               }

               if(side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal()) {
                  return this.iconInput;
               }

               return this.iconBatteryBox;
            }

            if(side == metadata + 2) {
               return this.iconOutput;
            }

            if(side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal()) {
               return this.iconCoalGenerator;
            }
         }

         return this.iconMachineSide;
      } else {
         return this.blockIcon;
      }
   }

   @Override
   public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
      int metadata = world.getBlockMetadata(x, y, z);
      int angle = MathHelper.floor_double((double)(entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
      byte change = 0;
      switch(angle) {
      case 0:
         change = 1;
         break;
      case 1:
         change = 2;
         break;
      case 2:
         change = 0;
         break;
      case 3:
         change = 3;
      }

      if(metadata >= 8) {
         world.setBlockMetadataWithNotify(x, y, z, 8 + change, 3);
      } else if(metadata >= 4) {
         switch(angle) {
         case 0:
            change = 3;
            break;
         case 1:
            change = 1;
            break;
         case 2:
            change = 2;
            break;
         case 3:
            change = 0;
         }

         world.setBlockMetadataWithNotify(x, y, z, 4 + change, 3);
      } else {
         world.setBlockMetadataWithNotify(x, y, z, 0 + change, 3);
      }

   }

   public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ) {
      int metadata = par1World.getBlockMetadata(x, y, z);
      int original = metadata;
      int change = 0;
      if(metadata >= 8) {
         original = metadata - 8;
      } else if(metadata >= 4) {
         original = metadata - 4;
      }

      switch(original) {
      case 0:
         change = 3;
         break;
      case 1:
         change = 2;
         break;
      case 2:
         change = 0;
         break;
      case 3:
         change = 1;
      }

      if(metadata >= 8) {
         change += 8;
      } else if(metadata >= 4) {
         change += 4;
      }

      par1World.setBlockMetadataWithNotify(x, y, z, change, 3);
      return true;
   }

   public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ) {
      int metadata = par1World.getBlockMetadata(x, y, z);
      if(!par1World.isRemote) {
         if(metadata >= 8) {
            par5EntityPlayer.openGui(BasicComponents.INSTANCE, -1, par1World, x, y, z);
            return true;
         } else if(metadata >= 4) {
            par5EntityPlayer.openGui(BasicComponents.INSTANCE, -1, par1World, x, y, z);
            return true;
         } else {
            par5EntityPlayer.openGui(BasicComponents.INSTANCE, -1, par1World, x, y, z);
            return true;
         }
      } else {
         return true;
      }
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public TileEntity createNewTileEntity(World world, int metadata) {
      return (TileEntity)(metadata >= 8?new TileEntityElectricFurnace():(metadata >= 4?new TileEntityBatteryBox():new TileEntityCoalGenerator()));
   }

   @Override
   public TileEntity createTileEntity(World var1, int meta) {
      return createNewTileEntity(var1, meta);
   }

   @Override
   public boolean hasTileEntity(int metadata) {
      return true;
   }

   public ItemStack getCoalGenerator() {
      return new ItemStack(Item.getItemFromBlock(this), 1, 0);
   }

   public ItemStack getBatteryBox() {
      return new ItemStack(Item.getItemFromBlock(this), 1, 4);
   }

   public ItemStack getElectricFurnace() {
      return new ItemStack(Item.getItemFromBlock(this), 1, 8);
   }

   @Override
   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(this.getCoalGenerator());
      par3List.add(this.getBatteryBox());
      par3List.add(this.getElectricFurnace());
   }

   @Override
   public int damageDropped(int metadata) {
      return metadata >= 8?8:(metadata >= 4?4:0);
   }

   //TODO WTF
   /*@Override
   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      int id = this.func_71922_a(world, x, y, z);
      if(id == 0) {
         return null;
      } else {
         Item item = Item.field_77698_e[id];
         if(item == null) {
            return null;
         } else {
            int metadata = this.func_71873_h(world, x, y, z);
            return new ItemStack(id, 1, metadata);
         }
      }
   }*/
}
