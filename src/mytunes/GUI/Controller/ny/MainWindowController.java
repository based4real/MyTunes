package mytunes.GUI.Controller.ny;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import mytunes.GUI.Controller.ny.Containers.LibraryContainer;
import mytunes.GUI.Controller.ny.Custom.ControlView;
import mytunes.GUI.Controller.ny.Pages.HomeController;
import mytunes.GUI.Controller.ny.Pages.SearchController;

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
            loadSearchSection();
            loadHomeSection();

            ControlView.setMainWindowController(this);
            ControlView.switchToHome();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadAlbumView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/new/pages/Album.fxml"));
        GridPane gridPane = fxmlLoader.load();

        mainWindow.setCenter(gridPane);
    }

    private void loadHomeSection() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/new/pages/Home.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();

        HomeController homeController = fxmlLoader.getController();

        ControlView.setHomeController(homeController);
    }

    private void loadLibrarySection() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/new/containers/Library.fxml"));
        VBox vbox = fxmlLoader.load();

        librarySection.getChildren().add(vbox);

        LibraryContainer libraryContainer = fxmlLoader.getController();
        libraryContainer.setMainWindowController(this);
        libraryContainer.LoadPlaylistSongsView(mainWindow);
    }

    private void loadArtistSection() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/new/pages/Artist.fxml"));
        GridPane gridPane = fxmlLoader.load();
    }

    private void loadMediaPlayerSection() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/new/containers/MediaPlayer.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();

        mainWindow.setBottom(anchorPane);
    }

    private void loadSearchSection() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/new/pages/Search.fxml"));
        GridPane gridPane = fxmlLoader.load();

        SearchController searchController = fxmlLoader.getController();
        searchController.loadAlbumView(mainWindow);
        searchController.loadArtistView(mainWindow);
        searchController.setMainWindowController(this);
    }

    public void btnHome(ActionEvent actionEvent) throws IOException {
        ControlView.switchToHome();
    }


    public void btnSearch(ActionEvent actionEvent) throws IOException {
        ControlView.switchToSearch();
    }
}
