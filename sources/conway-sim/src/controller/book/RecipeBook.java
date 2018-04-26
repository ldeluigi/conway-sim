package controller.book;

import java.util.List;

/**
 * Interface of a recipe collection.
 */
public interface RecipeBook {

    /**
     * Gives the selected recipe at the specified position.
     * 
     * @param pos
     *            of the element to extract
     * @return the chosen recipe
     */
    Recipe getRecipeByPos(int pos);

    /**
     * Gives the selected recipe matching the specified name.
     * 
     * @param name
     *            of element to get
     * @return element
     * @throws NullPointerException
     *             if recipe not found
     */
    Recipe getRecipeByName(String name);

    /**
     * Adds a new Recipe in the book given the content, the name of the recipe and
     * the author.
     * 
     * @param content
     *            of the Recipe.
     * @param name
     *            of the Recipe.
     * @param author
     *            of the Recipe.
     */
    void addRecipe(String content, String name, String author);

    /**
     * Adds a new Recipe in the book given the content and the name of the recipe.
     * 
     * @param content
     *            of the Recipe.
     * @param name
     *            of the Recipe.
     */
    void addRecipe(String content, String name);

    /**
     * Adds a new Recipe in the book given only the content of it.
     * 
     * @param content
     *            of the Recipe
     */
    void addRecipe(String content);

    /**
     * Gives the Book Size.
     * 
     * @return size of Book
     */
    int getRecipeBookSize();

    /**
     * Gives the Recipe List.
     * 
     * @return the list of recipes
     */
    List<Recipe> getRecipeList();

}
