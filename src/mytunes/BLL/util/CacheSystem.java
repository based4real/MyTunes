package mytunes.BLL.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CacheSystem {

    private static final String CACHE_DIR = "image_cache/";

    public String storeImage(String url) {
        if (url == null)
            return null;

        try {
            // Check if the image is already cached locally
            String fileName = getFileNameFromUrl(url);
            String fileExtension = getFileExtension(url);

            File cacheFile = new File(CACHE_DIR + fileName);

            // If exist return the absolute path of file
            if (cacheFile.exists()) {
                BufferedImage cachedImage = ImageIO.read(cacheFile);
                return cacheFile.getAbsolutePath();
            } else {
                // If not exists download it and send back the path
                BufferedImage downloadedImage = downloadImage(url);
                saveToCache(downloadedImage, fileName, fileExtension);

                return cacheFile.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Creates a BufferedImage we can use to cache
    private static BufferedImage downloadImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        return ImageIO.read(url);
    }

    // Checks if folder exists or not
    private static void createFolder() {
        File cacheDirectory = new File(CACHE_DIR);
        if (!cacheDirectory.exists())
            cacheDirectory.mkdir();
    }

    // Creates folder if not exist and downloads file to path
    private static void saveToCache(BufferedImage image, String fileName, String fileExtension) throws IOException {
        createFolder();

        File cacheFile = new File(CACHE_DIR + fileName);
        ImageIO.write(image, fileExtension, cacheFile);
    }

    // Gets the fil extension... jpg, png etc.
    private static String getFileExtension(String url) {
        int lastDotIndex = url.lastIndexOf('.');

        if (lastDotIndex > 0)
            return url.substring(lastDotIndex + 1);

        return null;
    }

    // Gets the filename from url all example /picturename.png
    private static String getFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

}
