package com.example.route_finder.entity;

public class ResponseBody {
    private final String winnerName;

    public ResponseBody(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getWinnerName() {
        return winnerName;
    }
}
