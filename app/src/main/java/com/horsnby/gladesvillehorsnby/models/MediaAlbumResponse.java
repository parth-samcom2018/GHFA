package com.horsnby.gladesvillehorsnby.models;

public class MediaAlbumResponse {
    private boolean error;
    private String message;
    private MediaAlbum data;

    public MediaAlbumResponse() {
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

    public MediaAlbum getData() {
        return data;
    }

    public void setData(MediaAlbum data) {
        this.data = data;
    }

}
