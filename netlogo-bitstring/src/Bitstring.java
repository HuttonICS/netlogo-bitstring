import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;


/**
 * Bitstring.java, 
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
 * <!-- Bitstring -->
 * 
 * @author Gary Polhill
 */
public class Bitstring implements Collection<Boolean>, RandomAccess, Cloneable {

	private final int length;

	private final List<Integer> bitstring;

	/**
	 * <!-- Bitstring constructor -->
	 * 
	 * Create an empty bitstring
	 * 
	 * @param length
	 *          The length of the bitstring
	 */
	public Bitstring(int length) {
		this.length = length;
		Integer temp[] = new Integer[(int)Math.ceil((double)length / Integer.SIZE)];
		Arrays.fill(temp, 0);
		bitstring = Collections.unmodifiableList(new ArrayList<Integer>(Arrays.asList(temp)));
	}

	/**
	 * <!-- Bitstring constructor -->
	 * 
	 * Create a bitstring with every bit set to the given value
	 * 
	 * @param length
	 *          The length of the bitstring
	 * @param set
	 *          <code>true</code> to set every bit to <code>1</code>;
	 *          <code>false</code> to set every bit to <code>0</code>
	 */
	public Bitstring(int length, boolean set) {
		this.length = length;
		Integer temp[] = new Integer[(int)Math.ceil((double)length / Integer.SIZE)];
		Arrays.fill(temp, set ? 1 : 0);
		bitstring = Collections.unmodifiableList(new ArrayList<Integer>(Arrays.asList(temp)));
	}

	/**
	 * <!-- Bitstring constructor -->
	 * 
	 * Create a bitstring as a copy of this one
	 * 
	 * @param bitstring
	 */
	public Bitstring(Bitstring bitstring) {
		this.length = bitstring.length;
		this.bitstring = Collections.unmodifiableList(bitstring.bitstring);
	}

	/**
	 * <!-- Bitstring constructor -->
	 * 
	 * Create a bitstring from a String
	 * 
	 * @param string
	 */
	public Bitstring(String string) {
		this.length = string.length();
		Integer temp[] = new Integer[(int)Math.ceil((double)length / Integer.SIZE)];
		for(int i = 0; i < length; i++) {
			int j = bitToArr(i);
			if(is1(string.charAt(i))) {
				temp[j] = temp[j] | getBit(i);
			}
			else if(is0(string.charAt(i))) {
				temp[j] = temp[j] & butBit(i);
			}
			else {
				throw new IllegalArgumentException("Cannot initialise bitstring from string \"" + string
						+ "\" because character at element " + i + " is not interpretable as a boolean");
			}
		}
		bitstring = Collections.unmodifiableList(new ArrayList<Integer>(Arrays.asList(temp)));
	}

	/**
	 * <!-- Bitstring constructor -->
	 * 
	 * Create a random bitstring with the specified probability of each bit being
	 * set to 1
	 * 
	 * @param length
	 *          The length of the bitstring
	 * @param probability
	 *          The probability of setting each bit to 1
	 */
	public Bitstring(int length, double probability) {
		this.length = length;
		Integer temp[] = new Integer[(int)Math.ceil((double)length / Integer.SIZE)];
		Arrays.fill(temp, 0);
		for(int i = 0; i < length; i++) {
			int j = bitToArr(i);
			if(Math.random() < probability) {
				temp[j] = temp[j] | getBit(i);
			}
			else {
				temp[j] = temp[j] & butBit(i);
			}
		}
		bitstring = Collections.unmodifiableList(new ArrayList<Integer>(Arrays.asList(temp)));
	}

	/**
	 * <!-- Bitstring constructor -->
	 * 
	 * Build a bitstring using an iterable of booleans.
	 * 
	 * @param input
	 */
	public Bitstring(Iterable<Boolean> input) {
		ArrayList<Boolean> list = new ArrayList<Boolean>();

		for(Boolean bit: input) {
			list.add(bit);
		}

		this.length = list.size();

		Integer temp[] = new Integer[(int)Math.ceil((double)length / Integer.SIZE)];
		Arrays.fill(temp, 0);
		for(int i = 0; i < length; i++) {
			int j = bitToArr(i);

			if(list.get(i)) {
				temp[j] = temp[j] | getBit(i);
			}
			else {
				temp[j] = temp[j] & butBit(i);
			}
		}
		bitstring = Collections.unmodifiableList(new ArrayList<Integer>(Arrays.asList(temp)));
	}

	/**
	 * <!-- Bitstring constructor -->
	 * 
	 * Private constructor to build a bitstring from an array of integers
	 * 
	 * @param length
	 *          The length of the bitstring
	 * @param arr
	 *          The array to initialise from
	 */
	private Bitstring(int length, Integer arr[]) {
		this.length = length;
		bitstring = Collections.unmodifiableList(new ArrayList<Integer>(Arrays.asList(arr)));
	}

	/**
	 * <!-- is1 -->
	 * 
	 * @param chr
	 * @return <code>true</code> if <code>chr</code> is recognised as one of those
	 *         that will be interpreted as a 1 in a bitstring
	 */
	public static boolean is1(char chr) {
		return chr == '1' || chr == 't' || chr == 'T' || chr == 'y' || chr == 'Y';
	}

	/**
	 * <!-- is0 -->
	 * 
	 * @param chr
	 * @return <code>true</code> if <code>chr</code> is recognised as one of those
	 *         that will be interpreted as a 0 in a bitstring
	 */
	public static boolean is0(char chr) {
		return chr == '0' || chr == 'f' || chr == 'F' || chr == 'n' || chr == 'N';
	}

	/**
	 * <!-- bitToArr -->
	 * 
	 * @param bit
	 * @return The position in an array of integers that the <code>bit</code> in
	 *         the bitstring is to be found in
	 */
	private static int bitToArr(int bit) {
		return (int)Math.floor((double)bit / Integer.SIZE);
	}

	/**
	 * <!-- bitInArr -->
	 * 
	 * @param bit
	 * @return The position in the bits of a particular array that the
	 *         <code>bit</code> is found in
	 */
	private static int bitInArr(int bit) {
		return bit - bitToArr(bit);
	}

	/**
	 * <!-- getBit -->
	 * 
	 * @param bit
	 * @return An integer with a single bit set in the appropriate place given the
	 *         position <code>bit</code> in the bitstring as a whole
	 */
	private static int getBit(int bit) {
		return 1 << bitInArr(bit);
	}

	/**
	 * <!-- butBit -->
	 * 
	 * @param bit
	 * @return An integer with all bits set to 1 except the bit in the appropriate
	 *         place given the position <code>bit</code> in the bitstring as a
	 *         whole
	 */
	private static int butBit(int bit) {
		return ~getBit(bit);
	}

	/**
	 * <!-- to10 -->
	 * 
	 * @param string
	 * @return A conversion of the string into a string of
	 *         <code>1<code>s (where the string has a <code>1</code>,
	 *         <code>t</code>, <code>T</code>, <code>y</code>, or <code>Y</code>)
	 *         and 0s (any other value)
	 */
	public static String to10(CharSequence string) {
		StringBuffer buff = new StringBuffer();

		for(int i = 0; i < string.length(); i++) {
			if(is1(string.charAt(i))) {
				buff.append("1");
			}
			else {
				buff.append("0");
			}
		}

		return buff.toString();
	}

	/**
	 * <!-- get -->
	 * 
	 * @param bit
	 * @return The value of the <code>bit</code><sup>th</sup> bit in the bitstring
	 *         as a boolean
	 */
	public boolean get(int bit) {
		if(bit < 0 || bit >= length) {
			throw new IllegalArgumentException("Bit " + bit + " is outside the range [0, " + length + "[");
		}
		return (bitstring.get(bitToArr(bit)) & getBit(bit)) != 0;
	}

	/**
	 * <!-- set -->
	 * 
	 * @param bit
	 * @param value
	 * @return A new bitstring that is a copy of this one, but with the
	 *         <code>bit</code><sup>th</sup> bit set to the specified
	 *         <code>value</code>
	 */
	public Bitstring set(int bit, boolean value) {
		if(bit < 0 || bit >= length) {
			throw new IllegalArgumentException("Bit " + bit + " is outside the range [0, " + length + "[");
		}
		Integer arr[] = toIntArray();
		if(value) {
			arr[bitToArr(bit)] = arr[bitToArr(bit)] | getBit(bit);
		}
		else {
			arr[bitToArr(bit)] = arr[bitToArr(bit)] & butBit(bit);
		}

		return new Bitstring(length, arr);
	}

	public Bitstring set(int bit) {
		return set(bit, true);
	}

	public Bitstring unset(int bit) {
		return set(bit, false);
	}

	public Bitstring toggle(int bit) {
		return set(bit, !get(bit));
	}

	public Bitstring jitter(double prob) {
		double probs[] = new double[length];

		Arrays.fill(probs, prob);

		return jitter(probs);
	}

	public Bitstring jitter(double probs[]) {
		if(probs.length != length) {
			throw new IllegalArgumentException("Probability array has a different length (" + probs.length
					+ ") from  that of the bitstring (" + length + ")");
		}
		ArrayList<Boolean> list = new ArrayList<Boolean>(length);
		for(int i = 0; i < length; i++) {
			if(Math.random() < probs[i]) {
				list.set(i, get(i));
			}
			else {
				list.set(i, !get(i));
			}
		}
		return new Bitstring(list);
	}

	public Bitstring subbitstring(int start, int finish) {
		if(start < 0 || start >= length) {
			throw new IllegalArgumentException("Start index " + start + " is outside the range [0, " + length + "[");
		}
		if(finish <= 0 || finish > length) {
			throw new IllegalArgumentException("Finish index " + finish + " is outside the range ]0, " + length + "]");
		}

		ArrayList<Boolean> list = new ArrayList<Boolean>(finish - start);
		for(int i = start; i < finish; i++) {
			list.set(i - start, get(i));
		}
		return new Bitstring(list);
	}

	public Bitstring append(Bitstring other) {
		ArrayList<Boolean> list = new ArrayList<Boolean>(this.length + other.length);

		for(int i = 0; i < this.length + other.length; i++) {
			list.set(i, (i < this.length ? get(i) : other.get(i - this.length)));
		}

		return new Bitstring(list);
	}

	public Bitstring not() {
		Integer thisarr[] = toIntArray();
		Integer mask[] = mask();

		for(int i = 0; i < thisarr.length; i++) {
			thisarr[i] = (~thisarr[i]) & mask[i];
		}
		return new Bitstring(length, thisarr);
	}

	public Bitstring and(Bitstring other) {
		if(other.length != this.length) {
			throw new IllegalArgumentException("Cannot AND bitstrings of different lengths (" + this.length + " and "
					+ other.length + ")");
		}
		Integer thisarr[] = toIntArray();
		Integer otherarr[] = other.toIntArray();

		for(int i = 0; i < thisarr.length; i++) {
			thisarr[i] = thisarr[i] & otherarr[i];
		}
		return new Bitstring(length, thisarr);
	}

	public Bitstring or(Bitstring other) {
		if(other.length != this.length) {
			throw new IllegalArgumentException("Cannot OR bitstrings of different lengths (" + this.length + " and "
					+ other.length + ")");
		}
		Integer thisarr[] = toIntArray();
		Integer otherarr[] = other.toIntArray();

		for(int i = 0; i < thisarr.length; i++) {
			thisarr[i] = thisarr[i] | otherarr[i];
		}
		return new Bitstring(length, thisarr);
	}

	public Bitstring xor(Bitstring other) {
		if(other.length != this.length) {
			throw new IllegalArgumentException("Cannot XOR bitstrings of different lengths (" + this.length + " and "
					+ other.length + ")");
		}
		Integer thisarr[] = toIntArray();
		Integer otherarr[] = other.toIntArray();
		Integer mask[] = mask();

		for(int i = 0; i < thisarr.length; i++) {
			thisarr[i] = (thisarr[i] ^ otherarr[i]) & mask[i];
		}
		return new Bitstring(length, thisarr);
	}

	public Bitstring parity(Bitstring other) {
		if(other.length != this.length) {
			throw new IllegalArgumentException("Cannot PARITY bitstrings of different lengths (" + this.length + " and "
					+ other.length + ")");
		}
		Integer thisarr[] = toIntArray();
		Integer otherarr[] = other.toIntArray();
		Integer mask[] = mask();

		for(int i = 0; i < thisarr.length; i++) {
			thisarr[i] = (~(thisarr[i] ^ otherarr[i])) & mask[i];
		}
		return new Bitstring(length, thisarr);
	}

	private Integer[] mask() {
		Integer mask[] = new Integer[bitstring.size()];

		for(int i = 0; i < mask.length - 1; i++) {
			mask[i] = ~0;
		}

		int remainder = (mask.length - 1) * Integer.SIZE;

		mask[mask.length - 1] = ~0 >>> (Integer.SIZE - remainder);

		return mask;
	}
	
	public int match(Bitstring other) {
		if(this.length != other.length) return -1;
		return parity(other).count1();
	}
	
	public int count1() {
		int n = 0;
		
		for(int i = 0; i < length; i++) {
			if(get(i)) {
				n++;
			}
		}
		
		return n;
	}
	

	public int count0() {
		int n = 0;
		
		for(int i = 0; i < length; i++) {
			if(!get(i)) {
				n++;
			}
		}
		
		return n;
	}

	/**
	 * <!-- size -->
	 * 
	 * @return The number of bits in the bitstring
	 */
	@Override
	public int size() {
		return length;
	}

	/**
	 * <!-- isEmpty -->
	 * 
	 * @return <code>true</code> if all bits are <code>0</code>
	 */
	@Override
	public boolean isEmpty() {
		for(Integer i: bitstring) {
			if(i > 0) return false;
		}
		return true;
	}

	/**
	 * <!-- isFull -->
	 * 
	 * @return <code>true</code> if all bits are <code>1</code>
	 */
	public boolean isFull() {
		for(int i = 0; i < length; i++) {
			if(!get(i)) return false;
		}
		return true;
	}

	/**
	 * <!-- asList -->
	 * 
	 * @return The bitstring as an <code>ArrayList</code> of <code>Boolean</code>s
	 */
	public ArrayList<Boolean> asList() {
		ArrayList<Boolean> l = new ArrayList<Boolean>(length);
		for(int i = 0; i < length; i++) {
			l.set(i, get(i));
		}
		return l;
	}

	/**
	 * <!-- contains -->
	 * 
	 * @param o
	 * @return <code>true</code> if the bitstring contains a sequence of bits that
	 *         corresponds to some reasonable interpretation of <code>o</code>
	 */
	@Override
	public boolean contains(Object o) {
		if(o instanceof Boolean) {
			return contains((Boolean)o);
		}
		else if(o instanceof Bitstring) {
			return toString().contains(((Bitstring)o).toString());
		}
		else if(o instanceof CharSequence) {
			return toString().contains(to10((CharSequence)o));
		}
		else if(o instanceof Byte || o instanceof Short) {
			return contains((Integer)o);
		}
		else if(o instanceof Integer) {
			return toString().contains(Integer.toBinaryString((Integer)o));
		}
		else if(o instanceof Long) {
			return toString().contains(Long.toBinaryString((Long)o));
		}
		return false;
	}

	/**
	 * <!-- contains -->
	 * 
	 * @param o
	 * @return <code>true</code> if the bitstring contains a bit <code>o</code>
	 */
	public boolean contains(Boolean o) {
		if(o) return !isEmpty();
		else return !isFull();
	}

	/**
	 * <!-- iterator -->
	 * 
	 * @return An iterator over all individual bits in the bitstring
	 */
	@Override
	public Iterator<Boolean> iterator() {
		return asList().iterator();
	}

	/**
	 * <!-- toArray -->
	 * 
	 * @return The bitstring as an array of Booleans
	 */
	@Override
	public Object[] toArray() {
		return asList().toArray();
	}

	/**
	 * <!-- toArray -->
	 * 
	 * @param a
	 * @return The bitstring as an array of Booleans
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		return asList().toArray(a);
	}

	/**
	 * <!-- toIntArray -->
	 * 
	 * @return The bitstring as an array of Integers
	 */
	private Integer[] toIntArray() {
		Integer arr[] = new Integer[bitstring.size()];

		for(int i = 0; i < arr.length; i++) {
			arr[i] = bitstring.get(i);
		}

		return arr;
	}

	/**
	 * <!-- add -->
	 * 
	 * This object is immutable, so you cannot add anything to it
	 * 
	 * @param e
	 * @return <code>false</code>
	 */
	@Override
	public boolean add(Boolean e) {
		return false;
	}

	/**
	 * <!-- remove -->
	 * 
	 * This object is immutable, so you cannot remove anything from it
	 * 
	 * @param o
	 * @return <code>false</code>
	 */
	@Override
	public boolean remove(Object o) {
		return false;
	}

	/**
	 * <!-- containsAll -->
	 * 
	 * @param c
	 * @return <code>true</code> if the bitstring {@link #contains(Object)} all
	 *         members of <code>c</code>
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object o: c) {
			if(!contains(o)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <!-- addAll -->
	 * 
	 * The bitstring cannot be modified
	 * 
	 * @param c
	 * @return <code>false</code>
	 */
	@Override
	public boolean addAll(Collection<? extends Boolean> c) {
		return false;
	}

	/**
	 * <!-- removeAll -->
	 * 
	 * The bitstring cannot be modified
	 * 
	 * @param c
	 * @return <code>false</code>
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	/**
	 * <!-- retainAll -->
	 * 
	 * The bitstring cannot be modified
	 * 
	 * @param c
	 * @return <code>false</code>
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	/**
	 * <!-- clear -->
	 * 
	 * The bitstring cannot be modified
	 * 
	 * @throws RuntimeException
	 */
	@Override
	public void clear() {
		throw new RuntimeException("Bitstrings are immutable");
	}

	/**
	 * <!-- toString -->
	 * 
	 * @return A <code>String</code> representation of the bitstring
	 */
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		for(int i = 0; i < length; i++) {
			buff.append(get(i) ? "1" : "0");
		}
		return buff.toString();
	}

	/**
	 * <!-- clone -->
	 * 
	 * @return A copy of this bitstring
	 */
	@Override
	public Bitstring clone() {
		return new Bitstring(this);
	}

	/**
	 * <!-- equals -->
	 * 
	 * @param oother
	 * @return <code>true</code> if this bitstring equals the other
	 */
	@Override
	public boolean equals(Object oother) {
		if(oother == null) {
			return false;
		}
		else if(oother instanceof String) {
			return toString().equals(oother);
		}
		else if(!(oother instanceof Bitstring)) {
			return false;
		}
		Bitstring other = (Bitstring)oother;
		if(this.length != other.length) {
			return false;
		}

		for(int i = 0; i < bitstring.size(); i++) {
			if(this.bitstring.get(i) != other.bitstring.get(i)) {
				return false;
			}
		}

		return true;
	}

	public static void main(String args[]) {
		System.out.println("size of int = " + Integer.SIZE);
		System.out.println("complement of 0 = " + Integer.toHexString(~0));
		System.out.println("complement of 0 >>> 0 = " + Integer.toHexString(~0 >>> 0));
		System.out.println("complement of 0 >>> 4 = " + Integer.toHexString(~0 >>> 4));
		System.out.println("complement of 0 >> 4 = " + Integer.toHexString(~0 >> 4));
		Bitstring a = new Bitstring(43, 0.3);
		Bitstring b = new Bitstring(43, 0.7);
		System.out.println("    A = " + a);
		System.out.println("    B = " + b);
		System.out.println("A p B = " + a.parity(b));
		System.out.println("match = " + a.match(b));
	}
}
