package org.codegrinders.treasure_hunter_mobile.settings;

import java.util.ArrayList;

public class Sound {
    public static int menuMusic;
    public static int buttonSound;
    public static int gameMusic;
    public static int correctSound;
    public static int wrongSound;

    static ArrayList<AudioData> audioList = new ArrayList<>();
    public static boolean firstInit = true;

    public static int add(int resource, String type){
        audioList.add(new AudioData(resource, type));
        return audioList.size()-1;
    }

    public static AudioData get(int index){
        return audioList.get(index);
    }
}