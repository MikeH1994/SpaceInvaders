package objects;

import controller.*;
import utils.*;
import java.awt.Graphics;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public abstract class SIObject extends SIUtils {
    protected enum SHAPE {RECTANGLE,LINE};
    protected enum TYPE {OBJECT,BLOCK,UNIT,PROJECTILE};
    protected SHAPE _objectShape = SHAPE.RECTANGLE;
    protected TYPE _objectType = TYPE.OBJECT;
    protected Color _color = Color.WHITE;
    protected final SIGameController _controller;
    protected final long _serialID;
    protected boolean _checkCollisions = true;
    protected boolean _isDestroyable = true;
    protected boolean _canMove = false;
    protected boolean _canFire = false;
    protected boolean _disableFiring = true;
    private int _x = 0;
    private int _y = 0;
    private int _lastX = 0;
    private int _lastY = 0;
    private int _health = 1;
    private int _currentCellX = 0;
    private int _currentCellY = 0;
    private int _currentCellIndex = 0;
    private float _vx = 0;
    private float _vy = 0;
    private int _movementMaxCounter = 5;
    private int _movementCounter = _movementMaxCounter;
    private int _fireMaxCounter = 10;
    private int _fireCounter = _fireMaxCounter;
    private float _maxVelocity = 4;

    public SIObject(int x, int y){
        _x = x;
        _y = y;
        _lastX = x;
        _lastY = y;
        _controller = SIGameController.getInstance();
        _serialID = _controller.generateID();
        _controller.addObject(this);
    }
    private final void decrementCounters(){
        _fireCounter--;
        _movementCounter--;
        _canFire =(_fireCounter<=0);
        _canMove = (_movementCounter<=0);
    }
    public final void nextStep(){
        move();
        actionsOnNextStep();
        decrementCounters();
        SIObject collisionObject = objectThisCollidesWith();
        if (collisionObject!=null){
            hit(collisionObject);
        }
        if (_health<0 && _isDestroyable){
            destroyObject();
        }
    }
    public final void move(){
        if (!_canMove){
            return;
        }
        _lastX = _x;
        _lastY = _y;
        _x = (int) (_x + _vx);
        _y = (int) (_y + _vy);
        resetMoveCounter();
        if (!positionInBounds()){
            destroyObject();
        }
    }
    public final void destroyObject(){
        _vx = 0;
        _vy = 0;
        _maxVelocity = 0;
        _controller.removeObject(this);
        actionsOnDestruction();
    }
    public final void reduceHealth(int value){
        _health-=value;
    }
    public final void resetMoveCounter(){
        _canMove = false;
        _movementCounter = _movementMaxCounter;
    }
    public final void resetFireCounter(){
        _canFire = false;
        _fireCounter = _fireMaxCounter;
    }



    public final void setColor(Color color){
        _color = color;
    }
    public final void setCellIndex(int cellIndex){
        _currentCellIndex = cellIndex;
        _currentCellX = _controller.xCell(cellIndex);
        _currentCellY = _controller.yCell(cellIndex);
    }
    public final void setVelocity(float vx, float vy){
        if (_maxVelocity == 0){
            _vx = 0;
            _vy = 0;
        }
        float velocity = magnitude(vx,vy);
        float constant = 1;

        if (velocity>_maxVelocity) {
            constant = (float) Math.sqrt(_maxVelocity/velocity);
        }
        _vx= vx*constant;
        _vy= vy*constant;
    }
    public final void setMaxVelocity(float v){
        _maxVelocity = v;
    }
    public final void setMovementMaxCounter(int arg){
        _movementMaxCounter = arg;
    }
    public final void setFireMaxCounter(int arg){
        _fireMaxCounter = arg;
    }
    public final void setHealth(int h){
        _health = h;
    }
    public final void setXDirection(int dirn){
        setVelocity(dirn*_maxVelocity,0);
    }
    public final void setYDirection(int dirn){
        setVelocity(0,dirn*_maxVelocity);
    }

    public final boolean positionInBounds(){
        return _controller.positionInBounds(_x,_y);
    }
    public final int calculateCellIndex(){
        return _controller.getCellIndexFromPosition(_x,_y);
    }
    public final int getCellIndex(){
        return _currentCellIndex;
    }
    public final int xOnScreen(int x){
        return _controller._gameWindow.xOnScreen(x);
    }
    public final int yOnScreen(int y){
        return _controller._gameWindow.yOnScreen(y);
    }
    public final int lengthOnScreen(int l){
        return _controller._gameWindow.lengthOnScreen(l);
    }
    public final int getX(){return _x;}
    public final int getY(){return _y;}
    public final int getLastX(){return _lastX;}
    public final int getLastY(){return _lastY;}
    public final float getVx(){
        return _vx;
    }
    public final float getVy(){
        return _vy;
    }
    public final long getID(){
        return _serialID;
    }
    public final float xFromVector(float r, float theta){
        theta = theta%360;
        float radians = (float) (theta*Math.PI)/180;
        float x = (float) (r*Math.cos(radians));
        //if (theta>90 && theta<270){
        //    x*=-1;
        //}
        return x;
    }
    public final float yFromVector(float r, float theta){
        theta = theta%360;
        float radians = (float) (theta*Math.PI)/180;
        float y = (float) (r*Math.sin(radians));
        return y;
    }
    public final float distanceToObject(SIObject obj){
        return magnitude(_x-obj._x,_y-obj._y);
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
