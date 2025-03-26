package mods.vintage.core.helpers.pos;

import com.google.common.base.Objects;
import mods.vintage.core.annotation.Immutable;
import net.minecraft.util.MathHelper;

@Immutable
public class Vec3i implements Comparable<Vec3i> {

    /**
     * An immutable vector with zero as all coordinates.
     */
    public static final Vec3i NULL_VECTOR = new Vec3i(0, 0, 0);
    /**
     * X coordinate
     */
    private int x;
    /**
     * Y coordinate
     */
    private int y;
    /**
     * Z coordinate
     */
    private int z;

    public Vec3i(int xIn, int yIn, int zIn) {
        this.x = xIn;
        this.y = yIn;
        this.z = zIn;
    }

    public Vec3i(double xIn, double yIn, double zIn) {
        this(MathHelper.floor_double(xIn), MathHelper.floor_double(yIn), MathHelper.floor_double(zIn));
    }

    /**
     * Gets the X coordinate.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Gets the Y coordinate.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Gets the Z coordinate.
     */
    public int getZ() {
        return this.z;
    }

    /**
     * Calculate the cross product of this and the given Vector
     */
    public Vec3i crossProduct(Vec3i vec) {
        return new Vec3i(this.getY() * vec.getZ() - this.getZ() * vec.getY(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getY() - this.getY() * vec.getX());
    }

    public double getDistance(int xIn, int yIn, int zIn) {
        double d0 = (double) (this.getX() - xIn);
        double d1 = (double) (this.getY() - yIn);
        double d2 = (double) (this.getZ() - zIn);
        return Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    /**
     * Calculate squared distance to the given coordinates
     */
    public double distanceSq(double toX, double toY, double toZ) {
        double d0 = (double) this.getX() - toX;
        double d1 = (double) this.getY() - toY;
        double d2 = (double) this.getZ() - toZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    /**
     * Compute square of distance from point x, y, z to center of this Block
     */
    public double distanceSqToCenter(double xIn, double yIn, double zIn) {
        double d0 = (double) this.getX() + 0.5D - xIn;
        double d1 = (double) this.getY() + 0.5D - yIn;
        double d2 = (double) this.getZ() + 0.5D - zIn;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    /**
     * Calculate squared distance to the given Vector
     */
    public double distanceSq(Vec3i to) {
        return this.distanceSq((double) to.getX(), (double) to.getY(), (double) to.getZ());
    }

    protected void setX(int x) {
        this.x = x;
    }

    protected void setY(int y) {
        this.y = y;
    }

    protected void setZ(int z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Vec3i)) return false;
        Vec3i vector = (Vec3i) other;
        return this.getX() == vector.getX() &&
                this.getY() == vector.getY() &&
                this.getZ() == vector.getZ();
    }

    @Override
    public int hashCode() {
        return (this.getY() + this.getZ() * 31) * 31 + this.getX();
    }

    @Override
    public int compareTo(Vec3i vec3i) {
        if (this.getY() == vec3i.getY()) {
            return this.getZ() == vec3i.getZ() ? this.getX() - vec3i.getX() : this.getZ() - vec3i.getZ();
        } else {
            return this.getY() - vec3i.getY();
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).toString();
    }
}