package org.codegrinders.treasure_hunter_mobile.retrofit;

import org.codegrinders.treasure_hunter_mobile.model.Puzzle;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PuzzlesCall {

    public static int questionNumber = 0;
    private List<Puzzle> puzzles;
    private RetroCallBack callBack;
    private Call<List<Puzzle>> call;
    private Puzzle puzzle;

    public String getQuestion() {
        return puzzles.get(questionNumber).getQuestion();
    }

    public int getPoints() {
        return puzzles.get(questionNumber).getPoints();
    }

    public int searchPuzzleByID(String ID) {
        int pos = -1;
        for (int i = 0; i < puzzles.size(); i++) {
            if (puzzles.get(i).getId().equals(ID)) {
                pos = i;
                break;
            }
        }
        questionNumber = pos;
        return pos;
    }

    public void puzzlesGetRequest() {
        call = RetroInstance.initializeAPIService().getPuzzles();
        call.enqueue(new Callback<List<Puzzle>>() {
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

    public void puzzleIsCorrect(String answer) {
        Call<Puzzle> call = RetroInstance.initializeAPIService().answerIsCorrect(puzzles.get(questionNumber).getId(), answer, UsersCall.user.getId());
        call.enqueue(new Callback<Puzzle>() {
            @Override
            public void onResponse(@NotNull Call<Puzzle> call, @NotNull Response<Puzzle> response) {
                assert response.body() != null;
                puzzle = new Puzzle(response.body().getId(), response.body().getAnswer());
                callBack.onCallFinished("postAnswer");
            }

            @Override
            public void onFailure(@NotNull Call<Puzzle> call, @NotNull Throwable t) {
                puzzle = null;
            }
        });

    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
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

    public Call<List<Puzzle>> getCall() {
        return call;
    }

    public void setCall(Call<List<Puzzle>> call) {
        this.call = call;
    }
}
