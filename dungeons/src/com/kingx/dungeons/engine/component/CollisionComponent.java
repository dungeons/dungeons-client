package com.kingx.dungeons.engine.component;

import com.kingx.artemis.Component;
import com.kingx.dungeons.geom.Point.Int;

public class CollisionComponent extends Component {

    private Int up;
    private Int down;
    private Int left;
    private Int right;

    public Int getUp() {
        return up;
    }

    public void setUp(Int up) {
        this.up = up;
    }

    public Int getDown() {
        return down;
    }

    public void setDown(Int down) {
        this.down = down;
    }

    public Int getLeft() {
        return left;
    }

    public void setLeft(Int left) {
        this.left = left;
    }

    public Int getRight() {
        return right;
    }

    public void setRight(Int right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "CollisionComponent [up=" + up + ", down=" + down + ", left=" + left + ", right=" + right + "]";
    }

}