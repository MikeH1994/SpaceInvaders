package controller;

import java.util.ArrayList;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.io.Serializable;


import objects.*;

public final class SIController implements Serializable, KeyListener{
    private static SIController _STATIC_INSTANCE;
    private final SIGameWindow _gameWindow;
    private long _currentID = 0;
    private long _lastStepInMillisecs = 0;
    private float _maxFrameRate = 20;
    private float _minimumStepTimeInMilliseconds = 1000.0f/_maxFrameRate;
    private int _xCollisionCells = 3;
    private int _yCollisionCells = 3;
    private int _updateCellsEveryNSteps = 4;
    private int _playRegionWidth = 1000;
    private int _playRegionHeight = 1000;
    private int _currentStep = 0;

    public ArrayList<SIObject> _objects = new ArrayList<SIObject>();
    private ArrayList<SIObject> _objectAddQueue = new ArrayList<SIObject>();
    private ArrayList<SIObject> _objectRemoveQueue = new ArrayList<SIObject>();
    private ArrayList<ArrayList<SIObject>> _heirarchicalBoundingBox = new ArrayList<ArrayList<SIObject>>();

    public static void main(String[] args){
        new SIController().run();
    }
    public static SIController getInstance(){
        return _STATIC_INSTANCE;
    }

    public SIController(){
       _STATIC_INSTANCE = this;
       _gameWindow  = new SIGameWindow();
       for(int i = 0; i<_xCollisionCells*_yCollisionCells; i++){
           _heirarchicalBoundingBox.add(new ArrayList<SIObject>());
       }
    }
    public void run(){
        while(true){
            _lastStepInMillisecs = System.currentTimeMillis();
            nextStep();
            parseQueue();
            _gameWindow.repaint();
            if (_currentStep%_updateCellsEveryNSteps == 0){
                updateHeirarchicalBoundingBox();
            }
            capStepRate();
            _currentStep++;
        }
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
        int xCell = (x*_xCollisionCells)/_playRegionWidth;
        int yCell = (y*_yCollisionCells)/_playRegionHeight;
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
        _objectRemoveQueue.remove(getObjectIndex(obj));
    }
    public void capStepRate(){
        long timeSpentInFrame = System.currentTimeMillis() - _lastStepInMillisecs;
        if (timeSpentInFrame<_minimumStepTimeInMilliseconds){
            try{
                wait((long) (_minimumStepTimeInMilliseconds-timeSpentInFrame));
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
        while(_objectAddQueue.size()>0){
            _objects.add(_objectAddQueue.remove(0));
        }
        while(_objectRemoveQueue.size()>0){
            _objects.add(_objectRemoveQueue.remove(0));
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

    }
    @Override
    public void keyReleased(KeyEvent e){

    }
    @Override
    public void keyTyped(KeyEvent e){

    }
}

