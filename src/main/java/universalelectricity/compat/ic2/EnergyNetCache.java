package universalelectricity.compat.ic2;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.Pair;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.tile.ElectricTileDriver;

public class EnergyNetCache {
    
    private static Map<Pair<World, Vector3>, ElectricTileDriver> tiles = new HashMap<>();

    public static Pair<World, Vector3> toKey(TileEntity tile) {
        return new Pair<World, Vector3>(tile.getWorldObj(), new Vector3(tile));
    }

    public static void load(TileEntity tile) {
        tiles.put(toKey(tile), new ElectricTileDriver(new ElectricityTileWrapper(tile)));
    }

    public static void unload(TileEntity tile) {
        ElectricTileDriver handler = tiles.get(toKey(tile));
        if (handler != null) handler.invalidate();
        tiles.remove(toKey(tile));
    }

    public static boolean canConnect(Pair<World, Vector3> pos, ForgeDirection side) {
        ElectricTileDriver handler = tiles.get(pos);
        return handler == null ? false : handler.canConnect(side);
    }

    public static void tickAll() {
        for (ElectricTileDriver handler : tiles.values()) {
            handler.tick();
        }
    }

    public static boolean contains(TileEntity tile) {
        return tiles.containsKey(toKey(tile));
    }

}
