package com.exosphere.game.gameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.exosphere.game.Assets;
import com.exosphere.game.astroPhysics.Orbit;
import com.exosphere.game.astroPhysics.SphericalCoord;

import static java.lang.Math.acos;
import static java.lang.Math.toDegrees;


/**
 * exosphere - Satellite
 * Created by tistatos on 9/14/15.
 */
public class Satellite {

    private ModelInstance mActiveControl;

    public Orbit getOrbit() {
        return mOrbit;
    }

    private Orbit mOrbit;
    private ModelInstance mModel;
    private ModelInstance mCone;
    private ModelInstance mAxisControl;
    private ModelInstance mInclinationControl;
    private ModelInstance mEccentricityControl;
    private ModelInstance mDeleteControl;
    private Array<ModelInstance> mControls;

    public static float getConeVolume() {
        return mConeVolume;
    }

    public static void setConeVolume(float mConeVolume) {
        Satellite.mConeVolume = mConeVolume;
    }

    public static void resetConeVolume() {
        Satellite.mConeVolume = 4.0e9f;
    }

    private static float mConeVolume = 4.0e9f;
    public ModelInstance getModel() {
        return mModel;
    }
    public ModelInstance getCone() { return mCone; }

    public float getConeRadius() {
        return getConeRadius(mOrbit.getCurrentRadius());
    }

    public void update(float delta) {

        mOrbit.updateOrbit(delta);
        SphericalCoord sc = mOrbit.getCoordinates();
        sc.setPhi(0);
        Vector3 translation = sc.toCartesian();

        mModel.transform.setToRotationRad(Vector3.Z, (float) mOrbit.getInclination());
        mModel.transform.translate(translation);
        Vector3 modelFront = new Vector3(1,0,0);
        Vector3 inwards = new Vector3(translation.cpy().nor());
        float d = modelFront.dot(inwards);
        Vector3 up = modelFront.crs(inwards).nor();
        float deg = (float)toDegrees(acos(d));
        mModel.transform.rotate(Vector3.Y, 180.0f);
        mModel.transform.rotate(Vector3.Y, up.y * deg);
        mModel.transform.scale(60, 60, 60);

        float coneRadius = getConeRadius(mOrbit.getCurrentRadius());
        mCone.materials.get(0).set(ColorAttribute.createDiffuse((coneRadius < mOrbit.getCelestial().getRadius() * 0.1f) ? Color.RED : Color.GREEN));
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
        mCone.transform.rotate(Vector3.X, coneUp.y * coneDeg);


        mCone.transform.translate(0, -distance / 2, 0);
        mCone.transform.scale(coneRadius * 3, distance, coneRadius * 3);

        mAxisControl.transform.setToScaling(800f, 800f, 800f);
        mAxisControl.transform.rotateRad(Vector3.Z, (float) mOrbit.getInclination());
        mAxisControl.transform.rotateRad(Vector3.Y, (float) mOrbit.getOrbitOffset());
        mAxisControl.transform.translate(((float) (-mOrbit.getApoapsis() / 10000.0f) - 500f) / 800f, 0, 0);
        mAxisControl.transform.rotate(Vector3.Y, -90.0f);

        mInclinationControl.transform.setToScaling(800f, 800f, 800f);
        mInclinationControl.transform.rotateRad(Vector3.Z, (float) mOrbit.getInclination());
        mInclinationControl.transform.rotateRad(Vector3.Y, (float) mOrbit.getOrbitOffset());
        mInclinationControl.transform.rotate(Vector3.X, -90.0f);
        mInclinationControl.transform.translate((float) ((mOrbit.getPeriapsis() / 10000.0f) + 500f) / 800f, 0, 0);

        mEccentricityControl.transform.setToRotationRad(Vector3.Z, (float) mOrbit.getInclination());
        mEccentricityControl.transform.translate(0, 0, (float) (mOrbit.getRadius(MathUtils.PI2 / 4) / 10000.0f) + 500f);

        mDeleteControl.transform.setToScaling(5f, 5f, 5f);
        mDeleteControl.transform.rotateRad(Vector3.Z, (float) mOrbit.getInclination());
        mDeleteControl.transform.rotate(Vector3.Y, 10.0f);
        mDeleteControl.transform.translate(0, 0, (float) ((mOrbit.getRadius(MathUtils.PI2 / 4) / 10000.0f) + 500f) / 5f);


    }

    private float getConeRadius(double height) {
        double radi = Math.sqrt((3 * mConeVolume) / (height * Math.PI));
        return (float)((radi < mOrbit.getCelestial().getRadius()*0.3f) ? radi : mOrbit.getCelestial().getRadius()*0.3f);
    }

    public Satellite(Orbit orbit) {
        mOrbit = orbit;
        mModel = new ModelInstance(Assets.getSatelliteModel());

        Material conMat = new Material(ColorAttribute.createDiffuse(Color.GREEN));
        conMat.set(new BlendingAttribute(0.2f));
        mCone = new ModelInstance(new ModelBuilder()
                .createCone(1,
                        (float) 1,
                        1, 16,
                        conMat,
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));



        mControls = new Array<>();

        mAxisControl = new ModelInstance(Assets.getArrowModel());
        mControls.add(mAxisControl);

        mInclinationControl = new ModelInstance(Assets.getArrowModel());
        mControls.add(mInclinationControl);

        mEccentricityControl = new ModelInstance(Assets.getSphereModel());
        mControls.add(mEccentricityControl);

        mDeleteControl = new ModelInstance(Assets.getCrossModel());
        mControls.add(mDeleteControl);

    }


    public Array<ModelInstance> getControls() {
        return mControls;
    }

    public void setActiveControl(ModelInstance m) {
        mActiveControl = m;
    }

    public ModelInstance getActiveControl() {
        return mActiveControl;
    }

    public ModelInstance getAxisControl () { return mAxisControl;}
    public ModelInstance getInclinationControl() { return mInclinationControl;}

    public ModelInstance getEccentricityControl() {
        return mEccentricityControl;
    }

    public ModelInstance getDeleteControl() {
        return mDeleteControl;
    }
}
