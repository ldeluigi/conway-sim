package controller.io;

import java.awt.Image;
import java.io.File;
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
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            return DEFAULT_IMAGE;
        }
    }

    private static String getPath(final String resource) {
        return RESOURCE_MAP.get(resource);
    }
}
