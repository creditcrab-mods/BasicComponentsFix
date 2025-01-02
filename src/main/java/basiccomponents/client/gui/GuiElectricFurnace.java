package basiccomponents.client.gui;

import basiccomponents.common.container.ContainerElectricFurnace;
import basiccomponents.common.tileentity.TileEntityElectricFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import universalelectricity.api.energy.UnitDisplay;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiElectricFurnace extends GuiContainer {

   private TileEntityElectricFurnace tileEntity;
   private int containerWidth;
   private int containerHeight;


   public GuiElectricFurnace(InventoryPlayer par1InventoryPlayer, TileEntityElectricFurnace tileEntity) {
      super(new ContainerElectricFurnace(par1InventoryPlayer, tileEntity));
      this.tileEntity = tileEntity;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int par1, int par2) {
      this.fontRendererObj.drawString(this.tileEntity.getInventoryName(), 45, 6, 4210752);
      this.fontRendererObj.drawString("Smelting:", 10, 28, 4210752);
      this.fontRendererObj.drawString("Battery:", 10, 53, 4210752);
      String displayText = "";
      if(this.tileEntity.isDisabled()) {
         displayText = "Disabled!";
      } else if(this.tileEntity.processTicks > 0) {
         displayText = "Smelting";
      } else {
         displayText = "Idle";
      }

      this.fontRendererObj.drawString("Status: " + displayText, 82, 45, 4210752);
      this.fontRendererObj.drawString(tileEntity.RF_PER_TICK + " RF/t", 82, 56, 4210752);
      this.fontRendererObj.drawString(tileEntity.energyStorage.getEnergyStored() + " RF", 82, 68, 4210752);
      this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
      this.mc.renderEngine.bindTexture(new ResourceLocation("basiccomponents", "textures/gui/electric_furnace.png"));
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.containerWidth = (this.width - this.xSize) / 2;
      this.containerHeight = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);
      if(this.tileEntity.processTicks > 0) {
         int scale = (int)((double)this.tileEntity.processTicks / 130.0D * 23.0D);
         this.drawTexturedModalRect(this.containerWidth + 77, this.containerHeight + 24, 176, 0, 23 - scale, 20);
      }

   }
}
