package controller.book;

/**
 * 
 *
 */
public interface Recipe {
    /**
     * @return name of recipe
     */
    String getName();
    /**
     * 
     * @param name of recipe
     */
    void setName(String name);
    /**
     * 
     * @param content of recipe
     */
    void setContent(String content);
    /**
     * 
     * @return content of recipe
     */
    String getContent();
    /**
     * 
     * @return author of recipe
     */
    String getAuthor();
    /**
     * 
     * @param author of recipe
     */
    void setAuthor(String author);


}
