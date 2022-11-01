package universalelectricity.prefab.modifier;

import net.minecraft.item.ItemStack;

public interface IModifier
{
    String getType(final ItemStack p0);
    
    double getEffectiveness(final ItemStack p0);
    
    int getTier(final ItemStack p0);
}
