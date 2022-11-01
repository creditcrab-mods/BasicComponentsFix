package basiccomponents.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BCTab extends CreativeTabs {

    public static final BCTab INSTANCE = new BCTab();

    public BCTab() {
        super("BasicComponents");
    }

    @Override
    public Item getTabIconItem() {
        return Item.getItemFromBlock(BasicComponents.blockCopperWire);
    }
    
}
