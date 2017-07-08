package com.example.siddharth.fzrevjsonparsing;

/**
 * Created by saisi on 06-Jun-17.
 */

public class PostsModelClass {
    private String title;
    private String content;
    private String source_url;

    public PostsModelClass() {

    }
    public PostsModelClass(String title, String content, String source_url) {
        this.title = title;
        this.content = content;
        this.source_url = source_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }
}
