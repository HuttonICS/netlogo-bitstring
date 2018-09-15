# netlogo-bitstring
A bitstring extension for NetLogo. Bitstrings are strings of binary digits. This
extension provides you with an implementation of bitstrings. The following
explains how to use the bitstring extension in NetLogo.

## Using the extension in your models.
To use the extension, you should put `bitstring.jar` in a folder named `bitstring`
in the same directory as your `.nlogo` model file. Then in the `Code` tab, put

```
extensions [bitstring]
```

at the top of your code.

## Creating bitstrings

Four commands are available to create bitstrings:

* `bitstring:make _length_ _value_` -- build a bitstring of length `_length_`
  initialized with all elements set to truth value `_value_`. Examples:

  ```
  observer> print bitstring:make 10 true
  {{bitstring: 1111111111}}
  observer> print bitstring:make 10 false
  {{bitstring: 0000000000}}
  ```

* `bitstring:from-list _list_` -- build a bitstring using elements of _list_ to
  initialize it. For numeric items `1.0` or `1` are treated as `true`, and `0.0`
  or `0` as `false`. All other numbers will throw an exception. For string items,
  `"TRUE"`, `"T"`, `"YES"`, `"Y"` or `"1"` are all treated as `true`, while
  `"FALSE"`, `"F"`, `"NO"`, `"N"` or `"0"` are all treated as `false`. These
  strings are not case-sensitive. (So `"yes"` is also treated as `true`.) All
  other strings will throw an exception. Booleans (`true` and `false`) can also
  be used as list items. All elements of the list must have the same type (numeric,
  string or Boolean). Any other type will throw an exception. Example:

  ```
  observer> print bitstring:from-list [1 0 0 1 0.0 0.0]
  {{bitstring: 100100}}
  observer> print bitstring:from-list ["true" "yes" "Y" "No" "FALSE" "0"]
  {{bitstring: 111000}}
  observer> print bitstring:from-list [true true false false true true false]
  {{bitstring: 1100110}}
  ```

* `bitstring:from-string _str_` -- build a bitstring using each character of
  _str_ to initialize it. Characters `1`, `T`, `t`, `Y` and `y` are treated
  as `true`; `0`, `F`, `f`, `N` and `n` as `false`. Example:

  ```
  observer> print bitstring:from-string "1ty0fn"
  {{bitstring: 111000}}
  ```

* `bitstring:random _length_ _p-true_` -- build a random bitstring of length
  `_length_` with probability `_p-true_` of each bit being set to `true`.
  Examples:

  ```
  observer> print bitstring:random 20 0.1
  {{bitstring: 00000001000000100000}}
  observer> print bitstring:random 20 1.0
  {{bitstring: 11111111111111111111}}
  ```

* `bitstring:cat _bitstring1_ _bitstring2_` -- build a bitstring from the
  concatenation of `_bitstring1_` and `_bitstring2_`.

## Accessors

Functions allowing you to inspect or access the status of bits in a bitstring.

* `bitstring:get? _bitstring_ _index_` -- return a Boolean indicating whether the bit at
  position `_index_` in `_bitstring` is set to `true`. Indexes start at 0.

* `bitstring:first? _bitstring_` -- is the first bit of the `_bitstring_` set to `true`?

* `bitstring:last? _bitstring_` -- is the last bit of the `_bitstring_` set to `true`?

* `bitstring:but-first _bitstring_` -- return a bitstring one bit shorter than
  `_bitstring_`, lacking the first bit thereof.

* `bitstring:but-last _bitstring_` -- return a bitstring one bit shorter than
  `_bitstring_`, lacking the last bit thereof.

* `bitstring:sub _bitstring_ _start_ _finish_` -- return a sub-bitstring of
  `_bitstring_`, starting at `_start_` and ending at the bit before `_finish_`.

* `bitstring:count0 _bitstring_` -- return a count of the number of bits in
  `_bitstring_` that are set to `false`.

* `bitstring:count1 _bitstring_` -- return a count of the number of bits in
  `_bitstring_` that are set to `true`.

* `bitstring:all0? _bitstring_` -- `true` iff all bits in `_bitstring_` are set
  to `false`.

* `bitstring:all1? _bitstring_` -- `true` iff all bits in `_bitstring_` are set
  to `true`.

* `bitstring:any0? _bitstring_` -- `true` iff at least one bit in `_bitstring_`
  is set to `false`.

* `bitstring:any1? _bitstring_` -- `true` iff at least one bit in `_bitstring_`
  is set to `true`.

* `bitstring:to-list _bitstring_` -- return a NetLogo list of Booleans. For
  example:

  ```
  observer> print bitstring:to-list bitstring:random 10 0.5
  [true true false false false true true false true true]
  ```

## "Setters"

Bitstrings are immutable, but these functions give you a new bitstring with
the stated effect.

* `bitstring:set _bitstring_ _pos_ _value_` -- return a bitstring the same as
  `_bitstring_` but with the bit at position `_pos_` set to `_value_`. Positions
  start at 0, and `_value_` should be `true` or `false`.

* `bitstring:fput _bitstring_ _value_` -- return a bitstring one bit longer
  than `_bitstring_`, with the first element set to `_value_` and the remaining
  elements a copy of `_bitstring_`. `_value_` must be `true` or `false`.

* `bitstring:lput _bitstring_ _value_` -- return a bitstring one bit longer
  than `_bitstring_`, with the last element set to `_value_` and the remaining
  elements a copy of `_bitstring_`. `_value_` must be `true` or `false`.

## Bitwise operators

Various functions are provided to implement bitwise operators on bitstrings.

* `bitstring:not _bitstring_` -- return the complement of `_bitstring_`.

* `bitstring:and _bitstring1_ _bitstring2_` -- return a bitstring that is `true`
  in all positions where both `_bitstring1_` and `_bitstring2_` are `true`, and
  `false` otherwise. Both bitstrings must have the same length.

* `bitstring:or _bitstring1_ _bitstring2_` -- return a bitstring that is `false`
  in all positions where both `_bitstring1_` and `_bitstring2_` are `false`, and
  `true` otherwise. Both bitstrings must have the same length.

* `bitstring:xor _bitstring1_ _bitstring2_` -- return a bitstring that is `true`
  in all positions where `_bitstring1_` and `_bitstring2_` have different values,
  and `false` when they have the same value. Both bitstrings must have the same
  length.

* `bitstring:parity _bitstring1_ _bitstring2_` -- return a bitstring that is
  `true` in all positions where `_bitstring1_` and `_bitstring2_` have the
  same value, and `false` when they have different values. Both bitstrings must
  have the same length.

* `bitstring:right-shift _bitstring_` -- return a bitstring in which all the bits in
  _bitstring_ have been shifted to the right. For example:

  ```
  observer> print bitstring:right-shift bitstring:from-string "100000"
  {{bitstring: 010000}}
  ```

* `bitstring:gray-code _bitstring_` -- return a [Gray-coding](https://en.wikipedia.org/wiki/Gray_code "Wikipedia page on Gray code")
  of the `_bitstring_`.

* `bitstring:inverse-gray-code _bitstring_` -- return the inverse [Gray-coding](https://en.wikipedia.org/wiki/Gray_code "Wikipedia page on Gray code")
  of the `_bitstring_`.

## Comparison functions

* `bitstring:contains? _bitstring1_ _bitstring2_` -- return `true` if `_bitstring2_`
  is a sub-bitstring of `_bitstring1_`.

* `bitstring:match _bitstring1_ _bitstring2_` -- return a count of the number of
  positions in which both `_bitstring1_` and `_bitstring2_` have the same value.

## "Genetic" operators

Functions that might be useful if you are using the bitstrings to implement
some sort of genetic algorithm.

* `bitstring:mutate _bitstring_ _pos_` -- Return a bitstring equal to
  `_bitstring_`, but with the bit at position `_pos_` set to a random value.

* `bitstring:toggle _bitstring_ _pos_` -- Return a bitstring equal to
  `_bitstring_`, but with the bit at position `_pos_` set to the complement
  of the corresponding bit in `_bitstring_`.

* `bitstring:crossover _bitstring1_ _bitstring2_ _pos_` -- Return a NetLogo
  list containing two bitstrings, each derived from crossing over `_bitstring1_`
  and `_bitstring2_` at position `_pos_`. It's easier to see an example:

  ```
  observer> print bitstring:crossover bitstring:random 10 0.0 bitstring:random 10 1.0 5
  [{{bitstring: 0000011111}} {{bitstring: 1111100000}}]
  ```

* `bitstring:jitter _bitstring_ _probs_ ...` -- Return a new bitstring, created
  by toggling each of the bits in `_bitstring_` with probabilities `_probs_`.
  The set of probabilities appearing in the arguments after `_bitstring_` will
  be recycled to fill the length of `_bitstring_`. It probably makes sense to
  call this with just one probability, or with a number of probabilities equal
  to the length of the `_bitstring_`. Some examples:

  ```
  observer> print bitstring:jitter bitstring:from-string "0000000000" 0.5
  {{bitstring: 1100110111}}
  observer> print (bitstring:jitter bitstring:from-string "0000000000" 0.1 0.2 0.3 0.4 0.5 0.6 0.7 0.8 0.9 1.0)
  {{bitstring: 0000010111}}
  ```
