package com.exosphere.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.exosphere.game.Assets;
import com.exosphere.game.Exosphere;
import com.exosphere.game.Settings;
import com.exosphere.game.astroPhysics.Orbit;
import com.exosphere.game.astroPhysics.SphericalCoord;
import com.exosphere.game.gameObjects.Earth;
import com.exosphere.game.gameObjects.PlayerCamera;
import com.exosphere.game.gameObjects.Satellite;
import com.exosphere.game.gameObjects.SatelliteContainer;
import com.exosphere.game.states.LevelState;

import java.util.Iterator;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static java.lang.Math.signum;
import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.acos;

/**
 * exosphere - GameScreen
 * game screen during gameplay
 * Created by tistatos on 9/4/15.
 */
public class GameScreen extends ScreenAdapter {

    private Exosphere mGame;
    private SatelliteContainer mSatelliteContainer;
    private Earth mEarth;
    private Array<LevelState> mObjectives;
    private PlayerCamera mCamera;
    private boolean rightBtnDown = false;
    private boolean leftBtnDown = false;
    private boolean modifyOrbit = false;
    private Satellite mLastCreatedSatellite;
    private String mControlString = "";
    private Vector3 satelliteSpawnVector;
    private final static boolean DEBUG = true;
    Rectangle mNextLevelBounds;


    Environment mEnvironment;


    public GameScreen(Exosphere game) {
        mGame = game;

        Satellite.resetConeVolume();
        mSatelliteContainer = new SatelliteContainer();

        mCamera = new PlayerCamera(new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        mEarth = new Earth();

        mEnvironment = new Environment();
        mEnvironment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        mEnvironment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        mObjectives = new Array<>();
        mObjectives.add(new LevelState("Put satellite in orbit") {
            @Override
            public boolean levelRequirements() {
                return Satellites.numberOfSatellites() > 0;
            }
        });

        LevelState.Celestial = mEarth;
        LevelState.Satellites = mSatelliteContainer;

        mObjectives.add(new LevelState("Put satellite in Geostationary orbit") {
            @Override
            public boolean levelRequirements() {
                Iterator<Satellite> it = Satellites.iterator();
                while (it.hasNext()) {
                    Satellite s = it.next();
                    if (s.getOrbit().getSemiMajorAxis() >= Celestial.getGeostationaryOrbit() / 10000 && abs(s.getOrbit().getInclination()) < MathUtils.PI/10)
                        return true;
                }
                return false;
            }
        });

        mObjectives.add(new LevelState("Put satellite in geosynchronous polar orbit") {
            @Override
            public boolean levelRequirements() {
                Iterator<Satellite> it = Satellites.iterator();
                while (it.hasNext()) {
                    Satellite s = it.next();
                    if (s.getOrbit().getSemiMajorAxis() >= Celestial.getGeostationaryOrbit() / 10000 && abs(s.getOrbit().getInclination()) > MathUtils.PI/2-0.1 )
                        return true;
                }
                return false;
            }
        });

        mObjectives.add(new LevelState("Increase inclination with 30 degrees") {
            @Override
            public boolean levelRequirements() {
                Iterator<Satellite> it = Satellites.iterator();
                while (it.hasNext()) {
                    Satellite s = it.next();
                    if (abs(s.getOrbit().getInclination()) > MathUtils.PI / 6)
                        return true;
                }
                return false;
            }
        });

        mObjectives.add(new LevelState("Get 95+% Coverage of Earth") {
            @Override
            public boolean levelRequirements() {
                return (LevelState.mCoveragePercentage > 95);
            }
        });

        SphericalCoord cameraPos = new SphericalCoord(15000f,-MathUtils.PI/2,MathUtils.PI/6);
        Gdx.input.setInputProcessor(mCamera);
        mCamera.getCamera().position.set(cameraPos.toCartesian());
        mCamera.getCamera().near = 0.1f;
        mCamera.getCamera().far = 600000f;



        mNextLevelBounds = new Rectangle(Gdx.graphics.getWidth()/2-100, 120,200,200);

    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    private void draw() {
        ModelBatch batcher = mGame.getBatcher();
        SpriteBatch spriteBatch= mGame.getSpriteBatcher();
        ShapeRenderer shapeRenderer = mGame.getShapeRenderer();
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Assets.getCubeMap().render(mCamera.getCamera());


        batcher.begin(mCamera.getCamera());
        batcher.render(mEarth.getModel(), mEnvironment);
        batcher.render(mSatelliteContainer, mEnvironment);
        batcher.end();

        shapeRenderer.setProjectionMatrix(mCamera.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.setColor(1, 1, 1, 1);
        mSatelliteContainer.drawOrbits(shapeRenderer);
        shapeRenderer.end();

        spriteBatch.begin();
        BitmapFont font = new BitmapFont();
        String str = "Operation Objectives: \n";
        font.draw(spriteBatch, str, 25, Gdx.graphics.getHeight() - 20);

        for(int i = 0; i < mObjectives.size; i++) {
            font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            str = mObjectives.get(i).getLevelDescription();
            if(mObjectives.get(i).levelRequirementsMet()) {
                str += " - DONE";
                font.setColor(0.0f, 0.80f, 0.0f, 1.0f);

            }
            font.draw(spriteBatch, str, 25, Gdx.graphics.getHeight() - 20 * (2 + i));
            font.draw(spriteBatch, mControlString, Gdx.input.getX() + 40, Gdx.graphics.getHeight() - Gdx.input.getY());
        }
        spriteBatch.end();

        spriteBatch.begin();
        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        str = "Surface Coverage: " + LevelState.mCoveragePercentage + "%";
        spriteBatch.draw(Assets.getEarthMecatorTexture(), 0, 0, 256, 256);
        spriteBatch.draw(LevelState.getCurrentCoverage(), 0, 0, 256, 256);
        spriteBatch.draw(LevelState.getOrbitMap(), 0, 0, 256, 256);
        font.draw(spriteBatch, str, 25, 25);

        if(mSatelliteContainer.numberOfSatellites() == 0) {
            str = "Use Right Click to spawn Satellite";
            font.draw(spriteBatch, str, Gdx.graphics.getWidth()/2-100, Gdx.graphics.getHeight()/2+50);
        }


        spriteBatch.end();

    }

    private void update(float delta) {
        //update models
        mSatelliteContainer.update(delta);
        LevelState.update();
        mEarth.update(delta);

        boolean leftClick = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        boolean rightClick = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);

        //See if we are clicking on a satellite
        Satellite s = null;
        if(leftClick && !modifyOrbit) {
            Ray r = mCamera.getPickingRay();
            s = mSatelliteContainer.selelectSatellite(r);
        }
        Satellite active = mSatelliteContainer.getSelectedSatellite();
        if(mSatelliteContainer.getSelectedSatellite() != null && leftClick && modifyOrbit) {
            Vector3 intersection = mCamera.getPlaneIntersectionPoint();
            if(active.getActiveControl() == active.getAxisControl())
            {
                if(intersection.len() > 800.0f) {
                    if(abs(mEarth.getGeostationaryOrbit()/10000-intersection.len())< 1000) {
                        active.getOrbit().setSemiMajorAxis(mEarth.getGeostationaryOrbit()/10000);
                    }
                    else {
                        active.getOrbit().setSemiMajorAxis(intersection.len());
                    }
                }
            }
            else if(active.getActiveControl() == active.getInclinationControl())
            {
                Vector3 leftOrRight = intersection.cpy().nor().crs(satelliteSpawnVector.nor());
                double dot = clamp(satelliteSpawnVector.nor().dot(intersection.cpy().nor()), -1, 1);
                double inclinationChange = (-signum(leftOrRight.y)*acos(dot))/2;
                active.getOrbit().setInclination(active.getOrbit().getInclination()+inclinationChange);
            }
            else if(active.getActiveControl() == active.getEccentricityControl()) {
                double diff = satelliteSpawnVector.len() - intersection.len();
                active.getOrbit().setEccentricity(active.getOrbit().getEccentricity() + 0.01f*diff/200);
            }
            else if(active.getActiveControl() == active.getDeleteControl()) {
                mSatelliteContainer.remove(active);
            }
                satelliteSpawnVector = intersection;
        }
        else if(mSatelliteContainer.getSelectedSatellite() != null && mSatelliteContainer.ControlIntersect(mCamera.getPickingRay())) {
            if(leftClick && !modifyOrbit) {
                satelliteSpawnVector = mCamera.getPlaneIntersectionPoint();
                modifyOrbit = true;
            }
            else if(active.getActiveControl() == active.getAxisControl()) {
                mControlString = "Orbit Control";
            }
            else if(active.getActiveControl() == active .getInclinationControl()) {
                mControlString = "Inclination Control";

            }
            else if(active.getActiveControl() == active.getEccentricityControl()) {
                mControlString = "Eccentricity Control";
            }
            else if(active.getActiveControl() == active.getDeleteControl()) {
                mControlString = "Remove Satellite";
            }
        }
        else {
            modifyOrbit = false;
            mControlString = "";
        }
        if(s == null && !leftBtnDown && !modifyOrbit)
        {
            mCamera.update(delta);
            leftBtnDown = true;
        }
        else {
            leftBtnDown = false;

        }


        //Right click to spawn new satellites
        if(rightClick && !rightBtnDown) {
            rightBtnDown = true;
            Vector3 intersection = mCamera.getPlaneIntersectionPoint();

            if(intersection.len() != 0) {
                satelliteSpawnVector = intersection;
                double startAnomaly = acos(intersection.cpy().nor().dot(Vector3.X));
                Vector3 leftOrRight = intersection.cpy().nor().crs(Vector3.X);
                startAnomaly *= -signum(leftOrRight.y);

                mLastCreatedSatellite = new Satellite(new Orbit(satelliteSpawnVector.len()*10, 0,0.0, startAnomaly, 0.0, mEarth));
                mSatelliteContainer.setSelectedSatellite(mLastCreatedSatellite);
                mSatelliteContainer.add(mLastCreatedSatellite);
            }
        }
        else if(!rightClick && rightBtnDown) {
            rightBtnDown = false;
        }
        else if(rightBtnDown) {
            //Drag around spawn satellite
            Vector3 intersection = mCamera.getPlaneIntersectionPoint();
            if(intersection.len() > 800.0f)
            {
                Vector3 leftOrRight = intersection.cpy().nor().crs(satelliteSpawnVector.nor());
                double dot = clamp(satelliteSpawnVector.nor().dot(intersection.cpy().nor()), -1, 1);
                double anomalyChange = -signum(leftOrRight.y)*acos(dot);
                mLastCreatedSatellite.getOrbit().addToAnomaly(anomalyChange);
                if(abs(mEarth.getGeostationaryOrbit()/10000-intersection.len()) < 1000 ) {
                    mLastCreatedSatellite.getOrbit().setSemiMajorAxis(mEarth.getGeostationaryOrbit()/10000);
                }
                else {
                    mLastCreatedSatellite.getOrbit().setSemiMajorAxis(intersection.len());
                }
                satelliteSpawnVector = intersection;
            }
        }


        if(DEBUG) {
            if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                Settings.setTimeFactor(60 * 800);
            }
            else if(Gdx.input.isKeyPressed(Input.Keys.S)) {
                Settings.setTimeFactor(0);
            }
            else {
                Settings.setTimeFactor(60 * 200);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.O) && mSatelliteContainer.getSelectedSatellite() != null) {
                Satellite selected = mSatelliteContainer.getSelectedSatellite();
                selected.getOrbit().setInclination(selected.getOrbit().getInclination() + Math.PI / 128.0f);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.L) && mSatelliteContainer.getSelectedSatellite() != null) {
                Satellite selected = mSatelliteContainer.getSelectedSatellite();
                selected.getOrbit().setInclination(selected.getOrbit().getInclination() - Math.PI / 128.0f);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.I) && mSatelliteContainer.getSelectedSatellite() != null) {
                Satellite selected = mSatelliteContainer.getSelectedSatellite();
                selected.getOrbit().setOrbitOffset(selected.getOrbit().getOrbitOffset() +  Math.PI / 128.0f);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.K) && mSatelliteContainer.getSelectedSatellite() != null) {
                Satellite selected = mSatelliteContainer.getSelectedSatellite();
                selected.getOrbit().setOrbitOffset(selected.getOrbit().getOrbitOffset() - Math.PI / 128.0f);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.U) && mSatelliteContainer.getSelectedSatellite() != null) {
                Satellite selected = mSatelliteContainer.getSelectedSatellite();
                selected.getOrbit().setSemiMajorAxis(mEarth.getGeostationaryOrbit()/10000);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.J) && mSatelliteContainer.getSelectedSatellite() != null) {
                Satellite selected = mSatelliteContainer.getSelectedSatellite();
                if(selected.getOrbit().getSemiMajorAxis()-500.0f > 8000.0f)
                    selected.getOrbit().setSemiMajorAxis(selected.getOrbit().getSemiMajorAxis()-500.0f);
            }
        }


    }

    @Override
    public void dispose() {

    }
}

