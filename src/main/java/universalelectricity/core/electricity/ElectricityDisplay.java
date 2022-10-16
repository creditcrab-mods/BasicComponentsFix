package universalelectricity.core.electricity;


public class ElectricityDisplay {

   public static String getDisplay(double value, ElectricityDisplay.ElectricUnit unit, int decimalPlaces, boolean isShort) {
      String unitName = unit.name;
      if(isShort) {
         unitName = unit.symbol;
      } else if(value > 1.0D) {
         unitName = unit.getPlural();
      }

      return value == 0.0D?value + " " + unitName:(value <= ElectricityDisplay.MeasurementUnit.MILLI.value?roundDecimals(ElectricityDisplay.MeasurementUnit.MICRO.process(value), decimalPlaces) + " " + ElectricityDisplay.MeasurementUnit.MICRO.getName(isShort) + unitName:(value < 1.0D?roundDecimals(ElectricityDisplay.MeasurementUnit.MILLI.process(value), decimalPlaces) + " " + ElectricityDisplay.MeasurementUnit.MILLI.getName(isShort) + unitName:(value > ElectricityDisplay.MeasurementUnit.MEGA.value?roundDecimals(ElectricityDisplay.MeasurementUnit.MEGA.process(value), decimalPlaces) + " " + ElectricityDisplay.MeasurementUnit.MEGA.getName(isShort) + unitName:(value > ElectricityDisplay.MeasurementUnit.KILO.value?roundDecimals(ElectricityDisplay.MeasurementUnit.KILO.process(value), decimalPlaces) + " " + ElectricityDisplay.MeasurementUnit.KILO.getName(isShort) + unitName:roundDecimals(value, decimalPlaces) + " " + unitName))));
   }

   public static String getDisplay(double value, ElectricityDisplay.ElectricUnit unit) {
      return getDisplay(value, unit, 2, false);
   }

   public static String getDisplayShort(double value, ElectricityDisplay.ElectricUnit unit) {
      return getDisplay(value, unit, 2, true);
   }

   public static String getDisplayShort(double value, ElectricityDisplay.ElectricUnit unit, int decimalPlaces) {
      return getDisplay(value, unit, decimalPlaces, true);
   }

   public static String getDisplaySimple(double value, ElectricityDisplay.ElectricUnit unit, int decimalPlaces) {
      return value > 1.0D?(decimalPlaces < 1?(int)value + " " + unit.getPlural():roundDecimals(value, decimalPlaces) + " " + unit.getPlural()):(decimalPlaces < 1?(int)value + " " + unit.name:roundDecimals(value, decimalPlaces) + " " + unit.name);
   }

   public static double roundDecimals(double d, int decimalPlaces) {
      int j = (int)(d * Math.pow(10.0D, (double)decimalPlaces));
      return (double)j / Math.pow(10.0D, (double)decimalPlaces);
   }

   public static double roundDecimals(double d) {
      return roundDecimals(d, 2);
   }

   public static enum ElectricUnit {

      AMPERE("AMPERE", 0, "Amp", "I"),
      AMP_HOUR("AMP_HOUR", 1, "Amp Hour", "Ah"),
      VOLTAGE("VOLTAGE", 2, "Volt", "V"),
      WATT("WATT", 3, "Watt", "W"),
      WATT_HOUR("WATT_HOUR", 4, "Watt Hour", "Wh"),
      RESISTANCE("RESISTANCE", 5, "Ohm", "R"),
      CONDUCTANCE("CONDUCTANCE", 6, "Siemen", "S"),
      JOULES("JOULES", 7, "Joule", "J");
      public String name;
      public String symbol;
      // $FF: synthetic field
      private static final ElectricityDisplay.ElectricUnit[] $VALUES = new ElectricityDisplay.ElectricUnit[]{AMPERE, AMP_HOUR, VOLTAGE, WATT, WATT_HOUR, RESISTANCE, CONDUCTANCE, JOULES};


      private ElectricUnit(String var1, int var2, String name, String symbol) {
         this.name = name;
         this.symbol = symbol;
      }

      public String getPlural() {
         return this.name + "s";
      }

   }

   public static enum MeasurementUnit {

      MICRO("MICRO", 0, "Micro", "mi", 1.0E-6D),
      MILLI("MILLI", 1, "Milli", "m", 0.001D),
      KILO("KILO", 2, "Kilo", "k", 1000.0D),
      MEGA("MEGA", 3, "Mega", "M", 1000000.0D);
      public String name;
      public String symbol;
      public double value;
      // $FF: synthetic field
      private static final ElectricityDisplay.MeasurementUnit[] $VALUES = new ElectricityDisplay.MeasurementUnit[]{MICRO, MILLI, KILO, MEGA};


      private MeasurementUnit(String var1, int var2, String name, String symbol, double value) {
         this.name = name;
         this.symbol = symbol;
         this.value = value;
      }

      public String getName(boolean isSymbol) {
         return isSymbol?this.symbol:this.name;
      }

      public double process(double value) {
         return value / this.value;
      }

   }
}
