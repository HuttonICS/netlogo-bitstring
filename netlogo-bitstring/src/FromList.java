import java.util.LinkedList;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultReporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.LogoList;
import org.nlogo.api.Syntax;


/**
 * FromList.java, 
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
 * <!-- FromList -->
 * 
 * @author Gary Polhill
 */
public class FromList extends DefaultReporter {

	@Override
	public Syntax getSyntax() {
		return Syntax.reporterSyntax(new int[] { Syntax.ListType() },
																	Syntax.WildcardType());
	}

	@Override
	public Object report(Argument[] args, Context context) throws ExtensionException, LogoException {
		LogoList list = args[0].getList();
		Class<?> type = null;
		int element = 0;
		for(Object item: list) {
			element++;
			if(type == null) {
				type = item.getClass();
			}
			else if(type != item.getClass()) {
				throw new ExtensionException("List has inconsistent types. The first one has " + type.getSimpleName()
						+ " whereas element " + element + " has " + item.getClass().getSimpleName());
			}
		}
		LinkedList<Boolean> bitlist = new LinkedList<Boolean>();

		for(Object item: list) {
			if(item instanceof String) {
				if("T".equalsIgnoreCase(item.toString()) || "TRUE".equalsIgnoreCase(item.toString())
						|| "Y".equalsIgnoreCase(item.toString()) || "YES".equalsIgnoreCase(item.toString())
						|| "1".equals(item.toString())) {
					bitlist.add(true);
				}
				else if("F".equalsIgnoreCase(item.toString()) || "FALSE".equalsIgnoreCase(item.toString())
						|| "N".equalsIgnoreCase(item.toString()) || "NO".equalsIgnoreCase(item.toString())
						|| "0".equals(item.toString())) {
					bitlist.add(false);
				}
				else {
					throw new ExtensionException("Unable to interpret string " + item.toString() + " into a boolean");
				}
			}
			else if(item instanceof Double) {
				if((Double)item == 1.0) {
					bitlist.add(true);
				}
				else if((Double)item == 0.0) {
					bitlist.add(false);
				}
				else {
					throw new ExtensionException("Unable to interpret number " + item.toString()
							+ " into a boolean (must be 1 or 0)");
				}
			}
			else if(item instanceof Integer) {
				if((Integer)item == 1) {
					bitlist.add(true);
				}
				else if((Integer)item == 0) {
					bitlist.add(false);
				}
				else {
					throw new ExtensionException("Unable to interpret number " + item.toString()
							+ " into a boolean (must be 1 or 0)");
				}
			}
			else if(item instanceof Boolean) {
				bitlist.add((Boolean)item);
			}
			else {
				throw new ExtensionException("Unable to interpret objects of type " + item.getClass().toString()
						+ " into a boolean");
			}
		}
		return new NetLogoBitstring(bitlist);
	}

	@Override
	public String getAgentClassString() {
		return "OTPL";
	}

}
