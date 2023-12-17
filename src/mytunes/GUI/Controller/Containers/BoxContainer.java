package mytunes.GUI.Controller.Containers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mytunes.GUI.Model.AlbumModel;
import mytunes.GUI.Model.ArtistModel;

import java.io.File;
import java.io.IOException;

public class BoxContainer {

    @FXML
    private ImageView coverImg;

    @FXML
    private Label lblHeader;

    @FXML
    private Label lblDescription;

    private ArtistModel artistModel;
    private AlbumModel albumModel;


    public BoxContainer() throws Exception {
        artistModel = ArtistModel.getInstance();
        albumModel = AlbumModel.getInstance();
    }

    public void setDescription(String txt) {
        lblDescription.setText(txt);
    }

    public void setHeader(String txt) {
        lblHeader.setText(txt);
    }

    public String getHeader(){
        return lblHeader.getText().toLowerCase();
    }

    public void setImage(String URL) {
        Image newImage = new Image(new File(URL).toURI().toString());
        coverImg.setFitWidth(125);
        coverImg.setFitHeight(125);
        coverImg.setPreserveRatio(false);

        coverImg.setImage(newImage);
    }

    public void btnBoxAction(ActionEvent actionEvent) throws IOException {
    }
}
