import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultReporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Syntax;


/**
 * Count.java, 
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
 * <!-- Count -->
 * 
 * @author Gary Polhill
 */
public class Count extends DefaultReporter {

	protected enum Mode {
		ONE, ZERO
	};

	private final Mode mode;

	public Count(Mode mode) {
		this.mode = mode;
	}

	@Override
	public Syntax getSyntax() {
		return Syntax.reporterSyntax(new int[] { Syntax.WildcardType() }, Syntax.NumberType());
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

		switch(mode) {
		case ONE:
			return new Double(bs[0].count1());
		case ZERO:
			return new Double(bs[0].count0());
		default:
			throw new RuntimeException("PANIC!");
		}
	}

	@Override
	public String getAgentClassString() {
		return "OTPL";
	}

}
