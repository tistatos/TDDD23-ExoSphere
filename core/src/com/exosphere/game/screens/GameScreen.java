package com.exosphere.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.exosphere.game.Assets;
import com.exosphere.game.Exosphere;
import com.exosphere.game.astroPhysics.Orbit;
import com.exosphere.game.gameObjects.Earth;
import com.exosphere.game.gameObjects.PlayerCamera;
import com.exosphere.game.gameObjects.Satellite;
import com.exosphere.game.gameObjects.SatelliteContainer;
import com.exosphere.game.states.LevelState;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static java.lang.Math.max;
import static java.lang.Math.signum;
import static java.lang.StrictMath.acos;
import static java.lang.StrictMath.min;

/**
 * exosphere - GameScreen
 * game screen during gameplay
 * Created by tistatos on 9/4/15.
 */
public class GameScreen extends ScreenAdapter {

    private Exosphere mGame;
    private SatelliteContainer mSatelliteContainer;
    private Earth mEarth;
    private LevelState currentLevel;
    private PlayerCamera mCamera;
    private boolean rightBtnDown = false;
    private boolean leftBtnDown = false;
    private Satellite mLastCreatedSatellite;
    private Array<ModelInstance> mCubes = new Array<>();

    private Vector3 satelliteSpawnVector;

    public GameScreen(Exosphere game, LevelState l) {
        mGame = game;
        currentLevel = l;

        mSatelliteContainer = new SatelliteContainer();
        mCamera = new PlayerCamera(new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        mEarth = new Earth();

        mCamera.getCamera().position.set(0f, 80000f, 194000f );
        mCamera.getCamera().near = 0.1f;
        mCamera.getCamera().far = 600000f;
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    private void draw() {
        ModelBatch batcher = mGame.getBatcher();
        ShapeRenderer shapeRenderer = mGame.getShapeRenderer();
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Assets.getCubeMap().render(mCamera.getCamera());

        shapeRenderer.setProjectionMatrix(mCamera.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1);
        mSatelliteContainer.drawOrbits(shapeRenderer);
        shapeRenderer.end();


        batcher.begin(mCamera.getCamera());
        batcher.render(mEarth.getModel());
        batcher.render(mSatelliteContainer);
        batcher.render(mCubes);
        batcher.end();

    }

    private void update(float delta) {
        boolean leftClick = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        boolean rightClick = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);

        //See if we are clicking on a satellite
        Satellite s = null;
        if(leftClick) {
            Ray r = mCamera.getPickingRay();
            s = mSatelliteContainer.selelectSatellite(r);
        }

        //if we clicked a satellite, dont rotate camera
        if(s == null && !leftBtnDown)
        {
            mCamera.update(delta);
            leftBtnDown = true;

        }
        else if(s == null)
        {
            leftBtnDown = false;
        }

        //update models
        mSatelliteContainer.update(delta);
        mEarth.update(delta);


        //Right click to spawn new satellites
        if(rightClick && !rightBtnDown) {
            rightBtnDown = true;
            Vector3 intersection = mCamera.getPlaneIntersectionPoint();

            if(intersection.len() != 0) {
                satelliteSpawnVector = intersection;
                mLastCreatedSatellite = new Satellite(new Orbit(intersection.len(), 0, mEarth));
                mSatelliteContainer.add(mLastCreatedSatellite);
            }
        }
        else if(!rightClick && rightBtnDown) {
            rightBtnDown = false;
        }
        else if(rightBtnDown) {
            //Drag around spawn satellite
            Vector3 intersection = mCamera.getPlaneIntersectionPoint();
            if(intersection.len() > 8000.0f)
            {
                Vector3 leftOrRight = intersection.cpy().nor().crs(satelliteSpawnVector.nor());
                double dot = clamp(satelliteSpawnVector.nor().dot(intersection.cpy().nor()), -1, 1);
                double anomalyChange = -signum(leftOrRight.y)*acos(dot);
                mLastCreatedSatellite.getOrbit().addToAnomaly(anomalyChange);
                mLastCreatedSatellite.getOrbit().setSemiMajorAxis(intersection.len());
                satelliteSpawnVector = intersection;
            }
        }
    }

    @Override
    public void dispose() {

    }
}

