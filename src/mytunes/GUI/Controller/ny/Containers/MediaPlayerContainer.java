package mytunes.GUI.Controller.ny.Containers;

import javafx.fxml.Initializable;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MediaPlayerContainer implements Initializable {

    public Slider sliderAudio;
    public Slider sliderTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sliderDesign();
    }

    public void sliderDesign() {

        sliderTime.valueProperty().addListener((obs, oldValue, newValue) -> {
            double percentage = 100.0 * newValue.doubleValue() / sliderTime.getMax();
            String style = String.format(Locale.US,
                    // in the String format,
                    // %1$.1f%% gives the first format argument ("1$"),
                    // i.e. percentage, formatted to 1 decimal place (".1f").
                    // Note literal % signs must be escaped ("%%")

                    "-track-color: linear-gradient(to right, " +
                            "-fx-accent 0%%, " +
                            "-fx-accent %1$.1f%%, " +
                            "-default-track-color %1$.1f%%, " +
                            "-default-track-color 100%%);",
                    percentage);
            sliderTime.setStyle(style);
        });

    }
}
