package com.wadhams.regex

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

println ''
println "${this.class.simpleName} ended..."
