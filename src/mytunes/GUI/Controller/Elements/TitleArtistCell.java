package mytunes.GUI.Controller.Elements;

import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import mytunes.BE.Artist;
import mytunes.BE.Song;
import mytunes.BLL.SongManager;
import mytunes.GUI.Model.SongModel;

import java.io.File;

public class TitleArtistCell extends TextFieldTableCell<Song, String>  {

    private final GridPane gridPane = new GridPane();
    private final VBox vBox = new VBox();
    private final Text titleLabel = new Text();
    private final Text artistLabel = new Text();
    private final ImageView imageView = new ImageView();

    private static final int IMAGE_SIZE = 41;

    private SongModel songModel;

    public TitleArtistCell() throws Exception {
        gridPane.add(imageView, 0, 0); // Image in the first column
        gridPane.add(vBox, 1, 0); // Title in the second column

        handleArtistClick();

        titleLabel.getStyleClass().add("playlist-column-title");
        imageView.setEffect(new DropShadow(10, Color.BLACK));

        songModel = SongModel.getInstance();
    }

    private void handleArtistClick() {
        // Check if hovering artistLabel and call setHoverStyle
        artistLabel.setOnMouseEntered(event -> setHoverStyle(true));
        artistLabel.setOnMouseExited(event -> setHoverStyle(false));

        // If artistLabel has been clicked on
        artistLabel.setOnMouseClicked(event -> {
            // Go to artist page or soemthing ??
            Song song = getTableRow().getItem();
            if (song == null)
                return;


            System.out.println("Clicked on artist: " + getTableRow().getItem().getArtistName());
            ControlView.switchToArtist();
            try {
                ControlView.getArtistController().updatePage(songModel.getArtistFromSong(song));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
    }

    // Used to check if we hover the artistLabel
    private void setHoverStyle(boolean isHovered) {
        artistLabel.setUnderline(isHovered);
        artistLabel.setStyle(isHovered ? "-fx-cursor: hand; -fx-fill: rgb(255, 255, 255)" : "-fx-cursor: default; -fx-fill: rgb(150, 150, 150)");
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
            return;
        }

        // Get song from row
        Song song = getTableRow().getItem();
        if (song == null)
            return;

        // Set up the image view for the picture
        String imageUrl = song.getPictureURL();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Image image = new Image(new File(imageUrl).toURI().toString());
            imageView.setFitHeight(IMAGE_SIZE);
            imageView.setFitWidth(IMAGE_SIZE);
            imageView.setImage(image);
        }

        titleLabel.setText(song.getTitle());
        artistLabel.setText(song.getArtistName());

        vBox.getChildren().clear();
        vBox.setTranslateX(20.0);

        vBox.getChildren().add(titleLabel);
        vBox.getChildren().add(artistLabel);

        setHoverStyle(false);
        setGraphic(gridPane);

    }
}
