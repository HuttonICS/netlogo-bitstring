import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.Reporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;


/**
 * Get.java, 
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
 * <!-- GetBit -->
 * 
 * @author Gary Polhill
 */
public class GetBit implements Reporter {

	public enum Mode {
		RANDOM_ACCESS, FIRST, LAST
	};

	private final Mode mode;

	public GetBit(Mode mode) {
		this.mode = mode;
	}

	@Override
	public Syntax getSyntax() {
		switch(mode) {
		case RANDOM_ACCESS:
			return SyntaxJ.reporterSyntax(new int[] { Syntax.WildcardType(), Syntax.NumberType() },
																		Syntax.BooleanType());
		case FIRST:
		case LAST:
			return SyntaxJ.reporterSyntax(new int[] { Syntax.WildcardType() }, Syntax.BooleanType());
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

		int pos;

		switch(mode) {
		case RANDOM_ACCESS:
			pos = args[1].getIntValue();
			break;
		case FIRST:
			pos = 0;
			break;
		case LAST:
			pos = bs[0].size() - 1;
			break;
		default:
			throw new RuntimeException("PANIC!");
		}

		if(pos < 0 || pos >= bs[0].size()) {
			throw new ExtensionException("Position " + pos + " is outside the range [0, " + bs[0].size() + "[");
		}
		return new Boolean(bs[0].get(pos));
	}


}
