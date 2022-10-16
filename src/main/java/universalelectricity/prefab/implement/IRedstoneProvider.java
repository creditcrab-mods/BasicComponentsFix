package universalelectricity.prefab.implement;

import net.minecraftforge.common.util.ForgeDirection;

public interface IRedstoneProvider {

   boolean isPoweringTo(ForgeDirection var1);

   boolean isIndirectlyPoweringTo(ForgeDirection var1);
}
