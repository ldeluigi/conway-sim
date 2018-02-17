package core.model;

/**
 * A factory used to create {@link Environment}.
 */
public interface EnvironmentFactory {

    /**
     * A method that creates an {@link Environment} with the standard rules of Conway's Game of Life.
     * @param width of the Environment
     * @param height of the Environment
     * @return the Environment as specified
     */
    Environment standardRules(int width, int height);
}
