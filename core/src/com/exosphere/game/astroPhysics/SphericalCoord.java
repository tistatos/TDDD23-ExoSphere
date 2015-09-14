package com.exosphere.game.astroPhysics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import static com.badlogic.gdx.math.MathUtils.atan2;
import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;
import static java.lang.Math.acos;
import static java.lang.Math.max;
import static java.lang.Math.sqrt;

/**
 * exosphere - SphericalCoord
 * Created by tistatos on 9/14/15.
 */
public class SphericalCoord {

    float mRadius;
    float mTheta;
    float mPhi;

    public SphericalCoord(Vector3 v) {
        mRadius = (float)sqrt(v.x*v.x+v.y*v.y+v.z*v.z);
        mTheta = atan2(v.z, v.x);
        mPhi = (float)acos(v.y/mRadius);
    }

    public SphericalCoord(float r, float theta, float phi) {
        mRadius = r;
        mTheta = theta % MathUtils.PI2;
        mPhi = phi % MathUtils.PI;
    }

    public SphericalCoord(SphericalCoord sc) {
        this(sc.getRadius(), sc.getTheta(), sc.getPhi());
    }

    public void setRadius(float radius) {
        mRadius = max(0,radius);
    }

    public void setTheta(float theta) {
        this.mTheta = theta % MathUtils.PI;
    }

    public void setPhi(float phi) {
        this.mPhi = phi % MathUtils.PI2;
    }

    public float getRadius() {
        return mRadius;
    }

    public float getTheta() {
        return mTheta;
    }

    public float getPhi() {
        return mPhi;
    }

    public Vector3 toCartesian() {
        float x = mRadius*sin(mTheta)*cos(mPhi);
        float y = mRadius*sin(mPhi);
        float z = mRadius*sin(mTheta)*sin(mPhi);

        return new Vector3(x, y, z);
    }



}
