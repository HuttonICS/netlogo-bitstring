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

	public static final String EMPTY_BITSTRING_STRING = "<empty>";

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
		if(length > 0) {
			Integer temp[] = new Integer[(int)Math.ceil((double)length / Integer.SIZE)];
			Arrays.fill(temp, 0);
			bitstring = Collections.unmodifiableList(new ArrayList<Integer>(Arrays.asList(temp)));
		}
		else {
			bitstring = Collections.unmodifiableList(new ArrayList<Integer>());
		}
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
		if(length > 0) {
			Integer temp[] = new Integer[(int)Math.ceil((double)length / Integer.SIZE)];
			Arrays.fill(temp, set ? ~0 : 0);
			if(set) {
				int remainder = (temp.length - 1) * Integer.SIZE;

				temp[temp.length - 1] = ~0 >>> (Integer.SIZE - remainder);
			}
			bitstring = Collections.unmodifiableList(new ArrayList<Integer>(Arrays.asList(temp)));
		}
		else {
			bitstring = Collections.unmodifiableList(new ArrayList<Integer>());
		}
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
		if(string.equals(EMPTY_BITSTRING_STRING)) string = "";
		this.length = string.length();
		if(length > 0) {
			Integer temp[] = new Integer[(int)Math.ceil((double)length / Integer.SIZE)];
			Arrays.fill(temp, 0);
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
		else {
			bitstring = Collections.unmodifiableList(new ArrayList<Integer>());
		}
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
		if(length > 0) {
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
		else {
			bitstring = Collections.unmodifiableList(new ArrayList<Integer>());
		}
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

		if(length > 0) {
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
		else {
			bitstring = Collections.unmodifiableList(new ArrayList<Integer>());
		}

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
		return Integer.SIZE - (bit - (bitToArr(bit) * Integer.SIZE)) - 1;
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
	 * <!-- is10 -->
	 * 
	 * @param string
	 * @return <code>true</code> if all characters in the <code>string</code> are
	 *         interpretable as a 1 or a 0
	 */
	public static boolean is10(CharSequence string) {
		for(int i = 0; i < string.length(); i++) {
			if(!(is1(string.charAt(i)) || is0(string.charAt(i)))) return false;
		}
		return true;
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
	 * <!-- get -->
	 * 
	 * @param bit
	 * @param bitstring
	 * @param length
	 * @return 1 if the bit is set in the Integer array, 0 if not
	 */
	private static int get(int bit, Integer[] bitstring, int length) {
		if(bit < 0 || bit >= length) {
			throw new IllegalArgumentException("Bit " + bit + " is outside the range [0, " + length + "[");
		}
		return (bitstring[bitToArr(bit)] & getBit(bit)) == 0 ? 0 : 1;
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

	/**
	 * <!-- set -->
	 * 
	 * @param bit
	 * @param bitstring
	 * @param length
	 * @param value
	 * @return A value to set the <code>bitToArr(bit)</code><sup>th</sup> element
	 *         of <code>bitstring</code> to as a result of setting the
	 *         <code>bit</code><sup>th</sup> bit of the bitstring to
	 *         <code>value</code>
	 */
	private static int set(int bit, Integer[] bitstring, int length, int value) {
		if(bit < 0 || bit >= length) {
			throw new IllegalArgumentException("Bit " + bit + " is outside the range [0, " + length + "[");
		}
		if(value == 1) {
			return bitstring[bitToArr(bit)] | getBit(bit);
		}
		else {
			return bitstring[bitToArr(bit)] & butBit(bit);
		}
	}


	/**
	 * <!-- set -->
	 * 
	 * @param bit
	 * @return A Bitstring that is the same as this one but with bit
	 *         <code>bit</code> definitely set to 1
	 */
	public Bitstring set(int bit) {
		return set(bit, true);
	}

	/**
	 * <!-- unset -->
	 * 
	 * @param bit
	 * @return A Bitstring that is the same as this one but with bit
	 *         <code>bit</code> definitely set to 0
	 */
	public Bitstring unset(int bit) {
		return set(bit, false);
	}

	/**
	 * <!-- toggle -->
	 * 
	 * @param bit
	 * @return A Bitstring that is the same as this one but with bit
	 *         <code>bit</code> set to the opposite of what it is now
	 */
	public Bitstring toggle(int bit) {
		return set(bit, !get(bit));
	}

	/**
	 * <!-- jitter -->
	 * 
	 * @param prob
	 * @return A Bitstring that is the same as this one but with each bit
	 *         {@link #toggle(int)}-ed with probability <code>prob</code>
	 */
	public Bitstring jitter(double prob) {
		double probs[] = new double[length];

		Arrays.fill(probs, prob);

		return jitter(probs);
	}

	/**
	 * <!-- jitter -->
	 * 
	 * @param probs
	 *          An array of probabilities, which must have the same length as the
	 *          number of bits in this bitstring
	 * @return A Bitstring that is the same as this one but with each bit
	 *         {@link #toggle(int)}-ed with probability in the corresponding
	 *         element of <code>probs[]</code>
	 */
	public Bitstring jitter(double probs[]) {
		if(probs.length != length) {
			throw new IllegalArgumentException("Probability array has a different length (" + probs.length
					+ ") from  that of the bitstring (" + length + ")");
		}
		if(length == 0) {
			return clone();
		}
		ArrayList<Boolean> list = new ArrayList<Boolean>(length);
		for(int i = 0; i < length; i++) {
			if(Math.random() < probs[i]) {
				list.add(i, get(i));
			}
			else {
				list.add(i, !get(i));
			}
		}
		return new Bitstring(list);
	}

	/**
	 * <!-- subbitstring -->
	 * 
	 * Return a sub-bitstring of this one, with <code>start</code> as the first
	 * element to include (starting at 0), and <code>finish</code> as the first
	 * element not to include. For example, given the bitstring <code>00110</code>
	 * ,
	 * subbitstring(2, 3) would be the bitstring <code>11</code>.
	 * 
	 * @param start
	 * @param finish
	 * @return A Bitstring that is a part of this one's sequence in the range [
	 *         <code>start</code>, <code>finish</code>[
	 */
	public Bitstring subbitstring(int start, int finish) {
		if(start < 0 || start >= length) {
			throw new IllegalArgumentException("Start index " + start + " is outside the range [0, " + length + "[");
		}
		if(finish < 0 || finish > length) {
			throw new IllegalArgumentException("Finish index " + finish + " is outside the range [0, " + length + "]");
		}
		if(finish < start) {
			throw new IllegalArgumentException("Finish index " + finish + " must be >= start index " + start);
		}

		ArrayList<Boolean> list = new ArrayList<Boolean>(finish - start);
		for(int i = start; i < finish; i++) {
			list.add(i - start, get(i));
		}
		return new Bitstring(list);
	}

	/**
	 * <!-- append -->
	 * 
	 * @param other
	 * @return A bitstring formed of the concatenation of this one with
	 *         <code>other</code>
	 */
	public Bitstring append(Bitstring other) {
		if(length == 0 && other.length == 0) {
			return clone();
		}

		ArrayList<Boolean> list = new ArrayList<Boolean>(this.length + other.length);

		for(int i = 0; i < this.length + other.length; i++) {
			list.add(i, (i < this.length ? get(i) : other.get(i - this.length)));
		}

		return new Bitstring(list);
	}

	/**
	 * <!-- not -->
	 * 
	 * @return The complement of this bitstring
	 */
	public Bitstring not() {
		if(length == 0) {
			return clone();
		}
		Integer thisarr[] = toIntArray();
		Integer mask[] = mask();

		for(int i = 0; i < thisarr.length; i++) {
			thisarr[i] = (~thisarr[i]) & mask[i];
		}
		return new Bitstring(length, thisarr);
	}

	/**
	 * <!-- and -->
	 * 
	 * @param other
	 * @return The bitwise AND of this bitstring with <code>other</code>
	 */
	public Bitstring and(Bitstring other) {
		if(other.length != this.length) {
			throw new IllegalArgumentException("Cannot AND bitstrings of different lengths (" + this.length + " and "
					+ other.length + ")");
		}
		if(length == 0) {
			return clone();
		}
		Integer thisarr[] = toIntArray();
		Integer otherarr[] = other.toIntArray();

		for(int i = 0; i < thisarr.length; i++) {
			thisarr[i] = thisarr[i] & otherarr[i];
		}
		return new Bitstring(length, thisarr);
	}

	/**
	 * <!-- or -->
	 * 
	 * @param other
	 * @return The bitwise OR of this bitstring with <code>other</code>
	 */
	public Bitstring or(Bitstring other) {
		if(other.length != this.length) {
			throw new IllegalArgumentException("Cannot OR bitstrings of different lengths (" + this.length + " and "
					+ other.length + ")");
		}
		if(length == 0) {
			return clone();
		}
		Integer thisarr[] = toIntArray();
		Integer otherarr[] = other.toIntArray();

		for(int i = 0; i < thisarr.length; i++) {
			thisarr[i] = thisarr[i] | otherarr[i];
		}
		return new Bitstring(length, thisarr);
	}

	/**
	 * <!-- xor -->
	 * 
	 * @param other
	 * @return The bitwise XOR of this bitstring with <code>other</code>
	 */
	public Bitstring xor(Bitstring other) {
		if(other.length != this.length) {
			throw new IllegalArgumentException("Cannot XOR bitstrings of different lengths (" + this.length + " and "
					+ other.length + ")");
		}
		if(length == 0) {
			return clone();
		}
		Integer thisarr[] = toIntArray();
		Integer otherarr[] = other.toIntArray();
		Integer mask[] = mask();

		for(int i = 0; i < thisarr.length; i++) {
			thisarr[i] = (thisarr[i] ^ otherarr[i]) & mask[i];
		}
		return new Bitstring(length, thisarr);
	}

	/**
	 * <!-- parity -->
	 * 
	 * @param other
	 * @return The complement of the bitwise XOR of this bitstring with
	 *         <code>other</code>
	 */
	public Bitstring parity(Bitstring other) {
		if(other.length != this.length) {
			throw new IllegalArgumentException("Cannot PARITY bitstrings of different lengths (" + this.length + " and "
					+ other.length + ")");
		}
		if(length == 0) {
			return clone();
		}
		Integer thisarr[] = toIntArray();
		Integer otherarr[] = other.toIntArray();
		Integer mask[] = mask();

		for(int i = 0; i < thisarr.length; i++) {
			thisarr[i] = (~(thisarr[i] ^ otherarr[i])) & mask[i];
		}
		return new Bitstring(length, thisarr);
	}

	/**
	 * <!-- mask -->
	 * 
	 * @return A mask with 1 where a bit in the integer array is part of the
	 *         bitstring and 0 elsewhere
	 */
	private Integer[] mask() {
		if(length == 0) {
			return new Integer[0];
		}
		Integer mask[] = new Integer[bitstring.size()];

		for(int i = 0; i < mask.length - 1; i++) {
			mask[i] = ~0;
		}

		int remainder = length - ((mask.length - 1) * Integer.SIZE);

		mask[mask.length - 1] = (~0 << (Integer.SIZE - remainder));
		
		return mask;
	}

	/**
	 * <!-- mutate -->
	 * 
	 * @param prob
	 * @return A new <code>Bitstring</code> with a randomly selected bit mutated
	 *         from this one with probability <code>prob</code>
	 */
	public Bitstring mutate(double prob) {
		if(length == 0) {
			return clone();
		}
		if(Math.random() < prob) {
			int bit = (int)Math.floor(Math.random() * length);
			return mutate(bit);
		}
		else {
			return clone();
		}
	}

	/**
	 * <!-- mutate -->
	 * 
	 * @param bit
	 * @return A new <code>BitString</code> with the specified bit set to a random
	 *         value
	 */
	public Bitstring mutate(int bit) {
		if(length == 0) {
			return clone();
		}
		return set(bit, Math.random() < 0.5);
	}

	/**
	 * <!-- crossover -->
	 * 
	 * @param other
	 * @param prob
	 * @return Two <code>Bitstring</code>s, the result of a crossover operator
	 *         applied with the specified probability
	 */
	public Bitstring[] crossover(Bitstring other, double prob) {
		if(this.length != other.length) {
			throw new IllegalArgumentException("Cannot crossover bitstrings of different lengths (" + length + " and "
					+ other.length + ")");
		}
		if(length == 0) {
			return new Bitstring[] { clone(), clone() };
		}
		if(Math.random() < prob) {
			int bit = (int)Math.floor(Math.random() * (length + 1));
			return crossover(other, bit);
		}
		else {
			return new Bitstring[] { clone(), other.clone() };
		}
	}

	/**
	 * <!-- crossover -->
	 * 
	 * @param other
	 * @param bit
	 * @return Two <code>Bitstring</code>s, the result of a crossover operator
	 *         applied at the specified point.
	 */
	public Bitstring[] crossover(Bitstring other, int bit) {
		if(this.length != other.length) {
			throw new IllegalArgumentException("Cannot crossover bitstrings of different lengths (" + length + " and "
					+ other.length + ")");
		}
		if(length == 0) {
			return new Bitstring[] { clone(), clone() };
		}
		return new Bitstring[] {
														this.subbitstring(0, bit).append(other.subbitstring(bit, length)),
														other.subbitstring(0, bit).append(this.subbitstring(bit, length))
		};
	}

	/**
	 * <!-- grayCode -->
	 *
	 * @return Gray coding of the bitstring
	 */
	public Bitstring grayCode() {
		if(length == 0) {
			return clone();
		}
		return xor(rightShift());
	}

	/**
	 * <!-- inverseGrayCode -->
	 *
	 * @return Inverse Gray coding of the bitstring
	 */
	public Bitstring inverseGrayCode() {
		if(length == 0) {
			return clone();
		}

		Integer thisarr[] = toIntArray();
		Integer result[] = new Integer[thisarr.length];
		Arrays.fill(result, 0);

		result[0] = getBit(0) & thisarr[0];

		for(int i = 1; i < length; i++) {
			int j = bitToArr(i);

			result[j] = set(i, result, length, get(i - 1, result, length) ^ get(i, thisarr, length));
		}
		return new Bitstring(length, result);
	}

	/**
	 * <!-- rightShift -->
	 *
	 * @return Right shift of the bitstring
	 */
	public Bitstring rightShift() {
		if(length == 0) {
			return clone();
		}

		Integer thisarr[] = toIntArray();
		Integer mask[] = mask();

		int prev = 0;
		int msb = ~((~0) >>> 1);
		int lsb = 1;
		
		for(int i = 0; i < thisarr.length; i++) {
			int next = thisarr[i] & lsb;
			thisarr[i] >>>= 1;
			if(prev != 0) {
				thisarr[i] |= msb;
			}
			prev = next;
			thisarr[i] &= mask[i];
		}

		return new Bitstring(length, thisarr);
	}

	/**
	 * <!-- match -->
	 * 
	 * @param other
	 * @return The number of bits in this bitstring and <code>other</code> that
	 *         have the same value
	 */
	public int match(Bitstring other) {
		if(this.length != other.length) return -1;
		if(length == 0) return 0;
		return parity(other).count1();
	}

	/**
	 * <!-- count1 -->
	 * 
	 * @return The number of <code>1</code>s in the bitstring
	 */
	public int count1() {
		int n = 0;

		for(int i = 0; i < length; i++) {
			if(get(i)) {
				n++;
			}
		}

		return n;
	}

	/**
	 * <!-- count0 -->
	 * 
	 * @return The number of <code>0</code>s in the bitstring
	 */
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
	 * @return <code>true</code> if the length is 0
	 */
	@Override
	public boolean isEmpty() {
		return length == 0;
	}

	/**
	 * <!-- all0 -->
	 * 
	 * @return <code>true</code> if all bits are <code>1</code>
	 */
	public boolean all0() {
		for(Integer i: bitstring) {
			if(i > 0) return false;
		}
		return true;
	}

	/**
	 * <!-- all1 -->
	 * 
	 * @return <code>true</code> if all bits are <code>1</code>
	 */
	public boolean all1() {
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
			l.add(i, get(i));
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
		if(o) return !all0();
		else return !all1();
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
		if(length == 0) return EMPTY_BITSTRING_STRING;
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
			if(!this.bitstring.get(i).equals(other.bitstring.get(i))) {
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
		for(int i = 0; i < 43; i++) {
			System.out.println("i = " + i + "; bitToArr(i) = " + bitToArr(i) + "; bitInArr(i) = " + bitInArr(i) + "; getBit(i) = " + Integer.toHexString(getBit(i)));
		}
		Bitstring a = new Bitstring(43, 0.3);
		Bitstring b = new Bitstring(43, 0.7);
		System.out.println("    A = " + a);
		System.out.println("    B = " + b);
		System.out.println("A p B = " + a.parity(b));
		System.out.println("match = " + a.match(b));

		System.out.println("              A = " + a);
		System.out.println("          rsh A = " + a.rightShift());
		System.out.println("         gray A = " + a.grayCode());
		System.out.println("     inv gray A = " + a.inverseGrayCode());
		System.out.println("inv gray gray A = " + a.grayCode().inverseGrayCode());
		
		System.out.println("Trying invgray(gray(A)) == A for a large number of random bitstrings");
		
		for(int i = 1; i <= 1000000; i++) {
			Bitstring c = new Bitstring(10 + (int)Math.rint(Math.random() * 500), Math.random());
			Bitstring d = c.grayCode().inverseGrayCode();
			
			if(!c.equals(d)) {
				System.out.println("     C" + i + " = " + c);
				System.out.println(" rsh C" + i + " = " + c.rightShift());
				System.out.println("gray C" + i + " = " + c.grayCode());
				System.out.println("     D" + i + " = " + d);
			}
			if(i % 100000 == 0) {
				System.out.println("Now tried " + i + " random bitstrings");
			}
		}

		Bitstring empty = new Bitstring(0);
		System.out.println("empty clone " + empty.clone());
		Bitstring string = new Bitstring("10101010101");
		System.out.println(string);
		System.out.println(string.subbitstring(0, 0));
		System.out.println(string.subbitstring(0, 1));
		System.out.println(string.subbitstring(0, string.size()));
	}
}
