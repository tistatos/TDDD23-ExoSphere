package com.exosphere.game.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

/**
 * exosphere - PlayerCamera
 * Created by tistatos on 9/29/15.
 */
public class PlayerCamera {

    PerspectiveCamera mCamera;
    boolean mMovable;
    boolean mMoving;
    boolean mRightClicked;

    float veloX = 0.0f;
    float veloY = 0.0f;

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


    public void update(float delta) {
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && mMovable) {
            if(!mMoving) {
                veloX = 0;
                veloY = 0;
            }
            float dx = Gdx.input.getDeltaX();
            float dy = Gdx.input.getDeltaY();

            veloX += 1.5f*dx*delta;
            veloY += 1.5f*dy*delta;
            mMoving = true;
        }
        else {
            mMoving = false;
            veloX *= 0.98f;
            veloY *= 0.98f;
        }


        if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            if(!mRightClicked) {
                mRightClicked = true;
                float mouseX = Gdx.input.getX()/mCamera.viewportWidth;
                float mouseY = Gdx.input.getY()/mCamera.viewportHeight;
                Ray ray = mCamera.getPickRay(mouseX, mouseY);

                veloX = 0f;
                veloY = 0f;
                Plane p = new Plane(Vector3.Z, Vector3.Zero);
                Vector3 intersection = Vector3.Zero;
                if(Intersector.intersectRayPlane(ray, p, intersection)) {
                    System.out.println("Spawn satellite at " + intersection);
                }
            }
            else {

            }
        }
        else {
            mRightClicked = false;

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
    }
}
