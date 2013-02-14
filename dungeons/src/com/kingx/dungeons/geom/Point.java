package com.kingx.dungeons.geom;

public abstract class Point {

    public static final class Int {
        public int x;
        public int y;

        public Int(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Int(Int p) {
            set(p);
        }

        public Int() {
            this(0, 0);
        }

        public void set(Int p) {
            this.x = p.x;
            this.y = p.y;
        }
        
        public boolean equals(int i, int j) {
            // TODO Auto-generated method stub
            return x == i && y == j;
        }

        @Override
        public String toString() {
            return "Int [x=" + x + ", y=" + y + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + x;
            result = prime * result + y;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Int other = (Int) obj;
            if (x != other.x)
                return false;
            if (y != other.y)
                return false;
            return true;
        }
        
        


    }

    public static final class Float {
        public float x;
        public float y;

        public Float(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Float(Float p) {
            set(p);
        }

        public Float() {
            this(0, 0);
        }

        public void set(Float p) {
            this.x = p.x;
            this.y = p.y;
        }
        
        public boolean equals(int i, int j) {
            // TODO Auto-generated method stub
            return x == i && y == j;
        }

        @Override
        public String toString() {
            return "Float [x=" + x + ", y=" + y + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + java.lang.Float.floatToIntBits(x);
            result = prime * result + java.lang.Float.floatToIntBits(y);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Float other = (Float) obj;
            if (java.lang.Float.floatToIntBits(x) != java.lang.Float.floatToIntBits(other.x))
                return false;
            if (java.lang.Float.floatToIntBits(y) != java.lang.Float.floatToIntBits(other.y))
                return false;
            return true;
        }

        
        

    }

   
}
