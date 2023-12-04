package mytunes.GUI.Controller.ny;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MediaPlayerController implements Initializable {

    public Slider sliderAudio;
    public Slider sliderTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sliderDesign();
    }

    public void sliderDesign() {


    }
}
