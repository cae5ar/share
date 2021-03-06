/*
 * #%L
 * share-po
 * %%
 * Copyright (C) 2005 - 2016 Alfresco Software Limited
 * %%
 * This file is part of the Alfresco software. 
 * If the software was purchased under a paid Alfresco license, the terms of 
 * the paid license agreement will prevail.  Otherwise, the software is 
 * provided under the following open source license terms:
 * 
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

package org.alfresco.po.share.user;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.po.HtmlPage;
import org.alfresco.po.RenderTime;
import org.alfresco.po.exception.PageOperationException;
import org.alfresco.po.share.ShareLink;
import org.alfresco.po.share.SharePage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

/**
 * Following page object, holds all element of the html page relating to following page
 *
 * Created by Olga Lokhach
 */
public class FollowingPage extends SharePage
{
    private static Log logger = LogFactory.getLog(FollowingPage.class);
    private static final By PRIVATE_CHECKBOX = By.cssSelector(".private>input");
    private static final By NOT_FOLLOWING_MESSAGE = By.cssSelector("div.viewcolumn p");
    private static final By USERS_LIST = By.xpath(".//div[@class='profile']//ul[1]");
    private static final By FOLLOWING_COUNT = By.cssSelector("div>a[href='following']");

    @SuppressWarnings("unchecked")
    @Override
    public FollowingPage render(RenderTime timer)
    {
        while (true)
        {
            timer.start();
            synchronized (this)
            {
                try
                {
                    this.wait(100L);
                }
                catch (InterruptedException e)
                {
                }
            }
            try
            {
                try
                {
                    if (driver.findElement(USERS_LIST).isDisplayed())
                    {
                        break;
                    }
                }
                catch (Exception e)
                {
                }

                if (driver.findElement(NOT_FOLLOWING_MESSAGE).isDisplayed() || driver.findElement(NOT_FOLLOWING_MESSAGE).getText().equals(getValue("user.profile.following.notfollowing")))
                {
                    break;
                }
            }
            catch (Exception e)
            {
            }
            finally
            {
                timer.end();
            }
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public FollowingPage render()
    {
        return render(new RenderTime(maxPageLoadingTime));
    }

    /**
     * Get the navigation bar.
     *
     * @return ProfileNavigation
     */
    public ProfileNavigation getProfileNav()
    {
        return new ProfileNavigation(driver, factoryPage);
    }

    /**
     * Return <code>true</code> if the Not Following message is displayed on screen.
     *
     * @return boolean present
     */

    public boolean isNotFollowingMessagePresent()
    {
        boolean present = false;
        try
        {
            present = findAndWait(NOT_FOLLOWING_MESSAGE).getText().equals(getValue("user.profile.following.notfollowing"));
            return present;
        }
        catch (NoSuchElementException e)
        {
        }

        return present;
    }

    /**
     * Returns <code>true</true> if the Private checkbox is checked.
     *
     * @return boolean
     */
    public boolean isPrivateChecked()
    {
        boolean present = false;
        try
        {
            present = findAndWait(PRIVATE_CHECKBOX).isSelected();
            return present;
        }
        catch (NoSuchElementException e)
        {
         logger.error("Private checkbox not present", e);
        }
        return present;
    }

    /**
     * Set the private checkbox status.
     *
     * @param enabled <code>true</code> to check the checkbox.
     */
    public void togglePrivate (boolean enabled)
    {
        try
        {
            if (enabled != isPrivateChecked())
            {
                findAndWait(PRIVATE_CHECKBOX).click();
            }
        }
        catch (TimeoutException e)
        {
            throw new PageOperationException("Unable to find private checkbox", e);
        }
    }

    /**
     * Gets the list of the user names.
     *
     * @return List of user names
     */


    public List<ShareLink> getUserLinks()
    {
        List<ShareLink> shareLinks = new ArrayList<>();
        try
        {
            List<WebElement> elements = driver.findElements(USERS_LIST);

            for (WebElement element : elements)
            {
                WebElement result = element.findElement(By.tagName("a"));
                shareLinks.add(new ShareLink(result, driver, factoryPage));
            }
        }
        catch (TimeoutException nse)
        {
            throw new PageOperationException("Unable to find any users", nse);
        }
        return shareLinks;
    }

    /**
     * Return <code>true</code> if the user name is displayed on screen.
     *
     * @return boolean present
     */

    public boolean isUserLinkPresent (String testUser)
    {
        List<ShareLink> userLink = getUserLinks();

        try
        {
            for (ShareLink shareLink : userLink)
            {
                if (shareLink.getDescription().contains(testUser))
                {
                    return true;
                }
            }
        }
        catch (TimeoutException e)
        {
            logger.error("Time out while finding user", e);
            return false;
        }
        return false;
    }

    /**
     * Get count of the users following.
     *
     * @return String number of users following
     */
    public String getFollowingCount()
    {
        String count = "";
        try
        {
            count = findAndWait(FOLLOWING_COUNT).getText().split("[()]+")[1];
        }
        catch (TimeoutException nsee)
        {
            logger.error("Element :" + FOLLOWING_COUNT + " does not exist", nsee);
        }
        return count;
    }

    /**
     * Click unfollow button
     *
     * @return Following page
     */

    public HtmlPage selectUnfollowForUser(String userName)
    {
        List<WebElement> elements = findAndWaitForElements(USERS_LIST);
        try
        {
            if (!elements.isEmpty())
            {
                for (WebElement webElement : elements)
                {
                    if ((webElement.findElement(By.tagName("a")).getText()).contains(userName))
                    {
                        webElement.findElement(By.tagName("button")).click();
                    }
                }
            }
        }
        catch (TimeoutException e)
        {
            throw new PageOperationException("Unable to find the unfollow button", e);
        }

        return getCurrentPage();
    }

}
