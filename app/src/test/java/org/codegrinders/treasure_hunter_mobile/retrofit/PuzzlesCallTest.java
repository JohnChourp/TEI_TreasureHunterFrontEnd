package org.codegrinders.treasure_hunter_mobile.retrofit;

import org.codegrinders.treasure_hunter_mobile.model.Puzzle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.assertEquals;

public class PuzzlesCallTest {

    MockWebServer server = new MockWebServer();
    PuzzlesCall puzzlesCall = new PuzzlesCall();

    @Before
    public void setUp() throws Exception {
        server.start(8080);
    }

    @Test
    public void whenSearchPuzzleByIDIsCalledWithExistingIDItReturnsPuzzleId() {
        puzzlesCall.setPuzzles(new ArrayList<>());
        puzzlesCall.getPuzzles().add(new Puzzle());
        puzzlesCall.getPuzzles().get(0).setId("first id");
        assertEquals("first id", puzzlesCall.getPuzzles().get(puzzlesCall.searchPuzzleByID("first id")).getId());
    }

    @Test
    public void whenSearchPuzzleByIDIsCalledWithExistingIDItReturnsPuzzleQuestion() {
        puzzlesCall.setPuzzles(new ArrayList<>());
        puzzlesCall.getPuzzles().add(new Puzzle());
        puzzlesCall.getPuzzles().get(0).setId("first id");
        puzzlesCall.getPuzzles().get(0).setQuestion("Question1");
        assertEquals("Question1", puzzlesCall.getPuzzles().get(puzzlesCall.searchPuzzleByID("first id")).getQuestion());
    }

    @Test
    public void whenSearchPuzzleByIDIsCalledWithExistingIDItReturnsPuzzleAnswer() {
        puzzlesCall.setPuzzles(new ArrayList<>());
        puzzlesCall.getPuzzles().add(new Puzzle());
        puzzlesCall.getPuzzles().get(0).setId("first id");
        puzzlesCall.getPuzzles().get(0).setAnswer("answer1");
        assertEquals("answer1", puzzlesCall.getPuzzles().get(puzzlesCall.searchPuzzleByID("first id")).getAnswer());
    }

    @Test
    public void whenSearchPuzzleByIDIsCalledWithExistingIDItReturnsPuzzlePoints() {
        puzzlesCall.setPuzzles(new ArrayList<>());
        puzzlesCall.getPuzzles().add(new Puzzle());
        puzzlesCall.getPuzzles().get(0).setId("first id");
        puzzlesCall.getPuzzles().get(0).setPoints(500);
        assertEquals(500, puzzlesCall.getPuzzles().get(puzzlesCall.searchPuzzleByID("first id")).getPoints());
    }

    @Test
    public void whenSearchPuzzleByIDIsCalledWithNonExistingIDItReturnsMinusOne() {
        puzzlesCall.setPuzzles(new ArrayList<>());
        puzzlesCall.getPuzzles().add(new Puzzle());
        puzzlesCall.getPuzzles().get(0).setId("first id");
        assertEquals(-1, puzzlesCall.searchPuzzleByID("some id"));
    }

    @Test
    public void whenGetPointsIsCalledWithExistingPointsItReturnsPuzzlePoints() {
        puzzlesCall.setPuzzles(new ArrayList<>());
        puzzlesCall.getPuzzles().add(new Puzzle());
        puzzlesCall.getPuzzles().get(0).setPoints(500);
        assertEquals(500, puzzlesCall.getPoints());
    }

    @Test
    public void whenGetQuestionIsCalledWithExistingQuestionItReturnsPuzzleQuestion() {
        puzzlesCall.setPuzzles(new ArrayList<>());
        puzzlesCall.getPuzzles().add(new Puzzle());
        puzzlesCall.getPuzzles().get(0).setQuestion("question1");
        assertEquals("question1", puzzlesCall.getQuestion());
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
}