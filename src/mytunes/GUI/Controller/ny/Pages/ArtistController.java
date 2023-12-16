package mytunes.GUI.Controller.ny.Pages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import mytunes.BE.Album;
import mytunes.BE.Artist;
import mytunes.GUI.Controller.ny.Custom.ControlView;
import mytunes.GUI.Controller.ny.Custom.TitleArtistCell;

import java.io.File;
import java.net.URL;
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
    private TableColumn columnPos;

    @FXML
    private TableColumn columnTitle;

    @FXML
    private TableColumn columnGenre;

    @FXML
    private TableColumn columnDuration;

    @FXML
    private HBox boxAlbums;

    public ArtistController() {
        ControlView.setArtistController(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupArtistSongsTableView();
    }

    private void setupArtistSongsTableView() {
        columnTitle.setCellFactory(col -> new TitleArtistCell());
        columnGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        columnDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        columnPos.setCellValueFactory(new PropertyValueFactory<>("orderID"));
    }

    private void updatePicture(Artist artist) {
        Image newImage = new Image(new File(artist.getPictureURL()).toURI().toString());
        imgCover.setFitWidth(220);
        imgCover.setFitHeight(220);
        imgCover.setPreserveRatio(false);

        imgCover.setImage(newImage);
    }


    public void updateUI(Artist artist) {
        lblName.setText(artist.getName());
        lblAlias.setText(artist.getAlias());
        updatePicture(artist);
    }

    public void btnPageBack(ActionEvent actionEvent) {
    }

    public void btnNextPage(ActionEvent actionEvent) {
    }

    public void btnPlayPlaylist(ActionEvent actionEvent) {
    }

}
