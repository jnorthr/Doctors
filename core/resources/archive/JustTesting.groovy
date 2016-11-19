import groovyx.text.DoctorTemplateEngine;
DoctorTemplateEngine dte = new DoctorTemplateEngine();        

String source = '''= testCreateTemplate() Test 1
Community Documentation <Fred@groovy.codehaus.org>
v1.1 2016-07-21, updated 17 Aug. 2016
:linkcss:

== Sample 1 Notes

This is a simple sample.
'''.toString()
def fi = new File('/Users/jim/Dropbox/Projects/Doctors/README.adoc')
URL u = new URL('http://github.com/jnorthr/Doctors/blob/master/README.adoc')
def output = dte.createTemplate(u).make([:])
println "output=\n"+output;

println "--- the end ---"