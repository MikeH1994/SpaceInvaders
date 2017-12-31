package objects;

import java.awt.*;
import java.awt.geom.Line2D;

public class SIProjectile extends SIObject{
    int _damage = 1;
    int _length = 2;
    public SIProjectile(int x, int y, float vx, float vy){
        super(x,y);
        _objectShape = SHAPE.LINE;
        _objectType = TYPE.PROJECTILE;
        setVelocity(vx,vy);
    }
    @Override
    protected void actionsOnDestruction() {
        //
    }
    @Override
    public void paintThis(Graphics g){
        g.setColor(_color);
        int x1 = xOnScreen(getX());
        int y1 = yOnScreen(getY());
        int x2 = xOnScreen(getLastX());
        int y2 = yOnScreen(getLastY());
        g.drawLine(x1,y1,x2,y2);
    }
    @Override
    protected void actionsOnNextStep(){

    }
    @Override
    protected void hit(SIObject other){
        other.reduceHealth(_damage);
        setHealth(0);
    }
    @Override
    protected Shape getBoundingBox(){
        return new Line2D.Float(getX(),getY(),getX(),getY());
    }
}
