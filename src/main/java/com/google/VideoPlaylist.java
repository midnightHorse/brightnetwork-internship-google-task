package com.google;

import java.util.ArrayList;

/** A class used to represent a Playlist */
class VideoPlaylist {
    String name;
    ArrayList<Video> videos = new ArrayList<>();
    VideoPlaylist (String name) {
        this.name = name;
    }
}
