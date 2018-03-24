package controller.io;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
/**
 *
 */
public final class ResourceLoader {

    private static final Image DEFAULT_IMAGE = null;
    private static final Map<String, String> RESOURCE_MAP = new HashMap<>();
    private static final Map<String, Boolean> IS_BUFFERED = new HashMap<>();
    private static final Map<String, Image> IMG_BUFFER = new HashMap<>();

    static { 
        RESOURCE_MAP.put("background.main", "/bg_main.jpg");
    }

    private ResourceLoader() { }

    /**
     * 
     * @param resource the resource tag to load
     * @return loaded image if found, or else throws ResourceNotFoundException
     */
    public static Image loadImage(final String resource) {
        final String path = getPath(resource);
        System.out.println(path);
        if (isBuffered(resource)) {
            return getBufferedImage(resource);
        }
        try {
            final Image result = ImageIO.read(ResourceLoader.class.getResource(path));
            addBufferedImage(resource, result);
            return result;
        } catch (IOException e) {
            return DEFAULT_IMAGE;
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
        return IS_BUFFERED.containsKey(resource);
    }

    private static String getPath(final String resource) {
        return RESOURCE_MAP.get(resource);
    }
}
