package com.yellowbyte.goofyguitarhero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

public class LeaderboardManager {

    private static final String LEADERBOARD_URL = "https://leaderboardapi.firebaseapp.com/scores";
    public static ArrayList<ScoreItem> scoreItems = new ArrayList<ScoreItem>();


    public void postScore(final String name, final int score) {

        HttpRequestBuilder builder = new HttpRequestBuilder();
        Net.HttpRequest request = builder.newRequest().method(Net.HttpMethods.GET).url(LEADERBOARD_URL).build();
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");


        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                scoreItems = new ArrayList<ScoreItem>();
                Gdx.app.log("WebRequest", "HTTP Response code: " + httpResponse.getStatus().getStatusCode());
                String result = httpResponse.getResultAsString();
                Gdx.app.log("WebRequest", "HTTP Response code: " + result);

                JsonValue scoresArrJSON = new JsonReader().parse(result).get(0);

                for (int i = 0; i < 10; i++) {
                    JsonValue scoreJSON = scoresArrJSON.get(i);
                    ScoreItem scoreItem = new ScoreItem(scoreJSON.get("name").asString(), scoreJSON.get("score").asInt());
                    scoreItems.add(scoreItem);
                }

                int lowestScoreIndex = 0;

                for (int i = 0; i < scoreItems.size(); i++) {
                    if (scoreItems.get(i).score < scoreItems.get(lowestScoreIndex).score) {
                        lowestScoreIndex = i;
                    }
                }

                if (score > scoreItems.get(lowestScoreIndex).score) {

                    HttpRequestBuilder builder = new HttpRequestBuilder();
                    Net.HttpRequest request = builder.newRequest().method(Net.HttpMethods.POST).url(LEADERBOARD_URL).build();
                    request.setHeader("Content-Type", "application/x-www-form-urlencoded");
                    request.setContent("name=" + name + "&score=" + score + "&position=" + lowestScoreIndex);

                    Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
                        @Override
                        public void handleHttpResponse(Net.HttpResponse httpResponse) {
                            Gdx.app.log("WebRequest", "HTTP Response code: " + httpResponse.getStatus().getStatusCode());
                            String result = httpResponse.getResultAsString();
                            Gdx.app.log("WebRequest", "HTTP Response code: " + result);
                        }

                        @Override
                        public void failed(Throwable t) {
                            Gdx.app.log("WebRequest", "HTTP request failed");
                        }

                        @Override
                        public void cancelled() {
                            Gdx.app.log("WebRequest", "HTTP request cancelled");
                        }
                    });
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("WebRequest", "HTTP request failed");
            }

            @Override
            public void cancelled() {
                Gdx.app.log("WebRequest", "HTTP request cancelled");
            }
        });
    }

    public void getScores() {
        HttpRequestBuilder builder = new HttpRequestBuilder();
        Net.HttpRequest request = builder.newRequest().method(Net.HttpMethods.GET).url(LEADERBOARD_URL).build();
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");


        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                scoreItems = new ArrayList<ScoreItem>();
                Gdx.app.log("WebRequest", "HTTP Response code: " + httpResponse.getStatus().getStatusCode());
                String result = httpResponse.getResultAsString();

                JsonValue scores = new JsonReader().parse(result).get(0);

                for (int i = 0; i < 10; i++) {
                    JsonValue scoreJSON = scores.get(i);
                    ScoreItem scoreItem = new ScoreItem(scoreJSON.get("name").asString(), scoreJSON.get("score").asInt());


                    boolean added = false;

                    for (int j = 0; j < scoreItems.size(); j++) {
                        if (scoreItem.score > scoreItems.get(j).score) {
                            System.out.println("added");
                            scoreItems.add(j, scoreItem);
                            added = true;
                            break;
                        }
                    }

                    if (!added) {
                        scoreItems.add(scoreItems.size(), scoreItem);
                    }
                }

                for (ScoreItem s : scoreItems) {
                    System.out.println(s.name + ": " + s.score);
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("WebRequest", "HTTP request failed");
            }

            @Override
            public void cancelled() {
                Gdx.app.log("WebRequest", "HTTP request cancelled");
            }
        });
    }

    public boolean showHighScoreInput(int score) {
        int lowestScoreIndex = 0;

        for (int i = 0; i < scoreItems.size(); i++) {
            if (scoreItems.get(i).score < scoreItems.get(lowestScoreIndex).score) {
                lowestScoreIndex = i;
            }
        }

        return (score > scoreItems.get(lowestScoreIndex).score);
    }

    public class ScoreItem {
        public String name;
        public int score;

        public ScoreItem(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }
}