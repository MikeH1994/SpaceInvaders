package objects;

import java.awt.*;
import java.awt.geom.Line2D;

public class SIProjectile extends SIObject{
    int _damage = 1;
    int _length = 2;
    public SIProjectile(int x, int y, float vx, float vy){
        super(x,y);
        _vx = vx;
        _vy = vy;
        _objectShape = SHAPE.LINE;
    }
    @Override
    protected void actionsOnDestruction() {
        //
    }
    @Override
    public void paintThis(Graphics g){

    }
    @Override
    protected void actionsOnNextStep(){

    }
    protected void hit(SIObject other){
        other.reduceHealth(_damage);
        _health = -1;
    }
    @Override
    protected Shape getBoundingBox(){
        return new Line2D.Float(_x,_y,_lastX,_lastY);
    }
}
