package com.dominikgames.filefinder.filefinder;

/**
 * Created by Dominik on 04.06.2017.
 */

public class FileWithUrl {
    String url;
    String name;
    long size;

    public FileWithUrl(String url, String name, Long size){
        this.url = url;
        this.name = name;
        this.size = size;
    }
}
