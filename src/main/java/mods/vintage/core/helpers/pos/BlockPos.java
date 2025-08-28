package mods.vintage.core.helpers.pos;

import com.google.common.collect.AbstractIterator;
import net.jcip.annotations.Immutable;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@Immutable
public class BlockPos extends Vec3i {

    public BlockPos(int x, int y, int z) {
        super(x, y, z);
    }

    public BlockPos(double x, double y, double z) {
        super(x, y, z);
    }

    public BlockPos(Entity source) {
        this(source.posX, source.posY, source.posZ);
    }

    public BlockPos(Vec3i source) {
        this(source.getX(), source.getY(), source.getZ());
    }

    public BlockPos add(double x, double y, double z) {
        return x == 0.0D && y == 0.0D && z == 0.0D ? this : new BlockPos((double) this.getX() + x, (double) this.getY() + y, (double) this.getZ() + z);
    }

    public BlockPos add(int x, int y, int z) {
        return x == 0 && y == 0 && z == 0 ? this : new BlockPos(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    public static Iterable<BlockPos> getAllInBox(BlockPos from, BlockPos to) {
        return getAllInBox(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()), Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
    }

    public static Iterable<BlockPos> getAllInBox(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        return new Iterable<BlockPos>() {
            @NotNull
            @Override
            public Iterator<BlockPos> iterator() {
                return new AbstractIterator<BlockPos>() {
                    private boolean first = true;
                    private int lastPosX;
                    private int lastPosY;
                    private int lastPosZ;

                    protected BlockPos computeNext() {
                        if (this.first) {
                            this.first = false;
                            this.lastPosX = x1;
                            this.lastPosY = y1;
                            this.lastPosZ = z1;
                            return new BlockPos(x1, y1, z1);
                        } else if (this.lastPosX == x2 && this.lastPosY == y2 && this.lastPosZ == z2) {
                            return this.endOfData();
                        } else {
                            if (this.lastPosX < x2) {
                                ++this.lastPosX;
                            } else if (this.lastPosY < y2) {
                                this.lastPosX = x1;
                                ++this.lastPosY;
                            } else if (this.lastPosZ < z2) {
                                this.lastPosX = x1;
                                this.lastPosY = y1;
                                ++this.lastPosZ;
                            }

                            return new BlockPos(this.lastPosX, this.lastPosY, this.lastPosZ);
                        }
                    }
                };
            }
        };
    }

    public BlockPos toImmutable() {
        return this;
    }

    public static Iterable<MutableBlockPos> getAllInBoxMutable(BlockPos from, BlockPos to) {
        return getAllInBoxMutable(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()), Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
    }

    public static Iterable<BlockPos.MutableBlockPos> getAllInBoxMutable(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        return new Iterable<BlockPos.MutableBlockPos>() {
            @Override
            @NotNull
            public Iterator<BlockPos.MutableBlockPos> iterator() {
                return new AbstractIterator<BlockPos.MutableBlockPos>() {
                    private BlockPos.MutableBlockPos pos;

                    protected BlockPos.MutableBlockPos computeNext() {
                        if (this.pos == null) {
                            this.pos = new BlockPos.MutableBlockPos(x1, y1, z1);
                            return this.pos;
                        } else if (this.pos.x == x2 && this.pos.y == y2 && this.pos.z == z2) {
                            return this.endOfData();
                        } else {
                            if (this.pos.x < x2) {
                                ++this.pos.x;
                            } else if (this.pos.y < y2) {
                                this.pos.x = x1;
                                ++this.pos.y;
                            } else if (this.pos.z < z2) {
                                this.pos.x = x1;
                                this.pos.y = y1;
                                ++this.pos.z;
                            }

                            return this.pos;
                        }
                    }
                };
            }
        };
    }

    public static class MutableBlockPos extends BlockPos {
        protected int x;
        protected int y;
        protected int z;

        public MutableBlockPos() {
            super(0, 0, 0);
        }

        public MutableBlockPos(BlockPos pos) {
            this(pos.getX(), pos.getY(), pos.getZ());
        }

        public MutableBlockPos(int x, int y, int z) {
            super(0, 0, 0);
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public BlockPos add(double x, double y, double z) {
            return super.add(x, y, z).toImmutable();
        }

        public BlockPos add(int x, int y, int z) {
            return super.add(x, y, z).toImmutable();
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public int getZ() {
            return this.z;
        }

        public void setX(int x) {
            super.setX(x);
        }

        public void setY(int y) {
            super.setY(y);
        }

        public void setZ(int z) {
            super.setZ(z);
        }

        public MutableBlockPos set(int x, int y, int z) {
            this.setX(x);
            this.setY(y);
            this.setZ(z);
            return this;
        }

        public MutableBlockPos set(double x, double y, double z) {
            return this.set(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
        }

        public MutableBlockPos set(Vec3i vec3i) {
            return this.set(vec3i.getX(), vec3i.getY(), vec3i.getZ());
        }

        public BlockPos toImmutable() {
            return new BlockPos(this);
        }
    }
}
