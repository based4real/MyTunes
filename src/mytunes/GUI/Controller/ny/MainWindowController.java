package mytunes.GUI.Controller.ny;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    public VBox librarySection;
    public BorderPane mainWindow;

    @FXML
    private AnchorPane homeTab, playlistTab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadLibrarySection();
           // loadPlaylistSection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setPlaylistTab(AnchorPane pane) {
        playlistTab = pane;
    }

    public void setHomeTab(AnchorPane pane) {
        homeTab = pane;
    }

    public void switchView(Parent view) {
        mainWindow.setCenter(view);
    }

    private void loadLibrarySection() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/new/Library.fxml"));
        VBox vbox = fxmlLoader.load();

        librarySection.getChildren().add(vbox);

        LibraryController libraryController = fxmlLoader.getController();
        libraryController.setMainWindowController(this);
        libraryController.LoadPlaylistSongsView(mainWindow);
    }

    public void btnHome(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/new/HomeContainer.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();

        switchView(anchorPane);
    }
}
