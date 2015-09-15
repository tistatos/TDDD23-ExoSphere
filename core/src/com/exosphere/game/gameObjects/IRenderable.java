package com.exosphere.game.gameObjects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * exosphere - IModel
 * Created by tistatos on 9/14/15.
 */
public interface IRenderable {
    ModelInstance getModel();

    void update(float delta);
}
