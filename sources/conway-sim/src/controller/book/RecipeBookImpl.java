package controller.book;

import java.util.ArrayList;
/**
 * 
 *
 */
public abstract class RecipeBookImpl implements RecipeBook {
    private ArrayList<RecipeImpl> rb = new ArrayList<RecipeImpl>();
    /**
     * 
     */
    public RecipeBookImpl() {
        //EMPTY BUILDER
    }
    /**
     * 
     * @param pos of the elem to extract
     * @return recipe
     */
    public RecipeImpl getRecipeByPos(final int pos) {
        return rb.get(pos);
    }
    /**
     * 
     * @param name of elem to get
     * @return elem
     */
    public RecipeImpl getRecipeByName(final String name) {
        try {
            for (RecipeImpl e : rb) {
                if (e.getName() == name) {
                    return e;
                }
            } 
        }
        catch (NullPointerException e) {

            throw new IllegalArgumentException("No recipe found with the given name, returning the first one.");
        }
        return rb.get(0);

    }
    /**
     * Method for adding a new Recipe in the book given the content, the name of the recipe and the author.
     * @param content of the Recipe.
     * @param name of the Recipe.
     * @param author of the Recipe.
     */
    public void addRecipe(final String content, final String name, final String author) {
        rb.add(new RecipeImpl(content, name, author));
    }
    /**
     * Method for adding a new Recipe in the book given the content and the name of the recipe.
     * @param content of the Recipe.
     * @param name of the Recipe.
     */
    public void addRecipe(final String content, final String name) {
        rb.add(new RecipeImpl(content, name));
    }
    /**
     * Method for adding a new Recipe in the book given only the content of it.
     * @param content of the Recipe
     */
    public void addRecipe(final String content) {
        rb.add(new RecipeImpl(content));
    }
}
