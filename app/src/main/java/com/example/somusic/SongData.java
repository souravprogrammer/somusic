package com.example.somusic;

public class SongData {
    private String path=null ,album = null,duration =null,title = null;
    long Id ;
    public SongData() { }

    public SongData(String path, String title, String album, String duration) {
        this.path = new String(path);
        this.album = album;
        this.duration = duration;
        this.title = title;
    }



    public void setId(long id) {
        Id = id;
    }

    public long getId() {
        return Id;
    }

    public String getPath() {
        return path;
    }

    public String getAlbum() {
        return album;
    }

    public String getDuration() {
        return duration;
    }

    public String getTitle() {
        return title;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
