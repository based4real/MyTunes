package mytunes.GUI.Controller.Pages;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import mytunes.BE.Album;
import mytunes.BE.Artist;
import mytunes.BE.Song;
import mytunes.GUI.Controller.Elements.ClickableLabelTableCell;
import mytunes.GUI.Controller.Elements.TitleArtistCell;
import mytunes.GUI.Controller.Containers.BoxContainer;
import mytunes.GUI.Controller.Elements.ControlView;
import mytunes.GUI.Controller.Elements.TableContextMenu;
import mytunes.GUI.Controller.Elements.helpers.CustomButton;
import mytunes.GUI.Controller.MainWindowController;
import mytunes.GUI.Model.AlbumModel;
import mytunes.GUI.Model.ArtistModel;
import mytunes.GUI.Model.MediaPlayerModel;
import mytunes.GUI.Model.SongModel;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchController implements Initializable {

    @FXML
    public GridPane mainTab;

    @FXML
    private HBox hboxAlbums;
    @FXML
    private HBox hboxArtists;
    @FXML
    private TextField txfSearchBar;

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

    private ArtistController artistController;
    private AlbumController albumController;
    private MediaPlayerModel mediaPlayerModel;

    public SearchController() throws Exception {
        songModel = SongModel.getInstance();
        artistModel = ArtistModel.getInstance();
        albumModel = AlbumModel.getInstance();
        mediaPlayerModel = MediaPlayerModel.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setupSongTableView();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        txfSearchBarListener();

        ControlView.setSearchController(this);

        try {
            addArtists();
            addAlbums();
            enableRightClick();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void enableRightClick() {
        tblSongs.setRowFactory(tv -> {
            TableRow<Song> row = new TableRow<>();
            try {
                TableContextMenu tableContextMenu = new TableContextMenu(tblSongs, null);
                tableContextMenu.createContextMenu(row);
            } catch (Exception e) {
                System.out.println("Cannot create ContextMenu for Search page");
                throw new RuntimeException(e);
            }
            return row;
        });
    }

    private void setupSongTableView() throws Exception {
        ObservableList<Song> songs = songModel.getObservableSongs();

        columnTitle.setCellFactory(col -> {
            try {
                return new TitleArtistCell();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        columnDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        columnGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        columnAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
        columnAlbum.setCellFactory(column -> {
            try {
                return new ClickableLabelTableCell<>();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        tblSongs.setItems(songs);
        mediaPlayerModel.wasClickedTable(tblSongs);
    }


    private void addArtists() throws Exception {
        List<Artist> allArtists = artistModel.getAllArtists();
        CustomButton artistButton = new CustomButton(CustomButton.Type.ARTIST, hboxArtists, allArtists);
    }

    private void addAlbums() throws Exception {
        List<Album> allAlbums = albumModel.getAllAlbums();
        CustomButton albumButton = new CustomButton(CustomButton.Type.ALBUM, hboxAlbums, allAlbums);
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
