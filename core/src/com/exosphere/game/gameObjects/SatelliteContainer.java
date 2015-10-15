package com.exosphere.game.gameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.exosphere.game.astroPhysics.Orbit;

/**
 * exosphere - SatelliteContainer
 * Created by tistatos on 10/12/15.
 */
public class SatelliteContainer implements RenderableProvider{

    Array<Satellite> mSatellites;

    public Satellite getSelectedSatellite() {
        return mSelectedSatellite;
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
        }
    }

    public Satellite selelectSatellite(Ray r) {
        for(Satellite s : mSatellites) {
            Vector3 intersection = new Vector3();
            BoundingBox bb = new BoundingBox();
            s.getModel().extendBoundingBox(bb);
            bb.mul(s.getModel().transform);
            if(Intersector.intersectRayBounds(r, bb, intersection)) {
                System.out.println("intersect!");
                mSelectedSatellite = s;
                return s;
            }
        }
        return null;
    }

    public void drawOrbits(ShapeRenderer sr) {
        for(Satellite s : mSatellites) {

            Orbit orbit = s.getOrbit();
            Array<Vector3> pts = orbit.getPointsOrbit();
            if(s == mSelectedSatellite)
                sr.setColor(Color.GREEN);
            for(int i = 0; i < pts.size; i++) {
                Vector3 first = pts.get(i);
                Vector3 second = pts.get((i + 1) % pts.size);
                sr.identity();
                sr.rotate(0, 0, 1, (float) (orbit.getInclination() * 180 / Math.PI));
                sr.rotate(1, 0, 0, 90.0f);
                sr.rectLine(first.x, first.z, second.x, second.z, 500.0f);
            }
            sr.setColor(Color.WHITE);
        }
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        for(Satellite s : mSatellites) {
            s.getModel().getRenderables(renderables,pool);
            s.getCone().getRenderables(renderables, pool);
        }
    }
}
