package mytunes.GUI.Controller.ny;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mytunes.BE.Playlist;

import java.net.URL;
import java.util.ResourceBundle;

public class PlaylistController implements Initializable {

    @FXML
    private Label playlistName;

    @FXML
    private ImageView cover;

    private Playlist playlist;

    @FXML
    private Button btnPlaylist;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ContextMenu contextMenu = createContextMenu();

        // Attach the context menu to the playlist button
        btnPlaylist.setOnContextMenuRequested(event -> contextMenu.show(btnPlaylist, event.getScreenX(), event.getScreenY()));
    }

    public void setPlaylist(Playlist playlist) {
        playlistName.setText(playlist.getName());
        setPicture(playlist);
        this.playlist = playlist;
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        // Add menu items to the context menu
        MenuItem menuItem1 = new MenuItem("Play");
        MenuItem menuItem2 = new MenuItem("Edit");
        MenuItem menuItem3 = new MenuItem("Delete");

        // Add actions for each menu item
        menuItem1.setOnAction(event -> playAction());
        menuItem2.setOnAction(event -> editAction());
        menuItem3.setOnAction(event -> deleteAction());

        // Add menu items to the context menu
        contextMenu.getItems().addAll(menuItem1, menuItem2, menuItem3);

        return contextMenu;
    }

    private void playAction() {
        System.out.println("Play action");
    }

    private void editAction() {
        System.out.println("Edit action");
    }

    private void deleteAction() {
        System.out.println("Delete action");
    }


    public String getplaylistLabel() {
        return playlistName.getText();
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    private void setPicture(Playlist playlist) {
        Image image = new Image("https://user-images.githubusercontent.com/24848110/33519396-7e56363c-d79d-11e7-969b-09782f5ccbab.png");
        cover.setImage(image);
    }

}
