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
package com.craftfire.bifrost.classes.cms;

import java.sql.SQLException;
import java.util.List;

import com.craftfire.commons.database.DataManager;

import com.craftfire.bifrost.classes.general.Script;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.ScriptException;

/**
 * This class contains methods relevant to direct methods for each cms script.
 */
public class CMSScript extends Script {

    protected CMSScript(Scripts script, String version, DataManager dataManager) {
        super(script, version, dataManager);
    }

    @Override
    public CMSHandle getHandle() {
        return (CMSHandle) super.getHandle();
    }

    /**
     * Returns a CMSComment object of the given comment id, if nothing is found it returns null.
     * 
     * @param  commentid          the comment ID
     * @return                    CMSComment object, null if nothing was found
     * @throws ScriptException  if the method is not supported by the script
     */
    public CMSComment getComment(int commentid) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a List with CMSComment objects.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * Set {@code limit} to {@code 0} to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param  limit              the limit, set to 0 if you want to return all
     * @return                    List with CMSComment objects, if none are found it returns an empty List
     * @throws ScriptException  if the method is not supported by the script
     */
    public List<CMSComment> getComments(int limit) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the complete count of how many comments have been made.
     * 
     * @return                    the amount of how many comments have been made
     * @throws ScriptException  if the method is not supported by the script
     */
    public int getCommentTotalCount() throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a List with CMSComment objects from the given article ID.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * Set {@code limit} to {@code 0} to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param  articleid          the artcile ID to grab the comments from
     * @param  limit              the limit, set to 0 if you want to return all
     * @return                    List with CMSComment objects, if none are found it returns an empty List
     * @throws ScriptException  if the method is not supported by the script
     */
    public List<CMSComment> getCommentsOnArticle(int articleid, int limit) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the count of how many comments are there on the given {@code articleid}.
     *
     * @param  articleid          the article ID to count comments on
     * @return                    the amount of how many comments there are on the article
     * @throws ScriptException  if the method is not supported by the script
     * @see                       CMSArticle
     */
    public int getCommentCount(int articleid) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a List with CMSComment objects that reply to the given {@code commentid}.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * Set {@code limit} to {@code 0} to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param  commentid          the ID of the comment to grab the replying comments from
     * @param  limit              the limit, set to 0 if you want to return all
     * @return                    List with CMSComment objects, if none are found it returns an empty List
     * @throws ScriptException  if the method is not supported by the script
     */
    public List<CMSComment> getCommentReplies(int commentid, int limit) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the count of how many comments replying to the given {@code commentid} are there.
     *
     * @param  commentid          the ID of the comment to count the replying comments
     * @return                    the amount of how many comments there are replying to given comment
     * @throws ScriptException  if the method is not supported by the script
     */
    public int getCommentReplyCount(int commentid) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a List with CMSComment objects that have been made by specified {@code username}.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * Set {@code limit} to {@code 0} to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param  username           the username to grab the comments from
     * @param  limit              the limit, set to 0 if you want to return all
     * @return                    List with CMSComment objects, if none are found it returns an empty List
     * @throws ScriptException  if the method is not supported by the script
     */
    public List<CMSComment> getUserComments(String username, int limit) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns an amount of how many comments {@code username} has made.
     *
     * @param  username           the username to get the count from.
     * @return                    the amount of how many comments the username have made, returns 0 if none
     * @throws ScriptException  if the method is not supported by the script
     */
    public int getUserCommentCount(String username) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the CMSComment object of the last comment that has been made.
     *
     * @return                    CMSComment object of the last comment
     * @throws ScriptException  if the method is not supported by the script
     * @see                       CMSComment
     */
    public CMSComment getLastComment() throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the CMSComment object of the last comment that has been made by {@code username}.
     *
     * @param  username           the username to grab the last comment from
     * @return                    CMSComment object of the last comment made by the user
     * @throws ScriptException  if the method is not supported by the script
     * @see                       CMSComment
     */
    public CMSComment getLastUserComment(String username) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the CMSComment object of the last comment from the given article ID.
     * 
     * @param  articleid          the article ID to grab the last comment from
     * @return                    CMSComment object of the last comment from the article
     * @throws ScriptException  if the method is not supported by the script
     */
    public CMSComment getLastCommentOnArticle(int articleid) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Updates the CMSComment object with whatever values set by the user.
     *
     * @param  comment            the CMSComment object.
     * @throws ScriptException  if the method is not supported by the script
     */
    public void updateComment(CMSComment comment) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Creates the CMSComment object with whatever values set by the user.
     *
     * @param  comment            the CMSComment object.
     * @throws ScriptException  if the method is not supported by the script
     */
    public void createComment(CMSComment comment) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a CMSArticle object of the given article id, if nothing is found it returns null.
     * 
     * @param  articleid          the article ID
     * @return                    CMSArticle object, null if nothing was found
     * @throws ScriptException  if the method is not supported by the script
     */
    public CMSArticle getArticle(int articleid) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a List with CMSArticle objects.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * Set {@code limit} to 0 to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param  limit              the limit, set to 0 if you want to return all
     * @return                    List with CMSArticle objects, if none are found it returns an empty List
     * @throws ScriptException  if the method is not supported by the script
     */
    public List<CMSArticle> getArticles(int limit) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the complete count of how many articles have been made.
     * 
     * @return                    the amount of how many articles have been made
     * @throws ScriptException  if the method is not supported by the script
     */
    public int getArticleTotalCount() throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a List with CMSArticle objects from the given category ID.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * Set {@code limit} to 0 to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param  catid              the category ID to grab the articles from
     * @param  limit              the limit, set to 0 if you want to return all
     * @return                    List with CMSArticle objects, if none are found it returns an empty List
     * @throws ScriptException  if the method is not supported by the script
     */
    public List<CMSArticle> getArticlesFromCategory(int catid, int limit) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the count of how many articles are there in the given category ID.
     * 
     * @param  catid              the category ID to count articles from
     * @return                    the amount of how many articles there are in the category
     * @throws ScriptException  if the method is not supported by the script
     */
    public int getArticleCount(int catid) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a List with CMSArticle objects that have been made by specified {@code username}.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * Set {@code limit} to 0 to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param  username           the username to grab the articles from
     * @param  limit              the limit, set to 0 if you want to return all
     * @return                    List with CMSArticle objects, if none are found it returns an empty List
     * @throws ScriptException  if the method is not supported by the script
     */
    public List<CMSArticle> getUserArticles(String username, int limit) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns an amount of how many articles {@code username} has made.
     *
     * @param  username           the username to get the count from.
     * @return                    the amount of how many articles the username have made, returns 0 if none
     * @throws ScriptException  if the method is not supported by the script
     */
    public int getUserArticleCount(String username) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the CMSArticle object of the last article that has been made.
     *
     * @return                    CMSArticle object of the last article
     * @throws ScriptException  if the method is not supported by the script
     */
    public CMSArticle getLastArticle() throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the CMSArticle object of the last article that has been made by {@code username}.
     *
     * @param  username           the username to grab the last article from
     * @return                    CMSArticle object of the last article made by the user
     * @throws ScriptException  if the method is not supported by the script
     */
    public CMSArticle getLastUserArticle(String username) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the CMSArticle object of the last article from the given categorz ID.
     * 
     * @param catid               the category ID to grab the last article from
     * @return                    CMSArticle object of the last article from the category
     * @throws ScriptException  if the method is not supported by the script
     */
    public CMSArticle getLastArticleFromCategory(int catid) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Updates the CMSArticle object with whatever values set by the user.
     *
     * @param  article            the CMSArticle object
     * @throws ScriptException  if the method is not supported by the script
     */
    public void updateArticle(CMSArticle article) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Creates the CMSArticle object with whatever values set by the user.
     *
     * @param  article            the CMSArticle object
     * @throws ScriptException  if the method is not supported by the script
     */
    public void createArticle(CMSArticle article) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a CMSCategory object of the given category id, if nothing is found it returns null.
     * 
     * @param  catid              the category ID
     * @return                    CMSCategory object, null if nothing was found
     * @throws ScriptException  if the method is not supported by the script
     */
    public CMSCategory getCategory(int catid) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a List with CMSCategory objects.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * Set {@code limit} to 0 to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param  limit              the limit, set to 0 if you want to return all
     * @return                    List with CMSCategory objects, if none are found it returns an empty List
     * @throws ScriptException  if the method is not supported by the script
     */
    public List<CMSCategory> getCategories(int limit) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the complete count of how many categories are there.
     * 
     * @return                    the amount of how many categories are there
     * @throws ScriptException  if the method is not supported by the script
     */
    public int getCategoryCount() throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a List with CMSCategory objects that are subcategories of the given category ID.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * Set {@code limit} to 0 to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param  catid              the category ID to grab the subcategories from
     * @param  limit              the limit, set to 0 if you want to return all
     * @return                    List with CMSCategory objects, if none are found it returns an empty List
     * @throws ScriptException  if the method is not supported by the script
     */
    public List<CMSCategory> getSubCategories(int catid, int limit) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the count of how many subcategories are there in the given category ID.
     * 
     * @param  catid              the category ID to count subcategories from
     * @return                    the amount of how many subcategories there are in the category
     * @throws ScriptException  if the method is not supported by the script
     */
    public int getSubCategoryCount(int catid) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Updates the CMSCategory object with whatever values set by the user.
     *
     * @param  category           the CMSCategory object
     * @throws ScriptException  if the method is not supported by the script
     */
    public void updateCategory(CMSCategory category) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Creates the CMSCategory object with whatever values set by the user.
     *
     * @param  category           the CMSCategory object
     * @throws ScriptException  if the method is not supported by the script
     */
    public void createCategory(CMSCategory category) throws ScriptException {
        throw new ScriptException();
    }

    /* (non-Javadoc)
     * @see com.craftfire.bifrost.script.Script#getUser(java.lang.String)
     */
    @Override
    public CMSUser getUser(String username) throws ScriptException, SQLException {
        return (CMSUser) super.getUser(username);
    }

    /* (non-Javadoc)
     * @see com.craftfire.bifrost.script.Script#getUser(int)
     */
    @Override
    public CMSUser getUser(int userid) throws ScriptException, SQLException {
        return (CMSUser) super.getUser(userid);
    }

    /* (non-Javadoc)
     * @see com.craftfire.bifrost.script.Script#getLastRegUser()
     */
    @Override
    public CMSUser getLastRegUser() throws ScriptException, SQLException {
        return (CMSUser) super.getLastRegUser();
    }

    /**
     * @see com.craftfire.bifrost.classes.general.Script#updateUser(ScriptUser)
     */
    public void updateUser(CMSUser user) throws SQLException, ScriptException {
        updateUser((ScriptUser) user);
    }

    /**
     * @see com.craftfire.bifrost.classes.general.Script#createUser(ScriptUser)
     * ()
     */
    public void createUser(CMSUser user) throws SQLException, ScriptException {
        createUser((ScriptUser) user);
    }
}
