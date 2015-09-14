package com.exosphere.game.astroPhysics;

/**
 * exosphere - Celestial
 * Created by tistatos on 9/14/15.
 */
public class Celestial {
    double mMass;
    double mRadius;

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

    public Celestial(double mass,double radius) {
        mMass = mass;
        mRadius = radius;
    }

    public double getGravityAcc() {
        return (Constants.GRAVITY_CONSTANT * mMass)/(mRadius*mRadius);
    }

    public double getMu() {
        return Constants.GRAVITY_CONSTANT * mMass;
    }


}
