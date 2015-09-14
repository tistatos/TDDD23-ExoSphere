package com.exosphere.game.astroPhysics;

import static java.lang.Math.sqrt;

/**
 * exosphere - Orbit
 * Created by tistatos on 9/14/15.
 */
public class Orbit {
    double mSemiMajorAxis; //a
    double mInclination;
    double mEccentricity; //e
    Celestial mOrbitedBody;

    public Orbit(double semiMajorAxis, double inclination, Celestial mOrbitedBody) {
        mSemiMajorAxis = semiMajorAxis;
        mInclination = inclination;
    }

    public Orbit(double semiMajorAxis, double inclination, double eccentricity, Celestial mOrbitedBody) {
        this(semiMajorAxis, inclination, mOrbitedBody);
        this.mEccentricity = eccentricity;
    }

    //FIXME: only valid for ciruclar motion i.e. e=0. the velocity is not constant for
    public double orbitVelocity() {
        return sqrt(mOrbitedBody.getMu()/mSemiMajorAxis);
    }

}
