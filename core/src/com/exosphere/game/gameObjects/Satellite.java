package com.exosphere.game.gameObjects;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.exosphere.game.Assets;
import com.exosphere.game.astroPhysics.Celestial;
import com.exosphere.game.astroPhysics.Orbit;
import com.intellij.openapi.graph.layout.tree.AbstractRotatableNodePlacer;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

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
        double x = getCurrentRadius()*cos(getCurrentAnomaly());
        double z = getCurrentRadius()*sin(-getCurrentAnomaly());
        mModel.transform.setTranslation((float)x, 0.0f, (float)z);
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
