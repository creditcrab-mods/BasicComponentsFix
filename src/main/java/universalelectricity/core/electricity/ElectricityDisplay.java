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

      AMPERE("Amp", "I"),
      AMP_HOUR("Amp Hour", "Ah"),
      VOLTAGE("Volt", "V"),
      WATT("Watt", "W"),
      WATT_HOUR("Watt Hour", "Wh"),
      RESISTANCE("Ohm", "R"),
      CONDUCTANCE("Siemen", "S"),
      JOULES("Joule", "J");
      public String name;
      public String symbol;

      private ElectricUnit(String name, String symbol) {
         this.name = name;
         this.symbol = symbol;
      }

      public String getPlural() {
         return this.name + "s";
      }

   }

   public static enum MeasurementUnit {

      MICRO("Micro", "mi", 1.0E-6D),
      MILLI("Milli", "m", 0.001D),
      KILO("Kilo", "k", 1000.0D),
      MEGA("Mega", "M", 1000000.0D);
      public String name;
      public String symbol;
      public double value;


      private MeasurementUnit(String name, String symbol, double value) {
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
