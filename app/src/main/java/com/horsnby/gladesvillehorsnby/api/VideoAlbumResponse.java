package com.horsnby.gladesvillehorsnby.api;

import com.horsnby.gladesvillehorsnby.models.MediaAlbum;

import java.util.List;

public class VideoAlbumResponse {

    private boolean error;
    private String message;
    private List<MediaAlbum> data;

    public VideoAlbumResponse() {
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MediaAlbum> getData() {
        return data;
    }

    public void setData(List<MediaAlbum> data) {
        this.data = data;
    }
}
