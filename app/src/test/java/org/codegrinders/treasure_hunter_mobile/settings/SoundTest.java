package org.codegrinders.treasure_hunter_mobile.settings;

import org.codegrinders.treasure_hunter_mobile.R;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class SoundTest {

    @Mock
    ArrayList<AudioData> audioList = new ArrayList<>();

    @Test
    public void searchByResId() {
        int resource = R.raw.wanabe_epic_music;
        String type = "music";
        AudioData audioData = new AudioData(resource, type);
        audioList.add(audioData);
        assertEquals("music", audioList.get(0).type);
    }
}