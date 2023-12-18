package mytunes.GUI.Controller.Elements;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import mytunes.BE.Album;
import mytunes.BE.Artist;
import mytunes.GUI.Controller.Containers.BoxContainer;
import mytunes.GUI.Controller.Elements.Helpers.ControlView;

import java.io.IOException;
import java.util.List;

public class CustomButton {

    public enum Type {
        ALBUM, ARTIST
    }

    public CustomButton(Type type, HBox addTo, List<?> list) throws Exception {
        switch(type) {
            case ALBUM -> createAlbum(addTo, (List<Album>) list);
            case ARTIST -> createArtist(addTo, (List<Artist>) list);
        }
    }

    private void checkArtistClick(Button btn, Artist artist) throws Exception {
        btn.setOnAction(e -> {
            ControlView.switchToArtist();
            try {
                ControlView.getArtistController().updatePage(artist);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void createArtist(HBox addTo, List<Artist> list) throws Exception {
        addTo.getChildren().clear();
        for (Artist a : list) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/containers/Box.fxml"));

            Button button = fxmlLoader.load();
            BoxContainer boxContainer = fxmlLoader.getController();

            boxContainer.setHeader(a.getName());
            boxContainer.setDescription(a.getAlias());
            boxContainer.setImage(a.getPictureURL());

            // Set userdata so can be used later to determine which
            // id was dragged to update in database.
            button.setUserData(boxContainer);

            // With 0 on, adds on top instead of bottom
            addTo.getChildren().add(0, button);

            checkArtistClick(button, a);
        }
    }

    private void checkAlbumClick(Button btn, Album album) throws IOException {
        btn.setOnAction(e -> {
            ControlView.switchToAlbum();
            try {
                ControlView.getAlbumController().tableAlbumSongs(album);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void createAlbum(HBox addTo, List<Album> list) throws IOException {
        addTo.getChildren().clear();
        for (Album a : list) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/containers/Box.fxml"));

            Button button = fxmlLoader.load();
            BoxContainer boxContainer = fxmlLoader.getController();

            boxContainer.setHeader(a.getTitle());
            boxContainer.setDescription(a.getType());
            boxContainer.setImage(a.getPictureURL());

            // Set userdata so can be used later to determine which
            // id was dragged to update in database.
            button.setUserData(boxContainer);

            // With 0 on, adds on top instead of bottom
            addTo.getChildren().add(0, button);

            checkAlbumClick(button, a);
        }

    }
}
