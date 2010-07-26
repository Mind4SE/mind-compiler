/**
 * Copyright (C) 2010 STMicroelectronics
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

package org.ow2.mind.preproc.parser;

import java.io.PrintStream;

import org.antlr.runtime.Parser;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.TokenStream;
import org.ow2.mind.error.ErrorManager;
import org.ow2.mind.preproc.CPLChecker;

public abstract class AbstractCPLParser extends Parser {

  public AbstractCPLParser(final TokenStream input,
      final RecognizerSharedState state) {
    super(input, state);
  }

  public AbstractCPLParser(final TokenStream input) {
    super(input);
  }

  protected PrintStream  out           = System.out;
  protected PrintStream  headerOut     = null;
  protected boolean      singletonMode = false;
  protected CPLChecker   cplChecker    = null;
  protected ErrorManager errorManager  = null;

  public void setOutputStream(final PrintStream out) {
    this.out = out;
  }

  public void setHeaderOutputStream(final PrintStream out) {
    this.headerOut = out;
  }

  public void setSingletonMode(final boolean singletonMode) {
    this.singletonMode = singletonMode;
  }

  public void setCplChecker(final CPLChecker cplChecker) {
    this.cplChecker = cplChecker;
  }

  public void setErrorManager(final ErrorManager errorManager) {
    this.errorManager = errorManager;
  }

  public abstract void preprocess() throws RecognitionException;
}
