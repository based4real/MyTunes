package mytunes.GUI.Controller.ny.Pages;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.GUI.Controller.ny.Custom.SVGMenu;
import mytunes.GUI.Controller.ny.Custom.TitleArtistCell;
import mytunes.GUI.Model.PlaylistModel;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PlaylistController implements Initializable {
    @FXML
    private TableView tblSongsPlaylist;

    @FXML
    private TableColumn<Playlist, String> colPlaylistNavn;

    @FXML
    private TableColumn columnID;

    @FXML
    private TableColumn<Song, String> columnTitle;

    public TableColumn columnGenre;
    public TableColumn columnAdded;
    public TableColumn columnAlbum;
    public TableColumn columnDuration;

    private PlaylistModel playlistModel;
    private ObservableList<Song> playlistSongs = null;
    private Playlist playlist;

    private static final String ICON_PLAYLIST = "M12 13c0 1.105-1.12 2-2.5 2S7 14.105 7 13s1.12-2 2.5-2 2.5.895 2.5 2 M12 3v10h-1V3z M11 2.82a1 1 0 0 1 .804-.98l3-.6A1 1 0 0 1 16 2.22V4l-5 1z M0 11.5a.5.5 0 0 1 .5-.5H4a.5.5 0 0 1 0 1H.5a.5.5 0 0 1-.5-.5m0-4A.5.5 0 0 1 .5 7H8a.5.5 0 0 1 0 1H.5a.5.5 0 0 1-.5-.5m0-4A.5.5 0 0 1 .5 3H8a.5.5 0 0 1 0 1H.5a.5.5 0 0 1-.5-.5";
    private static final String ICON_DELETE = "M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5M11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H2.506a.58.58 0 0 0-.01 0H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1h-.995a.59.59 0 0 0-.01 0zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47ZM8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5";

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public PlaylistController() {
        try {
            playlistModel = new PlaylistModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setupPlistSongsTableView() {
        columnTitle.setCellFactory(col -> new TitleArtistCell());
    }

    private void updatePlaylistSongs(Playlist playlist) throws Exception {
        playlistSongs = playlistModel.getObservableSongs(playlist);
        tblSongsPlaylist.setItems(playlistSongs);
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
        try {
            for (Playlist p : playlistModel.getPlaylists()) {
                MenuItem playlistMenuItem = new MenuItem(p.getName());
                playlistSubMenu.getItems().add(playlistMenuItem);

                playlistMenuItem.setOnAction(event -> {
                    try {
                        Song song = (Song) row.getItem();

                        if (playlistModel.isSongInPlaylist(song, p)) {
                            Optional<ButtonType> result = alertPlaylist();
                            if (!result.get().equals(ButtonType.OK))
                                return;
                        }

                        if (playlistModel.addSongToPlaylist(p, song) && tblSongsPlaylist.getSelectionModel().getSelectedItem() == p) {
                            updatePlaylistSongs(p);
                        }

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void createContextMenu() {
        tblSongsPlaylist.setRowFactory(tableView -> {
            final TableRow row = new TableRow();
            final ContextMenu contextMenu = new ContextMenu();

            SVGMenu svgMenu = new SVGMenu();

            final MenuItem removeMenuItem = svgMenu.createSVGMenuItem("Fjern fra playliste", ICON_DELETE);
            final Menu playlistSubMenu = svgMenu.createSVGMenu("Tilføj til playliste", ICON_PLAYLIST);

            contextAddToPlaylist(row, playlistSubMenu);

            removeMenuItem.setOnAction(event -> {
                this.tblSongsPlaylist.getItems().remove(row.getItem());
            });

            contextMenu.getItems().add(removeMenuItem);
            contextMenu.getItems().add(playlistSubMenu);


            // Set context menu on row, but use a binding to make it only show for non-empty rows:
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );

            return row;
        });
    }

    private MenuItem createSVGMenuItem(String text, String svgPath) {
        MenuItem menuItem = new MenuItem(text);
        SVGPath svgIcon = new SVGPath();

        svgIcon.setContent(svgPath);
        svgIcon.setScaleX(1);
        svgIcon.setScaleY(1);
        svgIcon.setFill(Color.WHITE);

        menuItem.setGraphic(svgIcon);
        return menuItem;
    }

    private Menu createSVGMenu(String text, String svgPath) {
        Menu menu = new Menu(text);
        SVGPath svgIcon = new SVGPath();

        svgIcon.setContent(svgPath);
        svgIcon.setScaleX(1);
        svgIcon.setScaleY(1);
        svgIcon.setFill(Color.WHITE);

        menu.setGraphic(svgIcon);
        return menu;
    }


    public void tablePlaylistSongsClick(Playlist p) throws Exception {
        if (p == null)
            return;

        tblSongsPlaylist.refresh();
        tblSongsPlaylist.setItems(playlistModel.getObservableSongs(p));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupPlistSongsTableView();
        createContextMenu();
    }
}
