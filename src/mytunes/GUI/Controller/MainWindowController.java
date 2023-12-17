package mytunes.GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import mytunes.GUI.Controller.Containers.LibraryContainer;
import mytunes.GUI.Controller.Pages.HomeController;
import mytunes.GUI.Controller.Pages.SearchController;
import mytunes.GUI.Controller.Elements.ControlView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    private VBox librarySection;

    @FXML
    public BorderPane mainWindow;

    @FXML
    private VBox mainSection;

    @FXML
    private VBox mainShit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadLibrarySection();
            loadMediaPlayerSection();
            loadAlbumView();
            loadArtistView();
            loadSearchSection();
            loadHomeSection();

            ControlView.setMainWindowController(this);
            ControlView.switchToHome();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAlbumView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/pages/Album.fxml"));
        GridPane gridPane = fxmlLoader.load();

        mainWindow.setCenter(gridPane);
    }

    private void loadArtistView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/pages/Artist.fxml"));
        GridPane gridPane = fxmlLoader.load();

        mainWindow.setCenter(gridPane);
    }

    private void loadHomeSection() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/pages/Home.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();

        HomeController homeController = fxmlLoader.getController();

        ControlView.setHomeController(homeController);
    }

    private void loadLibrarySection() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/containers/Library.fxml"));
        VBox vbox = fxmlLoader.load();

        librarySection.getChildren().add(vbox);

        LibraryContainer libraryContainer = fxmlLoader.getController();
        libraryContainer.setMainWindowController(this);
        libraryContainer.LoadPlaylistSongsView(mainWindow);
    }

    private void loadArtistSection() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/pages/Artist.fxml"));
        GridPane gridPane = fxmlLoader.load();
    }

    private void loadMediaPlayerSection() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/containers/MediaPlayer.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();

        mainWindow.setBottom(anchorPane);
    }

    private void loadSearchSection() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/pages/Search.fxml"));
        GridPane gridPane = fxmlLoader.load();

        SearchController searchController = fxmlLoader.getController();
        searchController.setMainWindowController(this);
    }

    public void btnHome(ActionEvent actionEvent) throws IOException {
        ControlView.switchToHome();
    }

    public void btnSearch(ActionEvent actionEvent) throws IOException {
        ControlView.switchToSearch();
    }
}
