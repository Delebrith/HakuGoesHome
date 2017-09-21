package com.mygdx.game.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.HakuGoesHome;
import com.mygdx.game.sprites.Haku;
import com.mygdx.game.ui.scenes.Frame;
import com.mygdx.game.utils.Box2DWorldGenerator;

/**
 * Created by p.szwed on 9/20/17.
 */

public class PlayScreen implements Screen {

    private HakuGoesHome game;
    private OrthographicCamera orthographicCamera;
    private Frame frame;
    private Viewport viewport;

    private TiledMap tiledMap;
    private TmxMapLoader mapLoader;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private Haku haku;

    public PlayScreen(HakuGoesHome game){
        this.game = game;
        orthographicCamera = new OrthographicCamera();
        viewport = new FitViewport(HakuGoesHome.VIRTUAL_WIDTH /HakuGoesHome.PIXELS_PER_METER,
                HakuGoesHome.VIRTUAL_HEIGHT /HakuGoesHome.PIXELS_PER_METER,
                orthographicCamera);
        viewport.apply();

        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap, (float)1/HakuGoesHome.PIXELS_PER_METER);
        orthographicCamera.position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 0);
        frame = new Frame(game.batch);

        world = new World(new Vector2(0, -10), true);
        box2DDebugRenderer = new Box2DDebugRenderer();
        box2DDebugRenderer.SHAPE_STATIC.set(0,1,0,1);

        Box2DWorldGenerator.generate(world, tiledMap);

        haku = new Haku(world, orthographicCamera);

    }

    public void handleInput(float dt){
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            haku.jump();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            haku.runForwards();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            haku.runBackwards();
        }
    }


    public void update(float dt){
        handleInput(dt);

        world.step(1/60f, 6, 6);
        haku.track(orthographicCamera);

        orthographicCamera.update();
        renderer.setView(orthographicCamera);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(frame.stage.getCamera().combined);

        renderer.render();
        box2DDebugRenderer.render(world, orthographicCamera.combined);
        frame.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        tiledMap.dispose();
        renderer.dispose();
        world.dispose();
        box2DDebugRenderer.dispose();
        frame.dispose();
    }
}
