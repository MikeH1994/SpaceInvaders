package controller;

import java.util.ArrayList;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.io.Serializable;


import objects.*;
import objects.units.*;

public final class SIGameController implements Serializable, KeyListener{
    public final SIGameWindow _gameWindow;
    private static SIGameController _STATIC_INSTANCE;
    private SIPlayer _player = null;
    private long _currentID = 0;
    private long _lastStepInMillisecs = 0;
    private float _maxFrameRate = 120;
    private float _minimumStepTimeInMilliseconds = 1000.0f/_maxFrameRate;
    private int _xCollisionCells = 3;
    private int _yCollisionCells = 3;
    private int _updateCellsEveryNSteps = 4;
    private int _currentStep = 0;
    private int _mapWidth = 1000;
    private int _mapHeight = 1000;

    public ArrayList<SIObject> _objects = new ArrayList<SIObject>();
    private ArrayList<SIObject> _objectAddQueue = new ArrayList<SIObject>();
    private ArrayList<SIObject> _objectRemoveQueue = new ArrayList<SIObject>();
    private ArrayList<ArrayList<SIObject>> _heirarchicalBoundingBox = new ArrayList<ArrayList<SIObject>>();

    public static void main(String[] args){
        new SIGameController().run();
    }
    public static SIGameController getInstance(){
        return _STATIC_INSTANCE;
    }

    public SIGameController(){
       _STATIC_INSTANCE = this;
       _gameWindow  = new SIGameWindow();
       for(int i = 0; i<_xCollisionCells*_yCollisionCells; i++){
           _heirarchicalBoundingBox.add(new ArrayList<SIObject>());
       }
    }
    public void run(){
        new SIPlayer(500,500);
        while(true){
            _lastStepInMillisecs = System.currentTimeMillis();
            if (_currentStep%_updateCellsEveryNSteps == 0){
                updateHeirarchicalBoundingBox();
            }
            nextStep();
            parseQueue();
            _gameWindow.repaint();
            capStepRate();
            _currentStep++;
        }
    }
    public void setPlayer(SIPlayer player){
        _player = player;
    }
    public long generateID(){
        return _currentID++;
    }
    public boolean cellInBounds(int xCell, int yCell){
        if (xCell>=0 && xCell<_xCollisionCells){
            if (yCell>=0 && yCell<_yCollisionCells){
                return true;
            }
        }
        return false;
    }
    public boolean positionInBounds(int x, int y){
        if (x>= 0 && x<_mapWidth){
            if (y>=0 && y<_mapHeight){
                return true;
            }
        }
        return false;
    }

    public int getMapWidth(){
        return _mapWidth;
    }
    public int getMapHeight(){
        return _mapHeight;
    }
    public int getObjectIndex(SIObject obj){
        long id = obj.getID();
        long currentID;
        for(int i = 0; i<_objects.size(); i++){
            currentID = _objects.get(i).getID();
            if (currentID==id){
                return i;
            }
        }
        return -1;
    }
    public int getCellIndexFromPosition(int x,int y){
        int xCell = (x*_xCollisionCells)/_mapWidth;
        int yCell = (y*_yCollisionCells)/_mapHeight;
        int arrayIndex = getCellIndex(xCell,yCell);
        return arrayIndex;
    }
    public int getCellIndex(int xCell, int yCell){
        return  yCell*_xCollisionCells + xCell;
    }
    public int xCell(int cellIndex){
        return cellIndex%_xCollisionCells;
    }
    public int yCell(int cellIndex){
        return cellIndex/_xCollisionCells;
    }
    public void addObject(SIObject obj){
        _objectAddQueue.add(obj);
    }
    public void removeObject(SIObject obj){
        _objectRemoveQueue.add(obj);
    }
    public void capStepRate(){
        long timeSpentInFrame = System.currentTimeMillis() - _lastStepInMillisecs;
        if (timeSpentInFrame<_minimumStepTimeInMilliseconds){
            try{
                Thread.sleep((long) (_minimumStepTimeInMilliseconds-timeSpentInFrame));
            } catch (InterruptedException e){
                System.out.println("Interrupted Exception caught in capStepRate()");
            }
        }
    }
    public ArrayList<SIObject> getCell(int xCell, int yCell){
        int index = getCellIndex(xCell,yCell);
        return _heirarchicalBoundingBox.get(index);
    }

    private void parseQueue() {
        SIObject obj;
        while(_objectAddQueue.size()>0){
            obj = _objectAddQueue.remove(0);
            _objects.add(obj);
        }

        while(_objectRemoveQueue.size()>0){
            obj = _objectRemoveQueue.remove(0);
            _objects.remove(obj);
            _heirarchicalBoundingBox.get(obj.getCellIndex()).remove(obj);
        }
    }
    private void nextStep(){
        for(int i = 0; i<_objects.size(); i++){
            _objects.get(i).nextStep();
        }
    }
    private void updateHeirarchicalBoundingBox(){
        for(int i = 0; i<_heirarchicalBoundingBox.size();i++){
            _heirarchicalBoundingBox.get(i).clear();
        }
        int cellIndex;
        SIObject currentObject;
        for(int i = 0; i<_objects.size(); i++){
            currentObject = _objects.get(i);
            cellIndex = currentObject.calculateCellIndex();
            _heirarchicalBoundingBox.get(cellIndex).add(currentObject);
            currentObject.setCellIndex(cellIndex);
        }
    }


    @Override
    public void keyPressed(KeyEvent e){
        if (_player!=null){
            if (e.getKeyCode() == KeyEvent.VK_D){
                _player.setXDirection(1);
            }
            if (e.getKeyCode() == KeyEvent.VK_A){
                _player.setXDirection(-1);
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE){
                _player.fire();
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_A){
            if (_player!=null){
                _player.setXDirection(0);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            if (_player!=null){

            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e){

    }
}

