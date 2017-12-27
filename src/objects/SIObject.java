package objects;

import controller.*;
import utils.*;
import java.awt.Graphics;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public abstract class SIObject extends SIUtils {
    enum SHAPE {RECTANGLE,LINE};
    SHAPE _objectShape = SHAPE.RECTANGLE;
    protected Color _color = Color.WHITE;
    protected final SIController _controller;
    protected final long _serialID;
    protected boolean _checkCollisions = true;
    protected boolean _isDestroyable = true;
    protected int _lastX = 0;
    protected int _lastY = 0;
    protected int _x = 0;
    protected int _y = 0;
    protected float _vx = 0;
    protected float _vy = 0;
    protected float _maxVelocity = 4;
    protected int _health = 1;
    private int _currentCellX = 0;
    private int _currentCellY = 0;
    private int _currentCellIndex = 0;

    public SIObject(int x, int y){
        _controller = SIController.getInstance();
        _serialID = _controller.generateID();
        _controller.addObject(this);
    }
    public final float distanceToObject(SIObject obj){
        return magnitude(_x-obj._x,_y-obj._y);
    }
    public final void nextStep(){
        _lastX = _x;
        _lastY = _y;
        _x = (int) (_x + _vx);
        _y = (int) (_y + _vy);
        actionsOnNextStep();
        SIObject collisionObject = objectThisCollidesWith();
        if (collisionObject!=null){
            hit(collisionObject);
        }
        if (_health<0 && _isDestroyable){
            destroyObject();
        }
    }
    public final void setVelocity(float vx, float vy){
        if (_maxVelocity == 0){
            _vx = 0;
            _vy = 0;
        }
        float velocity = magnitude(vx,vy);
        if (velocity>_maxVelocity){
            float constant = (float) Math.sqrt(_maxVelocity/velocity);
            _vx*=constant;
            _vy*=constant;
        }
    }
    public final void reduceHealth(int value){
        _health-=value;
    }
    public final void setColor(Color color){
        _color = color;
    }
    public final void setCellIndex(int cellIndex){
        _currentCellIndex = cellIndex;
        _currentCellX = _controller.xCell(cellIndex);
        _currentCellY = _controller.yCell(cellIndex);
    }
    public final void destroyObject(){
        _vx = 0;
        _vy = 0;
        _maxVelocity = 0;
        _controller.removeObject(this);
        actionsOnDestruction();
    }
    public final long getID(){
        return _serialID;
    }
    public final int calculateCellIndex(){
        return _controller.getCellIndexFromPosition(_x,_y);
    }
    public final int getCellIndex(){
        return _currentCellIndex;
    }
    public final SIObject objectThisCollidesWith(){
        if (!_checkCollisions){
            return null;
        }
        int xCell, yCell;
        SIObject collidingObject;
        ArrayList<SIObject> cell;
        for(int i = -1; i<=1; i++){
            xCell = _currentCellX + i;
            for(int j = -1; j<=1; j++){
                yCell = _currentCellY + j;
                if (_controller.cellInBounds(xCell,yCell)){
                    cell = _controller.getCell(xCell,yCell);
                    collidingObject = objectThisCollidesWith(cell);
                    if (collidingObject!=null){
                        return collidingObject;
                    }
                }
            }
        }
        return null;
    }
    public final SIObject objectThisCollidesWith(ArrayList<SIObject> list){
        SIObject obj;
        for(int i = 0; i<list.size(); i++){
            obj = list.get(i);
            if (objectsCollide(obj)){
                return obj;
            }
        }
        return null;

    }
    public final Class getShapeClass(){
        if (_objectShape == SHAPE.RECTANGLE){
            return Rectangle.class;
        } else {
            return Line2D.class;
        }
    }
    protected boolean objectsCollide(SIObject other){
        Shape shape1 = getBoundingBox();
        Shape shape2 = other.getBoundingBox();

        if (_objectShape == SHAPE.RECTANGLE){
            if (other._objectShape==SHAPE.RECTANGLE){
                return ((Rectangle) shape1).intersects((Rectangle) shape2);
            } else if (other._objectShape == SHAPE.LINE){
                return ((Rectangle) shape1).intersectsLine((Line2D) shape2);
            }
        } else{
            if (other._objectShape==SHAPE.RECTANGLE){
                return ((Line2D) shape1).intersects((Rectangle) shape2);
            } else if (other._objectShape == SHAPE.LINE){
                return ((Line2D) shape1).intersectsLine((Line2D) shape2);
            }
        }
        System.out.println("ObjectsCollide error");
        System.exit(1);
        return false;
    }


    public abstract void paintThis(Graphics g);
    protected abstract Shape getBoundingBox();
    protected abstract void actionsOnDestruction();
    protected abstract void actionsOnNextStep();
    protected abstract void hit(SIObject other);
}
