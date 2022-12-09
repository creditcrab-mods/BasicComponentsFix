package universalelectricity.compat.ic2;

public class IC2CompatHelper {

    public static int voltToTier(double volt) {
        if (volt <= 120.0) return 1;
        if (volt <= 240.0) return 2;
        if (volt <= 480.0) return 3;
        return 4;
    }

    public static double tierToVolt(int tier) {
        switch (tier) {
            case 1: return 120.0;
            case 2: return 240.0;
            case 3: return 480.0;
            default: return 960.0;
        }
    }

}
