package org.codegrinders.treasure_hunter_mobile.settings;

import android.media.MediaPlayer;

public class AudioData {

    public MediaPlayer player;
    public int resource;
    public int position;
    public boolean looping;
    public boolean allowPlaying;
    public String type;

    public AudioData(int resource, String type){
        this.resource = resource;
        this.type = type;
        position = 0;
        looping = false;
        allowPlaying = true;
    }
}
