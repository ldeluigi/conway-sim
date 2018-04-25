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
     * {@inheritDoc}
     */
    @Override
    public Recipe getRecipeByPos(final int pos) {
        return this.rb.get(pos);
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public void addRecipe(final String content, final String name, final String author) {
        this.rb.add(new RecipeImpl(content, name, author));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addRecipe(final String content, final String name) {
        this.rb.add(new RecipeImpl(content, name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addRecipe(final String content) {
        this.rb.add(new RecipeImpl(content));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRecipeBookSize() {
        return this.rb.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Recipe> getRecipeList() {
        return Collections.unmodifiableList(rb);
    }

    private void setRb(final List<Recipe> rb) {
        this.rb = (ArrayList<Recipe>) rb;
    }
}
