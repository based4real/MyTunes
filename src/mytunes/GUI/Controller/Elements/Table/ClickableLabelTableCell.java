package mytunes.GUI.Controller.Elements.Table;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import mytunes.BE.Album;
import mytunes.BE.Song;
import mytunes.GUI.Controller.Elements.Helpers.ControlView;
import mytunes.GUI.Model.AlbumModel;
import mytunes.GUI.Model.SongModel;

public class ClickableLabelTableCell<T> extends TableCell<T, String> {
    private final Label label;
    private AlbumModel albumModel;
    private SongModel songModel;

    public enum Types {
        ALBUM, GENRE
    }

    public ClickableLabelTableCell(Types type) throws Exception {
        this.label = new Label();
        this.albumModel = AlbumModel.getInstance();
        this.songModel = SongModel.getInstance();

        setHoverStyle(false, this.label);

        this.label.setOnMouseEntered(event -> setHoverStyle(true, this.label));
        this.label.setOnMouseExited(event -> setHoverStyle(false, this.label));

        this.label.setOnMouseClicked(event -> {
            Song song = (Song) getTableRow().getItem();
            if (song != null) {
                try {
                    switch (type) {
                        case ALBUM -> loadAlbum(song);
                        case GENRE -> loadGenre(song);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void loadGenre(Song song) throws Exception {
        ControlView.switchToAlbum();
        ControlView.getAlbumController().tableGenreSongs(song.getGenre());
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
