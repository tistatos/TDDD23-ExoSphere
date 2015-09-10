package com.exosphere.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * exosphere - Assets
 * Created by tistatos on 9/4/15.
 */
public class Assets {

    public static Texture mMenuTexture;
    public static TextureRegion logo;

    public static boolean load() {

        mMenuTexture = loadTexture("menu/menuTexture.png");
        logo = new TextureRegion(mMenuTexture, 0, 0, 252, 73);
        return true;
    }

    //TODO: load OBJ-file method
    public static Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
    }
}