package controller.book;

import java.util.ArrayList;
/**
 * 
 *
 */
public class RecipeBookImpl implements RecipeBook {
    private ArrayList<RecipeImpl> rb;
    /**
     * 
     */
    public RecipeBookImpl() {
        this.rb = new ArrayList<RecipeImpl>();
    }
    /**
     * 
     * @param pos of the elem to extract
     * @return recipe
     */
    public RecipeImpl getRecipeByPos(final int pos) {
        return this.rb.get(pos);
    }
    /**
     * 
     * @param name of elem to get
     * @return elem
     */
    public RecipeImpl getRecipeByName(final String name) {
        try {
            for (RecipeImpl e : this.rb) {
                if (e.getName() == name) {
                    return e;
                }
            } 
        }
        catch (NullPointerException e) {

            throw new IllegalArgumentException("No recipe found with the given name, returning the first one.");
        }
        return this.rb.get(0);

    }
    /**
     * Method for adding a new Recipe in the book given the content, the name of the recipe and the author.
     * @param content of the Recipe.
     * @param name of the Recipe.
     * @param author of the Recipe.
     */
    public void addRecipe(final String content, final String name, final String author) {
        this.rb.add(new RecipeImpl(content, name, author));
    }
    /**
     * Method for adding a new Recipe in the book given the content and the name of the recipe.
     * @param content of the Recipe.
     * @param name of the Recipe.
     */
    public void addRecipe(final String content, final String name) {
        this.rb.add(new RecipeImpl(content, name));
    }
    /**
     * Method for adding a new Recipe in the book given only the content of it.
     * @param content of the Recipe
     */
    public void addRecipe(final String content) {
        this.rb.add(new RecipeImpl(content));
    }
    /**
     * Method for getting the Book Size.
     * @return size of Book
     */
    public int getRecipeBookSize() {
        return this.rb.size();
    }
    /**
     * SEVERE TO REFACTOR
     * This method returns the recipebook List.
     * @return the Arraylist of recipebook
     */
    public ArrayList<RecipeImpl> getBookList() {
        return this.rb;
    }
}
