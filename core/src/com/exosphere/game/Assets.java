package com.exosphere.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.exosphere.EnvironmentCubemap;
import sun.security.provider.certpath.Vertex;

/**
 * exosphere - Assets
 * Created by tistatos on 9/4/15.
 */
public class Assets {

    static ModelLoader mModelLoader;

    static Texture mMenuTexture;
    static Texture mEarthTexture;
    static TextureRegion mLogo;
    static TextureRegion mStart;
    static TextureRegion mExit;
    static Model mSatelliteModel;
    static Model mEarthModel;
    private static AssetManager assets;
    static EnvironmentCubemap mSkyBox;

    public static Model getEarthModel() {
        return mEarthModel;
    }

    public static Model getSatelliteModel() {
        return mSatelliteModel;
    }

    public static TextureRegion getLogo() {
        return mLogo;
    }
    public static TextureRegion getStart() {
        return mStart;
    }
    public static TextureRegion getExit() {
        return mExit;
    }
    public static Texture getMenuTexture() {
        return mMenuTexture;
    }
    public static EnvironmentCubemap getCubeMap() {return mSkyBox;}
    public static void load() {
        mMenuTexture = loadTexture("menu/menuTexture.png");
        mEarthTexture = loadTexture("earth.jpg");
        mLogo = new TextureRegion(mMenuTexture, 0, 0, 1024, 300);
        mStart = new TextureRegion(mMenuTexture, 0, 312, 320, 82);
        mExit = new TextureRegion(mMenuTexture, 0, 399, 216, 82);

        mSkyBox = new EnvironmentCubemap( Gdx.files.internal("sky/exo_left.png"),
                               Gdx.files.internal("sky/exo_right.png"),
                               Gdx.files.internal("sky/exo_up.png"),
                               Gdx.files.internal("sky/exo_down.png"),
                               Gdx.files.internal("sky/exo_front.png"),
                                Gdx.files.internal("sky/exo_back.png"));
        assets = new AssetManager();
        assets.load("satelite.g3db", Model.class);
        assets.update();
        assets.finishLoading();
        mSatelliteModel = assets.get("satelite.g3db", Model.class);
//        mSatelliteModel = loadModel("box.obj");
//        mSatelliteModel.materials.add(new Material(ColorAttribute.createDiffuse(Color.RED)));
        mEarthModel = new ModelBuilder().createSphere(6371f, 6371f, 6371f, 36, 36, new Material(TextureAttribute.createDiffuse(mEarthTexture)),//ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
    }

    //TODO: load OBJ-file method
    public static Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
    }
    public static Model loadModel (String file) {return mModelLoader.loadModel(Gdx.files.internal(file));}

    public static Model getCube(Color c) {
        return new  ModelBuilder().createBox(6000f, 6000f, 6000f, new Material(ColorAttribute.createDiffuse(c)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    }
}