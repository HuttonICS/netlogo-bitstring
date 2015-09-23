import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultReporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Syntax;


/**
 * BitWise.java, 
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
 * <!-- BitWise -->
 * 
 * @author Gary Polhill
 */
public class BitWise extends DefaultReporter {

	protected enum Op {
		NOT, AND, OR, XOR, PARITY
	};

	private final Op op;

	public BitWise(Op op) {
		this.op = op;
	}

	@Override
	public Syntax getSyntax() {
		switch(op) {
		case NOT:
			return Syntax.reporterSyntax(new int[] { Syntax.WildcardType() }, Syntax.WildcardType());
		case AND:
		case OR:
		case XOR:
		case PARITY:
			return Syntax
					.reporterSyntax(Syntax.WildcardType(), new int[] { Syntax.WildcardType() }, Syntax.WildcardType(), 1);
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
		if(op == Op.NOT) {
			NetLogoBitstring bs[] = BitstringExtension.getNetLogoBitstringArgs(args, 0);
			return new NetLogoBitstring(bs[0].not());
		}
		else {
			NetLogoBitstring bs[] = BitstringExtension.getNetLogoBitstringArgs(args, 0, 1);
			switch(op) {
			case AND:
				return new NetLogoBitstring(bs[0].and(bs[1]));
			case OR:
				return new NetLogoBitstring(bs[0].or(bs[1]));
			case XOR:
				return new NetLogoBitstring(bs[0].xor(bs[1]));
			case PARITY:
				return new NetLogoBitstring(bs[0].parity(bs[1]));
			default:
				throw new RuntimeException("PANIC!");
			}
		}
	}

	@Override
	public String getAgentClassString() {
		return "OTPL";
	}

}
