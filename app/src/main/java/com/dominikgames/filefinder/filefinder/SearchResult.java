package com.dominikgames.filefinder.filefinder;

/**
 * Created by Dominik on 04.06.2017.
 */

public class SearchResult {
    private String name = "";
    private String url = "";
    private long size = 0;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getSize() {
        return size;
    }
}
