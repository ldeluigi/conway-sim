package controller.book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of {@link RecipeBook}.
 */
public class RecipeBookImpl implements RecipeBook {
    private List<Recipe> rb;

    /**
     * Constructor that initializes an empty list of recipes.
     */
    public RecipeBookImpl() {
        this.setRb(new ArrayList<Recipe>());
    }

    /**
     * @param pos
     *            of the elem to extract
     * @return recipe in given position
     */
    @Override
    public Recipe getRecipeByPos(final int pos) {
        return this.rb.get(pos);
    }

    /**
     * @param name
     *            of elem to get
     * @return elem with given name
     * @throws NullPointerException
     *             if recipe not found
     */
    @Override
    public Recipe getRecipeByName(final String name) throws NullPointerException {
        for (final Recipe e : this.rb) {
            if ((e.getName()).equals(name)) {
                return e;
            }
        }
        return this.rb.get(0);

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
    @Override
    public void addRecipe(final String content, final String name, final String author) {
        this.rb.add(new RecipeImpl(content, name, author));
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
    @Override
    public void addRecipe(final String content, final String name) {
        this.rb.add(new RecipeImpl(content, name));
    }

    /**
     * Method for adding a new Recipe in the book given only the content of it.
     * 
     * @param content
     *            of the Recipe
     */
    @Override
    public void addRecipe(final String content) {
        this.rb.add(new RecipeImpl(content));
    }

    /**
     * Method for getting the Book Size.
     * 
     * @return size of Book
     */
    @Override
    public int getRecipeBookSize() {
        return this.rb.size();
    }

    /**
     * Returns an unmodifiable list of {@link Recipe}.
     */
    @Override
    public List<Recipe> getRecipeList() {
        return Collections.unmodifiableList(rb);
    }

    private void setRb(final List<Recipe> rb) {
        this.rb = (ArrayList<Recipe>) rb;
    }
}
