import java.util.List;

import org.nlogo.api.Argument;
import org.nlogo.api.DefaultClassManager;
import org.nlogo.api.Dump;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.ExtensionManager;
import org.nlogo.api.ImportErrorHandler;
import org.nlogo.api.LogoException;
import org.nlogo.api.PrimitiveManager;
import org.nlogo.core.CompilerException;
import org.nlogo.core.ExtensionObject;


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
		manager.addPrimitive("from-list", new FromList());
		manager.addPrimitive("random", new Random());
		manager.addPrimitive("from-string", new FromString());

		manager.addPrimitive("get?", new GetBit(GetBit.Mode.RANDOM_ACCESS));
		manager.addPrimitive("first?", new GetBit(GetBit.Mode.FIRST));
		manager.addPrimitive("last?", new GetBit(GetBit.Mode.LAST));
		manager.addPrimitive("set", new SetBit());
		manager.addPrimitive("fput", new FLPut(FLPut.Mode.FIRST));
		manager.addPrimitive("lput", new FLPut(FLPut.Mode.LAST));

		manager.addPrimitive("not", new BitWise(BitWise.Op.NOT));
		manager.addPrimitive("and", new BitWise(BitWise.Op.AND));
		manager.addPrimitive("or", new BitWise(BitWise.Op.OR));
		manager.addPrimitive("xor", new BitWise(BitWise.Op.XOR));
		manager.addPrimitive("parity", new BitWise(BitWise.Op.PARITY));
		manager.addPrimitive("right-shift", new BitWise(BitWise.Op.RSH));
		manager.addPrimitive("gray-code", new BitWise(BitWise.Op.GRAY));
		manager.addPrimitive("inverse-gray-code", new BitWise(BitWise.Op.INVGRAY));

		manager.addPrimitive("match", new Match());
		manager.addPrimitive("contains?", new Contains());
		manager.addPrimitive("cat", new Cat());
		manager.addPrimitive("sub", new SubBitstring(SubBitstring.Mode.RANDOM_ACCESS));
		manager.addPrimitive("but-first", new SubBitstring(SubBitstring.Mode.BUT_FIRST));
		manager.addPrimitive("but-last", new SubBitstring(SubBitstring.Mode.BUT_LAST));
		manager.addPrimitive("count0", new Count(Count.Mode.ZERO));
		manager.addPrimitive("count1", new Count(Count.Mode.ONE));
		manager.addPrimitive("all0?", new AllAny(AllAny.Mode.ALL_ZERO));
		manager.addPrimitive("any0?", new AllAny(AllAny.Mode.ANY_ZERO));
		manager.addPrimitive("all1?", new AllAny(AllAny.Mode.ALL_ONE));
		manager.addPrimitive("any1?", new AllAny(AllAny.Mode.ANY_ONE));
		manager.addPrimitive("empty?", new Empty());
		manager.addPrimitive("toggle", new Toggle());
		manager.addPrimitive("jitter", new Jitter());
		manager.addPrimitive("crossover", new Crossover());
		manager.addPrimitive("mutate", new Mutate());

		manager.addPrimitive("to-list", new ToList());
		manager.addPrimitive("to-string", new ToString());
		manager.addPrimitive("length", new Length());
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

	protected static NetLogoBitstring[] getNetLogoBitstringArgs(Argument args[], int... pos) throws ExtensionException,
			LogoException {
		NetLogoBitstring bs[] = new NetLogoBitstring[pos.length];

		for(int i = 0; i < pos.length; i++) {
			if(pos[i] >= args.length) {
				throw new ExtensionException("Command expects a " + (pos[i] + 1) + " argument, but only has " + args.length);
			}
			Object obj = args[pos[i]].get();
			if(obj instanceof NetLogoBitstring) {
				bs[i] = (NetLogoBitstring)obj;
			}
			else {
				throw new ExtensionException("Command expects a bitstring as argument " + (pos[i] + 1) + " but got a "
						+ obj.getClass().getSimpleName());
			}
		}

		return bs;
	}
}
