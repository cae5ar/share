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
package org.alfresco.po.share.workflow;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * enum class that holds all elements of start workflow drop down contents.
 * 
 * @author Abhijeet Bharade
 * @since 1.6.2
 */
public enum WorkFlowType
{

    CLOUD_TASK_OR_REVIEW("Cloud Task or Review"),
    NEW_WORKFLOW("New Task"),
    GROUP_REVIEW_AND_APPROVE("Review and Approve (group review)"),
    SEND_DOCS_FOR_REVIEW("Review and Approve (one or more reviewers)"),
    POOLED_REVIEW_AND_APPROVE("Review and Approve (pooled review)"),
    REVIEW_AND_APPROVE("Review And Approve (single reviewer)"),
    LIFECYCLE_REVIEW_AND_APPROVE("Lifecycle Review & Approve"),
    ADHOC_WORKFLOW("Adhoc Workflow (JBPM)"),
    GROUP_REVIEW_AND_APPROVE_JBPM("Group Review And Approve (JBPM)"),
    PARALLEL_REVIEW_AND_APPROVE("Parallel Review And Approve (JBPM)"),
    POOLED_REVIEW_AND_APPROVE_JBPM("Pooled Review And Approve (JBPM)"),
    HANDLE_CONTACT_REQUEST("Handle Contact Request"),
    REVIEW_AND_APPROVE_JBPM("Review And Approve (JBPM)");

    private String title;

    WorkFlowType(String desc)
    {
        this.title = desc;
    }

    public String getTitle()
    {
        return title;
    }

    protected WebElement getTaskTypeElement(WebDriver driver)
    {
        By dropDown = By.cssSelector("div[id$='default-workflow-definition-menu'] li span.title");
        List<WebElement> liElements = driver.findElements(dropDown);
        for (WebElement liElement : liElements)
        {
            String elementText = liElement.getText().trim();
            if (elementText.equalsIgnoreCase(this.title.trim()))
            {
                return liElement;
            }
        }
        return null;
    }

    public static WorkFlowType getWorkflowType(String type)
    {
        for (WorkFlowType workflow : WorkFlowType.values())
        {
            if (type.contains(workflow.toString()))
            {
                return workflow;
            }
        }
        throw new IllegalArgumentException("Workflow Type not able find for given name: " + type);
    }

    /**
     * Based title return {@link WorkFlowType}.
     * 
     * @param title String
     * @return {@link WorkFlowType}
     */
    public static WorkFlowType getWorkflowTypeByTitle(String title)
    {
        for (WorkFlowType workflow : WorkFlowType.values())
        {
            if (title.contains(workflow.getTitle()))
            {
                return workflow;
            }
        }
        throw new IllegalArgumentException("Workflow Type not able find for given Title: " + title);
    }

    public void setTitle(String newTitle)
    {
        this.title = newTitle;
    }
}
