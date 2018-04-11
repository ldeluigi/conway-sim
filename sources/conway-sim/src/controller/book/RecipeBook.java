package controller.book;
/**
 * 
 *
 */
public interface RecipeBook {
    /**
     * 
     * @param pos of the elem to extract
     * @return recipe
     */
    Recipe getRecipeByPos(int pos);
    /**
     * 
     * @param name of elem to get
     * @return elem
     */
    Recipe getRecipeByName(String name);
    /**
     * Method for adding a new Recipe in the book given the content, the name of the recipe and the author.
     * @param content of the Recipe.
     * @param name of the Recipe.
     * @param author of the Recipe.
     */
    void addRecipe(String content, String name, String author);
    /**
     * Method for adding a new Recipe in the book given the content and the name of the recipe.
     * @param content of the Recipe.
     * @param name of the Recipe.
     */
    void addRecipe(String content, String name);
    /**
     * Method for adding a new Recipe in the book given only the content of it.
     * @param content of the Recipe
     */
    void addRecipe(String content);
    /**
     * Method for getting the Book Size.
     * @return size of Book
     */
    int getRecipeBookSize();

}
