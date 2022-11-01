package universalelectricity.compat.ic2;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class TickHandler {
    
    @SubscribeEvent
    public void onTick(ServerTickEvent event) {
        if (event.phase == Phase.END) {
            EnergyNetCache.tickAll();
        }
    }

}
