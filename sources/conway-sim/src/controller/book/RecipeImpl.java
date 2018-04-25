package controller.book;

import java.util.Random;

//TODO JAVADOOOOC!!
/**
 * 
 *
 */
public class RecipeImpl implements Recipe {

    private String name;
    private String content;
    private String author;
    private final Random rand = new Random();

    /**
     * First constructor method for a recipe object. Three strings needed.
     * 
     * @param name
     *            of recipe
     * @param content
     *            of recipe
     * @param author
     *            of recipe
     */
    public RecipeImpl(final String content, final String name, final String author) {
        this.name = name;
        this.content = content;
        this.author = author;
    }

    /**
     * Second constructor method for a recipe object. Two stings needed.
     * 
     * @param content
     *            of recipe
     * @param name
     *            of recipe
     */
    public RecipeImpl(final String content, final String name) {
        this.name = name;
        this.content = content;
        this.setAuthor("AuthorNotSet:" + rand.nextInt());
    }

    /**
     * Third constructor method for a recipe object. Just one string needed.
     * 
     * @param content
     *            of recipe
     */
    public RecipeImpl(final String content) {
        this.setName("NameNotSet:" + rand.nextInt());
        this.content = content;
        this.setAuthor("AuthorNotSet:" + rand.nextInt());
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    public String getContent() {
        return content;
    }

    /**
     * {@inheritDoc}
     */
    public void setContent(final String content) {
        this.content = content;
    }

    /**
     * {@inheritDoc}
     */
    public String getAuthor() {
        return author;
    }

    /**
     * {@inheritDoc}
     */
    public void setAuthor(final String author) {
        this.author = author;
    }

}
