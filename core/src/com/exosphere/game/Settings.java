package com.exosphere.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * exosphere - Settings
 * holds settings for the game
 * Created by tistatos on 9/4/15.
 */
public class Settings {
    public static int msScreenSize[] = {800, 400};
    public static boolean msFullscreen = false;
    public static boolean msEnableSound = false;
    public static String msSettingsFile = "exosphere.settings";

    public static void load() {
        try {
            FileHandle settingsFile = Gdx.files.external(msSettingsFile);
        }
        catch (Exception ex) { }
    }

}
