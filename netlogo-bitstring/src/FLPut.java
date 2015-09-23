import java.util.ArrayList;
import java.util.Collections;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultReporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Syntax;

/**
 * FLPut.java, 
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
 * <!-- FLPut -->
 * 
 * @author Gary Polhill
 */
public class FLPut extends DefaultReporter {

	protected enum Mode {
		FIRST, LAST
	};

	private final Mode mode;

	public FLPut(Mode mode) {
		this.mode = mode;
	}

	@Override
	public Syntax getSyntax() {
		switch(mode) {
		case FIRST:
		case LAST:
			return Syntax.reporterSyntax(new int[] { Syntax.WildcardType(), Syntax.BooleanType() }, Syntax.WildcardType());
		default:
			throw new RuntimeException("PANIC!");
		}
	}

	/**
	 * <!-- report -->
	 * 
	 * @see org.nlogo.api.Reporter#report(org.nlogo.api.Argument[],
	 *      org.nlogo.api.Context)
	 */
	@Override
	public Object report(Argument[] args, Context context) throws ExtensionException, LogoException {
		NetLogoBitstring bs[] = BitstringExtension.getNetLogoBitstringArgs(args, 0);
		
		ArrayList<Boolean> list = bs[0].asList();
		
		switch(mode) {
		case FIRST:
			Collections.reverse(list);
			list.add(args[1].getBooleanValue());
			Collections.reverse(list);
			break;
		case LAST:
			list.add(args[1].getBooleanValue());
			break;
		default:
			throw new RuntimeException("PANIC!");
		}

		return new NetLogoBitstring(list);
	}

	@Override
	public String getAgentClassString() {
		return "OTPL";
	}

}
