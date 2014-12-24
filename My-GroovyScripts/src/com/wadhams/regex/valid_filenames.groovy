package com.wadhams.regex
import java.util.regex.Matcher
import java.util.regex.Pattern

println "${this.class.simpleName} started..."
println ''

def p = ~/[\w\-\.]+/

assert ('FormsLib.zip' ==~ p)		//period allowed
assert ('Forms-Lib.zip' ==~ p)		//dash allowed
assert ('Forms_Lib.zip' ==~ p)		//underscore allowed

assert !('Forms Lib.zip' ==~ p)		//no spaces allowed

println ''
println "${this.class.simpleName} ended..."