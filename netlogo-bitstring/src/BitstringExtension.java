import java.util.List;

import org.nlogo.api.CompilerException;
import org.nlogo.api.DefaultClassManager;
import org.nlogo.api.Dump;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.ExtensionManager;
import org.nlogo.api.ExtensionObject;
import org.nlogo.api.ImportErrorHandler;
import org.nlogo.api.PrimitiveManager;


/**
 * BitstringExtension.java, 
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
 * <!-- BitstringExtension -->
 * 
 * @author Gary Polhill
 */
public class BitstringExtension extends DefaultClassManager {

	/**
	 * <!-- load -->
	 * 
	 * @see org.nlogo.api.DefaultClassManager#load(org.nlogo.api.PrimitiveManager)
	 */
	@Override
	public void load(PrimitiveManager manager) throws ExtensionException {
		manager.addPrimitive("make", new Make());
	}

	@Override
	public void clearAll() {
		NetLogoBitstring.reset();
	}

	@Override
	public StringBuilder exportWorld() {
		StringBuilder buff = new StringBuilder();

		for(NetLogoBitstring bitstring: NetLogoBitstring.bitstrings()) {
			buff.append(Dump.csv().encode(Dump.extensionObject(bitstring, true, true, false)) + "\n");
		}
		return buff;
	}

	@Override
	public void importWorld(List<String[]> lines, ExtensionManager manager, ImportErrorHandler handler)
			throws ExtensionException {
		for(String line[]: lines) {
			try {
				manager.readFromString(line[0]);
			}
			catch(CompilerException e) {
				handler.showError("Error importing bitstrings from text \"" + line[0] + "\"", e.getMessage(),
													"This bitstring will be ignored");
			}
		}
	}

	@Override
	public ExtensionObject readExtensionObject(ExtensionManager manager, String typeName, String value)
			throws ExtensionException, CompilerException {
		return NetLogoBitstring.manifest(value);
	}
}
