package org.codegrinders.treasure_hunter_mobile.retrofit;

import org.codegrinders.treasure_hunter_mobile.tables.Puzzle;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PuzzlesGetRequest {

    private int questionNumber = 0;
    private List<Puzzle> puzzles;
    private RetroCallBack callBack;
    private Call<List<Puzzle>> callPuzzles;

    public String getQuestion() {
        return puzzles.get(questionNumber).getQuestion();
    }

    public boolean isCorrect(String input) {
        boolean correct = false;
        if (puzzles.get(questionNumber).getAnswer().equals(input)) {
            correct = true;
        }
        return correct;
    }

    public void puzzlesGetRequest() {
        callPuzzles = RetroInstance.initializeAPIService().getPuzzles();
        callPuzzles.enqueue(new Callback<List<Puzzle>>() {
            @Override
            public void onResponse(@NotNull Call<List<Puzzle>> call, @NotNull Response<List<Puzzle>> response) {
                if (!response.isSuccessful()) {
                    callBack.onCallFailed("code: " + response.code());
                    return;
                }
                puzzles = response.body();
                callBack.onCallFinished("Puzzles");
            }

            @Override
            public void onFailure(@NotNull Call<List<Puzzle>> call, @NotNull Throwable t) {
                callBack.onCallFailed(t.getMessage());
            }
        });
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public List<Puzzle> getPuzzles() {
        return puzzles;
    }

    public void setPuzzles(List<Puzzle> puzzles) {
        this.puzzles = puzzles;
    }

    public RetroCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(RetroCallBack callBack) {
        this.callBack = callBack;
    }

    public Call<List<Puzzle>> getCallPuzzles() {
        return callPuzzles;
    }

    public void setCallPuzzles(Call<List<Puzzle>> callPuzzles) {
        this.callPuzzles = callPuzzles;
    }
}
