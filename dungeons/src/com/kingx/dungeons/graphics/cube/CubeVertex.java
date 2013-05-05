package com.kingx.dungeons.graphics.cube;

public class CubeVertex {

    public static int size;
    private static boolean positionAttribute;
    private static boolean texCoordsAttribute;
    private static boolean normalAttribute;
    private static boolean colorAttribute;

    public static final int POSITION_SIZE = 3;
    private final float[] position = new float[POSITION_SIZE];
    private final float[] texCoords = new float[2];
    private final float[] normal = new float[3];
    private final float[] color = new float[4];

    public void setPosition(float x, float y, float z) {
        if (!positionAttribute) {
            positionAttribute = true;
            calculate();
        }
        position[0] = x;
        position[1] = y;
        position[2] = z;
    }

    public void setTexCoords(float x, float y) {
        if (!texCoordsAttribute) {
            texCoordsAttribute = true;
            calculate();
        }
        texCoords[0] = x;
        texCoords[1] = y;
    }

    public void setNormal(float x, float y, float z) {
        if (!normalAttribute) {
            normalAttribute = true;
            calculate();
        }
        normal[0] = x;
        normal[1] = y;
        normal[2] = z;
    }

    public void setColor(float r, float g, float b, float a) {
        if (!colorAttribute) {
            colorAttribute = true;
            calculate();
        }
        color[0] = r;
        color[1] = b;
        color[2] = g;
        color[3] = a;
    }

    private void calculate() {
        size = 0;
        size += positionAttribute ? 3 : 0;
        size += texCoordsAttribute ? 2 : 0;
        size += normalAttribute ? 3 : 0;
        size += colorAttribute ? 4 : 0;
    }

    public float[] getPosition() {
        return position;
    }

    public float[] getTexCoords() {
        return texCoords;
    }

    public float[] getNormal() {
        return normal;
    }

    public float[] getColor() {
        return color;
    }

    public static boolean isPositionAttribute() {
        return positionAttribute;
    }

    public static boolean isTexCoordsAttribute() {
        return texCoordsAttribute;
    }

    public static boolean isNormalAttribute() {
        return normalAttribute;
    }

    public static boolean isColorAttribute() {
        return colorAttribute;
    }

}
