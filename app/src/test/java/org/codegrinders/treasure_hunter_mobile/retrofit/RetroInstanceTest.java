package org.codegrinders.treasure_hunter_mobile.retrofit;

import org.codegrinders.treasure_hunter_mobile.model.Puzzle;
import org.codegrinders.treasure_hunter_mobile.model.RegisterRequest;
import org.codegrinders.treasure_hunter_mobile.model.RegisterResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;

import static org.junit.Assert.assertEquals;

public class RetroInstanceTest {

    MockWebServer server = new MockWebServer();

    @Before
    public void setUp() throws Exception {
        server.start(8080);
    }

    @Test
    public void whenServerStartThenCheckUlrUsers() {
        RegisterRequest registerRequest = new RegisterRequest();
        Call<RegisterResponse> registerResponseCall = RetroInstance.initializeAPIService().registerUser(registerRequest);
        assertEquals("https://treasure-hunter-codegrinders.herokuapp.com/user/", registerResponseCall.request().url().toString());
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
}