package mytunes.GUI.Controller.Elements;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import mytunes.BE.Album;
import mytunes.BE.Song;
import mytunes.GUI.Model.AlbumModel;

public class ClickableLabelTableCell<T> extends TableCell<T, String> {
    private final Label label;
    private AlbumModel albumModel;

    public ClickableLabelTableCell() throws Exception {
        this.label = new Label();
        this.albumModel = AlbumModel.getInstance();

        setHoverStyle(false, this.label);

        this.label.setOnMouseEntered(event -> setHoverStyle(true, this.label));
        this.label.setOnMouseExited(event -> setHoverStyle(false, this.label));

        this.label.setOnMouseClicked(event -> {
            Song song = (Song) getTableRow().getItem();
            if (song != null) {
                try {
                    loadAlbum(song);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void loadAlbum(Song song) throws Exception {
        Album getAlbum = albumModel.getAlbumFromSong(song);

        ControlView.switchToAlbum();
        ControlView.getAlbumController().tableAlbumSongs(getAlbum);
    }

    private void setHoverStyle(boolean isHovered, Label label) {
        label.setUnderline(isHovered);
        label.setStyle(isHovered ? "-fx-cursor: hand; -fx-text-fill: rgb(255, 255, 255)" : "-fx-cursor: default; -fx-text-fill: rgb(150, 150, 150)");
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            label.setText(item);
            setGraphic(label);
        }
    }
}
