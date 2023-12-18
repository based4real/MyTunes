package mytunes.BE;

import java.util.ArrayList;
import java.util.List;

public class Genre {


    private String name;
    private List<Song> genreSongs = new ArrayList<>();
    public Genre(String name){
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setGenreSongs(List<Song> list) {
        genreSongs.addAll(list);
    }

    public List<Song> getGenreSongs() {
        return genreSongs;
    }
}
