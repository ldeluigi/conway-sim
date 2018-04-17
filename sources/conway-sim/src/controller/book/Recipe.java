package controller.book;

/**
 * 
 *
 */
public interface Recipe {

    /**
     * The method which gives the name of the recipe.
     * 
     * @return name of recipe
     */
    String getName();

    /**
     * The method which sets the given string as the name of the recipe.
     * 
     * @param name
     *            of recipe
     */
    void setName(String name);

    /**
     * The method which sets the specified string as the content of the recipe.
     * 
     * @param content
     *            of recipe
     */
    void setContent(String content);

    /**
     * The method which gives the content of the recipe.
     * 
     * @return content of recipe
     */
    String getContent();

    /**
     * The method which gives the creator of the recipe.
     * 
     * @return author of recipe
     */
    String getAuthor();

    /**
     * The method which sets the creator of the recipe.
     * 
     * @param author
     *            of recipe
     */
    void setAuthor(String author);

}
