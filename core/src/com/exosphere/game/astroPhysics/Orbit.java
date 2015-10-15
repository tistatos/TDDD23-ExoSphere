package com.exosphere.game.astroPhysics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.exosphere.game.Settings;


import static java.lang.Math.*;

/**
 * exosphere - Orbit
 * Created by tistatos on 9/14/15.
 */
public class Orbit {
    double mSemiMajorAxis; //a
    protected double mInclination;
    double mEccentricity = 0; //e
    protected Celestial mOrbitedBody;

    double mCurrentDistance;
    double mCurrentAngle = 0.0f;

    double mOrbitOffset = 0.0f;
    double mTrueAnomaly = 0.0f;


    public Orbit(double semiMajorAxis, double inclination, Celestial orbitedBody) {
        mSemiMajorAxis = semiMajorAxis*1000;
        mInclination = inclination;
        mCurrentDistance = mSemiMajorAxis;
        mOrbitedBody = orbitedBody;
    }

    public Orbit(double semiMajorAxis, double inclination, double eccentricity, Celestial mOrbitedBody) {
        this(semiMajorAxis, inclination, mOrbitedBody);
        this.mEccentricity = eccentricity;
    }

    public Orbit(double semiMajorAxis, double inclination, double eccentricity, double orbitOffset, Celestial mOrbitedBody) {
        this(semiMajorAxis, inclination, mOrbitedBody);
        this.mOrbitOffset = orbitOffset;
        this.mEccentricity = eccentricity;
    }

    public Orbit(double semiMajorAxis, double inclination, double eccentricity, double startingAnomaly, double orbitOffset, Celestial mOrbitedBody) {
        this(semiMajorAxis, inclination, eccentricity, orbitOffset, mOrbitedBody);
        mTrueAnomaly = startingAnomaly;
    }
        /********************************************
         * 4.38 AND 4.39 ARE ONLY FOR CIRCULAR ORBIT
        *********************************************/
    //4.38
    private double meanAnomaly(double time) {
        double n = meanMotion();
        n = getVehicleVelocity()/mCurrentDistance;
        return n*time; // M
    }

    //4.39 mean motion: n
    private double meanMotion() {
        return sqrt(mOrbitedBody.getMu() / (pow(mSemiMajorAxis, 3)));
    }


    //4.40 eccentricic anomaly E given M
    private double eccentricAnomaly(double M) {
        double e = mEccentricity;
        return 0.0;
    }

    //4.41
//    private double MeanAnomaly() {
//        double E = eccentricAnomaly();
//        double e = mEccentricity;
//        return E-e*sin(E);
//    }

    //4.42
    private double calcTrueAnomaly(double M) {
        double e = mEccentricity;

        return M+2*e*sin(M)+1.25*e*e*sin(2*M); //Low accuracy
        //High accuracy need implementation of newton kepler via eccentricAnomaly
    }

    //4.43
    private double getRadius(double v) {
        double a = mSemiMajorAxis;
        double e = mEccentricity;
        return (a*(1-e*e)) / (1+e*cos(v));
    }

    //4.44
    private double getFlightPathAngle(double v) {
        double e = mEccentricity;
        return atan(e*sin(v)/(1+e*cos(v)));
    }

    public void updateOrbit(double delta) {
        double M = meanAnomaly(delta * Settings.getTimeFactor());
        mTrueAnomaly += calcTrueAnomaly(M);

        mCurrentDistance = getRadius(mTrueAnomaly);
        mCurrentAngle = getFlightPathAngle(mTrueAnomaly);
    }


    //4.45
    public double getVehicleVelocity() {
        return sqrt(mOrbitedBody.getMu()*(2/mCurrentDistance - 1/mSemiMajorAxis));
    }


    private double getInclination(double anomaly) {
        return getCurrentRadius()*sin(mInclination)*cos(anomaly+mOrbitOffset);
    }

    public double getCurrentAnomaly() {
        return mTrueAnomaly+mOrbitOffset;
    }

    public double getSemiMajorAxis() {
        return mSemiMajorAxis;
    }
    public void setSemiMajorAxis(double axis) {
        mSemiMajorAxis = axis*1000;
    }
    public double getInclination() {
        return mInclination;
    }

    public void setInclination(double v) {
        mInclination = v;
    }
    public double getCurrentRadius() {
        return mCurrentDistance/1000.0f;
    }

    public void addToAnomaly(double addition) {
        mTrueAnomaly += addition;
    }

    public Array<Vector3> getPointsOrbit() {
        Array<Vector3> points = new Array<>();
        int numberOfPoints = 128;
        double currentAngle = 0;
        for(int i = 0; i < numberOfPoints; i++) {
            double distance = getRadius(currentAngle);
            double anomaly = currentAngle+mOrbitOffset;
            distance /= 1000;

            float x = (float)(distance*cos(anomaly));
            float y = 0;
            float z = (float)(distance*sin(-anomaly));
            Vector3 point = new Vector3(x,y,z);
            point.rotate(Vector3.Z, (float)mInclination);

            points.add(point);
            currentAngle += 2*PI/numberOfPoints;
        }

        return points;
    }

    public SphericalCoord getCoordinates() {
        return new SphericalCoord((float)getCurrentRadius(), (float)getCurrentAnomaly(), 0);
    }

    public Celestial getCelestial() {
        return mOrbitedBody;
    }
}
