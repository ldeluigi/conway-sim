package controller.book;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 */
public class RecipeBookImpl implements RecipeBook {
    private List<Recipe> rb;

    /**
     * 
     */
    public RecipeBookImpl() {
        this.setRb(new ArrayList<Recipe>());
    }

    /**
     * 
     * @param pos
     *            of the elem to extract
     * @return recipe
     */
    public Recipe getRecipeByPos(final int pos) {
        return this.getRb().get(pos);
    }

    /**
     * 
     * @param name
     *            of elem to get
     * @return elem
     * @throws NullPointerException
     *             if recipe not found
     */
    public Recipe getRecipeByName(final String name) throws NullPointerException {
        for (final Recipe e : this.getRb()) {
            if ((e.getName()).equals(name)) {
                return e;
            }
        }
        return this.getRb().get(0);

    }

    /**
     * Method for adding a new Recipe in the book given the content, the name of the
     * recipe and the author.
     * 
     * @param content
     *            of the Recipe.
     * @param name
     *            of the Recipe.
     * @param author
     *            of the Recipe.
     */
    public void addRecipe(final String content, final String name, final String author) {
        this.getRb().add(new RecipeImpl(content, name, author));
    }

    /**
     * Method for adding a new Recipe in the book given the content and the name of
     * the recipe.
     * 
     * @param content
     *            of the Recipe.
     * @param name
     *            of the Recipe.
     */
    public void addRecipe(final String content, final String name) {
        this.getRb().add(new RecipeImpl(content, name));
    }

    /**
     * Method for adding a new Recipe in the book given only the content of it.
     * 
     * @param content
     *            of the Recipe
     */
    public void addRecipe(final String content) {
        this.getRb().add(new RecipeImpl(content));
    }

    /**
     * Method for getting the Book Size.
     * 
     * @return size of Book
     */
    public int getRecipeBookSize() {
        return this.getRb().size();
    }

    /**
     * SEVERE TO REFACTOR This method returns the recipebook List.
     * 
     * @return the Arraylist of recipebook
     */
    public List<Recipe> getBookList() {
        return this.getRb();
    }

    /**
     * 
     * @return recipebook
     */
    public final List<Recipe> getRb() {
        return rb;
    }

    /**
     * 
     * @param rb
     *            the recipebook to set
     */

    public final void setRb(final List<Recipe> rb) {
        this.rb = (ArrayList<Recipe>) rb;
    }
}
