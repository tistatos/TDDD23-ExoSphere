package com.exosphere.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

/**
 * exosphere - Assets
 * Created by tistatos on 9/4/15.
 */
public class Assets {

    static ModelLoader mModelLoader;

    static Texture mMenuTexture;
    static Texture mEarthTexture;
    static TextureRegion mLogo;
    static Model mSatelliteModel;
    static Model mEarthModel;


    public static Model getEarthModel() {
        return mEarthModel;
    }

    public static Model getSatelliteModel() {
        return mSatelliteModel;
    }

    public static TextureRegion getLogo() {
        return mLogo;
    }
    public static Texture getMenuTexture() {
        return mMenuTexture;
    }

    public static void load() {
        mModelLoader = new ObjLoader();
        mMenuTexture = loadTexture("menu/menuTexture.png");
        mEarthTexture = loadTexture("earth.jpg");
        mLogo = new TextureRegion(mMenuTexture, 0, 0, 252, 73);


        mSatelliteModel = new ModelBuilder().createBox(600f,600f,600f, new Material(ColorAttribute.createDiffuse(Color.RED)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        mEarthModel = new ModelBuilder().createSphere(6371f, 6371f, 6371f, 36, 36, new Material(TextureAttribute.createDiffuse(mEarthTexture)),//ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
    }

    //TODO: load OBJ-file method
    public static Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
    }
    public static Model loadModel (String file) {return mModelLoader.loadModel(Gdx.files.internal(file));}
}