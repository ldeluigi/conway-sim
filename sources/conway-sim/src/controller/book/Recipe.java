package controller.book;

/**
 * Interface for a pattern of Conway's Game of Life with metadata.
 */
public interface Recipe {

    /**
     * Gives the name of the recipe.
     * 
     * @return name of recipe
     */
    String getName();

    /**
     * Sets the given string as the name of the recipe.
     * 
     * @param name
     *            of recipe
     */
    void setName(String name);

    /**
     * Sets the specified string as the content of the recipe.
     * 
     * @param content
     *            of recipe
     */
    void setContent(String content);

    /**
     * Gives the content of the recipe.
     * 
     * @return content of recipe
     */
    String getContent();

    /**
     * Gives the creator of the recipe.
     * 
     * @return author of recipe
     */
    String getAuthor();

    /**
     * Sets the creator of the recipe.
     * 
     * @param author
     *            of recipe
     */
    void setAuthor(String author);

}
