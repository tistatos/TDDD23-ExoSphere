package com.exosphere.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * exosphere - Settings
 * holds settings for the game
 * Created by tistatos on 9/4/15.
 */
public class Settings {
    static int msScreenSize[] = {800, 400};
    static boolean msFullscreen = false;
    static boolean msEnableSound = false;
    static String msSettingsFile = "exosphere.settings";

    //1 = time factor
    static double msTimeFactor = 60*100;
    public static double Scale = 10000;

    public static void load() {
        try {
            FileHandle settingsFile = Gdx.files.external(msSettingsFile);
        }
        catch (Exception ex) { }
    }

    public static double getTimeFactor() {
        return  msTimeFactor;
    }
    public static void setTimeFactor(double factor) {
        msTimeFactor = factor;
    }

}
