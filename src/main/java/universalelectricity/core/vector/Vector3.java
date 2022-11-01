package universalelectricity.core.vector;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class Vector3 implements Cloneable {

   public double x;
   public double y;
   public double z;


   public Vector3() {
      this(0.0D, 0.0D, 0.0D);
   }

   public Vector3(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public Vector3(Entity par1) {
      this.x = par1.posX;
      this.y = par1.posY;
      this.z = par1.posZ;
   }

   public Vector3(TileEntity par1) {
      this.x = (double)par1.xCoord;
      this.y = (double)par1.yCoord;
      this.z = (double)par1.zCoord;
   }

   public Vector3(Vec3 par1) {
      this.x = par1.xCoord;
      this.y = par1.yCoord;
      this.z = par1.zCoord;
   }

   public Vector3(MovingObjectPosition par1) {
      this.x = (double)par1.blockX;
      this.y = (double)par1.blockY;
      this.z = (double)par1.blockZ;
   }

   public Vector3(ChunkCoordinates par1) {
      this.x = (double)par1.posX;
      this.y = (double)par1.posY;
      this.z = (double)par1.posZ;
   }

   public Vector3(ForgeDirection direction) {
      this.x = (double)direction.offsetX;
      this.y = (double)direction.offsetY;
      this.z = (double)direction.offsetZ;
   }

   public int intX() {
      return (int)Math.floor(this.x);
   }

   public int intY() {
      return (int)Math.floor(this.y);
   }

   public int intZ() {
      return (int)Math.floor(this.z);
   }

   public Vector3 clone() {
      return new Vector3(this.x, this.y, this.z);
   }

   public Block getBlock(IBlockAccess world) {
      return world.getBlock(this.intX(), this.intY(), this.intZ());
   }

   public int getBlockMetadata(IBlockAccess world) {
      return world.getBlockMetadata(this.intX(), this.intY(), this.intZ());
   }

   public TileEntity getTileEntity(IBlockAccess world) {
      return world.getTileEntity(this.intX(), this.intY(), this.intZ());
   }

   public boolean setBlock(World world, Block block, int metadata, int notify) {
      return world.setBlock(this.intX(), this.intY(), this.intZ(), block, metadata, notify);
   }

   public boolean setBlock(World world, Block block, int metadata) {
      return this.setBlock(world, block, metadata, 3);
   }

   public boolean setBlock(World world, Block block) {
      return this.setBlock(world, block, 0);
   }

   public Vector2 toVector2() {
      return new Vector2(this.x, this.z);
   }

   public Vec3 toVec3() {
      return Vec3.createVectorHelper(this.x, this.y, this.z);
   }

   public double getMagnitude() {
      return Math.sqrt(this.getMagnitudeSquared());
   }

   public double getMagnitudeSquared() {
      return this.x * this.x + this.y * this.y + this.z * this.z;
   }

   public Vector3 normalize() {
      double d = this.getMagnitude();
      if(d != 0.0D) {
         this.multiply(1.0D / d);
      }

      return this;
   }

   public static double distance(Vector3 par1, Vector3 par2) {
      double var2 = par1.x - par2.x;
      double var4 = par1.y - par2.y;
      double var6 = par1.z - par2.z;
      return (double)MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
   }

   public double distanceTo(Vector3 vector3) {
      double var2 = vector3.x - this.x;
      double var4 = vector3.y - this.y;
      double var6 = vector3.z - this.z;
      return (double)MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
   }

   public Vector3 add(Vector3 par1) {
      this.x += par1.x;
      this.y += par1.y;
      this.z += par1.z;
      return this;
   }

   public Vector3 add(double par1) {
      this.x += par1;
      this.y += par1;
      this.z += par1;
      return this;
   }

   public Vector3 subtract(Vector3 amount) {
      this.x -= amount.x;
      this.y -= amount.y;
      this.z -= amount.z;
      return this;
   }

   public Vector3 invert() {
      this.multiply(-1.0D);
      return this;
   }

   public Vector3 multiply(double amount) {
      this.x *= amount;
      this.y *= amount;
      this.z *= amount;
      return this;
   }

   public Vector3 multiply(Vector3 vec) {
      this.x *= vec.x;
      this.y *= vec.y;
      this.z *= vec.z;
      return this;
   }

   public static Vector3 subtract(Vector3 par1, Vector3 par2) {
      return new Vector3(par1.x - par2.x, par1.y - par2.y, par1.z - par2.z);
   }

   public static Vector3 add(Vector3 par1, Vector3 par2) {
      return new Vector3(par1.x + par2.x, par1.y + par2.y, par1.z + par2.z);
   }

   public static Vector3 add(Vector3 par1, double par2) {
      return new Vector3(par1.x + par2, par1.y + par2, par1.z + par2);
   }

   public static Vector3 multiply(Vector3 vec1, Vector3 vec2) {
      return new Vector3(vec1.x * vec2.x, vec1.y * vec2.y, vec1.z * vec2.z);
   }

   public static Vector3 multiply(Vector3 vec1, double vec2) {
      return new Vector3(vec1.x * vec2, vec1.y * vec2, vec1.z * vec2);
   }

   public Vector3 round() {
      return new Vector3((double)Math.round(this.x), (double)Math.round(this.y), (double)Math.round(this.z));
   }

   public Vector3 ceil() {
      return new Vector3(Math.ceil(this.x), Math.ceil(this.y), Math.ceil(this.z));
   }

   public Vector3 floor() {
      return new Vector3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
   }

   public List<Entity> getEntitiesWithin(World worldObj, Class<? extends Entity> par1Class) {
      return worldObj.getEntitiesWithinAABB(par1Class, AxisAlignedBB.getBoundingBox((double)this.intX(), (double)this.intY(), (double)this.intZ(), (double)(this.intX() + 1), (double)(this.intY() + 1), (double)(this.intZ() + 1)));
   }

   public Vector3 modifyPositionFromSide(ForgeDirection side, double amount) {
      switch(side.ordinal()) {
      case 0:
         this.y -= amount;
         break;
      case 1:
         this.y += amount;
         break;
      case 2:
         this.z -= amount;
         break;
      case 3:
         this.z += amount;
         break;
      case 4:
         this.x -= amount;
         break;
      case 5:
         this.x += amount;
      }

      return this;
   }

   public Vector3 modifyPositionFromSide(ForgeDirection side) {
      this.modifyPositionFromSide(side, 1.0D);
      return this;
   }

   public static Vector3 readFromNBT(NBTTagCompound nbtCompound) {
      Vector3 tempVector = new Vector3();
      tempVector.x = nbtCompound.getDouble("x");
      tempVector.y = nbtCompound.getDouble("y");
      tempVector.z = nbtCompound.getDouble("z");
      return tempVector;
   }

   public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound) {
      par1NBTTagCompound.setDouble("x", this.x);
      par1NBTTagCompound.setDouble("y", this.y);
      par1NBTTagCompound.setDouble("z", this.z);
      return par1NBTTagCompound;
   }

   public int hashCode() {
      return ("X:" + this.x + "Y:" + this.y + "Z:" + this.z).hashCode();
   }

   public boolean equals(Object o) {
      if(!(o instanceof Vector3)) {
         return false;
      } else {
         Vector3 vector3 = (Vector3)o;
         return this.x == vector3.x && this.y == vector3.y && this.z == vector3.z;
      }
   }

   public String toString() {
      return "Vector3 [" + this.x + "," + this.y + "," + this.z + "]";
   }
}
