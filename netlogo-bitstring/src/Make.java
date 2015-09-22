import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultReporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Syntax;


/**
 * Make.java, 
 *
 * Copyright (C) The James Hutton Institute 2015
 *
 * This file is part of netlogo-bitstring
 *
 * netlogo-bitstring is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * netlogo-bitstring is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * netlogo-bitstring. If not, see <http://www.gnu.org/licenses/>. 
 */

/**
 * <!-- Make -->
 * 
 * @author Gary Polhill
 */
public class Make extends DefaultReporter {

	@Override
	public Syntax getSyntax() {
		return Syntax.reporterSyntax(	new int[] { Syntax.NumberType(), Syntax.BooleanType() },
																	Syntax.WildcardType());
	}

	@Override
	public Object report(Argument[] args, Context context) throws ExtensionException, LogoException {
		int length = args[0].getIntValue();
		boolean value = args[1].getBooleanValue();
		return new NetLogoBitstring(length, value);
	}
	
	@Override
	public String getAgentClassString() {
		return "OTPL";
	}

}
