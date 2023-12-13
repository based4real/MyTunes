package mytunes.GUI.Controller.ny.Pages;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import mytunes.BE.Album;
import mytunes.BE.Artist;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.GUI.Controller.ny.Containers.BoxContainer;
import mytunes.GUI.Controller.ny.Custom.TitleArtistCell;
import mytunes.GUI.Controller.ny.MainWindowController;
import mytunes.GUI.Main;
import mytunes.GUI.Model.AlbumModel;
import mytunes.GUI.Model.ArtistModel;
import mytunes.GUI.Model.SongModel;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
    @FXML
    private HBox hboxAlbums;
    @FXML
    private HBox hboxArtists;
    @FXML
    private TextField txfSearchBar;
    @FXML
    private TableColumn<Song, Integer> columnID;
    @FXML
    private TableColumn<Song, String> columnTitle,columnGenre,columnDuration;
    @FXML
    private TableColumn columnAdded;
    @FXML
    private TableColumn<Album, String> columnAlbum;
    @FXML
    private TableView tblSongs;

    private SongModel songModel;
    private ArtistModel artistModel;
    private AlbumModel albumModel;

    private MainWindowController mainWindowController;
    private AnchorPane albumAnchorPane;
    private AlbumController albumController;

    public SearchController() throws Exception {
        songModel = SongModel.getInstance();
        artistModel = ArtistModel.getInstance();
        albumModel = AlbumModel.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupSongTableView();
        txfSearchBarListener();
        try {
            addArtists();
            addAlbums();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setupSongTableView() {
        ObservableList<Song> songs = songModel.getObservableSongs();
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnTitle.setCellFactory(col -> new TitleArtistCell());
        columnDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        columnGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        tblSongs.setItems(songs);
    }

    private void checkAlbumClick(Button btn, Album album) throws IOException {
        btn.setOnAction(e -> {
            mainWindowController.switchView(albumAnchorPane);
            try {
                albumController.tableAlbumSongs(album);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void loadAlbumView(BorderPane mainWindow) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/new/pages/Album.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();

        albumAnchorPane = anchorPane;
        albumController = fxmlLoader.getController();

        mainWindow.setCenter(anchorPane);
    }

    private void addArtists() throws Exception {
        List<Artist> allArtists = artistModel.getAllArtists();
        for (Artist a : allArtists) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/new/containers/Box.fxml"));

            Button button = fxmlLoader.load();
            BoxContainer boxContainer = fxmlLoader.getController();

            boxContainer.setHeader(a.getName());
            boxContainer.setDescription(a.getAlias());
            boxContainer.setImage(a.getPictureURL());
            boxContainer.setType("Artist");

            boxContainer.setSearchController(this);

            // Set userdata so can be used later to determine which
            // id was dragged to update in database.
            button.setUserData(boxContainer);

            // With 0 on, adds on top instead of bottom
            hboxArtists.getChildren().add(0, button);
        }
    }

    private void addAlbums() throws Exception {
        List<Album> allAlbums = albumModel.getAllAlbums();
        for (Album a : allAlbums) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/new/containers/Box.fxml"));

            Button button = fxmlLoader.load();
            BoxContainer boxContainer = fxmlLoader.getController();

            boxContainer.setHeader(a.getTitle());
            boxContainer.setDescription(a.getType());
            boxContainer.setImage(a.getPictureURL());
            boxContainer.setType("Album");

            boxContainer.setSearchController(this);

            // Set userdata so can be used later to determine which
            // id was dragged to update in database.
            button.setUserData(boxContainer);

            // With 0 on, adds on top instead of bottom
            hboxAlbums.getChildren().add(0, button);

            checkAlbumClick(button, a);
        }
    }


    private void txfSearchBarListener() {
        txfSearchBar.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                songModel.searchSong(newValue);
                searchForArtists();
                searchForAlbums();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void searchForArtists(){
        for (Node node : hboxArtists.getChildren()){
            if (node instanceof  Button existingButton){
                BoxContainer boxContainer = (BoxContainer) existingButton.getUserData();
                boolean visible = boxContainer.getHeader().contains(txfSearchBar.getText().toLowerCase());

                existingButton.setVisible(visible);
                existingButton.setManaged(visible);
            }
        }
    }

    private void searchForAlbums(){
        for (Node node : hboxAlbums.getChildren()){
            if (node instanceof  Button existingButton){
                BoxContainer boxContainer = (BoxContainer) existingButton.getUserData();
                boolean visible = boxContainer.getHeader().contains(txfSearchBar.getText().toLowerCase());

                existingButton.setVisible(visible);
                existingButton.setManaged(visible);
            }
        }
    }

    public void setMainWindowController(MainWindowController controller) {
        this.mainWindowController = controller;
    }
}
