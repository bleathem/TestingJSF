/**
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 **/
package org.richfaces.test.graphene.picklist;

import org.jboss.arquillian.graphene.spi.components.common.AbstractComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public class PickListComponent extends AbstractComponent {

    @FindBy(css = ".rf-pick-src")
    private WebElement sourceList;

    @FindBy(css = ".rf-pick-tgt")
    private WebElement targetList;

    @FindBy(css = ".rf-pick-add-all")
    private WebElement addAllButton;

    public List<String> getSelectedItems() {
        List<WebElement> elements = targetList.findElements(By.cssSelector(".rf-pick-opt"));
        List<String> labels = new ArrayList<String>(elements.size());
        for (WebElement element : elements) {
            labels.add(element.getText());
        }
        return labels;
    }

    public void addAll() {
        addAllButton.click();
    }
}