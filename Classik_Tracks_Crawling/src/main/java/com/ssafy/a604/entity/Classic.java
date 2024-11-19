package com.ssafy.a604.entity;

public class Classic {

    private final String title;
    private final String composer;
    private final String tags;
    private final String videoId;

    public Classic(String title, String composer, String tags, String videoUrl) {
        this.title = title;
        this.composer = composer;
        this.tags = tags;
        this.videoId = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getComposer() {
        return composer;
    }

    public String getTags() {
        return tags;
    }

    public String getVideoId() {
        return videoId;
    }

    @Override
    public String toString() {
        return title + "\n" + composer + "\n" + tags + "\n" + videoId;
    }

}
