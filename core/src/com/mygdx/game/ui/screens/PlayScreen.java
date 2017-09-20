package com.mygdx.game.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.HakuGoesHome;
import com.mygdx.game.ui.scenes.Hud;

/**
 * Created by p.szwed on 9/20/17.
 */

public class PlayScreen implements Screen {

    private HakuGoesHome game;
    private OrthographicCamera orthographicCamera;
    private Hud hud;
    private Viewport viewport;

    private TiledMap tiledMap;
    private TmxMapLoader mapLoader;
    private OrthogonalTiledMapRenderer renderer;

    public PlayScreen(HakuGoesHome game){
        this.game = game;
        orthographicCamera = new OrthographicCamera();
        viewport = new FitViewport(HakuGoesHome.VIRTUAL_WIDTH, HakuGoesHome.VIRTUAL_HEIGHT,
                orthographicCamera);
        viewport.apply();

        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
        orthographicCamera.position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 0);
        hud = new Hud(game.batch);
    }

    public void handleInput(float dt){
        if (Gdx.input.isTouched()){
            orthographicCamera.position.x += 100*dt;
        }
    }


    public void update(float dt){
        handleInput(dt);
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
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);

        renderer.render();
        hud.stage.draw();
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

    }
}
