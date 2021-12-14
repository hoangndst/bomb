package com.bom.game.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bom.game.screen.GameScreen;

public class Wall {
    private Body body;
    private Fixture fixture;
    private Rectangle bounds;

    public Wall(GameScreen gameScreen, Rectangle bounds) {
        this.bounds = bounds;
        BodyDef bdef = new BodyDef();
        FixtureDef fixDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        

    }

}
