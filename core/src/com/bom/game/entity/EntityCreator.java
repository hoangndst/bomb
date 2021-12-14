package com.bom.game.entity;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
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
}