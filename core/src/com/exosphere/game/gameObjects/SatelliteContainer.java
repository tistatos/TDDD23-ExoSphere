package com.exosphere.game.gameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.exosphere.game.astroPhysics.Orbit;

import java.util.Iterator;

/**
 * exosphere - SatelliteContainer
 * Created by tistatos on 10/12/15.
 */
public class SatelliteContainer implements RenderableProvider, Iterable<Satellite>{

    Array<Satellite> mSatellites;
    Color paleGreen = new Color(0,0.8f,0,1f);


    public Satellite getSelectedSatellite() {
        return mSelectedSatellite;
    }

    public void setSelectedSatellite(Satellite mSelectedSatellite) {
        this.mSelectedSatellite = mSelectedSatellite;
    }

    Satellite mSelectedSatellite = null;
    public SatelliteContainer() {
        this.mSatellites = new Array<>();
    }

    public void add(Satellite s) {
        mSatellites.add(s);
    }

    public void update(float delta) {
        for(Satellite s : mSatellites) {
            s.update(delta);
            s.getModel().materials.get(0).set(ColorAttribute.createDiffuse(Color.GRAY));
        }
        if(mSelectedSatellite != null)
            mSelectedSatellite.getModel().materials.get(0).set(ColorAttribute.createDiffuse(Color.GREEN));
    }

    public Satellite selelectSatellite(Ray r) {
        for(Satellite s : mSatellites) {
            Vector3 intersection = new Vector3();
            BoundingBox bb = new BoundingBox();
            s.getModel().extendBoundingBox(bb);
            bb.mul(s.getModel().transform);
            if(Intersector.intersectRayBounds(r, bb, intersection)) {
                mSelectedSatellite = s;
                return s;
            }
        }
        return null;
    }

    public boolean ControlIntersect(Ray r) {
        for(ModelInstance ctrl : mSelectedSatellite.getControls()) {
            Vector3 intersection = new Vector3();
            BoundingBox bb = new BoundingBox();
            ctrl.extendBoundingBox(bb);
            bb.mul(ctrl.transform);
            if(Intersector.intersectRayBounds(r, bb, intersection)) {
                ctrl.materials.get(0).set(ColorAttribute.createDiffuse(Color.YELLOW));
                if(ctrl.materials.size > 1) ctrl.materials.get(1).set(ColorAttribute.createDiffuse(Color.YELLOW));
                mSelectedSatellite.setActiveControl(ctrl);
                return true;
            }
            ctrl.materials.get(0).set(ColorAttribute.createDiffuse(paleGreen));
            if(ctrl == mSelectedSatellite.getDeleteControl())
                ctrl.materials.get(0).set(ColorAttribute.createDiffuse(Color.RED));

            if(ctrl.materials.size > 1)  ctrl.materials.get(1).set(ColorAttribute.createDiffuse(paleGreen));

        }
        return false;
    }

    public void drawOrbits(ShapeRenderer sr) {
        for(Satellite s : mSatellites) {

            Orbit orbit = s.getOrbit();
            Array<Vector3> pts = orbit.getOrbitCoords();
            if(s == mSelectedSatellite)
                sr.setColor(paleGreen);
            for(int i = 0; i < pts.size; i++) {
                Vector3 first = pts.get(i);
                Vector3 second = pts.get((i + 1) % pts.size);
                sr.identity();
                sr.rotate(0, 0, 1, (float) (orbit.getInclination() * 180 / Math.PI));
                sr.rotate(1, 0, 0, 90.0f);
                sr.rectLine(first.x, first.z, second.x, second.z, 50.0f);
            }
            sr.identity();
            sr.rotate(0, 0, 1, (float) (orbit.getInclination() * 180 / Math.PI));
            sr.rotate(0, 1, 0, (float) (orbit.getCurrentAnomaly() * 180 / Math.PI));
            Vector2 pos = new Vector2((float)orbit.getCurrentRadius(),0);
            sr.rectLine(pos, new Vector2(0, 0), 50.0f);

            sr.setColor(Color.WHITE);

        }


    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        for(Satellite s : mSatellites) {
            s.getModel().getRenderables(renderables, pool);
            s.getCone().getRenderables(renderables, pool);
            if(s == mSelectedSatellite)
                for(ModelInstance control : s.getControls()) {
                    control.getRenderables(renderables, pool);
                }
        }
    }

    public int numberOfSatellites() {
        return mSatellites.size;
    }

    public void remove(Satellite s) {
        mSatellites.removeValue(s, true);
    }

    public Satellite get(int index) {
        return mSatellites.get(index);
    }

    @Override
    public Iterator<Satellite> iterator() {
        return new Iterator<Satellite>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return (index < mSatellites.size);
            }

            @Override
            public Satellite next() {
                Satellite s = mSatellites.get(index);
                index++;
                return s;
            }

            @Override
            public void remove() {
            }
        };
    }
}
