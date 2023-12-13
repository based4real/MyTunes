package mytunes.GUI.Controller.ny.Containers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.GUI.Controller.ny.Custom.SVGMenu;
import mytunes.GUI.Controller.ny.PopUp.EditPlaylistController;
import mytunes.GUI.Model.PlaylistModel;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PlaylistContainer implements Initializable {

    @FXML
    private GridPane imagePane;

    @FXML
    private Label playlistName, lblSongs;

    @FXML
    private ImageView cover;

    @FXML
    private Button btnPlaylist;

    private static final String ICON_PLAYLIST = "M12 13c0 1.105-1.12 2-2.5 2S7 14.105 7 13s1.12-2 2.5-2 2.5.895 2.5 2 M12 3v10h-1V3z M11 2.82a1 1 0 0 1 .804-.98l3-.6A1 1 0 0 1 16 2.22V4l-5 1z M0 11.5a.5.5 0 0 1 .5-.5H4a.5.5 0 0 1 0 1H.5a.5.5 0 0 1-.5-.5m0-4A.5.5 0 0 1 .5 7H8a.5.5 0 0 1 0 1H.5a.5.5 0 0 1-.5-.5m0-4A.5.5 0 0 1 .5 3H8a.5.5 0 0 1 0 1H.5a.5.5 0 0 1-.5-.5";
    private static final String ICON_EDIT = "M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z";
    private static final String ICON_DELETE = "M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5M11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H2.506a.58.58 0 0 0-.01 0H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1h-.995a.59.59 0 0 0-.01 0zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47ZM8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5";


    private Playlist playlist;
    private LibraryContainer libraryContainer;

    private PlaylistModel playlistModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            playlistModel = PlaylistModel.getInstance();
            ContextMenu contextMenu = createContextMenu();

            // Attach the right click to the playlist button
            btnPlaylist.setOnContextMenuRequested(event -> contextMenu.show(btnPlaylist, event.getScreenX(), event.getScreenY()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setParentController(LibraryContainer controller) {
        this.libraryContainer = controller;
    }

    public void setPlaylist(Playlist playlist) throws Exception {
        playlistName.setText(playlist.getName());
        setPicture(playlist);
        this.playlist = playlist;
        songCountLabel();
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        SVGMenu svgMenu = new SVGMenu();

        // Add menu items to the context menu
        MenuItem createPlaylist = svgMenu.createSVGMenuItem("Opret playliste", ICON_PLAYLIST);

        MenuItem editPlaylist = svgMenu.createSVGMenuItem("Rediger oplysninger", ICON_EDIT);
        MenuItem deletePlaylist = svgMenu.createSVGMenuItem("Slet", ICON_DELETE);

        // Separator
        SeparatorMenuItem separator = new SeparatorMenuItem();

        // Add actions for each menu item
        createPlaylist.setOnAction(event -> createPlaylist());
        editPlaylist.setOnAction(event -> editPlaylist());
        deletePlaylist.setOnAction(event -> {
            try {
                deletePlaylist();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Add menu items to the context menu
        contextMenu.getItems().addAll(createPlaylist, separator, editPlaylist, deletePlaylist);

        return contextMenu;
    }


    private void createPlaylist() {
        libraryContainer.openPlaylistWindow();
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

    private void songCountLabel() throws Exception {
        int size = playlistModel.getSongs(playlist).size();
        lblSongs.setText(size > 0 ? "Antal sange: " + Integer.toString(size) : "Tom");
    }

    private void deletePlaylist() throws Exception {
        playlistModel.deletePlaylist(playlist);
        libraryContainer.removePlaylistButton(playlist);
    }

    public String getPlaylistLabel() {
        return playlistName.getText();
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    private boolean setCustomPicture(Playlist playlist) throws Exception {
        List<Song> playlistSongs = playlistModel.getSongs(playlist);
        boolean customPic = playlistSongs.size() > 3;

        if (customPic) {
            List<String> pictures = new ArrayList<>();

            pictures.add(playlistSongs.get(0).getPictureURL());
            pictures.add(playlistSongs.get(1).getPictureURL());
            pictures.add(playlistSongs.get(2).getPictureURL());
            pictures.add(playlistSongs.get(3).getPictureURL());

            int columns = 2;
            int rowIndex = 0;
            int colIndex = 0;

            for (String picture : pictures) {
                Image image = new Image(new File(picture).toURI().toString());

                // Create ImageView
                ImageView imageView = new ImageView(image);

                // Set the size of ImageView (optional)
                imageView.setFitWidth(25);
                imageView.setFitHeight(25);

                // Add ImageView to the GridPane
                imagePane.add(imageView, colIndex, rowIndex);

                // Update column and row indices
                colIndex++;
                if (colIndex >= columns) {
                    colIndex = 0;
                    rowIndex++;
                }
            }
            return true;

        }
        return false;
    }

    private void setPicture(Playlist playlist) throws Exception {
        if (setCustomPicture(playlist))
            return;

        String picture = playlist.getPictureURL();

        if (picture == null)
            return;

        Image image = new Image(new File(picture).toURI().toString());
        ImageView imageView = new ImageView(image);

        imagePane.getChildren().add(imageView);
    }

}
