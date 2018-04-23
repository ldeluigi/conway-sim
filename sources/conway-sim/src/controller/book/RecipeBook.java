package controller.book;

import java.util.List;

/**
 * Interface of a recipe reader and collection.
 */
public interface RecipeBook {

    /**
     * Method which gives the selected recipe at the specified position.
     * 
     * @param pos
     *            of the element to extract
     * @return the chosen recipe
     */
    Recipe getRecipeByPos(int pos);

    /**
     * Method which gives the selected recipe matching the specified name.
     * 
     * @param name
     *            of element to get
     * @return element
     * @throws NullPointerException
     *             if recipe not found
     */
    Recipe getRecipeByName(String name);

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
    void addRecipe(String content, String name, String author);

    /**
     * Method for adding a new Recipe in the book given the content and the name of
     * the recipe.
     * 
     * @param content
     *            of the Recipe.
     * @param name
     *            of the Recipe.
     */
    void addRecipe(String content, String name);

    /**
     * Method for adding a new Recipe in the book given only the content of it.
     * 
     * @param content
     *            of the Recipe
     */
    void addRecipe(String content);

    /**
     * Method for getting the Book Size.
     * 
     * @return size of Book
     */
    int getRecipeBookSize();

    /**
     * @return the list of recipes
     */
    List<Recipe> getRecipeList();

}
