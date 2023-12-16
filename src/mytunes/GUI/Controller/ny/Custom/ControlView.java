package mytunes.GUI.Controller.ny.Custom;

import javafx.scene.Parent;
import mytunes.BE.Album;
import mytunes.GUI.Controller.ny.MainWindowController;
import mytunes.GUI.Controller.ny.Pages.*;
import mytunes.GUI.Main;

public class ControlView {

    private static MainWindowController mainWindowController;
    private static PlaylistController playlistController;
    private static AlbumController albumController;
    private static SearchController searchController;
    private static HomeController homeController;
    private static ArtistController artistController;

    public static void setMainWindowController(MainWindowController controller) {
        mainWindowController = controller;
    }

    public static void setPlaylistController(PlaylistController controller) {
        playlistController = controller;
    }

    public static void setAlbumController(AlbumController controller) {
        albumController = controller;
    }

    public static void setSearchController(SearchController controller) {
        searchController = controller;
    }

    public static void setHomeController(HomeController controller) {
        homeController = controller;
    }

    public static void setArtistController(ArtistController controller) {
        artistController = controller;
    }

    private static void setMain(Parent view) {
        mainWindowController.mainWindow.setCenter(view);
    }

    public static MainWindowController getMainWindowController() {
        return mainWindowController;
    }

    public static PlaylistController getPlaylistController() {
        return playlistController;
    }

    public static AlbumController getAlbumController() {
        return albumController;
    }

    public static SearchController getSearchController() {
        return searchController;
    }

    public static HomeController getHomeController() {
        return homeController;
    }

    public static ArtistController getArtistController() {
        return artistController;
    }

    public static void switchToHome() {
        setMain(homeController.mainTab);
    }

    public static void switchToAlbum() {
        setMain(albumController.mainTab);
    }

    public static void switchToPlaylist() {
        setMain(playlistController.mainTab);
    }

    public static void switchToSearch() {
        setMain(searchController.mainTab);
    }

    public static void switchToArtist() {
        setMain(artistController.mainTab);
    }
}
