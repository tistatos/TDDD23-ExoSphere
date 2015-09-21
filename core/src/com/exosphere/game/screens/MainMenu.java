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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.exosphere.game.Assets;
import com.exosphere.game.Exosphere;
import com.exosphere.game.Settings;
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
    Satellite mSecondSattelite;
    Environment environment;
    ShapeRenderer shapeDebugger;
    Vector3 satPos;
    public MainMenu(Exosphere game) {
        this.mGame = game;
        mMenuCamera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//      mMenuCamera = new PerspectiveCamera(67, Settings.msScreenSize[0], Settings.msScreenSize[1]);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        mEarth = new Earth();
        mSattelite = new Satellite(38164.0f,0.0 ,0.6, (double)MathUtils.PI, mEarth);
        mSecondSattelite = new Satellite(28164.0f,0.0 ,0.4, mEarth);
//        mSecondSattelite = new Satellite(84328,0, mEarth);

        mMenuCamera.position.set(0f, 0, 64000f );
        mMenuCamera.lookAt(0, 0, 0);
        mMenuCamera.near = 0.1f;
        mMenuCamera.far = 600000f;

        shapeDebugger = new ShapeRenderer();
        satPos = new Vector3();
    }

    public void update(float delta) {
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {

        }

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            Settings.setTimeFactor(60*200);
        }
        else {
            Settings.setTimeFactor(60*10);
        }

        mMenuCamera.update();
        mEarth.update(delta);
        mSattelite.update(delta);
        mSecondSattelite.update(delta);
    }


    public void draw() {

        ModelBatch batcher = mGame.getBatcher();
        Gdx.gl30.glClearColor(0, 0, 0, 1);
        Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        shapeDebugger.setProjectionMatrix(mMenuCamera.combined);
        shapeDebugger.begin(ShapeRenderer.ShapeType.Line);
        shapeDebugger.setColor(1, 1, 1, 1);

        mSattelite.getModel().transform.getTranslation(satPos);
        Array<Vector3> pts = mSattelite.getOrbit();
        for(int i = 0; i < pts.size; i++) {
            Vector3 first = pts.get(i);
            Vector3 second = pts.get((i+1)%pts.size);
            shapeDebugger.line(first.x, first.y, first.z,second.x, second.y, second.z);
        }
        pts = mSecondSattelite.getOrbit();
        for(int i = 0; i < pts.size; i++) {
            Vector3 first = pts.get(i);
            Vector3 second = pts.get((i+1)%pts.size);
            shapeDebugger.line(first.x, first.y, first.z,second.x, second.y, second.z);
        }
        shapeDebugger.line(0, 0, 0, satPos.x, satPos.y, satPos.z);
        mSecondSattelite.getModel().transform.getTranslation(satPos);
        shapeDebugger.line(0, 0, 0, satPos.x, satPos.y, satPos.z);
        shapeDebugger.end();

        batcher.begin(mMenuCamera);
        batcher.setCamera(mMenuCamera);
        batcher.render(mEarth.getModel(), environment);
        batcher.render(mSattelite.getModel(), environment);
        batcher.render(mSecondSattelite.getModel(), environment);
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
