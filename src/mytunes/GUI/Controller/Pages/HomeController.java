package mytunes.GUI.Controller.Pages;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import mytunes.BE.Artist;
import mytunes.GUI.Controller.Containers.BoxContainer;
import mytunes.GUI.Controller.Elements.ControlView;
import mytunes.GUI.Model.ArtistModel;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    public AnchorPane mainTab;

    @FXML
    private HBox hboxArtists;

    private ArtistModel artistModel;


    public HomeController() throws Exception {
        artistModel = ArtistModel.getInstance();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            addArtist();
            ControlView.setHomeController(this);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addArtist() throws Exception {
        List<Artist> allArtist = artistModel.getAllArtists();
        for (Artist a : allArtist) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/containers/Box.fxml"));

            Button button = fxmlLoader.load();
            BoxContainer boxContainer = fxmlLoader.getController();

            boxContainer.setHeader(a.getName());
            boxContainer.setDescription(a.getAlias());
            boxContainer.setImage(a.getPictureURL());

            boxContainer.setHomeController(this);

            // Set userdata so can be used later to determine which
            // id was dragged to update in database.
            button.setUserData(boxContainer);

            // With 0 on, adds on top instead of bottom
            hboxArtists.getChildren().add(0, button);
        }
    }
}
