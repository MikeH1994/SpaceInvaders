package objects.units;

import objects.SIObject;

import java.awt.*;

public class SIPlayer extends SIUnit {
    public SIPlayer(int x, int y){
        super(x,y);
        _controller.setPlayer(this);
        setMaxVelocity(5);
    }




    @Override
    protected void actionsOnNextStep(){

    }
    @Override
    protected void actionsOnDestruction() {
        //
    }
    @Override
    protected void hit(SIObject other){

    }
    @Override
    public void paintThis(Graphics g){
        g.setColor(_color);
        int x1 = xOnScreen(getX()-_halfWidth);
        int y1 = yOnScreen(getY()-_halfHeight);
        int l = lengthOnScreen(2*_halfWidth);
        g.fillRect(x1,y1,l,l);
    }
}
