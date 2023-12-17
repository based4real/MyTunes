package mytunes.GUI.Controller.Containers;

import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mytunes.BE.Playlist;
import mytunes.GUI.Controller.Pages.PlaylistController;
import mytunes.GUI.Controller.PopUp.NewPlaylistController;
import mytunes.GUI.Controller.Elements.ControlView;
import mytunes.GUI.Controller.MainWindowController;
import mytunes.GUI.Model.PlaylistModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LibraryContainer implements Initializable {

    @FXML
    private TextField txtPlaylistFilter;

    @FXML
    private VBox boxPlaylists;

    private PlaylistModel playlistModel;

    private PlaylistController playlistController;

    @FXML
    private GridPane playlistContainerPane;

    private MainWindowController mainWindowController;

    public LibraryContainer() throws Exception {
            playlistModel = PlaylistModel.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            addToPlaylist();
            txtPlaylistFilterListener();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setMainWindowController(MainWindowController controller) {
        mainWindowController = controller;
    }

    private void checkPlaylistClick(Button btn, Playlist p) throws IOException {
        btn.setOnAction(e -> {
            ControlView.switchToPlaylist();
            try {
                playlistController.tablePlaylistSongsClick(p);
                playlistController.setPlaylist(p);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void enableDragAndDrop(Button button) {

        button.setOnDragDetected(e -> {
            Dragboard dragboard = button.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(button.getText());
            dragboard.setContent(content);
            e.consume();
        });

        button.setOnDragOver(e -> {
            if (e.getGestureSource() != button && e.getDragboard().hasString())
                e.acceptTransferModes(TransferMode.MOVE);

            e.consume();
        });

        button.setOnDragDropped(e -> {
            Dragboard dragboard = e.getDragboard();
            boolean success = false;

            if (dragboard.hasString()) {
                // Find the index of the dragged button
                Button draggedButton = (Button) e.getGestureSource();

                // Find the button of the drop target
                Button dropButton = (Button) e.getGestureTarget();

                // Remove the dragged button from its previous position
                boxPlaylists.getChildren().remove(draggedButton);

                // Add the dragged button to the new position
                boxPlaylists.getChildren().add(boxPlaylists.getChildren().indexOf(dropButton), draggedButton);

                // Get the controller of dragged button
                PlaylistContainer draggedController = (PlaylistContainer) draggedButton.getUserData();
                Playlist playlistNew = draggedController.getPlaylist();

                // Get the controller of dropped button
                PlaylistContainer oldController = (PlaylistContainer) dropButton.getUserData();
                Playlist playlistOld = oldController.getPlaylist();

                //https://stackoverflow.com/questions/73119688/pressed-state-gets-stuck-after-drag-and-drop
                draggedButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("pressed"), false);

                // Update in database
                try {
                    playlistModel.updateOrder(playlistNew, playlistOld);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                success = true;
            }

            e.setDropCompleted(success);
            e.consume();
        });
    }

    public void removePlaylistButton(Playlist playlist) {
        for (Node node : boxPlaylists.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                PlaylistContainer playlistContainer = (PlaylistContainer) button.getUserData();
                if (playlistContainer.getPlaylist() == playlist) {
                    boxPlaylists.getChildren().remove(button);
                    break;
                }
            }
        }
    }

    public void addNewPlaylist(Playlist playlist) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/containers/Playlists.fxml"));

        Button button = fxmlLoader.load();
        PlaylistContainer playlistContainer = fxmlLoader.getController();

        playlistContainer.setPlaylist(playlist);
        playlistContainer.setParentController(this);

        // Set userdata so can be used later to determine which
        // id was dragged to update in database.
        button.setUserData(playlistContainer);

        // With 0 on, adds on top instead of bottom
        boxPlaylists.getChildren().add(0, button);

        enableDragAndDrop(button);
        checkPlaylistClick(button, playlist);
    }

    private void addToPlaylist() throws Exception {
        for (Playlist p : playlistModel.getPlaylists())
            addNewPlaylist(p);
    }

    public void LoadPlaylistSongsView(BorderPane mainWindow) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/pages/Playlist.fxml"));
        GridPane gridPane = fxmlLoader.load();

        playlistController = fxmlLoader.getController();
        ControlView.setPlaylistController(playlistController);

        mainWindow.setCenter(gridPane);
    }

    private void txtPlaylistFilterListener() {
        txtPlaylistFilter.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                searchForPlaylists();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void searchForPlaylists() {
        for (Node node : boxPlaylists.getChildren()) {
            if (node instanceof Button existingButton) {
                PlaylistContainer playlistContainer = (PlaylistContainer) existingButton.getUserData();
                boolean visible = playlistContainer.getPlaylistLabel().contains(txtPlaylistFilter.getText().toLowerCase());

                existingButton.setVisible(visible);
                existingButton.setManaged(visible);
            }
        }
    }

    public void openPlaylistWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/popup/NewPlaylist.fxml"));
            Parent root = loader.load();

            NewPlaylistController newPlaylistController = loader.getController();
            newPlaylistController.setParentController(this);

            newPlaylistController.setPlaylistModel(playlistModel);

            Stage stage = new Stage();
            stage.setTitle("Opret ny playlist");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (Exception e){
            System.out.println("Cant load import song window");
        }
    }

    @FXML
    private void menuCreatePlaylist(ActionEvent actionEvent) {
        openPlaylistWindow();
    }

    @FXML
    private void menuImportSong(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/popup/ImportSong.fxml"));

            Parent root = loader.load();
            Stage stage = new Stage();

            stage.setTitle("Importer sang");
            stage.setScene(new Scene(root));

            stage.show();
        }
        catch (Exception e){
            System.out.println(e);
            System.out.println("Cant load import song window");
        }
    }
}
