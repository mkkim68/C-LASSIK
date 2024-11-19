package com.ssafy.a604.config;

public enum CrawlingSetting {

    COMPOSER_CARDS_COUNT("21"),       // max value : 21
    COMPOSER_TRACKS_COUNT("10"),      // max value : 52
    MAX_WAIT_SECOND("5"),
    URL("https://www.last.fm/tag/classical/artists?page=1");

    private final String value;

    CrawlingSetting(String value) {
        this.value = value;
    }

    public Object getValue() {
        if(Character.isDigit(value.charAt(0))) {
            return Integer.parseInt(value);
        }
        return value;
    }

}
