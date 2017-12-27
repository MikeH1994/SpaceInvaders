package controller;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import objects.*;

public class SIGameWindow extends JPanel {
    int _width = 800;
    int _height = 700;
    int _gameRegionOnScreenWidth = 700;
    int _gameRegionOnScreenHeight = _height;
    int _HUDRegionWidth = _width-_gameRegionOnScreenWidth;
    int _HUDRegionHeight = _height;

    protected Color _hudBackground = Color.BLUE;
    protected Color _windowBackground = Color.BLACK;
    SIController _controller;
    public JFrame _parentFrame;

    public SIGameWindow(){
        _controller = SIController.getInstance();
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
