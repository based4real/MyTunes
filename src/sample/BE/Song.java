package sample.BE;

public class Song {
    private int id;
    private String title;
    private String artist;
    private String album;
    private String filePath;

    public Song(int id, String title, String artist, String album, String filePath){
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.filePath = filePath;
    }
    public Song(String title, String artist, String album, String filePath){
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString(){
        return title + album;
    }
}
