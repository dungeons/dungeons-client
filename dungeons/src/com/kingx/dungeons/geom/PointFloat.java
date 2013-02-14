package com.kingx.dungeons.geom;

public class PointFloat {

    public float x;
    public float y;

    public PointFloat(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public PointFloat(PointFloat p) {
        set(p);
    }

    public PointFloat() {
        this(0, 0);
    }

    public void set(PointFloat p) {
        this.x = p.x;
        this.y = p.y;
    }

   

    @Override
    public String toString() {
        return "Point [x=" + x + ", y=" + y + "]";
    }

    public boolean equals(int i, int j) {
        if (x != i)
            return false;
        if (y != j)
            return false;
        return true;
    }

}
