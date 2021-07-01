package com.google;

import java.util.ArrayList;
import java.util.*;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private Video videoPlaying;
  private boolean isPaused;
  private ArrayList<VideoPlaylist> playlists = new ArrayList<>();

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    var videos = videoLibrary.getVideos();
    var strings = new ArrayList<String>();
    for (var i = 0; i < videos.size(); i++) {
      var tags = "[";
      if (videos.get(i).getTags().size() == 0)
        tags = "[]";
      for (var j = 0; j < videos.get(i).getTags().size(); j++) {
        if (j == videos.get(i).getTags().size() - 1)
          tags += videos.get(i).getTags().get(j) + "]";
        else
          tags += videos.get(i).getTags().get(j) + " ";
      }
      strings.add(videos.get(i).getTitle()
              + " (" + videos.get(i).getVideoId() + ") " + tags
      );
    }
    var toPrint = strings.toArray();
    Arrays.sort(toPrint);
    System.out.println("Here's a list of all available videos:");
    for (var i = 0; i < toPrint.length; i++) {
      System.out.println(toPrint[i]);
    }
  }

  public void playVideo(String videoId) {
    var videos = videoLibrary.getVideos();
    var videoFound = false;

    for (var i = 0; i < videos.size(); i++) {
      if (videos.get(i).getVideoId().equals(videoId)) {
        if (videoPlaying != null) {
          System.out.println("Stopping video: " + videoPlaying.getTitle());
        }
        videoPlaying = videos.get(i);
        videoFound = true;
      }
    }
    if (videoFound) {
      System.out.println("Playing video: " + videoPlaying.getTitle());
      isPaused = false;
    }
    else
      System.out.println("Cannot play video: Video does not exist");
  }

  public void stopVideo() {
    if (videoPlaying == null)
      System.out.println("Cannot stop video: No video is currently playing");
    else {
      System.out.println("Stopping video: " + videoPlaying.getTitle());
      videoPlaying = null;
    }
  }

  public void playRandomVideo() {
    var videos = videoLibrary.getVideos();
    if (videoPlaying != null)
      System.out.println("Stopping video: " + videoPlaying.getTitle());
    var randomIndex = (int) (Math.random() * videos.size());
    videoPlaying = videos.get(randomIndex);
    System.out.println("Playing video: " + videoPlaying.getTitle());
    isPaused = false;
  }

  public void pauseVideo() {
    if (isPaused)
      System.out.println("Video already paused: " + videoPlaying.getTitle());
    else {
      if (videoPlaying != null) {
        System.out.println("Pausing video: " + videoPlaying.getTitle());
        isPaused = true;
      }
      else
        System.out.println("Cannot pause video: No video is currently playing");
    }
  }

  public void continueVideo() {
    if (videoPlaying == null)
      System.out.println("Cannot continue video: No video is currently playing");
    else {
      if (isPaused) {
        isPaused = false;
        System.out.println("Continuing video: " + videoPlaying.getTitle());
      }
      else
        System.out.println("Cannot continue video: Video is not paused");
    }
  }

  public void showPlaying() {
    if (videoPlaying == null)
      System.out.println("No video is currently playing");
    else {
      var tags = "[";
      if (videoPlaying.getTags().size() == 0)
        tags = "[]";
      for (var i = 0; i < videoPlaying.getTags().size(); i++) {
        if (i == videoPlaying.getTags().size() - 1)
          tags += videoPlaying.getTags().get(i) + "]";
        else
          tags += videoPlaying.getTags().get(i) + " ";
      }
      var toPrint = "Currently playing: " + videoPlaying.getTitle()
              + " (" + videoPlaying.getVideoId() + ") " + tags;
      if (isPaused)
        toPrint += " - PAUSED";
      System.out.println(toPrint);
    }
  }

  public void createPlaylist(String playlistName) {
    if (playlists != null) {
      for (var i = 0; i < playlists.size(); i++) {
        if (playlists.get(i).name.equalsIgnoreCase(playlistName)) {
          System.out.println("Cannot create playlist: A playlist with the same name already exists");
          return;
        }
      }
    }
    var newPlaylist = new VideoPlaylist(playlistName);
    playlists.add(newPlaylist);
    System.out.println("Successfully created new playlist: " + playlistName);
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    VideoPlaylist playlistToAddTo = null;
    var playlistExists = false;
    for (var i = 0; i < playlists.size(); i++) {
      if (playlists.get(i).name.equalsIgnoreCase(playlistName)) {
        playlistExists = true;
        playlistToAddTo = playlists.get(i);
      }
    }
    if (!playlistExists) {
      System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
      return;
    }
    Video videoToAdd = null;
    var videoExists = false;
    var videos = videoLibrary.getVideos();
    for (var i = 0; i < videos.size(); i++) {
      if (videos.get(i).getVideoId().equals(videoId)) {
        videoExists = true;
        videoToAdd = videos.get(i);
      }
    }
    if (!videoExists) {
      System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
      return;
    }

    for (var i = 0; i < playlistToAddTo.videos.size(); i++) {
      if (playlistToAddTo.videos.get(i).equals(videoToAdd)) {
        System.out.println("Cannot add video to " + playlistName + ": Video already added");
        return;
      }
    }

    playlistToAddTo.videos.add(videoToAdd);
    System.out.println("Added video to " + playlistName + ": " + videoToAdd.getTitle());
  }

  public void showAllPlaylists() {
    if (playlists.size() == 0) {
      System.out.println("No playlists exist yet");
      return;
    }
    System.out.println("Showing all playlists:");
    ArrayList namesToPrint = new ArrayList<String>();
    for (var i = 0; i < playlists.size(); i++) {
      namesToPrint.add(playlists.get(i).name);
    }
    var names = namesToPrint.toArray();
    Arrays.sort(names);
    for (var i = 0; i < names.length; i++)
      System.out.println(names[i]);
  }

  public void showPlaylist(String playlistName) {
    var isFound = false;
    VideoPlaylist playListToPrint = null;
    for (var i = 0; i < playlists.size(); i++) {
      if (playlists.get(i).name.equalsIgnoreCase(playlistName)) {
        isFound = true;
        playListToPrint = playlists.get(i);
        break;
      }
    }
    if (!isFound) {
      System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
      return;
    }
    System.out.println("Showing playlist: " + playlistName);
    if (playListToPrint.videos.size() == 0) {
      System.out.println("No videos here yet");
      return;
    }
    var videos = playListToPrint.videos;
    for (var i = 0; i < videos.size(); i++) {
      var tags = "[";
      if (videos.get(i).getTags().size() == 0)
        tags = "[]";
      for (var j = 0; j < videos.get(i).getTags().size(); j++) {
        if (j == videos.get(i).getTags().size() - 1)
          tags += videos.get(i).getTags().get(j) + "]";
        else
          tags += videos.get(i).getTags().get(j) + " ";
      }
      System.out.println(videos.get(i).getTitle()
              + " (" + videos.get(i).getVideoId() + ") " + tags);
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    VideoPlaylist playlistToRemoveFrom = null;
    var playlistExists = false;
    for (var i = 0; i < playlists.size(); i++) {
      if (playlists.get(i).name.equalsIgnoreCase(playlistName)) {
        playlistExists = true;
        playlistToRemoveFrom = playlists.get(i);
      }
    }
    if (!playlistExists) {
      System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
      return;
    }
    Video videoToRemove = null;
    var videoExists = false;
    var videos = videoLibrary.getVideos();
    for (var i = 0; i < videos.size(); i++) {
      if (videos.get(i).getVideoId().equals(videoId)) {
        videoExists = true;
        videoToRemove = videos.get(i);
      }
    }
    if (!videoExists) {
      System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
      return;
    }

    if (playlistToRemoveFrom.videos.remove(videoToRemove))
      System.out.println("Removed video from " + playlistName + ": " + videoToRemove.getTitle());
    else
      System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");


  }

  public void clearPlaylist(String playlistName) {
    VideoPlaylist playlistToClear = null;
    var playlistExists = false;
    for (var i = 0; i < playlists.size(); i++) {
      if (playlists.get(i).name.equalsIgnoreCase(playlistName)) {
        playlistExists = true;
        playlistToClear = playlists.get(i);
      }
    }
    if (!playlistExists) {
      System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
      return;
    }
    playlistToClear.videos = new ArrayList<>();
    System.out.println("Successfully removed all videos from " + playlistName);
  }

  public void deletePlaylist(String playlistName) {
    VideoPlaylist playlistToDelete = null;
    var playlistExists = false;
    for (var i = 0; i < playlists.size(); i++) {
      if (playlists.get(i).name.equalsIgnoreCase(playlistName)) {
        playlistToDelete = playlists.get(i);
        break;
      }
    }
    if (!playlists.remove(playlistToDelete))
      System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
    else
      System.out.println("Deleted playlist: " + playlistName);

  }

  public void searchVideos(String searchTerm) {
    var videos = videoLibrary.getVideos();
    var videosThatMatch = new ArrayList<Video>();
    for (var i = 0; i < videos.size(); i++) {
      if (videos.get(i).getTitle().toLowerCase().contains(searchTerm.toLowerCase()))
        videosThatMatch.add(videos.get(i));
    }
    if (videosThatMatch.size() == 0) {
      System.out.println("No search results for " + searchTerm);
      return;
    }
    var strings = new ArrayList<String>();
    for (var i = 0; i < videosThatMatch.size(); i++) {
      var tags = "[";
      if (videosThatMatch.get(i).getTags().size() == 0)
        tags = "[]";
      for (var j = 0; j < videosThatMatch.get(i).getTags().size(); j++) {
        if (j == videosThatMatch.get(i).getTags().size() - 1)
          tags += videosThatMatch.get(i).getTags().get(j) + "]";
        else
          tags += videosThatMatch.get(i).getTags().get(j) + " ";
      }
      strings.add(videosThatMatch.get(i).getTitle()
              + " (" + videosThatMatch.get(i).getVideoId() + ") " + tags
      );
    }
    var toPrint = strings.toArray();
    Arrays.sort(toPrint);
    System.out.println("Here are the results for " + searchTerm + ":");
    for (var i = 0; i < toPrint.length; i++) {
      System.out.println((i+1) + ") " + toPrint[i]);
    }
    System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
    System.out.println("If your answer is not a valid number, we will assume it's a no.");
    var scanner = new Scanner(System.in);
    var input = scanner.next();
    var inputArray = input.toCharArray();
    if (inputArray.length != 1) {
      return;
    }
    if (Integer.parseInt(input) > 0 && Integer.parseInt(input) <= toPrint.length) {
      var videoDetails = toPrint[Integer.parseInt(input) - 1];
      var detailsArray = videoDetails.toString().split(" ");
      String vidTitle = "";
      for (var i = 0; i < detailsArray.length; i++) {
        if (detailsArray[i].contains("_id")) {
          for (var j = 0; j < i; j++) {
            if (j == i - 1)
              vidTitle += detailsArray[j];
            else
              vidTitle += detailsArray[j] + " ";
          }
        }
      }
      for (var i = 0; i < videos.size(); i++) {
        if (videos.get(i).getTitle().equalsIgnoreCase(vidTitle)) {
          videoPlaying = videos.get(i);
          break;
        }
      }
      System.out.println("Playing video: " + vidTitle);
    }
  }

  public void searchVideosWithTag(String videoTag) {
    var videos = videoLibrary.getVideos();
    var vidsWithTag = new ArrayList<Video>();
    for (var i = 0; i < videos.size(); i++) {
      var tags = videos.get(i).getTags();
      for (var j = 0; j < tags.size(); j++) {
        if (tags.get(j).equalsIgnoreCase(videoTag)) {
          vidsWithTag.add(videos.get(i));
          break;
        }
      }
    }
    if (vidsWithTag.isEmpty()) {
      System.out.println("No search results for " + videoTag);
      return;
    }
    System.out.println("Here are the results for " + videoTag + ":");
    var strings = new ArrayList<String>();
    for (var i = 0; i < vidsWithTag.size(); i++) {
      var tags = "[";
      if (vidsWithTag.get(i).getTags().size() == 0)
        tags = "[]";
      for (var j = 0; j < vidsWithTag.get(i).getTags().size(); j++) {
        if (j == vidsWithTag.get(i).getTags().size() - 1)
          tags += vidsWithTag.get(i).getTags().get(j) + "]";
        else
          tags += vidsWithTag.get(i).getTags().get(j) + " ";
      }
      strings.add((i+1) + ") " + vidsWithTag.get(i).getTitle()
              + " (" + vidsWithTag.get(i).getVideoId() + ") " + tags);
    }
    for (var i = 0; i < strings.size(); i++) {
      System.out.println(strings.get(i));
    }
    System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
    System.out.println("If your answer is not a valid number, we will assume it's a no.");
    var scanner = new Scanner(System.in);
    var input = scanner.next();
    var inputArray = input.toCharArray();
    var toPrint = strings.toArray();
    if (inputArray.length != 1) {
      return;
    }
    if (Integer.parseInt(input) > 0 && Integer.parseInt(input) <= toPrint.length) {
      var videoDetails = toPrint[Integer.parseInt(input) - 1];
      var detailsArray = videoDetails.toString().split(" ");
      String vidTitle = "";
      for (var i = 0; i < detailsArray.length; i++) {
        if (detailsArray[i].contains("_id")) {
          for (var j = 0; j < i; j++) {
            if (j == i - 1)
              vidTitle += detailsArray[j];
            else
              vidTitle += detailsArray[j] + " ";
          }
        }
      }
      for (var i = 0; i < videos.size(); i++) {
        if (videos.get(i).getTitle().equalsIgnoreCase(vidTitle)) {
          videoPlaying = videos.get(i);
          break;
        }
      }
      System.out.println("Playing video: " + vidTitle);
    }
  }

  public void flagVideo(String videoId) {
    System.out.println("flagVideo needs implementation");
  }

  public void flagVideo(String videoId, String reason) {
    System.out.println("flagVideo needs implementation");
  }

  public void allowVideo(String videoId) {
    System.out.println("allowVideo needs implementation");
  }
}