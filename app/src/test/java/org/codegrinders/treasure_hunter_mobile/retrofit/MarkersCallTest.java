package org.codegrinders.treasure_hunter_mobile.retrofit;

import org.codegrinders.treasure_hunter_mobile.model.Markers;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MarkersCallTest {
MarkersCall markersCall = new MarkersCall();

    @Test
    public void whenSearchMarkerByTitleIsCalledWithExistingTitleReturnMarkerId() {
        markersCall.setMarkers(new ArrayList<>());
        markersCall.getMarkers().add(new Markers());
        markersCall.getMarkers().get(0).setTitle("title");
        markersCall.getMarkers().get(0).setId("markerId1");
        assertEquals("markerId1",markersCall.searchMarkerByTitle("title").getId());
    }

    @Test
    public void whenSearchMarkerByTitleIsCalledWithExistingTitleReturnMarkerDescription() {
        markersCall.setMarkers(new ArrayList<>());
        markersCall.getMarkers().add(new Markers());
        markersCall.getMarkers().get(0).setTitle("title");
        markersCall.getMarkers().get(0).setDescription("description1");
        assertEquals("description1",markersCall.searchMarkerByTitle("title").getDescription());
    }

    @Test
    public void whenSearchMarkerByTitleIsCalledWithExistingTitleReturnMarkerPuzzleId() {
        markersCall.setMarkers(new ArrayList<>());
        markersCall.getMarkers().add(new Markers());
        markersCall.getMarkers().get(0).setTitle("title");
        markersCall.getMarkers().get(0).setPuzzleId("puzzleId");
        assertEquals("puzzleId",markersCall.searchMarkerByTitle("title").getPuzzleId());
    }

    @Test
    public void whenSearchMarkerByTitleIsCalledWithExistingTitleReturnMarkerSnippet() {
        markersCall.setMarkers(new ArrayList<>());
        markersCall.getMarkers().add(new Markers());
        markersCall.getMarkers().get(0).setTitle("title");
        markersCall.getMarkers().get(0).setSnippet("snippet1");
        assertEquals("snippet1",markersCall.searchMarkerByTitle("title").getSnippet());
    }

    @Test
    public void whenSearchMarkerByTitleIsCalledWithExistingTitleReturnMarkerTitle() {
        markersCall.setMarkers(new ArrayList<>());
        markersCall.getMarkers().add(new Markers());
        markersCall.getMarkers().get(0).setTitle("title");
        assertEquals("title",markersCall.searchMarkerByTitle("title").getTitle());
    }

    @Test
    public void whenSearchMarkerByTitleIsCalledWithNonExistingTitleReturnNull() {
        markersCall.setMarkers(new ArrayList<>());
        markersCall.getMarkers().add(new Markers());
        markersCall.getMarkers().get(0).setTitle("title");
        assertNull(markersCall.searchMarkerByTitle("some title"));
    }
}