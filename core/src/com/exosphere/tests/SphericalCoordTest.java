package com.exosphere.tests;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.exosphere.game.astroPhysics.SphericalCoord;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * exosphere - SphericalCoordTest
 * Created by tistatos on 9/14/15.
 */
public class SphericalCoordTest {

    @Test
    public void testSetRadius() throws Exception {
        SphericalCoord sc = new SphericalCoord(0,0,0);
        sc.setRadius(10);
        assertEquals(10, sc.getRadius(), 0.01);
    }

    @Test
    public void testSetTheta() throws Exception {
        SphericalCoord sc = new SphericalCoord(0,0,0);
        sc.setTheta(10);
        assertEquals(10, sc.getTheta(), 0.01);
    }

    @Test
    public void testSetPhi() throws Exception {
        SphericalCoord sc = new SphericalCoord(0,0,0);
        sc.setPhi(10);
        assertEquals(10, sc.getPhi(), 0.01);
    }

    @Test
    public void testToCartesian() throws Exception {
        SphericalCoord sc = new SphericalCoord(10, 0,MathUtils.PI/2);
        Vector3 res = sc.toCartesian();
        assertEquals(10, res.x, 0.01);
        assertEquals(0, res.y, 0.01);
        assertEquals(0, res.z, 0.01);


        sc.setRadius(12);
        res = sc.toCartesian();
        assertEquals(12, res.x, 0.01);
        assertEquals(0, res.y, 0.01);
        assertEquals(0, res.z, 0.01);
    }
}