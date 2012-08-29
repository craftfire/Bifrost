package com.craftfire.bifrost.classes.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.craftfire.bifrost.script.Script;

/**
 * Base class for all categories like ForumBoard, ArticleCategory, WikiCategory,
 * IssueCategory. Should not be instanced.
 * 
 */
public class Category implements IDable {
    private int catid;
    private String name, description;
    private Category parent;
    private List<Category> subcategories;
    private final Script script;

    /**
     * @param script a Script Object of the script this category comes from.
     */
    protected Category(Script script) {
        this.script = script;
    }

    /**
     * This constructor should be used only when loading the category from a
     * database.
     * 
     * @param script a Script Object of the script this category comes from.
     * @param catid the ID of the category.
     */
    protected Category(Script script, int catid) {
        this.script = script;
        this.catid = catid;
    }

    /**
     * This constructor should be preferred when creating a new category.
     * 
     * @param script a Script Object of the script this category comes from.
     * @param name the name of the category.
     * @param parent the parent category
     * @param subcats the list of subcategories
     */
    protected Category(Script script, String name, Category parent, List<Category> subcats) {
        this.script = script;
        this.name = name;
        this.parent = parent;
        if (subcats != null) {
            this.subcategories = subcats;
        } else {
            this.subcategories = new ArrayList<Category>();
        }
    }
    /**
     * Returns the ID of category.
     * 
     * @see com.craftfire.bifrost.classes.general.IDable#getID()
     */
    @Override
    public int getID() {
        return this.catid;
    }

    /**
     * Sets the ID of the category, this should be used only when putting the
     * category into a database.
     * 
     * @param id the ID of the category.
     */
    public void setID(int id) {
        this.catid = id;
    }

    /**
     * Returns the name of the category.
     * 
     * @return the name of the category.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the category.
     * 
     * @param name name of the category.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description of the category.
     * 
     * @return the description of the category.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description of the category.
     * 
     * @param desc description of the category.
     */
    public void setDescription(String desc) {
        this.description = desc;
    }

    /**
     * Returns the parent category.
     * 
     * @return the parent category
     */
    public Category getParent() {
        return this.parent;
    }

    /**
     * Sets the parent category.
     * 
     * @param parent the parent category
     */
    public void setParent(Category parent) {
        this.parent = parent;
    }

    /**
     * Adds a subcategory for this category.
     * 
     * @param cat a subcategory
     */
    public void addSubcategory(Category cat) {
        if (this.subcategories == null) {
            this.subcategories = new ArrayList<Category>();
        }
        this.subcategories.add(cat);
    }

    /**
     * Adds multiple subcategories for this category.
     * 
     * @param cats subcategories
     */
    public void addSubcategories(Collection<Category> cats) {
        if (this.subcategories == null) {
            this.subcategories = new ArrayList<Category>();
        }
        this.subcategories.addAll(cats);
    }

    /**
     * Sets the subcategory list of this category.
     * 
     * @param cats subcategory list
     */
    public void setSubcategories(List<Category> cats) {
        this.subcategories = cats;
    }

    /**
     * Returns the subcategory list of this category.
     * 
     * @return subcategory list
     */
    public List<Category> getSubcategories() {
        return this.subcategories;
    }

    /**
     * Returns a Script Object for the script this category comes from.
     * 
     * @return a Script Object
     */
    public Script getScript() {
        return this.script;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Category " + this.catid + " named " + this.name + " from script: " + this.script.getScriptName();
    }

}
