package mytunes.BE;

public class Artist {

    private int id;
    private String artist_id, name, alias;

    public Artist(int id, String artist_id, String name, String alias) {
        this.id = id;
        this.artist_id = artist_id;
        this.name = name;
        this.alias = alias;
    }

    public Artist(String artist_id, String name, String alias) {
        this.artist_id = artist_id;
        this.name = name;
        this.alias = alias;
    }

    public int getPrimaryID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtistID() {
        return artist_id;
    }

    public String getAlias() {
        return alias;
    }
}
