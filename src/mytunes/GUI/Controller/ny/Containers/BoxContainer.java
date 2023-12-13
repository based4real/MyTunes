package mytunes.GUI.Controller.ny.Containers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mytunes.GUI.Controller.ny.Pages.HomeController;
import mytunes.GUI.Controller.ny.Pages.SearchController;
import mytunes.GUI.Model.ArtistModel;

import java.io.File;
import java.net.URL;
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

    public BoxContainer() throws Exception {
        artistModel = ArtistModel.getInstance();
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
    public void setImage(String URL) {
        Image newImage = new Image(new File(URL).toURI().toString());
        coverImg.setFitWidth(125);
        coverImg.setFitHeight(125);

        coverImg.setImage(newImage);
    }

    public void btnBoxAction(ActionEvent actionEvent) {
        
    }

    public void setHomeController(HomeController controller) {
        this.homeController = controller;
    }

    public  void setSearchController(SearchController controller){
        this.searchController = controller;
    }
}
