package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.HakuGoesHome;
import com.sun.org.apache.xpath.internal.operations.Or;

/**
 * Created by p.szwed on 9/21/17.
 */

public class Haku extends Sprite {

    public static final float MIN_POSITION_ON_THE_SCREEN =
            0.455f * HakuGoesHome.VIRTUAL_WIDTH /HakuGoesHome.PIXELS_PER_METER;

    public World world;
    public Body body;
    private OrthographicCamera camera;

    private boolean trackable;

    public Haku(World world, OrthographicCamera camera){
        this.world = world;
        this.camera = camera;
        defineHaku();
    }

    public void defineHaku() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((float) 32/ HakuGoesHome.PIXELS_PER_METER, (float) 100/HakuGoesHome.PIXELS_PER_METER);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius((float) 8/HakuGoesHome.PIXELS_PER_METER);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        trackable  = true;
    }

    public void runForwards() {
        trackable = true;
        if (body.getLinearVelocity().x <= 1.2f) {
            body.applyLinearImpulse(new Vector2(0.06f, 0), body.getWorldCenter(), true);
        }
    }

    public void runBackwards() {
        trackable = false;
        body.applyLinearImpulse(new Vector2(-0.06f, 0), body.getWorldCenter(), true);
    }

    public void jump() {
        if (body.getLinearVelocity().y == 0) {
            body.applyLinearImpulse(new Vector2(0, 4), body.getWorldCenter(), true);
        }
    }

    public void track(OrthographicCamera camera){
        if (trackable) {
            if (camera.position.x - body.getPosition().x <= (0.1f * HakuGoesHome.VIRTUAL_WIDTH) /HakuGoesHome.PIXELS_PER_METER) {
                camera.position.set(body.getPosition().x + 0.1f * HakuGoesHome.VIRTUAL_WIDTH / HakuGoesHome.PIXELS_PER_METER,
                        camera.position.y, 0);
            }
        }
    }

    public boolean isTrackable() {
        return trackable;
    }
}
