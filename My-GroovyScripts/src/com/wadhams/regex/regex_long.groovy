package com.wadhams.regex
// Groovy supports regular expressions natively using the ~String
// (e.g. ~/foo/) expression, which creates a compiled Java Pattern
// object from the given pattern string.
// Groovy also supports the =~ and ==~ operators.
// =~  (creates a Matcher)
// ==~ (returns boolean, whether String matches the pattern)

// For matchers having groups, matcher[index] is either a
// matched String or a List of matched group Strings.

import java.util.regex.Matcher
import java.util.regex.Pattern

println "${this.class.simpleName} started..."
println ''

// ~ creates a Pattern from String
def pattern = ~/foo/
assert pattern instanceof Pattern
assert pattern.matcher("foo").matches() == true
assert pattern.matcher("foobar").matches() == false

// lets create a Matcher
def matcher = "cheesecheese" =~ /cheese/
assert matcher instanceof Matcher

// =~ creates a Matcher, and in a boolean context,
// it's "true" if it has at least one match, "false" otherwise.
assert "cheesecheese" =~ "cheese"
assert ("cheesecheese" =~ "cheese").asBoolean() == true
assert "cheesecheese" =~ /cheese/
assert ("cheesecheese" =~ /cheese/).asBoolean() == true
assert "cheese" == /cheese/   //slashy string syntax
assert ! ("cheese" =~ /ham/)
assert ("cheese" =~ /ham/).asBoolean() == false

// ==~ tests, if String matches the pattern exactly
assert "2009" ==~ /\d+/      // returns TRUE
assert ("2009" ==~ /\d+/) == true
assert ! ("holla" ==~ /\d+/) // returns FALSE
assert ("holla" ==~ /\d+/) == false // returns FALSE

// lets do some replacement
def cheese = ("cheesecheese" =~ /cheese/).replaceFirst("nice")
assert cheese == "nicecheese"
assert "color" == "colour".replaceFirst(/ou/, "o")

cheese = ("cheesecheese" =~ /cheese/).replaceAll("nice")
assert cheese == "nicenice"

// simple group demo
// You can also match a pattern that includes groups.
// First create a matcher object, either using the Java API,
// or more simply with the =~ operator.  Then, you can index
// the matcher object to find the matches.
// matcher[0] returns a List representing the first match of the
// regular expression in the string.  The first element is the string
// that matches the entire regular expression, and the remaining
// elements are the strings that match each group.
// Here's how it works:
def m = "foobarfoo" =~ /o(b.*r)f/
assert m[0] == ["obarf", "bar"]
assert m[0][1] == "bar"

// Although a Matcher isn't a list, it can be indexed like a list.
// In Groovy 1.6 this includes using a collection as an index:

matcher = "eat green cheese" =~ "e+"

assert "ee" == matcher[2]
assert ["ee", "e"] == matcher[2..3]
assert ["e", "ee"] == matcher[0, 2]
assert ["e", "ee", "ee"] == matcher[0, 1..2]

matcher = "cheese please" =~ /([^e]+)e+/
assert ["se", "s"] == matcher[1]
assert [["se", "s"], [" ple", " pl"]] == matcher[1, 2]
assert [["se", "s"], [" ple", " pl"]] == matcher[1 .. 2]
assert [["chee", "ch"], [" ple", " pl"], ["ase", "as"]] == matcher[0, 2..3]

// Matcher defines an iterator() method, so it can be used, for example,
// with collect() and each():
matcher = "cheese please" =~ /([^e]+)e+/
matcher.each { println it }
matcher.reset()
assert matcher.collect { it } ==
	  [["chee", "ch"], ["se", "s"], [" ple", " pl"], ["ase", "as"]]

// The semantics of the iterator were changed by Groovy 1.6.
// In 1.5, each iteration would always return a string of the entire match,
// ignoring groups. In 1.6, if the regex has any groups, it returns
// a list of Strings as shown above.

// there is also regular expression aware iterator grep()
assert ["foo", "moo"] == ["foo", "bar", "moo"].grep(~/.*oo$/)
// which can be written also with findAll() method
assert ["foo", "moo"] == ["foo", "bar", "moo"].findAll { it ==~ /.*oo/ }

//Since a Matcher coerces to a boolean by calling its find method,
//the =~ operator is consistent with the simple use of Perl's =~ operator,
//when it appears as a predicate (in 'if', 'while', etc.).
//The "stricter-looking" ==~ operator requires an exact match
// of the whole subject string. It returns a Boolean, not a Matcher.

// ANOTHER GROOVY REGEX EXAMPLE

// Groovy adds 3 operators for convenience:
// The regex find operator =~
// The regex match operator ==~
// The regex pattern operator ~string

// slashy strings
assert "abc" == /abc/
assert "\\d" == /\d/
def reference = "hello"
def slashy = /$reference/
assert reference == slashy
assert slashy instanceof groovy.lang.GString

def regex_1a = /^[A-Z]/
// .find() and =~ operator
assert "D123".find(regex_1a)	//groovy truth
assert "D123".find(regex_1a) == 'D'
assert !("123".find(regex_1a))	//groovy truth

assert "D123" =~ regex_1a		//matcher w/ match 
m = "D123" =~ regex_1a
assert m
assert 'D' == m[0]
m = "123" =~ regex_1a	//matcher w/o match
assert !m

def regex_1b = /^[A-Z][1-3]{3}/
// .matches() and ==~ operator
assert "D123".matches(regex_1b)
assert "D123" ==~ regex_1b

def regex_2 = /^\w\W\w\w\w/		//no double backslashing
assert "D@1_w".find(regex_2)

// Regular expressions
def twister = 'she sells sea shells at the sea shore of seychelles'
// twister must contain a substring of size 3 that starts with s and ends with a
assert twister =~ /s.a/

def finder = (twister =~ /s.a/)
assert finder instanceof java.util.regex.Matcher

// twister must contain only words delimited by single spaces
assert twister ==~ /(\w+ \w+)*/

def WORD = /\w+/
matches = (twister ==~ /($WORD $WORD)*/)
assert matches instanceof java.lang.Boolean

assert !(twister ==~ /s.e/)		//must match whole string

def wordsByX = twister.replaceAll(WORD, 'x')
assert wordsByX == 'x x x x x x x x x x'

def words = twister.split(/ /)
assert words.size() == 10
assert words[0] == 'she'

// Working on each match of a pattern
def myFairStringy = 'The rain in Spain stays mainly in the plain!'

def wordEnding = /\w*ain/
def rhyme = /\b$wordEnding\b/	// words that end with 'ain': \b\w*ain\b
def found = ''
myFairStringy.eachMatch(rhyme) { match ->
found += match + ' '
}
assert found == 'rain Spain plain '

found = ''
(myFairStringy =~ rhyme).each { match ->
found += match + ' '
}
assert found == 'rain Spain plain '

def cloze = myFairStringy.replaceAll(rhyme){ it-'ain'+'___' }
assert cloze == 'The r___ in Sp___ stays mainly in the pl___!'

matcher = 'a b c' =~ /\S/
assert matcher[0] == 'a'
assert matcher[1..2] == ['b','c']
assert matcher.size() == 3

matcher = 'a:1 b:2 c:3' =~ /(\S+):(\S+)/
assert matcher.hasGroup()
assert matcher[0] == ['a:1', 'a', '1'] // 1st match
assert matcher[1][2] == '2' // 2nd match, 2nd group

matcher = 'a:1 b:2 c:3' =~ /(\S+):(\S+)/
matcher.each { full, key, value ->
	assert full.size() == 3
	assert key.size() == 1 // a,b,c
	assert value.size() == 1 // 1,2,3
}

//Increase performance with pattern reuse
//words that starts and ends with same letter
def regex = /\b(\w)\w*\1\b/
def many = 100 * 1000

start = System.nanoTime()
many.times{
twister =~ regex
}
timeImplicit = System.nanoTime() - start

start = System.nanoTime()
pattern = ~regex
many.times{
pattern.matcher(twister)
}
timePredef = System.nanoTime() - start

assert timeImplicit > timePredef * 2

//Patterns for Classification
def fourLetters = ~/\w{4}/

assert fourLetters.isCase('work')

assert 'love' in fourLetters

switch('beer'){
case fourLetters:
	assert true
	break
default :
	assert false
}

beasts = ['bear','tiger','wolf','regex']

assert beasts.grep(fourLetters) == ['bear','wolf']

//case insensitive pattern
def caseInsensitivePattern = new Pattern(/robo-ftp\.exe/, Pattern.CASE_INSENSITIVE)
assert "robo-ftp.exe".find(caseInsensitivePattern)
assert "Robo-FTP.exe".find(caseInsensitivePattern)
assert "ROBO-FTP.EXE".find(caseInsensitivePattern)

println ''
println "${this.class.simpleName} ended..."
