package mytunes.GUI.Controller.ny;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Callback;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.GUI.Model.MediaPlayerModel;
import mytunes.GUI.Model.PlaylistModel;
import mytunes.GUI.Model.SongModel;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class PlaylistContainer implements Initializable {
    @FXML
    private TableView tblSongsPlaylist;

    @FXML
    private TableColumn<Playlist, String> colPlaylistNavn;

    @FXML
    private TableColumn columnID;

    @FXML
    private TableColumn columnTitle;
    public TableColumn<Song, String> columnArtist;
    public TableColumn columnGenre;
    public TableColumn columnAdded;
    public TableColumn columnAlbum;
    public TableColumn columnDuration;

    private PlaylistModel playlistModel;
    private ObservableList<Song> playlistSongs = null;


    public PlaylistContainer() {
        try {
            playlistModel = new PlaylistModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void renderPictureTable() {
        columnID.setCellFactory(new Callback<TableColumn<Song, Integer>, TableCell<Song, Integer>>() {
            @Override
            public TableCell<Song, Integer> call(TableColumn<Song, Integer> param) {
                return new TableCell<Song, Integer>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Song song = getTableView().getItems().get(getIndex());

                            String imageUrl = song.getPictureURL();

                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Image image = new Image(new File(imageUrl).toURI().toString());

                                imageView.setTranslateX(20);
                                imageView.setFitHeight(30);
                                imageView.setFitWidth(30);

                                imageView.setImage(image);
                                setGraphic(imageView);
                            } else {
                                // Handle case when imageUrl is empty or null
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        });

    }

    private void setupPlistSongsTableView() {

        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnArtist.setCellValueFactory(new PropertyValueFactory<>("artistName"));

        renderPictureTable();
    }

    public static class AvatarCell extends TableCell<Song, String> {
        private final ImageView imageView = new ImageView();

        @Override
        protected void updateItem(String url, boolean empty) {
            super.updateItem(url, empty);

            if (url == null || empty) {
                setGraphic(null);
            } else {
                // Assuming you have an image path or URL associated with each ID
                Image image = new Image("path/to/your/image.jpg");
                imageView.setImage(image);
                setGraphic(imageView);
            }
        }
    }

    public void tablePlaylistSongsClick(Playlist p) throws Exception {
        if (p == null)
            return;

        playlistSongs = playlistModel.getObservableSongs(p);
        tblSongsPlaylist.setItems(playlistSongs);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("CALLED");
        setupPlistSongsTableView();
    }
}
