package com.mygdx.game.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.HakuGoesHome;

/**
 * Created by p.szwed on 9/21/17.
 */

public class Box2DWorldGenerator {

    public static void generate(World world, TiledMap tiledMap){

        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        for (int i = 2; i < 6; i++) {
            for (MapObject object : tiledMap.getLayers().get(i).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set((rectangle.getX() + rectangle.getWidth()/2) / HakuGoesHome.PIXELS_PER_METER,
                        (rectangle.getY() + rectangle.getHeight()/2) /HakuGoesHome.PIXELS_PER_METER);
                body = world.createBody(bodyDef);
                polygonShape.setAsBox((rectangle.getWidth()/2) /HakuGoesHome.PIXELS_PER_METER,
                        (rectangle.getHeight()/2) /HakuGoesHome.PIXELS_PER_METER);
                fixtureDef.shape = polygonShape;
                body.createFixture(fixtureDef);
            }
        }
    }
}
