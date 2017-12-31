package controller;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import objects.*;

public class SIGameWindow extends JPanel {
    private int _mapWidth;
    private int _mapHeight;
    private int _viewpointX = 0;
    private int _viewpointY = 0;
    private int _width = 800;
    private int _height = 700;
    private int _gameRegionOnScreenWidth = 800;
    private int _gameRegionOnScreenHeight = _height;
    private int _HUDRegionWidth = _width-_gameRegionOnScreenWidth;
    private int _HUDRegionHeight = _height;

    float _magnification = 1;

    protected Color _hudBackground = Color.BLUE;
    protected Color _windowBackground = Color.BLACK;
    SIGameController _controller;
    public JFrame _parentFrame;

    public SIGameWindow(){
        _controller = SIGameController.getInstance();
        _mapWidth = _controller.getMapWidth();
        _mapHeight = _controller.getMapHeight();

        _parentFrame= new JFrame();
        _parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _parentFrame.setVisible(true);
        _parentFrame.setPreferredSize(new Dimension(_width,_height));
        _parentFrame.setSize(new Dimension(_width,_height));
        _parentFrame.add(this);
        _parentFrame.addKeyListener(_controller);
        setVisible(true);
    }
    public void paintComponent(Graphics g){
        paintHUD(g);
        paintInstances(g);
    }
    public int xOnScreen(int x){
        return (int) ((x-_viewpointX)/_magnification);
    }
    public int yOnScreen(int y){
        return (int) ((y-_viewpointY)/_magnification);
    }
    public int lengthOnScreen(int l){
        return (int) (l/_magnification);
    }
    private void paintInstances(Graphics g){
        paintHUD(g);
        ArrayList<SIObject> objectsInGame = _controller._objects;
        for(int i = 0; i<objectsInGame.size(); i++){
            objectsInGame.get(i).paintThis(g);
        }
    }
    private void paintHUD(Graphics g){
        g.setColor(_windowBackground);
        g.fillRect(0,0,_gameRegionOnScreenWidth,_gameRegionOnScreenHeight);
        g.setColor(_hudBackground);
        g.fillRect(_width-_HUDRegionWidth, _height- _HUDRegionHeight,_HUDRegionWidth, _HUDRegionHeight);
    }
}
