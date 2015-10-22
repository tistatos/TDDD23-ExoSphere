package com.exosphere.game.astroPhysics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.exosphere.game.Settings;


import static com.badlogic.gdx.math.MathUtils.clamp;
import static java.lang.Math.*;

/**
 * exosphere - Orbit
 * Created by tistatos on 9/14/15.
 */
public class Orbit {
    private static final double SCALE = Settings.Scale;
    double mSemiMajorAxis; //a
    protected double mInclination;
    double mEccentricity = 0; //e
    protected Celestial mOrbitedBody;

    double mCurrentDistance;
    double mCurrentAngle = 0.0f;

    public double getOrbitOffset() {
        return mOrbitOffset;
    }
    public void setOrbitOffset(double offset) {
        mOrbitOffset = offset;
    }

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
        n = getCurrentVelocity()/mCurrentDistance;
        return n*time; // M
    }

    //4.39 mean motion: n
    public double meanMotion() {
        return sqrt(mOrbitedBody.getMu() / (pow(mSemiMajorAxis, 3)));
    }


    //4.40 eccentricic anomaly E given M
    private double eccentricAnomaly(double v) {
        double e = mEccentricity;
        double E = acos((e+cos(v))/(1+e*cos(v)));
        return E;
    }

    //4.41
    public double getMeanAnomaly(double v) {
        double E = eccentricAnomaly(v);
        double e = mEccentricity;
        return E-e*sin(E);
    }

    //4.42
    private double calcTrueAnomaly(double M) {
        double e = mEccentricity;

        return M+2*e*sin(M)+1.25*e*e*sin(2*M); //Low accuracy
        //High accuracy need implementation of newton raphs via eccentricAnomaly
    }

    //4.43
    public double getRadius(double v) {
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
        mTrueAnomaly %= MathUtils.PI2;
        mCurrentDistance = getRadius(mTrueAnomaly);
        mCurrentAngle = getFlightPathAngle(mTrueAnomaly);
    }


    //4.45
    public double getCurrentVelocity() {
        return sqrt(mOrbitedBody.getMu()*(2/mCurrentDistance - 1/mSemiMajorAxis));
    }

    public double getCurrentAngularVelocity() {
        return getCurrentVelocity()/mCurrentDistance;
    }


    private double getInclination(double anomaly) {
        return getCurrentRadius()*sin(mInclination)*cos(anomaly+mOrbitOffset);
    }

    public double getCurrentAnomaly() {
        return (mTrueAnomaly+mOrbitOffset)% MathUtils.PI2;
    }

    public double getSemiMajorAxis() {
        return mSemiMajorAxis/SCALE;
    }
    public void setSemiMajorAxis(double axis) {
        mSemiMajorAxis = axis*SCALE;
    }
    public double getInclination() {
        return mInclination;
    }

    public double getEccentricity() { return mEccentricity;}
    public void setEccentricity(double e) { mEccentricity = clamp(e,0,0.9); }


    public void setInclination(double v) {
        mInclination = clamp(v,-Math.PI/2,Math.PI/2);
    }
    public double getCurrentRadius() {
        return mCurrentDistance/SCALE;
    }

    public void addToAnomaly(double addition) {
        mTrueAnomaly += addition;
    }

    public Array<Vector3> getOrbitCoords() {
        Array<Vector3> points = new Array<>();
        int numberOfPoints = 128;
        double currentAngle = 2*PI/numberOfPoints;
        for(int i = 0; i < numberOfPoints; i++) {
            double distance = getRadius(currentAngle);
            double anomaly = currentAngle-mOrbitOffset;
            distance /= SCALE;

            float x = (float)(distance*cos(anomaly));
            float y = (float)(distance*mInclination*cos(anomaly));
            float z = (float)(distance*sin(anomaly));
            Vector3 point = new Vector3(x,y,z);

            points.add(point);
            currentAngle += 2*PI/numberOfPoints;
        }

        return points;
    }

    public Array<Vector3> getOrbitCoords(double currentAnomaly, double length) {
        Array<Vector3> points = new Array<>();
        int numberOfPoints = 128;
        double currentAngle = currentAnomaly;
        for(int i = 0; i < numberOfPoints; i++) {
            double anomaly = currentAngle%MathUtils.PI2;
            double distance = getRadius(anomaly);
            distance /= SCALE;


            float r = (float)(distance);
            float theta = (float)(anomaly);
            float phi = (float)(mInclination*cos(anomaly));
            Vector3 point = new Vector3(r,theta,phi);

            points.add(point);
            currentAngle += length/numberOfPoints;
        }

        return points;



    }

    public SphericalCoord getCoordinates() {
        return new SphericalCoord(getCurrentRadius(), getCurrentAnomaly(), (mInclination*cos(getCurrentAnomaly())));
    }

    public Double getApoapsis() {
        return mSemiMajorAxis*(1+mEccentricity);
    }

    public Double getPeriapsis() {
        return mSemiMajorAxis*(1-mEccentricity);
    }

    public Celestial getCelestial() {
        return mOrbitedBody;
    }
}
