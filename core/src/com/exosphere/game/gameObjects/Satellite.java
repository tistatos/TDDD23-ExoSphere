package com.exosphere.game.gameObjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.exosphere.game.Assets;
import com.exosphere.game.astroPhysics.Celestial;
import com.exosphere.game.astroPhysics.Orbit;
import com.exosphere.game.astroPhysics.SphericalCoord;


/**
 * exosphere - Satellite
 * Created by tistatos on 9/14/15.
 */
public class Satellite extends Orbit implements IRenderable {
    private ModelInstance mModel;

    public ModelInstance getModel() {
        return mModel;
    }

    @Override
    public void update(float delta) {

        updateOrbit(delta);

        Array<Vector3> orbit = getOrbit();
        SphericalCoord pos = getCoordinates();
        mModel.transform.setToScaling(10000, 10000, 10000);
        mModel.transform.setTranslation(pos.toCartesian());
        mModel.transform.translate(pos.toCartesian().scl(-1));
        mModel.transform.setToRotationRad(Vector3.Z, (float) mInclination);
        mModel.transform.translate(pos.toCartesian());
    }

    @Override
    public void draw() {

    }

    public Satellite(double semiMajorAxis, double inclination, Celestial orbitedBody) {
        this(semiMajorAxis, inclination, 0.0, 0.0, orbitedBody);
    }
    public Satellite(double semiMajorAxis, double inclination, double eccentricity, Celestial orbitedBody) {
        this(semiMajorAxis, inclination, eccentricity, 0.0, orbitedBody);

    }
    public Satellite(double semiMajorAxis, double inclination, double eccentricity, double anomalyOffset, Celestial orbitedBody) {
        super(semiMajorAxis, inclination, eccentricity, anomalyOffset, orbitedBody);

        mModel = new ModelInstance(Assets.getSatelliteModel());
    }
}
