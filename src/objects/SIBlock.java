package objects;

import java.awt.*;

public class SIBlock extends SIObject{
    public SIBlock(int x, int y){
        super(x,y);
        _maxVelocity = 0;
        _health = 4;
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
    @Override
    protected void hit(SIObject obj){

    }
    @Override
    protected Shape getBoundingBox(){
        return null;
    }
}
