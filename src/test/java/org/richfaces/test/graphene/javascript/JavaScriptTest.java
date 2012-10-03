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
package org.richfaces.test.graphene.javascript;

import org.jboss.arquillian.ajocado.utils.URLUtils;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.javascript.JSInterfaceFactory;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.richfaces.test.graphene.javascript.Background;

import java.net.URL;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@RunWith(Arquillian.class)
public class JavaScriptTest {

    URL url = URLUtils.buildUrl("http://www.google.com/");

    @Drone
    WebDriver browser;

    @Test
    public void testOpeningHomePage() throws  Exception {
        browser.get(url.toString());
        Background background = JSInterfaceFactory.create(Background.class);

        System.out.println(String.format("Background color is: %s", background.getBackground()));

        background.setBackground("red");
        System.out.println(String.format("Background color is: %s", background.getBackground()));
        Thread.sleep((2000));

        background.setBackground("");
        System.out.println(String.format("Background color is: %s", background.getBackground()));
        Thread.sleep((2000));
    }
}