package com.exosphere.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.exosphere.game.Assets;
import com.exosphere.game.Exosphere;
import com.exosphere.game.astroPhysics.Orbit;
import com.exosphere.game.gameObjects.Earth;
import com.exosphere.game.gameObjects.PlayerCamera;
import com.exosphere.game.gameObjects.Satellite;
import com.exosphere.game.gameObjects.SatelliteContainer;
import com.exosphere.game.states.LevelState;

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

    Earth mEarth;
    Satellite mSatellite;
    Satellite mSecondSatellite;
    Satellite mThirdSatellite;


    SatelliteContainer mSatelliteContainer;
    Environment environment;
    ShapeRenderer shapeRender;
    Vector3 satPos;

    public MainMenu(Exosphere game) {
        this.mGame = game;
        mMenuCamera = new PlayerCamera(new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), false);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        mSatelliteContainer = new SatelliteContainer();
        mEarth = new Earth();
        mSatellite = new Satellite(new Orbit(98164.0f, 0, 0.7,2*Math.PI/3, mEarth));
        mSecondSatellite = new Satellite(new Orbit(98164.0f, 0,0.7,4*Math.PI/3, mEarth));
        mThirdSatellite = new Satellite(new Orbit(98164.0f, 0, 0.7,0, mEarth));

        mSatelliteContainer.add(mSatellite);
        mSatelliteContainer.add(mSecondSatellite);
        mSatelliteContainer.add(mThirdSatellite);

        mPlayBounds = new Rectangle(477,465,323, 82);
        mExitBounds = new Rectangle(477,615,323, 82);

        mMenuCamera.getCamera().position.set(0f, 80000f, 194000f );
        mMenuCamera.getCamera().near = 0.1f;
        mMenuCamera.getCamera().far = 600000f;

        shapeRender = new ShapeRenderer();
        satPos = new Vector3();
    }

    public void update(float delta) {

        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if(mPlayBounds.contains(Gdx.input.getX(),Gdx.input.getY())) {
                mGame.setScreen(new GameScreen(mGame, new LevelState()));
            }
            if(mExitBounds.contains(Gdx.input.getX(), Gdx.input.getY())) {
                Gdx.app.exit();
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

//        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
//            Settings.setTimeFactor(60*200);
//        }
//        else if(Gdx.input.isKeyPressed(Input.Keys.S)) {
//            Settings.setTimeFactor(0);
//        }
//        else {
//            Settings.setTimeFactor(60 * 10);
//        }
//
//        if(Gdx.input.isKeyPressed(Input.Keys.I)) {
//            mThirdSatellite.setSemiMajorAxis(mThirdSatellite.getSemiMajorAxis() + 50000.0f);
//        }
//        if(Gdx.input.isKeyPressed(Input.Keys.K)) {
//            mThirdSatellite.setSemiMajorAxis(mThirdSatellite.getSemiMajorAxis()-50000.0f);
//        }
//        if(Gdx.input.isKeyPressed(Input.Keys.O)) {
//            mThirdSatellite.setInclination(mThirdSatellite.getInclination() + Math.PI /128.0f);
//        }
//        if(Gdx.input.isKeyPressed(Input.Keys.L)) {
//            mThirdSatellite.setInclination(mThirdSatellite.getInclination() - Math.PI/128.0f);
//        }

        mMenuCamera.update(delta);
        mEarth.update(delta);
        mSatelliteContainer.update(delta);
    }


    public void draw() {

        ModelBatch batcher = mGame.getBatcher();

        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Assets.getCubeMap().render(mMenuCamera.getCamera());

        shapeRender.setProjectionMatrix(mMenuCamera.getCamera().combined);
        shapeRender.begin(ShapeRenderer.ShapeType.Filled);
        shapeRender.setColor(1, 1, 1, 1);
        mSatelliteContainer.drawOrbits(shapeRender);
        shapeRender.end();

        batcher.begin(mMenuCamera.getCamera());
        batcher.setCamera(mMenuCamera.getCamera());
        batcher.render(mEarth.getModel(), environment);
        batcher.render(mSatelliteContainer, environment);
        batcher.end();

        mGame.getSpriteBatcher().enableBlending();
        mGame.getSpriteBatcher().begin();
        mGame.getSpriteBatcher().draw(Assets.getLogo(), (Gdx.graphics.getWidth() / 2 - Assets.getLogo().getRegionWidth() / 2), Gdx.graphics.getHeight() - 350);
        mGame.getSpriteBatcher().draw(Assets.getStart(), (Gdx.graphics.getWidth()/2 -Assets.getStart().getRegionWidth()/2), Gdx.graphics.getHeight() - 550);
        mGame.getSpriteBatcher().draw(Assets.getExit(), (Gdx.graphics.getWidth()/2 -Assets.getExit().getRegionWidth()/2), Gdx.graphics.getHeight() - 700);
        mGame.getSpriteBatcher().end();
    }

    @Override
    public void render (float delta) {
        update(delta);
        draw();
    }
}
