package controller.book;

import java.util.Random;
/**
 * 
 *
 */
public abstract class RecipeImpl implements Recipe {

    private String name;
    private String content;
    private String author;
    private Random rand = new Random();
    /**
     * 
     * @param name of recipe
     * @param content of recipe
     * @param author of recipe
     */
    public RecipeImpl(final String content, final String name, final String author) {
        this.setName(name);
        this.setContent(content);
        this.setAuthor(author);
    }
    /**
     * 
     * @param content of recipe
     * @param name of recipe
     */
    public RecipeImpl(final String content, final String name) {
        this.setName(name);
        this.setContent(content);
        this.setAuthor("AuthorNotSet:" + rand.nextInt());
    }
    /**
     * 
     * @param content of recipe
     */
    public RecipeImpl(final String content) {
        this.setName("NameNotSet:" + rand.nextInt());
        this.setContent(content);
        this.setAuthor("AuthorNotSet:" + rand.nextInt());
    }
    /**
     * @return name of recipe
     */
    public String getName() {
        // TODO Auto-generated method stub
        return this.name;
    }

    /**
     * 
     * @param name of recipe
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * 
     * @return content of recipe
     */
    public String getContent() {
        return content;
    }
    /**
     * 
     * @param content of recipe
     */
    public void setContent(final String content) {
        this.content = content;
    }
    /**
     * 
     * @return author of recipe
     */
    public String getAuthor() {
        return author;
    }
    /**
     * 
     * @param author of recipe
     */
    public void setAuthor(final String author) {
        this.author = author;
    }

}
