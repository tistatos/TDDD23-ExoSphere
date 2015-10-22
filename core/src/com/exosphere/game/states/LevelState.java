package com.exosphere.game.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.exosphere.game.astroPhysics.Celestial;
import com.exosphere.game.astroPhysics.SphericalCoord;
import com.exosphere.game.gameObjects.Earth;
import com.exosphere.game.gameObjects.Satellite;
import com.exosphere.game.gameObjects.SatelliteContainer;

import java.nio.ByteBuffer;
import java.util.Iterator;

import static java.lang.StrictMath.*;

/**
 * exosphere - LevelState
 * Describes the state of a level will notify if user has succeeded or not
 * Created by tistatos on 9/4/15.
 */
public abstract class LevelState {

    String mLevelDescription;
    public static SatelliteContainer Satellites;
    boolean requirementsMet = false;

    static Texture mCurrentSurfaceCoverage;
    static Texture mOrbits;
    static Pixmap mPixOrbit;
    static Pixmap mPixCoverage;
    public static Celestial Celestial;
    public static int mCoveragePercentage = 0;

    private static Pixmap mProjectedCircle;

    public LevelState(String levelDescription) {
        mCurrentSurfaceCoverage = new Texture(256,256, Pixmap.Format.RGBA8888);
        mOrbits = new Texture(256,256, Pixmap.Format.RGBA8888);

        mPixOrbit = new Pixmap(256,256, Pixmap.Format.RGBA8888);
        mPixCoverage = new Pixmap(256,256, Pixmap.Format.RGBA8888);
        mProjectedCircle = new Pixmap(128,128, Pixmap.Format.RGBA8888);

        makeCircleGreen();

        mLevelDescription = levelDescription;
    }

    public String getLevelDescription() {
        return mLevelDescription;
    }

    public static void update() {
        updateCoverage();
        updateOrbits();
    }

    private static void updateOrbits() {
        Iterator<Satellite> it = Satellites.iterator();
        mPixOrbit.setColor(Color.argb8888(0, 0, 0, 0));
        mPixOrbit.fill();
        while(it.hasNext()) {
            Satellite s = it.next();
            Array<Vector3> pts= s.getOrbit().getOrbitCoords(s.getOrbit().getCurrentAnomaly(), MathUtils.PI2);
            for(int i = 0; i < pts.size-1; i++) {
                Vector3 pt = pts.get(i);
                SphericalCoord sc1 = new SphericalCoord(pt.x, pt.y, pt.z);
                pt = pts.get(i + 1);
                SphericalCoord sc2 = new SphericalCoord(pt.x, pt.y, pt.z);

                double celestial1 = Celestial.getCurrentRotation();
                double celestial2 = Celestial.getCurrentRotation();

                int startX = (int)(256*((sc1.getTheta()-celestial1)/(MathUtils.PI2)));
                int stopX = (int)(256*((sc2.getTheta()-celestial2)/(MathUtils.PI2)));

                int startY = (int)(256*(.5+log(tan(-sc1.getPhi())+(1/cos(sc1.getPhi())))/MathUtils.PI2));
                int stopY = (int)(256*(.5+log(tan(-sc2.getPhi())+(1/cos(sc2.getPhi())))/MathUtils.PI2));

                startX= (startX >= 0) ? startX : 256+startX;
                stopX = (stopX >= 0) ? stopX : 256+stopX;

                if(abs(startX-stopX) > 20)
                    continue;

                mPixOrbit.setColor(Color.WHITE);
                mPixOrbit.drawLine(startX, startY, stopX, stopY);
            }
            SphericalCoord sc = s.getOrbit().getCoordinates();
            int x = (int)(256*(sc.getTheta()-Celestial.getCurrentRotation())/(MathUtils.PI2));
            int y = (int)(256*(.5+log(tan(-sc.getPhi())+(1/cos(sc.getPhi())))/MathUtils.PI2));


            x = (x >= 0) ? x : 256+x;

            mPixOrbit.setColor(Color.GREEN);

            mPixOrbit.fillRectangle(x - 5, y - 5, 10, 10);
        }
        mOrbits.draw(mPixOrbit, 0, 0);
    }




    private static void updateCoverage() {
        Iterator<Satellite> it = Satellites.iterator();
        mPixCoverage.setColor(1, 0, 0, 1.0f);
        mPixCoverage.fill();
        while(it.hasNext()) {
            Satellite s = it.next();
            float radius = s.getConeRadius();
            SphericalCoord sc = s.getOrbit().getCoordinates();
            int x = (int)(256*(sc.getTheta()-Celestial.getCurrentRotation())/(Math.PI*2));
            int y = (int)(256*(.5+log(tan(-sc.getPhi())+(1/cos(sc.getPhi())))/MathUtils.PI2));

            x = (x >= 0) ? x : 256+x;
            float coverage = (float)((radius/(Earth.mEarthRadius*0.1f)));

            if(radius > (Earth.mEarthRadius*0.1f)) {
                coverage = 1;
            }

            mPixCoverage.drawPixmap(mProjectedCircle, 0, 0, 128, 128, x - (int) (64 * coverage), y - (int) (128 * coverage), (int) (128 * coverage), (int) (256 * coverage));
            if(x+coverage*64 > 64) {
                mPixCoverage.drawPixmap(mProjectedCircle, 0, 0, 128, 128, x-256-(int)(64*coverage), y - (int)(128 * coverage), (int)(128*coverage), (int)(256 * coverage));
            }
            if(x-coverage*64 < 0) {
                mPixCoverage.drawPixmap(mProjectedCircle, 0, 0, 128, 128, x+256-(int)(64*coverage), y - (int)(128 * coverage), (int)(128*coverage), (int)(256* coverage));

            }
        }

        mPixCoverage.setBlending(Pixmap.Blending.None);
        for (int x = 0; x < 256; x++) {
            for (int y = 0; y < 256; y++) {
                if(mPixCoverage.getPixel(x,y) == Color.RED.toIntBits()) {
                    mPixCoverage.setColor(0, 0, 0, 0.5f);
                    mPixCoverage.drawPixel(x, y);
                }
                else {
                    mPixCoverage.setColor(0,0,0,0.0f);
                    mPixCoverage.drawPixel(x, y);
                }
            }
        }
        mPixCoverage.setBlending(Pixmap.Blending.SourceOver);

        ByteBuffer pixels = mPixCoverage.getPixels();
        float sum = 0;
        for(int i = 3; i < 256*256*4; i += 4) {
            sum += (float)pixels.get(i)/127;
        }
        sum = (sum*100)/(256*256);
        mCoveragePercentage = (int)(100-sum);

        mCurrentSurfaceCoverage.draw(mPixCoverage, 0, 0);
    }

    public static Texture getCurrentCoverage() {
        return mCurrentSurfaceCoverage;
    }
    public static Texture getOrbitMap() {
        return mOrbits;
    }

    public boolean levelRequirementsMet() {
        if(levelRequirements())
            requirementsMet = true;
        return requirementsMet;
    }

    private static void makeCircleGreen() {
        mProjectedCircle.setColor(0, 1, 0, 0.0f);
        mProjectedCircle.fill();
        mProjectedCircle.setColor(0, 1, 0, 1.0f);
        mProjectedCircle.fillCircle(64, 64, 64);
    }

    public abstract boolean levelRequirements();
}
