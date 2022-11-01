package universalelectricity.compat;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.electricity.ElectricityPack;

public interface IElectricityTileHandler {
    
    boolean canInsert();

    boolean canExtract();

    boolean canInsertOn(ForgeDirection side);

    boolean canExtractOn(ForgeDirection side);

    void insert(ElectricityPack pack, ForgeDirection side);

    void extract(ElectricityPack pack, ForgeDirection side);

    ElectricityPack getDemandedJoules();

    ElectricityPack getProvidedJoules();

    TileEntity getTile();

    double getVoltage();

}
