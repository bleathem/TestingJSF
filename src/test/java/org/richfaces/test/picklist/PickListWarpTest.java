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
package org.richfaces.test.picklist;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.*;
import org.jboss.arquillian.warp.extension.phaser.AfterPhase;
import org.jboss.arquillian.warp.extension.phaser.BeforePhase;
import org.jboss.arquillian.warp.extension.phaser.Phase;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.test.picklist.PickListFragment;
import org.richfaces.test.picklist.RichBean;
import org.richfaces.test.util.JsfRequestFilter;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */


@WarpTest
@RunWith(Arquillian.class)
public class PickListWarpTest {
    private static final String WEBAPP_SRC = "src/main/webapp";

    @Deployment
    public static WebArchive createDeployment() {
        MavenDependencyResolver resolver = DependencyResolvers.use(
                MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");

        return ShrinkWrap.create(WebArchive.class, "picklistWarp.war")
                .addPackage(RichBean.class.getPackage())
                .addAsWebResource(new File(WEBAPP_SRC + "/pickList", "pickList.xhtml"))
                .addAsWebInfResource(new File("src/test/resources/beans.xml"))
                .addAsWebInfResource(new StringAsset("<faces-config version=\"2.0\"/>"), "faces-config.xml")
                .addAsLibraries(resolver.artifact("org.richfaces.ui:richfaces-components-ui:4.3.0-SNAPSHOT").resolveAsFiles())
                .addAsLibraries(resolver.artifact("org.richfaces.core:richfaces-core-impl:4.3.0-SNAPSHOT").resolveAsFiles());
    }

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL deploymentURL;

    @FindBy(css=".pickList")
    PickListFragment pickList;

    @FindBy(css=".submitButton")
    WebElement submitButton;

    @FindBy(css=".out")
    WebElement outPanel;

    @Test
    @RunAsClient
    public void testPickListInPage() {
        String page = deploymentURL + "pickList.jsf";
        browser.get(page);

        Warp.filter(new JsfRequestFilter()).execute(new ClientAction() {
            @Override
            public void action() {
                pickList.addAll();
                submitButton.submit();
            }
        }).verify(new CheckListSize());

        List<WebElement> elements = outPanel.findElements(By.cssSelector(".value"));
        List<String> values = new ArrayList<String>(elements.size());
        for (WebElement element : elements) {
            values.add(element.getText());
        }
        Assert.assertEquals(5, values.size());
        Assert.assertEquals(pickList.getSelectedItems(), values);
    }

    public static class CheckListSize extends ServerAssertion {

        private static final long serialVersionUID = 1L;

        @Inject
        RichBean richBean;

        @BeforePhase(Phase.UPDATE_MODEL_VALUES)
        public void beforeUpdateModelValues() {
            Assert.assertEquals(4, richBean.getValues().size());
        }

        @AfterPhase(Phase.UPDATE_MODEL_VALUES)
        public void afterUpdateModelValues() {
            Assert.assertEquals(5, richBean.getValues().size());
        }

    }

}