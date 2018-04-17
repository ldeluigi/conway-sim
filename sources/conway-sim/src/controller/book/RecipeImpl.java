package controller.book;

import java.util.Random;

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
	 * The method which gives the name of the recipe.
	 * 
	 * @return name of recipe
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * The method which sets the given string as the name of the recipe.
	 * 
	 * @param name
	 *            of recipe
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * The method which gives the content of the recipe.
	 * 
	 * @return content of recipe
	 */
	public String getContent() {
		return content;
	}

	/**
	 * The method which sets the specified string as the content of the recipe.
	 * 
	 * @param content
	 *            of recipe
	 */
	public void setContent(final String content) {
		this.content = content;
	}

	/**
	 * The method which gives the creator of the recipe.
	 * 
	 * @return author of recipe
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * The method which sets the creator of the recipe.
	 * 
	 * @param author
	 *            of recipe
	 */
	public void setAuthor(final String author) {
		this.author = author;
	}

}
