package universalelectricity.core.vector;

import net.minecraft.util.MathHelper;

public class Vector2 implements Cloneable {

   public double x;
   public double y;


   public Vector2() {
      this(0.0D, 0.0D);
   }

   public Vector2(double x, double y) {
      this.x = x;
      this.y = y;
   }

   public int intX() {
      return (int)Math.floor(this.x);
   }

   public int intY() {
      return (int)Math.floor(this.y);
   }

   public Vector2 clone() {
      return new Vector2(this.x, this.y);
   }

   public static double distance(Vector2 point1, Vector2 point2) {
      double xDifference = point1.x - point2.x;
      double yDiference = point1.y - point2.y;
      return (double)MathHelper.sqrt_double(xDifference * xDifference + yDiference * yDiference);
   }

   public static double slope(Vector2 point1, Vector2 point2) {
      double xDifference = point1.x - point2.x;
      double yDiference = point1.y - point2.y;
      return yDiference / xDifference;
   }

   public double distanceTo(Vector2 target) {
      double xDifference = this.x - target.x;
      double yDifference = this.y - target.y;
      return (double)MathHelper.sqrt_double(xDifference * xDifference + yDifference * yDifference);
   }

   public Vector2 add(Vector2 par1) {
      this.x += par1.x;
      this.y += par1.y;
      return this;
   }

   public Vector2 add(double par1) {
      this.x += par1;
      this.y += par1;
      return this;
   }

   public Vector2 invert() {
      this.multiply(-1.0D);
      return this;
   }

   public Vector2 multiply(double amount) {
      this.x *= amount;
      this.y *= amount;
      return this;
   }

   public Vector2 round() {
      return new Vector2((double)Math.round(this.x), (double)Math.round(this.y));
   }

   public Vector2 ceil() {
      return new Vector2(Math.ceil(this.x), Math.ceil(this.y));
   }

   public Vector2 floor() {
      return new Vector2(Math.floor(this.x), Math.floor(this.y));
   }

   public int hashCode() {
      return ("X:" + this.x + "Y:" + this.y).hashCode();
   }

   public boolean equals(Object o) {
      if(!(o instanceof Vector2)) {
         return false;
      } else {
         Vector2 vector = (Vector2)o;
         return this.x == vector.x && this.y == vector.y;
      }
   }

   public String toString() {
      return "Vector2 [" + this.x + "," + this.y + "]";
   }
}
