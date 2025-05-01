import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.nlogo.api.ExtensionException;
import org.nlogo.core.ExtensionObject;


public class NetLogoBitstring extends Bitstring implements ExtensionObject {

	private static long next = 0;

	private static Map<Long, NetLogoBitstring> bitstrings = new HashMap<Long, NetLogoBitstring>();

	private final long id;

	protected static void reset() {
		next = 0;
		bitstrings = new HashMap<Long, NetLogoBitstring>();
	}

	protected static NetLogoBitstring manifest(String id_value) throws ExtensionException {
		if(Bitstring.is10(id_value)) {
			return new NetLogoBitstring(id_value);
		}
		String s[] = id_value.split(":");
		long id = Long.parseLong(s[0]);

		if(bitstrings.containsKey(id)) {
			return bitstrings.get(id);
		}
		else if(s.length > 1) {
			if(s[1].startsWith(" ")) {
				s[1] = s[1].substring(1);
			}
			return new NetLogoBitstring(id, s[1]);
		}
		else {
			throw new ExtensionException("Cannot construct a bitstring from string " + id_value);
		}
	}

	protected static Collection<NetLogoBitstring> bitstrings() {
		return bitstrings.values();
	}

	public NetLogoBitstring(int length) {
		super(length);
		this.id = next;
		bitstrings.put(this.id, this);
		next++;
	}

	public NetLogoBitstring(int length, boolean set) {
		super(length, set);
		this.id = next;
		bitstrings.put(this.id, this);
		next++;
	}

	public NetLogoBitstring(Bitstring bitstring) {
		super(bitstring);
		this.id = next;
		bitstrings.put(this.id, this);
		next++;
	}

	public NetLogoBitstring(String string) {
		super(string);
		this.id = next;
		bitstrings.put(this.id, this);
		next++;
	}

	private NetLogoBitstring(long id, String string) {
		super(string);
		this.id = id;
		bitstrings.put(this.id, this);
		next = StrictMath.max(next, id + 1);
	}

	public NetLogoBitstring(int length, double probability) {
		super(length, probability);
		this.id = next;
		bitstrings.put(this.id, this);
		next++;
	}

	public NetLogoBitstring(Iterable<Boolean> input) {
		super(input);
		this.id = next;
		bitstrings.put(this.id, this);
		next++;
	}

	/**
	 * <!-- dump -->
	 * 
	 * @see org.nlogo.api.ExtensionObject#dump(boolean, boolean, boolean)
	 * @param readable
	 *          <code>true</code> means the result should be readable as netlogo
	 *          code
	 * @param exporting
	 *          <code>false</code> means the result is for display only
	 * @param reference
	 *          <code>true</code> means the result may be a reference to a
	 *          complete object stored in the extension section of the file;
	 *          <code>false</code> means the object should be recreatable from the
	 *          result
	 */
	@Override
	public String dump(boolean readable, boolean exporting, boolean reference) {
		StringBuilder buff = new StringBuilder();
		if(exporting) {
			buff.append(id);
			if(!reference) {
				buff.append(": ");
			}
		}
		if(!(reference && exporting)) {
			buff.append(this.toString());
		}

		return buff.toString();
	}

	@Override
	public String getExtensionName() {
		return "bitstring";
	}

	@Override
	public String getNLTypeName() {
		return "";
	}

	@Override
	public boolean equals(Object other) {
		return this == other;
	}

	@Override
	public boolean recursivelyEqual(Object other) {
		return super.equals(other);
	}

}
