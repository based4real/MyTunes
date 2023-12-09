package mytunes.GUI.Controller.ny;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import mytunes.BE.Playlist;
import mytunes.BLL.util.CacheSystem;
import mytunes.GUI.Controller.ny.PopUp.EditPlaylistController;
import mytunes.GUI.Model.PlaylistModel;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class PlaylistController implements Initializable {

    @FXML
    private Label playlistName;

    @FXML
    private ImageView cover;

    @FXML
    private Button btnPlaylist;

    private static final String ICON_PLAYLIST = "M12 13c0 1.105-1.12 2-2.5 2S7 14.105 7 13s1.12-2 2.5-2 2.5.895 2.5 2 M12 3v10h-1V3z M11 2.82a1 1 0 0 1 .804-.98l3-.6A1 1 0 0 1 16 2.22V4l-5 1z M0 11.5a.5.5 0 0 1 .5-.5H4a.5.5 0 0 1 0 1H.5a.5.5 0 0 1-.5-.5m0-4A.5.5 0 0 1 .5 7H8a.5.5 0 0 1 0 1H.5a.5.5 0 0 1-.5-.5m0-4A.5.5 0 0 1 .5 3H8a.5.5 0 0 1 0 1H.5a.5.5 0 0 1-.5-.5";
    private static final String ICON_EDIT = "M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z";
    private static final String ICON_DELETE = "M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5M11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H2.506a.58.58 0 0 0-.01 0H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1h-.995a.59.59 0 0 0-.01 0zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47ZM8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5";


    private Playlist playlist;
    private LibraryController libraryController;

    private PlaylistModel playlistModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            playlistModel = new PlaylistModel();
            ContextMenu contextMenu = createContextMenu();

            // Attach the right click to the playlist button
            btnPlaylist.setOnContextMenuRequested(event -> contextMenu.show(btnPlaylist, event.getScreenX(), event.getScreenY()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setParentController(LibraryController controller) {
        this.libraryController = controller;
    }

    public void setPlaylist(Playlist playlist) {
        playlistName.setText(playlist.getName());
        setPicture(playlist);
        this.playlist = playlist;
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        // Add menu items to the context menu
        MenuItem menuItem1 = createSVGMenuItem("Opret playliste", ICON_PLAYLIST);

        MenuItem menuItem2 = createSVGMenuItem("Rediger oplysninger", ICON_EDIT);
        MenuItem menuItem3 = createSVGMenuItem("Slet", ICON_DELETE);

        // Separator
        SeparatorMenuItem separator = new SeparatorMenuItem();
        separator.getStyleClass().add("seperator");

        // Add actions for each menu item
        menuItem1.setOnAction(event -> createPlaylist());
        menuItem2.setOnAction(event -> editPlaylist());
        menuItem3.setOnAction(event -> {
            try {
                deletePlaylist();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Add menu items to the context menu
        contextMenu.getItems().addAll(menuItem1, separator, menuItem3, menuItem2);

        return contextMenu;
    }

    private MenuItem createSVGMenuItem(String text, String svgPath) {
        MenuItem menuItem = new MenuItem(text);
        SVGPath svgIcon = new SVGPath();

        svgIcon.setContent(svgPath);
        svgIcon.setScaleX(1);
        svgIcon.setScaleY(1);
        svgIcon.setFill(Color.WHITE);

        menuItem.setGraphic(svgIcon);
        return menuItem;
    }


    private void createPlaylist() {
        libraryController.openPlaylistWindow();
    }

    private void editPlaylist() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/new/popup/EditPlaylist.fxml"));
            Parent root = loader.load();

            EditPlaylistController editPlaylistController = loader.getController();
            editPlaylistController.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Rediger playliste");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (Exception e) {
            System.out.println(e);
            System.out.println("Cant load edit playlist window");
        }
    }

    private void deletePlaylist() throws Exception {
        playlistModel.deletePlaylist(playlist);
        libraryController.removePlaylistButton(playlist);
    }

    public String getPlaylistLabel() {
        return playlistName.getText();
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    private void setPicture(Playlist playlist) {
        String picture = playlist.getPictureURL();
        if (picture == null)
            return;

        Image image = new Image(new File(picture).toURI().toString());
        cover.setImage(image);
    }

}
