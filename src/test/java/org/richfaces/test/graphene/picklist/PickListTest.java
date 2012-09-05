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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */

@RunWith(Arquillian.class)
@Ignore
public class PickListTest {
    private static final String WEBAPP_SRC = "src/main/webapp";

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        MavenDependencyResolver resolver = DependencyResolvers.use(
                MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");

        return ShrinkWrap.create(WebArchive.class, "picklist.war")
                .addPackage(RichBean.class.getPackage())
                .addAsWebResource(new File(WEBAPP_SRC + "/pickList", "pickList.xhtml"))
                .addAsWebResource(new File(WEBAPP_SRC + "/pickList", "pickList_strings.xhtml"))
                .addAsWebResource(new File(WEBAPP_SRC + "/pickList", "pickList_composite.xhtml"))
                .addAsWebResource(new File(WEBAPP_SRC + "/resources/components", "pickList.xhtml"), "/resources/components/pickList.xhtml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new StringAsset("<faces-config version=\"2.0\"/>"), "faces-config.xml")
                .addAsLibraries(resolver.artifact("org.richfaces.ui:richfaces-components-ui:4.3.0-SNAPSHOT").resolveAsFiles())
                .addAsLibraries(resolver.artifact("org.richfaces.core:richfaces-core-impl:4.3.0-SNAPSHOT").resolveAsFiles());
    }

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL deploymentURL;

    @FindBy(css=".pickList")
    PickListComponent pickList;

    @FindBy(css=".submitButton")
    WebElement submitButton;

    @FindBy(css=".out")
    WebElement outPanel;

    @Test
//    @Ignore
    public void testPickListInPage() {
        String page = deploymentURL + "pickList.jsf";
        browser.get(page);

        submitPickList();
    }

    @Test
//    @Ignore
    public void testPickListWithStrings() {
        String page = deploymentURL + "pickList_strings.jsf";
        browser.get(page);

        submitPickList();
    }

    @Test
    public void testPickListInComposite() {
        String page = deploymentURL + "pickList_composite.jsf";
        browser.get(page);

        submitPickList();
    }

    private void submitPickList() {
        pickList.addAll();
        Graphene.waitGui();
        submitButton.submit();
        List<WebElement> elements = outPanel.findElements(By.cssSelector(".value"));
        List<String> values = new ArrayList<String>(elements.size());
        for (WebElement element : elements) {
            values.add(element.getText());
        }
        Assert.assertEquals(5, values.size());
        Assert.assertEquals(pickList.getSelectedItems(), values);
    }

}