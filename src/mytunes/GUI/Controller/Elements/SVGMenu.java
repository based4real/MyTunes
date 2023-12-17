package mytunes.GUI.Controller.Elements;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class SVGMenu {

    private static final Color SVG_COLOR = Color.WHITE;
    private static final int SCALE_X = 1, SCALE_Y = 1;

    public MenuItem createSVGMenuItem(String text, String svgPath) {
        MenuItem menuItem = new MenuItem(text);
        SVGPath svgIcon = new SVGPath();

        svgIcon.setContent(svgPath);
        svgIcon.setScaleX(SCALE_X);
        svgIcon.setScaleY(SCALE_Y);
        svgIcon.setFill(SVG_COLOR);

        menuItem.setGraphic(svgIcon);
        return menuItem;
    }

    public Menu createSVGMenu(String text, String svgPath) {
        Menu menu = new Menu(text);
        SVGPath svgIcon = new SVGPath();

        svgIcon.setContent(svgPath);
        svgIcon.setScaleX(SCALE_X);
        svgIcon.setScaleY(SCALE_Y);
        svgIcon.setFill(SVG_COLOR);

        menu.setGraphic(svgIcon);
        return menu;
    }
}
