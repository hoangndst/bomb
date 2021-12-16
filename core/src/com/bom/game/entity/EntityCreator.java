package com.bom.game.entity;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bom.game.modules.Paths;
import com.bom.game.screen.GameScreen;

public class EntityCreator {
    
    private GameScreen gameScreen;
    public EntityManager entityManager;
    private static BodyDef bDef;
    private static PolygonShape pShape;
    private static FixtureDef fDef;
    private Body body;

    public EntityCreator(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }
    
    public void createEntity() {

        bDef = new BodyDef();
        pShape = new PolygonShape();
        fDef = new FixtureDef();
        
        entityManager = new EntityManager();

        createWallObjects();
        createBrickObjects();
        createBalloomObjects();
        createItemSpeedUP();
        createItemFlameUp();
        createItemBombUP();
        createKey();
        createPortal();
        // createStaticObjectsFromLayer(StringPaths.tiledMagnetsLayer, CollisionBits.MAGNET,
        //         CollisionBits.orOperation(
        //                 CollisionBits.PLAYER,
        //                 CollisionBits.BOMB
        //         )
        // );
    }
    
    // private void createStaticObjectsFromLayer(String layer, short categoryBits, short maskBits) {
        
    //     for (MapObject object : gameScreen.getMap().getLayers().get(layer)
    //             .getObjects().getByType(RectangleMapObject.class)) {
            
    //         Rectangle rect = ((RectangleMapObject) object).getRectangle();

    //         bDef.type = BodyDef.BodyType.StaticBody;
    //         bDef.position.set(
    //                 UnitHelper.coordPixelsToMeters(
    //                     UnitHelper.screenToBox2D(rect.getX(), rect.getWidth()),
    //                     UnitHelper.screenToBox2D(rect.getY(), rect.getHeight())
    //                 )
    //         );
    //         body = GameScreen.getWorld().createBody(bDef);

    //         pShape.setAsBox(
    //                 UnitHelper.pixelsToMeters(rect.getWidth() / 2),
    //                 UnitHelper.pixelsToMeters(rect.getHeight() / 2)
    //         );
    //         fDef.shape = pShape;
    //         fDef.filter.categoryBits = categoryBits;
    //         fDef.filter.maskBits = maskBits;
    //         body.createFixture(fDef);
    //     }
    // }
    
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
            System.err.println(ellipse.x + " " + ellipse.y + " " + ellipse.width + " " + ellipse.height);
            entityManager.addEnemy(new Balloom(gameScreen, ellipse));
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