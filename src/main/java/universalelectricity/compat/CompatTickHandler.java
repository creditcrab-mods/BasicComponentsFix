package universalelectricity.compat;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class CompatTickHandler {
    
    @SubscribeEvent
    public void onTick(ServerTickEvent event) {
        if (event.phase == Phase.END) {
            CompatHandler.tick();
        }
    }

}
