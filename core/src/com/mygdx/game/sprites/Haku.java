package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.HakuGoesHome;
import com.mygdx.game.ui.screens.PlayScreen;

/**
 * Created by p.szwed on 9/21/17.
 */

public class Haku extends Sprite {

    public static final float MAX_DIFF_ON_THE_SCREEN = HakuGoesHome.scale(0.457f *
                                                                HakuGoesHome.VIRTUAL_WIDTH);

    private static final float MIN_DIFF_ON_THE_SCREEN = HakuGoesHome.scale(0.1f * HakuGoesHome.VIRTUAL_WIDTH);
    private static final float NORMAL_VELOCITY = 1.3f;
    private static final float X_IMPULSE = 0.08f;
    private static final float JUMP_IMPULSE = 4;
    private static final float FLYING_VELOCITY = 0.8f;

    public World world;
    public Body body;
    private OrthographicCamera camera;
    private TextureAtlas textureAtlas;
    private TextureRegion hakuStanding;

    private boolean trackable;
    private State currentState;
    private State previousState;
    private Animation running;
    private float stateTimer;
    private boolean movingRight;
    private boolean dead;
    private boolean dying;


    public enum State {STANDING, RUNNING, JUMPING, FLYING, FALLING, DEAD}

    public Haku() {
        super();
    }

    public Haku(World world, PlayScreen playScreen){
        super(playScreen.getTextureAtlas().findRegion("basic_haku"));
        textureAtlas = playScreen.getTextureAtlas();
        hakuStanding = playScreen.getTextureAtlas().findRegion("basic_haku");

        this.world = world;
        this.camera = playScreen.getOrthographicCamera();
        defineHaku();

        this.setBounds(body.getPosition().x - getWidth()/2 ,
                body.getPosition().y - getWidth()/2, 32, 32);
                this.setRegion(hakuStanding);

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        movingRight = true;
        dead = false;
        dying = false;

        this.initializeAnimation(playScreen.getTextureAtlas());
    }

    public void update(float dt){
        this.setPosition(HakuGoesHome.VIRTUAL_WIDTH/2
                        - (camera.position.x - body.getPosition().x)*HakuGoesHome.PIXELS_PER_METER
                        - getWidth()/2,
                HakuGoesHome.VIRTUAL_HEIGHT/2
                        - (camera.position.y - body.getPosition().y)*HakuGoesHome.PIXELS_PER_METER
                        - getHeight()/2 );

        TextureRegion newRegion = this.getFrame(dt);
        this.setRegion(newRegion);
    }

    public void defineHaku() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(HakuGoesHome.scale(32f), HakuGoesHome.scale(100f));
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(HakuGoesHome.scale(12f));
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        trackable  = true;
    }

    public void runForwards() {
        trackable = true;
        if (body.getLinearVelocity().x <= NORMAL_VELOCITY) {
            body.applyLinearImpulse(new Vector2(X_IMPULSE, 0), body.getWorldCenter(), true);
        }
    }

    public void runBackwards() {
        trackable = false;
        if (body.getLinearVelocity().x >= -NORMAL_VELOCITY) {
            body.applyLinearImpulse(new Vector2(-X_IMPULSE, 0), body.getWorldCenter(), true);
        }
    }

    public void jump() {
        if (body.getLinearVelocity().y == 0) {
            body.applyLinearImpulse(new Vector2(0, JUMP_IMPULSE), body.getWorldCenter(), true);
        }
    }

    public void die(){
        dying = false;
        body.applyLinearImpulse(new Vector2(0, 2.0f * JUMP_IMPULSE), body.getWorldCenter(), true);
    }

    public void track(OrthographicCamera camera){
        if (trackable) {
            if (camera.position.x - body.getPosition().x <= MIN_DIFF_ON_THE_SCREEN) {
                camera.position.set(body.getPosition().x + MIN_DIFF_ON_THE_SCREEN,
                        camera.position.y, 0);
            }
        }
    }

    private void initializeAnimation(TextureAtlas textureAtlas){
        Array<TextureRegion> animationArray = new Array<TextureRegion>();
        animationArray.add(textureAtlas.findRegion("running_haku_1"));
        animationArray.add(textureAtlas.findRegion("running_haku_2"));
        running = new Animation(0.15f, animationArray);

    }

    public boolean isTrackable() {
        return trackable;
    }

    public TextureRegion getFrame(float dt){
        currentState = getCurrentState();

        TextureRegion newRegion;
        switch (currentState){
            case STANDING:
                newRegion = hakuStanding;
                break;
            case RUNNING:
                newRegion = (TextureRegion) running.getKeyFrame(stateTimer, true);
                break;
            case JUMPING:
                newRegion = textureAtlas.findRegion("jumping_haku_up");
                break;
            case FLYING:
                newRegion = textureAtlas.findRegion("jumping_horizontally_haku");
                break;
            case FALLING:
                newRegion = textureAtlas.findRegion("jumping_haku_down");
                break;
            case DEAD:
                dead = true;
                newRegion = textureAtlas.findRegion("dead_haku");
                break;
            default:
                newRegion = hakuStanding;
                break;
        }

        if ((body.getLinearVelocity().x < 0 || !movingRight) && !newRegion.isFlipX()){
            newRegion.flip(true, false);
            movingRight = false;
        } else if ((body.getLinearVelocity().x > 0 || movingRight) && newRegion.isFlipX()){
            newRegion.flip(true, false);
            movingRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return newRegion;
    }

    public State getCurrentState() {
        System.out.println(body.getPosition().y);
        if (!dead && !dying && previousState != State.JUMPING && body.getLinearVelocity().x != 0 && body.getLinearVelocity().y == 0) {
            return State.RUNNING;
        } else if (!dead && !dying && (previousState == State.JUMPING || previousState == State.FLYING) && body.getLinearVelocity().y >= -FLYING_VELOCITY
                && body.getLinearVelocity().y <= FLYING_VELOCITY) {
            return State.FLYING;
        } else if (!dead && !dying && body.getLinearVelocity().y > FLYING_VELOCITY) {
            return State.JUMPING;
        } else if (dying || dead || body.getPosition().y <= 0.0f) {
            if (!dead) dying = true;
            return State.DEAD;
        } else if (!dead && !dying && body.getLinearVelocity().y < -FLYING_VELOCITY || (body.getLinearVelocity().y < 0
                && previousState == State.RUNNING)) {
            return State.FALLING;
        } else {
            return State.STANDING;
        }
    }

    public boolean isDying() {
        return dying;
    }
}
