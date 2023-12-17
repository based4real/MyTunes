package mytunes.GUI.Controller.Pages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import mytunes.BE.Album;
import mytunes.BE.Artist;
import mytunes.BE.Song;
import mytunes.GUI.Controller.Elements.ClickableLabelTableCell;
import mytunes.GUI.Controller.Elements.TableContextMenu;
import mytunes.GUI.Controller.Elements.TitleArtistCell;
import mytunes.GUI.Controller.Elements.ControlView;
import mytunes.GUI.Controller.Elements.helpers.CustomButton;
import mytunes.GUI.Model.ArtistModel;
import mytunes.GUI.Model.MediaPlayerModel;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ArtistController implements Initializable {

    @FXML
    public GridPane mainTab;


    public GridPane imagePane;
    public ImageView imgCover;

    @FXML
    private Label lblType, lblName, lblAlias;

    @FXML
    private TableView tblSongs;

    @FXML
    private TableColumn columnTitle;

    @FXML
    private TableColumn columnGenre;

    @FXML
    private TableColumn columnDuration;

    @FXML
    private TableColumn columnAlbum;


    @FXML
    private HBox hboxAlbums;

    private ArtistModel artistModel;
    private MediaPlayerModel mediaPlayerModel;

    private Album currentAlbum;
    private Artist currentArtist;

    public ArtistController() throws Exception {
        artistModel = ArtistModel.getInstance();
        mediaPlayerModel = MediaPlayerModel.getInstance();
        ControlView.setArtistController(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControlView.setArtistController(this);

        setupArtistSongsTableView();
        enableRightClick();
    }

    private void enableRightClick() {
        tblSongs.setRowFactory(tv -> {
            TableRow<Song> row = new TableRow<>();
            try {
                TableContextMenu tableContextMenu = new TableContextMenu(tblSongs, null);
                tableContextMenu.createContextMenu(row);
            } catch (Exception e) {
                System.out.println("Cannot create ContextMenu for Playlists");
                throw new RuntimeException(e);
            }
            return row;
        });
    }

    public void updatePage(Artist artist) throws Exception {
        this.currentArtist = artist;

        //Update UI
        tableSongsUpdate(artist);
        addAlbums(artist);
    }

    private void setupArtistSongsTableView() {
        columnTitle.setCellFactory(col -> {
            try {
                return new TitleArtistCell();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        columnGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        columnDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        columnAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));

        columnAlbum.setCellFactory(column -> {
            try {
                return new ClickableLabelTableCell<>();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });    }

    private void updatePicture(Artist artist) {
        Image newImage = new Image(new File(artist.getPictureURL()).toURI().toString());
        imgCover.setFitWidth(220);
        imgCover.setFitHeight(220);
        imgCover.setPreserveRatio(false);

        imgCover.setImage(newImage);
    }


    private void updateUI(Artist artist) {
        lblName.setText(artist.getName());
        lblAlias.setText(artist.getAlias());
        updatePicture(artist);
    }

    public void tableSongsUpdate(Artist artist) throws Exception {
        if (artist == null)
            return;

        tblSongs.refresh();
        tblSongs.setItems(artistModel.getArtistObservableSongs(artist));
        mediaPlayerModel.wasClickedTable(tblSongs);

        updateUI(artist);
    }

    private void addAlbums(Artist artist) throws Exception {
        List<Album> allAlbums = artistModel.getArtistAlbums(artist);
        CustomButton albumButton = new CustomButton(CustomButton.Type.ALBUM, hboxAlbums, allAlbums);
    }

    public void btnPageBack(ActionEvent actionEvent) {
    }

    public void btnNextPage(ActionEvent actionEvent) {
    }

    public void btnPlayPlaylist(ActionEvent actionEvent) {
    }

}
