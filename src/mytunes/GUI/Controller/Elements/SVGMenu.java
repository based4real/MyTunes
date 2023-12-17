package mytunes.GUI.Controller.Elements;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class SVGMenu {
    public MenuItem createSVGMenuItem(String text, String svgPath) {
        MenuItem menuItem = new MenuItem(text);
        SVGPath svgIcon = new SVGPath();

        svgIcon.setContent(svgPath);
        svgIcon.setScaleX(1);
        svgIcon.setScaleY(1);
        svgIcon.setFill(Color.WHITE);

        menuItem.setGraphic(svgIcon);
        return menuItem;
    }

    public Menu createSVGMenu(String text, String svgPath) {
        Menu menu = new Menu(text);
        SVGPath svgIcon = new SVGPath();

        svgIcon.setContent(svgPath);
        svgIcon.setScaleX(1);
        svgIcon.setScaleY(1);
        svgIcon.setFill(Color.WHITE);

        menu.setGraphic(svgIcon);
        return menu;
    }
}
