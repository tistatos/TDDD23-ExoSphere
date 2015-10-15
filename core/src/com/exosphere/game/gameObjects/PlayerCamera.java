package com.exosphere.game.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import org.apache.velocity.runtime.VelocimacroFactory;

import static java.lang.StrictMath.abs;

/**
 * exosphere - PlayerCamera
 * Created by tistatos on 9/29/15.
 */
public class PlayerCamera {

    PerspectiveCamera mCamera;
    boolean mMovable;
    boolean mMoving;

    float veloX = 0.0f;
    float veloY = 0.0f;
    float moveSpeed = 1.0f;
    public PerspectiveCamera getCamera() {
        return mCamera;
    }

    public PlayerCamera(PerspectiveCamera camera, boolean movable) {
        this.mCamera = camera;
        mMovable = movable;
    }

    public PlayerCamera(PerspectiveCamera camera) {
        this(camera, true);
    }


    public Vector3 getPlaneIntersectionPoint() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();
        return getPlaneIntersectionPoint(mouseX, mouseY);
    }

    public Vector3 getPlaneIntersectionPoint(float x, float y) {
        Plane p = new Plane(Vector3.Y, Vector3.Zero);
        return getPlaneIntersectionPoint(p, x, y);
    }

    public Vector3 getPlaneIntersectionPoint(Plane p, float x, float y) {
        Ray ray = mCamera.getPickRay(x, y);

        Vector3 result = new Vector3(0,0,0);
        Intersector.intersectRayPlane(ray, p, result);
        return result;
    }


    public void update(float delta) {
        boolean leftMouse = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        boolean rightMouse = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
        if(leftMouse && mMovable && !rightMouse) {
            if(!mMoving) {
                veloX = 0;
                veloY = 0;
            }
            float dx = Gdx.input.getDeltaX();
            float dy = Gdx.input.getDeltaY();

            veloX += moveSpeed*dx*delta;
            veloY += moveSpeed*dy*delta;

            mMoving = true;
        }

        if(rightMouse) {
            veloX = 0;
            veloY = 0;
        }

        mCamera.rotateAround(Vector3.Zero, Vector3.Y, -veloX);

        Vector3 leftVector = new Vector3(mCamera.position).crs(Vector3.Y).nor();
        float dotProd = new Vector3(mCamera.position).nor().dot(Vector3.Y);
        if(Math.abs(dotProd) < 0.9) {
            mCamera.rotateAround(Vector3.Zero, leftVector, veloY);
        }
        else {
            double currentAngle = Math.acos(dotProd);
            double limitedAngle = Math.acos(Math.signum(dotProd)*0.9f);

            float angle = (float)Math.abs(currentAngle - limitedAngle);
            angle = (float)(Math.signum(dotProd) * Math.toDegrees(angle));

            mCamera.rotateAround(Vector3.Zero, leftVector, -angle);
            veloY = 0.0f;
        }
        mCamera.lookAt(0, 0, 0);
        mCamera.update();

        veloX *= 0.98f;
        veloY *= 0.98f;


        if(abs(veloX) < 0.2f)
            veloX = 0;
        if(abs(veloY) < 0.2f)
            veloY = 0;
    }

    public Ray getPickingRay() {
        float x = Gdx.input.getX();
        float y = Gdx.input.getY();
        return mCamera.getPickRay(x, y);

    }
}
