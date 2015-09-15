package com.exosphere.game.gameObjects;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.exosphere.game.Assets;
import com.exosphere.game.astroPhysics.Celestial;
import com.exosphere.game.astroPhysics.Orbit;

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

    }

    public Satellite(double semiMajorAxis, double inclination, Celestial mOrbitedBody) {
        super(semiMajorAxis, inclination, mOrbitedBody);

        mModel = new ModelInstance(Assets.getSatelliteModel());
    }
}
