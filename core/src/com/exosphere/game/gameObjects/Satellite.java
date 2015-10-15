package com.exosphere.game.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.exosphere.game.Assets;
import com.exosphere.game.astroPhysics.Orbit;

import static java.lang.Math.acos;
import static java.lang.Math.toDegrees;


/**
 * exosphere - Satellite
 * Created by tistatos on 9/14/15.
 */
public class Satellite {

    public Orbit getOrbit() {
        return mOrbit;
    }

    private Orbit mOrbit;
    private ModelInstance mModel;
    private ModelInstance mCone;
    private static final float mConeVolume = 1.0e13f;

    public ModelInstance getModel() {
        return mModel;
    }
    public ModelInstance getCone() { return mCone; }

    public void update(float delta) {

        mOrbit.updateOrbit(delta);
        Vector3 translation = mOrbit.getCoordinates().toCartesian();

        mModel.transform.setToRotationRad(Vector3.Z, (float) mOrbit.getInclination());
        mModel.transform.translate(translation);
        Vector3 modelFront = new Vector3(1,0,0);
        Vector3 inwards = new Vector3(translation.cpy().nor());
        float d = modelFront.dot(inwards);
        Vector3 up = modelFront.crs(inwards).nor();
        float deg = (float)toDegrees(acos(d));
        mModel.transform.rotate(Vector3.Y, 180.0f);
        mModel.transform.rotate(Vector3.Y, up.y * deg);
        mModel.transform.scale(600, 600, 600);

        float coneRadius = getConeRadius(mOrbit.getCurrentRadius());
        mCone.materials.get(0).set(ColorAttribute.createDiffuse((coneRadius < mOrbit.getCelestial().getRadius()) ? Color.RED : Color.GREEN));
        float distance = (float)mOrbit.getCurrentRadius();

        mCone.transform.setToRotationRad(Vector3.Z, (float) mOrbit.getInclination());
        mCone.transform.translate(translation);
        mCone.transform.rotate(Vector3.Z, 90.0f);
        Vector3 coneFront = new Vector3(1,0,0);
        Vector3 coneInwards = new Vector3(translation.cpy().nor());
        float coneD = coneFront.dot(coneInwards);
        Vector3 coneUp = coneFront.crs(coneInwards).nor();
        float coneDeg = (float)toDegrees(acos(coneD));
        mCone.transform.rotate(Vector3.X, 180.0f);
        mCone.transform.rotate(Vector3.X, coneUp.y*coneDeg);


        mCone.transform.translate(0, -distance / 2, 0);
        mCone.transform.scale(coneRadius, distance, coneRadius);

    }

    private float getConeRadius(double height) {
        double radi = Math.sqrt((3 * mConeVolume) / (height * Math.PI));
        return (float)((radi < mOrbit.getCelestial().getRadius()*3) ? radi : mOrbit.getCelestial().getRadius()*3);
    }

    public Satellite(Orbit orbit) {
        mOrbit = orbit;
        mModel = new ModelInstance(Assets.getSatelliteModel());

        Material conMat = new Material(ColorAttribute.createDiffuse(Color.GREEN));
        conMat.set(new BlendingAttribute(0.2f));
        mCone = new ModelInstance(new ModelBuilder()
                .createCone(1,
                        (float)1,
                        1, 16,
                        conMat,
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));
    }
}
