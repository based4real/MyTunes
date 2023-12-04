package mytunes.GUI.Controller.ny;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import mytunes.BE.Playlist;
import mytunes.GUI.Model.PlaylistModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LibraryController implements Initializable {

    @FXML
    private VBox boxPlaylists;

    private PlaylistModel playlistModel;

    private PlaylistContainer playlistContainer;

    public LibraryController() {
        try {
            playlistModel = new PlaylistModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkPlaylistClick(Button btn, Playlist p) throws IOException {
        btn.setOnAction(e -> {
            try {
                playlistContainer.tablePlaylistSongsClick(p);
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
                PlaylistController draggedController = (PlaylistController) draggedButton.getUserData();
                Playlist playlistNew = draggedController.getPlaylist();

                // Get the controller of dropped button
                PlaylistController oldController = (PlaylistController) dropButton.getUserData();
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


    private void addToPlaylist() throws Exception {
        for (Playlist p : playlistModel.getPlaylists()) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/new/Playlists.fxml"));

            Button button = fxmlLoader.load();
            PlaylistController playlistController = fxmlLoader.getController();
            playlistController.setPlaylist(p);

            // Set userdata so can be used later to determine which
            // id was dragged to update in database.
            button.setUserData(playlistController);

            boxPlaylists.getChildren().add(button);

            enableDragAndDrop(button);
            checkPlaylistClick(button, p);
        }
    }

    public void LoadPlaylistSongsView(BorderPane mainWindow) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/new/PlaylistContainer.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();

        playlistContainer = fxmlLoader.getController();

        mainWindow.setCenter(anchorPane);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            addToPlaylist();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
