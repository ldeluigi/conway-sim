package controller.io;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

import javax.imageio.ImageIO;

/**
 * Resource loader.
 */
public final class ResourceLoader {

    private static final String RES_DIR = "/";
    private static final String IMG_DIR = "img/";
    private static final Map<String, String> RESOURCE_MAP = new HashMap<>();
    private static final Map<String, Boolean> IS_BUFFERED = new HashMap<>();
    private static final Map<String, Image> IMG_BUFFER = new HashMap<>();

    static {
        RESOURCE_MAP.put("main.background", "bg_main.jpg");
        RESOURCE_MAP.put("main.title", "logo_main.png");
        RESOURCE_MAP.put("main.icon", "main_icon.png");
        RESOURCE_MAP.put("settings.background", "bg_main_blurred.jpg");
        RESOURCE_MAP.put("loading.background", "bg_main_blurred.jpg");
        RESOURCE_MAP.put("sandbox.background1", "bg_blank_blurred.jpg");
        RESOURCE_MAP.put("sandbox.background2", "corner.sandbox.jpg");
    }

    private ResourceLoader() {
    }

    /**
     * 
     * @param resource
     *            the resource tag to load
     * @return loaded image if found, or else throws IllegalStateException
     */
    public static Image loadImage(final String resource) {
        if (isBuffered(resource)) {
            return getBufferedImage(resource);
        }
        final String path = getImagePath(resource);
        try {
            final Image result = ImageIO.read(ResourceLoader.class.getResource(path));
            if (result == null) {
                throw new IllegalStateException("The image cannot be read.");
            }
            addBufferedImage(resource, result);
            return result;
        } catch (IOException e) {
            throw new IllegalStateException("Resource " + resource + " not found (or not accessible) in " + path);
        }
    }

    private static void addBufferedImage(final String resource, final Image result) {
        IS_BUFFERED.put(resource, true);
        IMG_BUFFER.put(resource, result);
    }

    private static Image getBufferedImage(final String resource) {
        return IMG_BUFFER.get(resource);
    }

    private static boolean isBuffered(final String resource) {
        return IS_BUFFERED.containsKey(resource) ? IS_BUFFERED.get(resource) : false;
    }

    private static String getImagePath(final String resource) {
        return RES_DIR + IMG_DIR + RESOURCE_MAP.get(resource);
    }

    /**
     * 
     * @param resource
     *            the resource tag to load
     * @param language
     *            a Locale representing the language of the required string
     * @return the string loaded or null
     */
    public static String loadString(final String resource, final Locale language) {
        final ResourceBundle labels = ResourceBundle.getBundle("LabelsBundle", language,
                Control.getControl(Control.FORMAT_PROPERTIES));
        return labels.getString(resource);
    }

    /**
     * 
     * @param resource
     *            the resource tag to load
     * @return the string loaded or null
     */
    public static String loadString(final String resource) {
        return loadString(resource, Locale.ROOT);
    }
}
