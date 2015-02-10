/**
 * Copyright (C) 2009 STMicroelectronics
 *
 * This file is part of "Mind Compiler" is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact: mind@ow2.org
 *
 * Authors: Matthieu Leclercq
 * Contributors: 
 */

package org.ow2.mind.adl.inheritance;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.fractal.adl.Definition;
import org.objectweb.fractal.adl.Loader;
import org.objectweb.fractal.adl.interfaces.Interface;
import org.objectweb.fractal.adl.interfaces.InterfaceContainer;
import org.ow2.mind.CommonFrontendModule;
import org.ow2.mind.adl.ADLFrontendModule;
import org.ow2.mind.adl.ASTChecker;
import org.ow2.mind.adl.GraphChecker;
import org.ow2.mind.adl.ast.MindInterface;
import org.ow2.mind.adl.graph.Instantiator;
import org.ow2.mind.error.ErrorManager;
import org.ow2.mind.idl.IDLFrontendModule;
import org.ow2.mind.plugin.PluginLoaderModule;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestOverride {

  Loader              loader;

  Instantiator        instantiator;

  Map<Object, Object> context;

  ASTChecker          astChecker;
  GraphChecker        graphChecker;

  ErrorManager        errorManager;

  @BeforeMethod(alwaysRun = true)
  protected void setUp() throws Exception {
    final Injector injector = Guice.createInjector(new CommonFrontendModule(),
        new PluginLoaderModule(), new IDLFrontendModule(),
        new ADLFrontendModule());
    loader = injector.getInstance(Loader.class);
    instantiator = injector.getInstance(Instantiator.class);

    context = new HashMap<Object, Object>();

    astChecker = new ASTChecker();
    graphChecker = new GraphChecker(astChecker);

    errorManager = injector.getInstance(ErrorManager.class);
  }

  /**
   * This test consists of a "Parent" definition providing the "I1 itf1"
   * interface, and a "Child" definition inheriting "Parent", overriding "itf1"
   * with an "I1Child itf1" interface, where the I1Child interface definition
   * inherits I1 (enforcing VTable compatibility, according to methods
   * appearance order).
   * 
   * @throws Exception
   */
  @Test(groups = {"functional"})
  public void testInterfaceLegitOverride() throws Exception {
    final Definition d = loader
        .load("pkg1.inheritance.ChildPrimitive", context);

    /*
     * Should not raise a InvalidMergeException
     * @see {org.ow2.mind.adl.ADLErrors.
     * INVALID_ATTRIBUTE_OVERRIDE_INHERITED_ATTRIBUTE_TYPE} error anymore.
     */
    Assert.assertTrue(errorManager.getErrors().isEmpty(),
        "Interface overriding failure (non-regression test).");

    Assert.assertTrue(d instanceof InterfaceContainer);
    final Interface[] itfs = ((InterfaceContainer) d).getInterfaces();

    // Our test case only has a single provided interface
    Assert.assertEquals(((MindInterface) itfs[0]).getSignature(),
        "pkg1.inheritance.I1Child");

  }

  /**
   * Nearly the same as {@see testInterfaceLegitOverride} except that we must
   * raise an error if the overriding interface is not inheriting the original
   * interface.
   * 
   * @throws Exception
   */
  @Test(groups = {"functional"})
  public void testInterfaceForbiddenOverride() throws Exception {
    final Definition d = loader.load("pkg1.inheritance.ChildPrimitive2",
        context);

    /*
     * Should not raise a InvalidMergeException
     * @see {org.ow2.mind.adl.ADLErrors.
     * INVALID_ATTRIBUTE_OVERRIDE_INHERITED_ATTRIBUTE_TYPE} error anymore.
     */
    Assert
        .assertFalse(
            errorManager.getErrors().isEmpty(),
            "Interface Overriding should not be allowed with non inheritance-related interface definitions");

  }

}
