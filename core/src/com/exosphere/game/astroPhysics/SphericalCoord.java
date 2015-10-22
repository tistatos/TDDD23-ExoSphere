package com.exosphere.game.astroPhysics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import static com.badlogic.gdx.math.MathUtils.atan2;
import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;
import static java.lang.Math.max;
import static java.lang.Math.sqrt;
import static java.lang.StrictMath.asin;

/**
 * exosphere - SphericalCoord
 * Created by tistatos on 9/14/15.
 */
public class SphericalCoord {

    double mRadius;
    double mTheta;
    double mPhi;

    public SphericalCoord(Vector3 v) {
        mRadius = sqrt(v.x*v.x+v.y*v.y+v.z*v.z);
        mTheta = atan2(v.z, v.x);
        mTheta = (mTheta > 0 ? mTheta : (2*MathUtils.PI + mTheta));
        mPhi = asin(v.y / mRadius);
    }

    public SphericalCoord(double r, double theta, double phi) {
        mRadius = r;
        mTheta = (theta < MathUtils.PI2) ? theta : (theta-MathUtils.PI2) ;
        mPhi = phi;
    }

    public SphericalCoord(SphericalCoord sc) {
        this(sc.getRadius(), sc.getTheta(), sc.getPhi());
    }

    public void setRadius(double radius) {
        mRadius = max(0,radius);
    }

    public void setTheta(double theta) {
        this.mTheta = (theta < MathUtils.PI2) ? theta : (theta-MathUtils.PI2);
    }

    public void setPhi(float phi) {
        this.mPhi = phi; /*% MathUtils.PI2;*/
    }

    public double getRadius() {
        return mRadius;
    }

    public double getTheta() {
        return mTheta;
    }

    public double getPhi() {
        return mPhi;
    }

    public Vector3 toCartesian() {
        float x = (float)(mRadius*cos((float)mTheta)*cos((float)mPhi));
        float y = (float)(mRadius*sin((float)mPhi));
        float z = (float)(mRadius*sin(-(float)mTheta));

        return new Vector3(x, y, z);
    }



}
