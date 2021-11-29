package org.codegrinders.treasure_hunter_mobile.model;

public class Puzzle {
    private String id;
    private String question;
    private String answer;
    private int points;

    public Puzzle(String id, String answer) {
        this.id = id;
        this.answer = answer;
    }

    public Puzzle(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
