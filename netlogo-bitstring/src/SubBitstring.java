import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultReporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Syntax;


/**
 * SubBitstring.java, 
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
 * <!-- SubBitstring -->
 * 
 * @author Gary Polhill
 */
public class SubBitstring extends DefaultReporter {

	protected enum Mode {
		RANDOM_ACCESS, BUT_FIRST, BUT_LAST
	};

	private final Mode mode;

	public SubBitstring(Mode mode) {
		this.mode = mode;
	}

	@Override
	public Syntax getSyntax() {
		switch(mode) {
		case BUT_FIRST:
		case BUT_LAST:
			return Syntax.reporterSyntax(new int[] { Syntax.WildcardType() },
																		Syntax.WildcardType());
		case RANDOM_ACCESS:
			return Syntax.reporterSyntax(new int[] { Syntax.WildcardType(), Syntax.NumberType(), Syntax.NumberType() },
																		Syntax.WildcardType());
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

		int start;
		int finish;

		switch(mode) {
		case BUT_FIRST:
			start = 1;
			finish = bs[0].size();
			break;
		case BUT_LAST:
			start = 0;
			finish = bs[0].size() - 1;
			break;
		case RANDOM_ACCESS:
			start = args[1].getIntValue();
			finish = args[2].getIntValue();
			break;
		default:
			throw new RuntimeException("PANIC!");
		}

		if(finish < start || start < 0 || finish > bs[0].size()) {
			if(mode == Mode.RANDOM_ACCESS) {
				throw new ExtensionException("Illegal sub-bitstring range [" + start + ", " + finish
						+ "[ for bitstring of size " + bs[0].size());
			}
			else {
				throw new ExtensionException("Attempt to remove first or last element from empty bitstring");
			}
		}

		return new NetLogoBitstring(bs[0].subbitstring(start, finish));
	}

	@Override
	public String getAgentClassString() {
		return "OTPL";
	}

}
