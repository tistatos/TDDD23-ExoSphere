package com.exosphere.game.gameObjects;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.exosphere.game.Assets;
import com.exosphere.game.Settings;
import com.exosphere.game.astroPhysics.Celestial;

/**
 * exosphere - Earth
 * Created by tistatos on 9/14/15.
 */
public class Earth extends Celestial implements IRenderable {
    public static final double mEarthMass = 5.97237e24;
    public static final double mEarthRadius = 6371;
    public static final double mEarthRotationSpeed = 7.2921150e-5;

    private ModelInstance mModel;

    public Earth() {
        super(mEarthMass, mEarthRadius, mEarthRotationSpeed);

        mModel = new ModelInstance(Assets.getEarthModel());
    }

    @Override
    public ModelInstance getModel() {
        return mModel;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        mModel.transform.rotateRad(Vector3.Y, (float)(delta * Settings.getTimeFactor() * mEarthRotationSpeed));
    }

    @Override
    public void draw() {

    }
}
