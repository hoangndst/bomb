package com.bom.game.entity;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.bom.game.modules.Paths;
import com.bom.game.screen.GameScreen;

public class EntityCreator {
    
    private GameScreen gameScreen;
    public EntityManager entityManager;

    public EntityCreator(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }
    
    public void createEntity() {
        
        entityManager = new EntityManager(gameScreen);

        createWallObjects();
        createBrickObjects();
        createBalloomObjects();
        createItemSpeedUP();
        createItemFlameUp();
        createItemBombUP();
        createKey();
        createPortal();
        createBulbObjects();
        
        entityManager.createMapArray();
        entityManager.setInitMap();
    }
    
    
    private void createWallObjects() {
        for (MapObject object : gameScreen.getMap().getLayers().get(Paths.tiledWallsLayer)
                .getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            entityManager.addWall(new Wall(gameScreen, rect));
        }
    }
    
    private void createBrickObjects() {
        for (MapObject object : gameScreen.getMap().getLayers().get(Paths.tiledChestsLayer)
                .getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            entityManager.addBrick(new Brick(gameScreen, rect));
        }
    }

    private void createItemSpeedUP() {
        for (MapObject object : gameScreen.getMap().getLayers().get(Paths.tiledSpeedUPLayer)
                .getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            entityManager.addItem(new ItemSpeedUp(gameScreen, rect));
        }
    }

    private void createItemFlameUp() {
        for (MapObject object : gameScreen.getMap().getLayers().get(Paths.tiledFlameUpLayer)
                .getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            entityManager.addItem(new ItemFlameUp(gameScreen, rect));
        }
    }

    private void createItemBombUP() {
        for (MapObject object : gameScreen.getMap().getLayers().get(Paths.tiledBombUPLayer)
                .getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            entityManager.addItem(new ItemBombUp(gameScreen, rect));
        }
    }

    private void createBalloomObjects() {
        for (MapObject object : gameScreen.getMap().getLayers().get(Paths.tiledBalloomLayer)
                .getObjects().getByType(EllipseMapObject.class)) {
            Ellipse ellipse = ((EllipseMapObject) object).getEllipse();
            entityManager.addEnemy(new Balloom(gameScreen, ellipse));
        }
    }

    private void createBulbObjects() {
        for (MapObject object : gameScreen.getMap().getLayers().get(Paths.tiledBulbLayer)
                .getObjects().getByType(EllipseMapObject.class)) {
            Ellipse ellipse = ((EllipseMapObject) object).getEllipse();
            entityManager.addEnemy(new Bulb(gameScreen, ellipse));
        }
    }

    private void createKey() {
        for (MapObject object : gameScreen.getMap().getLayers().get(Paths.tiledKey)
                .getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            entityManager.addItem(new Key(gameScreen, rect));
        }
    }

    private void createPortal() {
        for (MapObject object : gameScreen.getMap().getLayers().get(Paths.tiledPortal)
                .getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            entityManager.addItem(new Portal(gameScreen, rect));
        }
    }
}