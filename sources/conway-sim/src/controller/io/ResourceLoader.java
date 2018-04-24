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
 * Resource loader. Set the default {@link Locale} to use it as default language
 * of string properties loading.
 */
public final class ResourceLoader {

    private static final String RES_DIR = "/";
    private static final String IMG_DIR = "img/";
    private static final Map<String, String> RESOURCE_MAP = new HashMap<>();
    private static final Map<String, Image> IMG_BUFFER = new HashMap<>();
    private static final Locale[] LOCALE_LIST;

    static {
        Locale.setDefault(Locale.ENGLISH);
        LOCALE_LIST = new Locale[] { Locale.ITALIAN, Locale.ENGLISH };

        RESOURCE_MAP.put("main.background", "bg_main.jpg");
        RESOURCE_MAP.put("main.title", "logo_main.png");
        RESOURCE_MAP.put("main.icon", "main_icon.png");
        RESOURCE_MAP.put("settings.background", "bg_main_blurred.jpg");
        RESOURCE_MAP.put("loading.background", "bg_main_blurred.jpg");
        RESOURCE_MAP.put("sandbox.background1", "bg_blank_blurred.jpg");
        RESOURCE_MAP.put("sandbox.background2", "corner.sandbox.jpg");
        RESOURCE_MAP.put("sandbox.button.on", "button_pixel.png");
        RESOURCE_MAP.put("sandbox.button.off", "button_pixel_off.png");
        RESOURCE_MAP.put("sandbox.button.pressed", "button_pixel_pressed.png");
        RESOURCE_MAP.put("menu.arrow.left.on", "arrow_left.png");
        RESOURCE_MAP.put("menu.arrow.left.off", "arrow_left_off.png");
        RESOURCE_MAP.put("menu.arrow.left.pressed", "arrow_left_pressed.png");
        RESOURCE_MAP.put("menu.arrow.right.on", "arrow_right.png");
        RESOURCE_MAP.put("menu.arrow.right.off", "arrow_right_off.png");
        RESOURCE_MAP.put("menu.arrow.right.pressed", "arrow_right_pressed.png");
        RESOURCE_MAP.put("level.complete.background", "bg_level_complete.jpg");
        RESOURCE_MAP.put("level.complete.egg", "bg_level_complete_egg.jpg");
    }

    private ResourceLoader() {
    }

    /**
     * Loader of images.
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
        IMG_BUFFER.put(resource, result);
    }

    private static Image getBufferedImage(final String resource) {
        return IMG_BUFFER.get(resource);
    }

    private static boolean isBuffered(final String resource) {
        return IMG_BUFFER.containsKey(resource);
    }

    private static String getImagePath(final String resource) {
        return RES_DIR + IMG_DIR + RESOURCE_MAP.get(resource);
    }

    /**
     * Loader of strings of specified language.
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
     * Loader of strings with default language.
     * 
     * @param resource
     *            the resource tag to load
     * @return the string loaded or null
     */
    public static String loadString(final String resource) {
        return loadString(resource, Locale.getDefault());
    }

    /**
     * Returns an integer constant corresponding to the resource requested.
     * 
     * @param constant
     *            the constant
     * @return the constant value
     */
    public static int loadConstantInt(final String constant) {
        final ResourceBundle value = ResourceBundle.getBundle("ConstantBundle", Locale.ROOT,
                Control.getControl(Control.FORMAT_PROPERTIES));
        return Integer.parseInt(value.getString(constant).replaceAll("[^0-9]", ""));
    }

    /**
     * @return an array of available {@link Locale}
     */
    public static Locale[] getLocales() {
        return ResourceLoader.LOCALE_LIST;
    }
}
