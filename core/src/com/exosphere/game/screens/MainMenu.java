package com.exosphere.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.exosphere.game.Assets;
import com.exosphere.game.Exosphere;
import com.exosphere.game.gameObjects.Earth;
import com.exosphere.game.gameObjects.Satellite;

/**
 * exosphere - MainMenu
 * Main menu screen of game
 * Created by tistatos on 9/4/15.
 */
public class MainMenu extends ScreenAdapter {
    Exosphere mGame;

    PerspectiveCamera mMenuCamera;
    Rectangle mExitBounds;
    Rectangle mPlayBounds;
    Rectangle mSettings;

    Earth mEarth;
    Satellite mSattelite;
    Environment environment;


    public MainMenu(Exosphere game) {
        this.mGame = game;
        mMenuCamera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//      mMenuCamera = new PerspectiveCamera(67, Settings.msScreenSize[0], Settings.msScreenSize[1]);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        mEarth = new Earth();
        mSattelite = new Satellite(.2,0, mEarth);

        mMenuCamera.position.set(0f, 0f, 8000f);
        mMenuCamera.lookAt(0, 0, 0);
        mMenuCamera.near = 0.1f;
        mMenuCamera.far = 30000f;
    }

    public void update(float delta) {
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
        }

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        mMenuCamera.update();
        mEarth.update(delta);
    }


    public void draw() {

        ModelBatch batcher = mGame.getBatcher();
        Gdx.gl30.glClearColor(0, 0, 0, 1);
        Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        batcher.begin(mMenuCamera);
        batcher.setCamera(mMenuCamera);
        batcher.render(mEarth.getModel(), environment);
        batcher.render(mSattelite.getModel(), environment);
        batcher.end();

        mGame.getSpriteBatcher().enableBlending();
        mGame.getSpriteBatcher().begin();
        mGame.getSpriteBatcher().draw(Assets.getLogo(), (Gdx.graphics.getWidth()/2 -Assets.getLogo().getRegionWidth()/2), Gdx.graphics.getHeight() - 100);
        mGame.getSpriteBatcher().end();
    }

    @Override
    public void render (float delta) {
        update(delta);
        draw();
    }
}
