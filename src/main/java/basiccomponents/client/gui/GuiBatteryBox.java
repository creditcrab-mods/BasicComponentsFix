package basiccomponents.client.gui;

import basiccomponents.common.container.ContainerBatteryBox;
import basiccomponents.common.tileentity.TileEntityBatteryBox;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import universalelectricity.api.energy.RFDisplay;
import universalelectricity.api.energy.UnitDisplay;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiBatteryBox extends GuiContainer {

   private TileEntityBatteryBox tileEntity;
   private int containerWidth;
   private int containerHeight;


   public GuiBatteryBox(InventoryPlayer par1InventoryPlayer, TileEntityBatteryBox batteryBox) {
      super(new ContainerBatteryBox(par1InventoryPlayer, batteryBox));
      this.tileEntity = batteryBox;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int par1, int par2) {
      this.fontRendererObj.drawString(this.tileEntity.getInventoryName(), 65, 6, 4210752);
      String displayJoules = RFDisplay.displayRF(tileEntity.energyStorage.getEnergyStored());
      String displayMaxJoules = RFDisplay.displayRF(tileEntity.energyStorage.getMaxEnergyStored());
      if(this.tileEntity.isDisabled()) {
         displayMaxJoules = "Disabled";
      }

      this.fontRendererObj.drawString(displayJoules, 98 - displayJoules.length(), 30, 4210752);
      this.fontRendererObj.drawString(displayMaxJoules, 78, 40, 4210752);
      this.fontRendererObj.drawString("In/Out:" + RFDisplay.displayRF(tileEntity.MAX_TRANSFER) + "/t", 90, 60, 4210752);
      this.fontRendererObj.drawString("Charge:" + RFDisplay.displayRF(tileEntity.MAX_TRANSFER_INTERNAL) + "/t", 90, 70, 4210752);
      this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
      this.mc.renderEngine.bindTexture(new ResourceLocation("basiccomponents", "textures/gui/battery_box.png"));
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.containerWidth = (this.width - this.xSize) / 2;
      this.containerHeight = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);
      int scale = (int)((float)this.tileEntity.energyStorage.getEnergyStored() / (float)this.tileEntity.energyStorage.getMaxEnergyStored() * 72.0f);
      this.drawTexturedModalRect(this.containerWidth + 87, this.containerHeight + 52, 176, 0, scale, 20);
   }
}
