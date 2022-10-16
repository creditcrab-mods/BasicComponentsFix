package basiccomponents.common;

import basiccomponents.client.gui.GuiBatteryBox;
import basiccomponents.client.gui.GuiCoalGenerator;
import basiccomponents.client.gui.GuiElectricFurnace;
import basiccomponents.common.container.ContainerBatteryBox;
import basiccomponents.common.container.ContainerCoalGenerator;
import basiccomponents.common.container.ContainerElectricFurnace;
import basiccomponents.common.tileentity.TileEntityBatteryBox;
import basiccomponents.common.tileentity.TileEntityCoalGenerator;
import basiccomponents.common.tileentity.TileEntityElectricFurnace;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BCGuiHandler implements IGuiHandler {

   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      TileEntity tileEntity = world.getTileEntity(x, y, z);
      if(tileEntity != null) {
         if(tileEntity instanceof TileEntityBatteryBox) {
            return new GuiBatteryBox(player.inventory, (TileEntityBatteryBox)tileEntity);
         }

         if(tileEntity instanceof TileEntityCoalGenerator) {
            return new GuiCoalGenerator(player.inventory, (TileEntityCoalGenerator)tileEntity);
         }

         if(tileEntity instanceof TileEntityElectricFurnace) {
            return new GuiElectricFurnace(player.inventory, (TileEntityElectricFurnace)tileEntity);
         }
      }

      return null;
   }

   public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      TileEntity tileEntity = world.getTileEntity(x, y, z);
      if(tileEntity != null) {
         if(tileEntity instanceof TileEntityBatteryBox) {
            return new ContainerBatteryBox(player.inventory, (TileEntityBatteryBox)tileEntity);
         }

         if(tileEntity instanceof TileEntityCoalGenerator) {
            return new ContainerCoalGenerator(player.inventory, (TileEntityCoalGenerator)tileEntity);
         }

         if(tileEntity instanceof TileEntityElectricFurnace) {
            return new ContainerElectricFurnace(player.inventory, (TileEntityElectricFurnace)tileEntity);
         }
      }

      return null;
   }
}
