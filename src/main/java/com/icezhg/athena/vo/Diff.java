package com.icezhg.athena.vo;

/**
 * Created by zhongjibing on 2022/09/29.
 */
public class Diff {

    private static final Diff EMPTY = new Diff(null);
    private final String title;
    private final String content;

    public Diff(String content) {
        this(null, content);
    }

    public Diff(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Diff empty() {
        return EMPTY;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return (title != null ? title + ": " : "<compare>: ") + (content != null ? content : "");
    }
}
