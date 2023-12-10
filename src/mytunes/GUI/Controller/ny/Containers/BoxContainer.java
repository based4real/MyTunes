package mytunes.GUI.Controller.ny.Containers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mytunes.BE.Artist;
import mytunes.GUI.Controller.ny.Pages.HomeController;
import mytunes.GUI.Model.ArtistModel;

import java.net.URL;
import java.util.ResourceBundle;

public class BoxContainer implements Initializable {

    @FXML
    private ImageView coverImg;

    @FXML
    private Button btnBox;

    @FXML
    private Label lblHeader;

    @FXML
    private Label lblDescription;

    private HomeController homeController;
    private ArtistModel artistModel;

    public BoxContainer() throws Exception {
        artistModel = ArtistModel.getInstance();
    }

    public void setDescription(String txt) {
        lblDescription.setText(txt);
    }

    public void setHeader(String txt) {
        lblHeader.setText(txt);
    }

    public void setImage(String URL) {
        Image newImage = new Image(URL);
        coverImg.setImage(newImage);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void btnBoxAction(ActionEvent actionEvent) {
    }

    public void setParentController(HomeController controller) {
        this.homeController = controller;
    }
}
