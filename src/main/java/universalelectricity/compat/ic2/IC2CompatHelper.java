package universalelectricity.compat.ic2;

import universalelectricity.core.UniversalElectricity;

public class IC2CompatHelper {
    
    public static double EUToJoules(double eu) {
        return eu * UniversalElectricity.UE_IC2_RATIO;
    }

    public static double joulesToEU(double joules) {
        return joules / UniversalElectricity.UE_IC2_RATIO;
    }

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
