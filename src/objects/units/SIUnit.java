package objects.units;

import objects.*;

import java.awt.*;

public abstract class SIUnit extends SIObject {
    protected int _halfWidth = 15;
    protected int _halfHeight = 15;
    protected float _fireDirection = 90;

    public SIUnit(int x, int y){
        super(x,y);
        _objectType = TYPE.UNIT;
        _objectShape = SHAPE.RECTANGLE;
        _disableFiring = false;
    }
    public final void fire(){
        if (!_canFire || _disableFiring){
            return;
        }
        float vx = xFromVector(2*_halfWidth,_fireDirection);
        float vy =  (int) yFromVector(2*_halfWidth,_fireDirection);
        int startX = getX() + (int) vx;
        int startY = getY() + (int) vy;
        new SIProjectile(startX,startY,vx,vy);
        resetFireCounter();
    }
    @Override
    protected Shape getBoundingBox(){
        return new Rectangle(getX()-_halfWidth,getY()-_halfHeight,2*_halfWidth,2*_halfHeight);
    }

}
