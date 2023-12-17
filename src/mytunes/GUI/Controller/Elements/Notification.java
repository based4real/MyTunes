package mytunes.GUI.Controller.Elements;

import javafx.geometry.Pos;
import javafx.util.Duration;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import org.controlsfx.control.Notifications;

public class Notification {

    private static final Pos POSITION = Pos.BOTTOM_CENTER;
    private static final Duration SHOW_TIME = Duration.seconds(5);

    public Notification() {

    }

    public static void playlistAdded(Playlist p) {
        Notifications noticationBuilder = Notifications.create()
                .text("Tilføjet til " + p.getName())
                .hideAfter(SHOW_TIME)
                .position(POSITION);

        noticationBuilder.show();
    }

    public static void playlistEdited(Playlist p) {
        Notifications noticationBuilder = Notifications.create()
                .text("Redigering af playliste " + p.getName() + " gennemført")
                .hideAfter(SHOW_TIME)
                .position(POSITION);

        noticationBuilder.show();
    }

    public static void playlistDeleteSong(Playlist p, Song song) {
        Notifications noticationBuilder = Notifications.create()
                .text("Fjernet sang " + song.getArtistName() + " - " + song.getTitle() + " fra " + p.getName())
                .hideAfter(SHOW_TIME)
                .position(POSITION);

        noticationBuilder.show();
    }
}
