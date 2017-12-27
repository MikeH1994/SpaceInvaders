package objects;

import java.awt.*;

public class SIUnit extends SIObject{
    int _halfWidth = 5;
    int _halfHeight = 5;
    public SIUnit(int x, int y){
        super(x,y);

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

    }
    @Override
    protected Shape getBoundingBox(){
        return null;
    }
}
