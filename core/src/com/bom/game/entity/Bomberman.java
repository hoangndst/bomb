package com.bom.game.entity;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.bom.game.screen.GameScreen;

public class Bomberman extends EntityBase implements Disposable {

    private World world;
    private ArrayList<Bomb> bombs;

    private static BodyDef bDef = new BodyDef();
    private static FixtureDef fDef = new FixtureDef();
    private static CircleShape cShape = new CircleShape();

    public Bomberman(GameScreen gameScreen, Vector2 position) {
        super(gameScreen.entityCreator.entityManager);
        this.world = gameScreen.getWorld();
        this.bombs = new ArrayList<Bomb>();
        this.type = EntityType.BOMBERMAN;
    }



    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}
