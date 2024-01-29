package Labs8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

interface PlayerState{
    PlayerState play();
    PlayerState stop();
    PlayerState fwd(int numSongs);
    PlayerState rwd(int numSongs);
    int getCurrentSongIndex();
}

class PlayingState implements PlayerState{
    private int currentSongIdx;

    public PlayingState(int currentSongIdx) {
        this.currentSongIdx = currentSongIdx;
    }

    @Override
    public PlayerState play() {
        System.out.println("Song is already playing");
        return new PlayingState(currentSongIdx);
    }

    @Override
    public PlayerState stop() {
        System.out.println("Song " + currentSongIdx + " is paused");
        return new PausedState(0);
    }

    @Override
    public PlayerState fwd(int size) {
        if(currentSongIdx >= size - 1) currentSongIdx = 0;
        else currentSongIdx++;
        return new PausedState(currentSongIdx);
    }

    @Override
    public PlayerState rwd(int size) {
        if(currentSongIdx <= 0) currentSongIdx = size - 1;
        else currentSongIdx--;
        return new PausedState(currentSongIdx);
    }

    @Override
    public int getCurrentSongIndex() {
        return currentSongIdx;
    }

}
class PausedState implements PlayerState{
    private int currentSongIdx;
    private boolean wasStopped;

    public PausedState(int currentSongIdx) {
        this.currentSongIdx = currentSongIdx;
    }

    public PausedState(int currentSongIdx, boolean wasStopped) {
        this.currentSongIdx = currentSongIdx;
        this.wasStopped = wasStopped;
    }

    @Override
    public PlayerState play() {
        System.out.println("Song " + currentSongIdx + " is playing");
        return new PlayingState(currentSongIdx);
    }

    @Override
    public PlayerState stop() {
        if(wasStopped) System.out.println("Songs are already stopped");
        else System.out.println("Songs are stopped");
        return new PausedState(0,true);
    }

    @Override
    public PlayerState fwd(int size) {
        if(currentSongIdx >= size - 1) currentSongIdx = 0;
        else currentSongIdx++;
        return new PausedState(currentSongIdx);
    }

    @Override
    public PlayerState rwd(int size) {
        if(currentSongIdx <= 0) currentSongIdx = size - 1;
        else currentSongIdx--;
        return new PausedState(currentSongIdx);
    }

    @Override
    public int getCurrentSongIndex() {
        return currentSongIdx;
    }
}

class Song{
    private final String title;
    private final String artist;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title=" + title +
                ", artist=" + artist +
                '}';
    }
}

class MP3Player{
    private final List<Song> songList;
    private PlayerState state;
    public MP3Player(List<Song> songList) {
        this.songList = new ArrayList<>();
        this.songList.addAll(songList);
        state = new PausedState(0);
    }

    public void pressPlay(){
        state = state.play();
    }
    public void pressStop(){
        state = state.stop();
    }
    public void pressFWD(){
        System.out.println("Forward...");
        state = state.fwd(songList.size());
    }
    public void pressREW(){
        System.out.println("Reward...");
        state = state.rwd(songList.size());
    }

    public void printCurrentSong(){
        System.out.println(songList.get(state.getCurrentSongIndex()));
    }

    @Override
    public String toString() {
        return "MP3Player{" +
                "currentSong = " + state.getCurrentSongIndex() +
                ", songList = " + songList +
                '}';
    }
}


public class PatternTest {
    public static void main(String args[]) throws IOException {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}

//Vasiot kod ovde
