package com.exosphere.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.VertexArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.exosphere.game.Assets;
import com.exosphere.game.Exosphere;
import com.exosphere.game.Settings;
import com.exosphere.game.gameObjects.Earth;
import com.exosphere.game.gameObjects.PlayerCamera;
import com.exosphere.game.gameObjects.Satellite;

import java.awt.*;

/**
 * exosphere - MainMenu
 * Main menu screen of game
 * Created by tistatos on 9/4/15.
 */
public class MainMenu extends ScreenAdapter {
    Exosphere mGame;

    PlayerCamera mMenuCamera;
    Rectangle mExitBounds;
    Rectangle mPlayBounds;
    Rectangle mSettings;

    Earth mEarth;
    Satellite mSatellite;
    Satellite mSecondSatellite;
    Satellite mThirdSatellite;

    Environment environment;
    ShapeRenderer shapeDebugger;
    Vector3 satPos;
    public MainMenu(Exosphere game) {
        this.mGame = game;
        mMenuCamera = new PlayerCamera(new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), !false);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        mEarth = new Earth();
        mSatellite = new Satellite(18164.0f, Math.PI/4, 0, 0.6, mEarth);
        mSecondSatellite = new Satellite(28164.0f, -Math.PI/6,0.0, mEarth);
        mThirdSatellite = new Satellite(38164.0f, 0.0, 0.8, mEarth);


        mMenuCamera.getCamera().position.set(0f, 30000f, 64000f );
        mMenuCamera.getCamera().near = 0.1f;
        mMenuCamera.getCamera().far = 600000f;

        shapeDebugger = new ShapeRenderer();
        satPos = new Vector3();
    }

    public void update(float delta) {

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            Settings.setTimeFactor(60*200);
        }
        else {
            Settings.setTimeFactor(60 * 10);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            Settings.setTimeFactor(0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.I)) {
            mThirdSatellite.setSemiMajorAxis(mThirdSatellite.getSemiMajorAxis() + 50000.0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.K)) {
            mThirdSatellite.setSemiMajorAxis(mThirdSatellite.getSemiMajorAxis()-50000.0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.O)) {
            mThirdSatellite.setInclination(mThirdSatellite.getInclination() + Math.PI /128.0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.L)) {
            mThirdSatellite.setInclination(mThirdSatellite.getInclination() - Math.PI/128.0f);
        }

        mMenuCamera.update(delta);
        mEarth.update(delta);
        mSatellite.update(delta);
        mSecondSatellite.update(delta);
        mThirdSatellite.update(delta);
    }


    public void draw() {

        ModelBatch batcher = mGame.getBatcher();
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        shapeDebugger.setProjectionMatrix(mMenuCamera.getCamera().combined);
        shapeDebugger.begin(ShapeRenderer.ShapeType.Filled);
        shapeDebugger.setColor(1, 1, 1, 1);

        mSatellite.getModel().transform.getTranslation(satPos);
        Array<Vector3> pts = mSatellite.getOrbit();
        for(int i = 0; i < pts.size; i++) {
            Vector3 first = pts.get(i);
            Vector3 second = pts.get((i+1)%pts.size);
            shapeDebugger.identity();
            shapeDebugger.rotate(0, 0, 1, (float) (mSatellite.getInclination() * 180 / Math.PI));
            shapeDebugger.rotate(1, 0, 0, 90.0f);
            shapeDebugger.rectLine(first.x, first.z, second.x, second.z, 500.0f);
        }
        pts = mSecondSatellite.getOrbit();
        for(int i = 0; i < pts.size; i++) {
            Vector3 first = pts.get(i);
            Vector3 second = pts.get((i+1) % pts.size);
            shapeDebugger.identity();
            shapeDebugger.rotate(0, 0, 1, (float) (mSecondSatellite.getInclination() * 180 / Math.PI));
            shapeDebugger.rotate(1, 0, 0, 90.0f);
            shapeDebugger.rectLine(first.x, first.z, second.x, second.z, 500.0f);
        }
        pts = mThirdSatellite.getOrbit();
        for(int i = 0; i < pts.size; i++) {
            Vector3 first = pts.get(i);
            Vector3 second = pts.get((i+1)%pts.size);
            shapeDebugger.identity();
            shapeDebugger.rotate(1, 0, 0, 90.0f);
            shapeDebugger.rotate(0, 0, 0, (float) (mThirdSatellite.getInclination() * 180 / Math.PI));
            shapeDebugger.rectLine(first.x, first.z, second.x, second.z, 500.0f);
        }

        shapeDebugger.identity();
        shapeDebugger.line(Vector3.Zero, satPos);
        mSecondSatellite.getModel().transform.getTranslation(satPos);
        shapeDebugger.line(0, 0, 0, satPos.x, satPos.y, satPos.z);
        mThirdSatellite.getModel().transform.getTranslation(satPos);
        shapeDebugger.line(0, 0, 0, satPos.x, satPos.y, satPos.z);
        shapeDebugger.end();

        batcher.begin(mMenuCamera.getCamera());
        batcher.setCamera(mMenuCamera.getCamera());
        batcher.render(mEarth.getModel(), environment);
        batcher.render(mSatellite.getModel(), environment);
        batcher.render(mSecondSatellite.getModel(), environment);
        batcher.render(mThirdSatellite.getModel(), environment);
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
