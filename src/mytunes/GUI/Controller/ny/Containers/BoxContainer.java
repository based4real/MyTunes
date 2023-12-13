package mytunes.GUI.Controller.ny.Containers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import mytunes.BE.Album;
import mytunes.BE.Artist;
import mytunes.GUI.Controller.ny.MainWindowController;
import mytunes.GUI.Controller.ny.Pages.HomeController;
import mytunes.GUI.Controller.ny.Pages.SearchController;
import mytunes.GUI.Model.AlbumModel;
import mytunes.GUI.Model.ArtistModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class BoxContainer {

    @FXML
    private ImageView coverImg;

    @FXML
    private Button btnBox;

    @FXML
    private Label lblHeader;

    @FXML
    private Label lblDescription;

    private SearchController searchController;

    private HomeController homeController;
    private ArtistModel artistModel;
    private AlbumModel albumModel;

    private String type; //Artist or Album

    public BoxContainer() throws Exception {
        artistModel = ArtistModel.getInstance();
        albumModel = AlbumModel.getInstance();
    }

    public void setDescription(String txt) {
        lblDescription.setText(txt);
    }

    public void setHeader(String txt) {
        lblHeader.setText(txt);
    }

    public String getHeader(){
        return lblHeader.getText().toLowerCase();
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImage(String URL) {
        Image newImage = new Image(new File(URL).toURI().toString());
        coverImg.setFitWidth(125);
        coverImg.setFitHeight(125);

        coverImg.setImage(newImage);
    }

    public void btnBoxAction(ActionEvent actionEvent) throws IOException {
    }

    public void setHomeController(HomeController controller) {
        this.homeController = controller;
    }

    public void setSearchController(SearchController controller){
        this.searchController = controller;
    }
}
