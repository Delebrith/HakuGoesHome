package com.mygdx.game.ui.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.HakuGoesHome;

/**
 * Created by p.szwed on 9/20/17.
 */

public class Hud {

    public Stage stage;
    private Viewport viewport;

    private Integer score;
    private Integer lifes;
    private Integer level;

    Label hakuLabel;
    Label scoreLabel;
    Label actualScoreLabel;
    Label levelLabel;
    Label lifesLabel;

    public Hud(SpriteBatch spriteBatch){
        score = 0;
        lifes = 3;
        level = HakuGoesHome.level;

        viewport = new FitViewport(HakuGoesHome.VIRTUAL_WIDTH, HakuGoesHome.VIRTUAL_HEIGHT,
                new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        hakuLabel = new Label(String.format("%s %d", "Haku ",lifes),
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label(String.format("%s %03d", "LEVEL: ", level),
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label("SCORE: ",
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        actualScoreLabel = new Label(String.format("%06d", score),
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        table.add(hakuLabel).expandX().padTop(10);
        table.add(scoreLabel).expandX().padTop(10);
        table.add(levelLabel).expandX().padTop(10);
        table.row();
        table.add(new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE)));
        table.add(actualScoreLabel).expandX().center();



        stage.addActor(table);
    }

}
