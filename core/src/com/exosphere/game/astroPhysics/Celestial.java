package com.exosphere.game.astroPhysics;

import com.badlogic.gdx.math.MathUtils;
import com.exosphere.game.Settings;

import static java.lang.StrictMath.cbrt;
import static java.lang.StrictMath.pow;

/**
 * exosphere - Celestial
 * Created by tistatos on 9/14/15.
 */
public class Celestial {
    static final double SCALE = Settings.Scale;
    double mMass;
    double mRadius;
    double mRotationSpeed;
    double mCurrentRotation = 0.0;

    public Celestial(double mass,double radius, double speed) {
        mMass = mass;
        mRadius = radius;
        mRotationSpeed = speed;
    }

    public double getMass() {
        return mMass;
    }

    public void setMass(double mass) {
        this.mMass = mass;
    }

    public double getRadius() {
        return mRadius;
    }

    public void setRadius(double radius) {
        this.mRadius = radius;
    }

    public double getGravityAcc() {
        return (Constants.GRAVITY_CONSTANT * mMass)/((mRadius*SCALE)*(mRadius*SCALE));
    }

    public double getMu() {
        return Constants.GRAVITY_CONSTANT * mMass;
    }


    public double getGeostationaryOrbit() {
        return cbrt(getMu() * pow((getPeriod()/MathUtils.PI2), 2));
    }

    private double getPeriod() {
        return MathUtils.PI2/mRotationSpeed;
    }


    public double getRotationalSpeed() {
        return mRotationSpeed;
    }

    public void update(float delta) {
        mCurrentRotation += mRotationSpeed*delta* Settings.getTimeFactor();
        mCurrentRotation %= MathUtils.PI2;
    }

    public double getCurrentRotation() {
        return mCurrentRotation;
    }
}
