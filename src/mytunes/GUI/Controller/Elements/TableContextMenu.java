package mytunes.GUI.Controller.Elements;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.GUI.Model.PlaylistModel;
import mytunes.GUI.Model.SongModel;

import java.util.Optional;

public class TableContextMenu {

    private PlaylistModel playlistModel;
    private SongModel songModel;

    private TableView<Song> tblSongs;
    private Playlist playlist;


    private static final String ICON_PLAYLIST = "M12 13c0 1.105-1.12 2-2.5 2S7 14.105 7 13s1.12-2 2.5-2 2.5.895 2.5 2 M12 3v10h-1V3z M11 2.82a1 1 0 0 1 .804-.98l3-.6A1 1 0 0 1 16 2.22V4l-5 1z M0 11.5a.5.5 0 0 1 .5-.5H4a.5.5 0 0 1 0 1H.5a.5.5 0 0 1-.5-.5m0-4A.5.5 0 0 1 .5 7H8a.5.5 0 0 1 0 1H.5a.5.5 0 0 1-.5-.5m0-4A.5.5 0 0 1 .5 3H8a.5.5 0 0 1 0 1H.5a.5.5 0 0 1-.5-.5";
    private static final String ICON_DELETE_SONG = "M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5M11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H2.506a.58.58 0 0 0-.01 0H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1h-.995a.59.59 0 0 0-.01 0zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47ZM8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5";
    private static final String ICON_DELETE = "M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16 M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708";

    public TableContextMenu(TableView<Song> songs, Playlist playlist) throws Exception {
        this.playlistModel = PlaylistModel.getInstance();
        this.songModel = SongModel.getInstance();

        this.tblSongs = songs;
        this.playlist = playlist;
    }

    private Optional<ButtonType> alertPlaylist() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Allerede tilføjet");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Tilføj alligevel");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Tilføj ikke");

        alert.setHeaderText("Denne sang er allerede i playlisten");
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    private void contextAddToPlaylist(TableRow row, Menu playlistSubMenu) {
        playlistSubMenu.getItems().clear(); // Clear existing items

        try {
            for (Playlist p : playlistModel.getPlaylists()) {
                MenuItem playlistMenuItem = new MenuItem(p.getName());
                playlistSubMenu.getItems().add(0, playlistMenuItem);

                playlistMenuItem.setOnAction(event -> {
                    try {
                        Song song = (Song) row.getItem();

                        if (playlistModel.isSongInPlaylist(song, p)) {
                            Optional<ButtonType> result = alertPlaylist();
                            if (!result.get().equals(ButtonType.OK))
                                return;
                        }

                        // Add song, but dont update table, else it messes up
                        playlistModel.addSongToPlaylist(p, song);
                        Notification.playlistAdded(p);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void removeFromPlaylist(TableRow row, MenuItem item) {
        item.setOnAction(event -> {
            tblSongs.getItems().remove(row.getItem());
            try {
                Song song = (Song) row.getItem();
                if (playlistModel.removeSongFromPlaylist(playlist, song))
                    Notification.playlistDeleteSong(playlist, song);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void deleteSong(TableRow row, MenuItem item) {
        item.setOnAction(event -> {
            tblSongs.getItems().remove(row.getItem());
            try {
                Song song = (Song) row.getItem();
                if (songModel.deleteSong(song))
                    Notification.playlistDeleteSong(playlist, song);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void createContextMenu(TableRow row) {
        final ContextMenu contextMenu = new ContextMenu();

        SVGMenu svgMenu = new SVGMenu();

        final MenuItem deleteFromPlaylist = svgMenu.createSVGMenuItem("Fjern fra playliste", ICON_DELETE);

        final Menu playlistSubMenu = svgMenu.createSVGMenu("Tilføj til playliste", ICON_PLAYLIST);
        final MenuItem deleteSong = svgMenu.createSVGMenuItem("Slet sang", ICON_DELETE_SONG);

        // Separator
        SeparatorMenuItem separator = new SeparatorMenuItem();

        deleteSong(row, deleteSong);
        contextAddToPlaylist(row, playlistSubMenu);

        // Update the playlistSubMenu dynamically when the context menu is requested
        row.setOnContextMenuRequested(event -> contextAddToPlaylist(row, playlistSubMenu));

        boolean isInPlaylist = this.playlist != null;
        if (isInPlaylist) {
            removeFromPlaylist(row, deleteFromPlaylist);
            contextMenu.getItems().addAll(deleteFromPlaylist, playlistSubMenu, separator, deleteSong);
        }
        else
            contextMenu.getItems().addAll(playlistSubMenu, separator, deleteSong);

        // Set context menu on row, but use a binding to make it only show for non-empty rows:
        row.contextMenuProperty().bind(
                Bindings.when(row.emptyProperty())
                        .then((ContextMenu) null)
                        .otherwise(contextMenu)
        );
    }

}
