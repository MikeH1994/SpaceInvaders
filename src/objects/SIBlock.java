package objects;

import java.awt.*;

public class SIBlock extends SIObject{
    protected int _halfWidth  = 2;
    protected int _halfHeight = 2;
    public SIBlock(int x, int y){
        super(x,y);
        setMaxVelocity(0);
        setHealth(4);
        _objectType = TYPE.BLOCK;
        _objectShape = SHAPE.RECTANGLE;
    }
    @Override
    protected void actionsOnDestruction() {
        //
    }
    @Override
    public void paintThis(Graphics g){
        g.setColor(_color);
        int x1 = xOnScreen(getX()-_halfWidth);
        int y1 = yOnScreen(getY()-_halfHeight);
        int l = lengthOnScreen(2*_halfWidth);
        g.fillRect(x1,y1,l, l);
    }
    @Override
    protected void actionsOnNextStep(){

    }
    @Override
    protected void hit(SIObject obj){

    }
    @Override
    protected Shape getBoundingBox(){
        return null;
    }
}
