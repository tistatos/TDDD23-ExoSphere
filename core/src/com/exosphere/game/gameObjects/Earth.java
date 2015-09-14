package com.exosphere.game.gameObjects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.exosphere.game.Assets;
import com.exosphere.game.astroPhysics.Celestial;

/**
 * exosphere - Earth
 * Created by tistatos on 9/14/15.
 */
public class Earth extends Celestial implements IRenderable {
    private static final double mEarthMass = 5.972e24;
    private static final double mEarthRadius = 6371;
    private ModelInstance mModel;
    public Earth() {
        super(mEarthMass, mEarthRadius);

        mModel = new ModelInstance(Assets.getEarthModel());
    }

    @Override
    public ModelInstance getModel() {
        return mModel;
    }
}
