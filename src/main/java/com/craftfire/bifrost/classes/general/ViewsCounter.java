package com.craftfire.bifrost.classes.general;

/**
 * Everything that counts its views.
 */
public interface ViewsCounter {
    /**
     * Returns the view count of the object.
     * 
     * @return the view count
     */
    public int getViewsCount();

    /**
     * Sets the view count of the object.
     * 
     * @param views  view count
     */
    public void setViewsCount(int views);

}
