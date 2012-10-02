/*
 * This file is part of Bifrost.
 *
 * Copyright (c) 2011-2012, CraftFire <http://www.craftfire.com/>
 * Bifrost is licensed under the GNU Lesser General Public License.
 *
 * Bifrost is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bifrost is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.craftfire.bifrost.classes.general;

import java.util.List;

import com.craftfire.bifrost.exceptions.UnsupportedMethod;

/**
 * Base class for all categories like ForumBoard, ArticleCategory, WikiCategory, IssueCategory.
 * <p>
 * Should <code>not</code> be instanced.
 */
public abstract class Category extends GenericMethods implements MessageParent {
    private int parentid;
    private String name, description;

    /**
     * This constructor should be used in extending class's constructor, which
     * may be used to create new categories.
     * 
     * @param handle  a ScriptHandle Object of the script this category comes from
     */
    protected Category(ScriptHandle handle) {
        super(handle);
    }

    /**
     * This constructor should be used in extending class's constructor, which
     * is used only when loading the category from a database.
     * 
     * @param handle      a ScriptHandle Object of the script this category comes from.
     * @param categoryid  the ID of the category.
     */
    protected Category(ScriptHandle handle, int categoryid) {
        super(handle);
        this.id = categoryid;
    }

    /**
     * This constructor should be used in extending class's constructor that
     * will be preferred when creating a new category.
     * 
     * @param handle    a ScriptHandle Object of the script this category comes from.
     * @param name      the name of the category.
     * @param parentid  the ID of the parent category
     */
    protected Category(ScriptHandle handle, String name, int parentid) {
        super(handle);
        this.name = name;
        this.parentid = parentid;
    }

    /**
     * Returns the name of the category.
     * 
     * @return the name of the category
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the category.
     * 
     * @param name  name of the category
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description of the category.
     * 
     * @return the description of the category
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description of the category.
     * 
     * @param desc  description of the category
     */
    public void setDescription(String desc) {
        this.description = desc;
    }

    /**
     * Returns the ID of the parent category.
     * 
     * @return the id of the parent category
     */
    public int getParentID() {
        return this.parentid;
    }

    /**
     * Sets the parent category.
     * 
     * @param parentid  the ID of the parent category
     */
    public void setParentID(int parentid) {
        this.parentid = parentid;
    }

    /**
     * Returns a Category object of the parent category. Should be implemented
     * in classes of specific category types (such as ForumBoard). Loads it from
     * database if not cached.
     * 
     * @return                    a Category object
     * @throws UnsupportedMethod  if the method is not supported by script
     */
    public abstract Category getParent() throws UnsupportedMethod;

    /**
     * Returns the list of subcategories of this category. Should be implemented
     * in classes of specific category types (such as ForumBoard). Loads the
     * categories from database if not cached.
     * 
     * @param  limit              how many subcategories should be returned, 0 = returns all
     * @return                    the subcategory list
     * @throws UnsupportedMethod  if the method is not supported by script
     */
    public abstract List<? extends Category> getSubcategories(int limit) throws UnsupportedMethod;

    /**
     * Returns the list of messages contained in this category. Should be
     * implemented in classes of specific category types (such as ForumBoard).
     * Loads the messages from database if not cached.
     * 
     * @param  limit              how many messages should be returned, 0 = returns all
     * @return                    the list of messages
     * @throws UnsupportedMethod  if the method is not supported by script
     */
    public abstract List<? extends Message> getMessages(int limit) throws UnsupportedMethod;
    
    /* (non-Javadoc)
     * @see MessageParent#getChildMessages(int)
     */
    @Override
    public List<? extends Message> getChildMessages(int limit) throws UnsupportedMethod {
        return getMessages(limit);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Category " + this.getID() + " named " + this.name + " from script: " + this.getHandle().getScriptName();
    }

}
