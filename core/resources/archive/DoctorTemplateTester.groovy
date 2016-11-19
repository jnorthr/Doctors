/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package groovyx.text

import groovyx.text.*
import groovy.transform.*;

/** 
 * DoctorTemplateTester class description
 *
 * A class to test a main method for the DoctorTemplate class.
 */ 
@Canonical 
public class DoctorTemplateTester
{

   // =====================================================================
   /** 
    * Method to run class tests.
    * 
    * @param args Value is string array - possibly empty - of command-line values. 
    * @return void
    */    
    public static void main(String[] args)
    {
    	// write a log of everything as fo5 output file handle    
        File fo5 = new File('resources/DoctorTemplateTester.log')
        fo5.write("DoctorTemplate starting\n");
                
        def dr = new DoctorTemplate()
        
        // Use toString() of Writable closure.
        assert dr.make { Writer out -> out << "Hello world!" }.toString() == 'Hello world!'
        fo5.append "\nDoctorTemplate step 1"


	// ------------------------------------------------------------
        // Provide data for the binding.
        // The closure is not executed when the make method is finished.
        final writable = dr.make(user:'mrhaki', { out ->
            out.println "Welcome ${user},"
            out.print "Today on ${new Date(year: 115, month: 9, date: 4).format('dd-MM-yyyy')}, "
            out.println "we have a Groovy party!"
        });
        
        fo5.append "\nDoctorTemplate step 2 :"+writable.toString()
        

	// ------------------------------------------------------------
        // We invoke toString() and now the closure is executed.
        final result = writable.toString()
        fo5.append "\nDoctorTemplate step 3 :"+result

        assert result == '''Welcome mrhaki,
Today on 04-10-2015, we have a Groovy party!
'''.toString();


	// ------------------------------------------------------------
        // Append contents to a file.
        // NOTE: The leftShift (<<) operator on File is implemented in Groovy to use the File.append() method.
        // The append() method creates a new Writer and invokes the write() method which is re-implemented in Groovy if the argument
        // is a Writable object. Then the writeTo() method is invoked:
        // Writer.write(Writable) becomes Writable.writeTo(Writer).

        new File('resources/welcome.txt').delete()
        
        // So Groovy magic allows us to use the following one-liner and still the writeTo() method is used on Writable.
        new File('resources/welcome.txt') << writable;
        fo5.append "\nDoctorTemplate step 4"

        assert new File('resources/welcome.txt').text == '''Welcome mrhaki,
Today on 04-10-2015, we have a Groovy party!
'''    
         
	// step 5
        def m = ['user':'jnorthr']
        final writable2 = dr.make(m, { out ->
            out.println "Welcome ${user},"
            out.println "Today on ${new Date(year: 114, month: 4, date: 4).format('dd-MM-yyyy')}, "
            out.println "we have a Groovy party!"
        })
        
        fo5.append "\nDoctorTemplate step 5 :"+writable2.toString();
        
        File fo4 = new File('resources/DoctorTemplateTest5.html')
        fo4.write(writable2.toString());

	// ------------------------------------------------------------
        fo5.append "\nDoctorTemplate step 6 :";
        File fo6 = new File('resources/DoctorTemplateTest6.html')
        fo6.write(dr.make().toString());

	// ------------------------------------------------------------
        fo5.append "\nDoctorTemplate step 7 :";
        dr.setPayload("= DoctorTemplate step 7\n\nThis test uses the alternative code to define the payload to be rendered.")
        File fo7 = new File('resources/DoctorTemplateTest7.html')
        fo7.write(dr.make(['user':'jim']).toString());


	// ------------------------------------------------------------
        def payload = '''Welcome ${user},
Today on 04-09-2015, we have a Groovy party!
'''.toString();
        Writable doc = new DoctorTemplate(payload).make()
        fo5.append "\nDoctorTemplate step 8 - did not provide user in binding so it's not populated in the result :"+"-------------------------------------\n";
        File fo8 = new File('resources/DoctorTemplateTest8.html')
        fo8.write(doc.toString());



	// ------------------------------------------------------------
        def source = """= Servlets 3.0
Community Documentation <\${user}@groovy.codehaus.org>
v1.1, 2015-07-21 updated 17 July 2015
:toc: left
:allow-uri-read:
:linkcss:
:fred: 6

== Cloud Foundry Notes

The document title is called {doctitle} and has {fred} parts with groovy +++<%= user %>+++ users!
<%= user %> 
Here is a groovy gstring var:{name} that has no attribute declared in this doc, so it remains un-xlated. +++<% include file="resources/welcome.txt" %>+++
http://docs.gopivotal.com/pivotalcf/concepts/roles.html[Orgs, Spaces, Roles, and Permissions ]

The include directive works like this - here is a sample include that is part of a code fragment so it should NOT be xlated:

[source,groovy]
.Sample1.adoc
----
include::resources/Sample1.adoc[]
----

''''

The include directive works like this - here is a sample include that is NOT part of a code fragment so it should be xlated

include::resources/header.txt[]

''''

*That's All Folks*

""".toString();
        doc = new DoctorTemplate(source).make([user:'jimbo'])
        fo5.append "\nDoctorTemplate step 9 :"+"-------------------------------------\n";
        File fo9 = new File('resources/DoctorTemplateTest9.html')
        fo9.write(doc.toString());


	// -----------------------------------------------------
        source = '''<% include resources/header.txt %>

+++<% include resources/welcome.txt %>+++        

== Cloud Foundry Notes

The document title is called {doctitle} and has {fred} parts with groovy +++<%= user %>+++ users!
'''        
        doc = new DoctorTemplate(source).make([user:'jimbo'])
        fo5.append "\nDoctorTemplate step 10 :"+"-------------------------------------\n";
        File fo10 = new File('resources/DoctorTemplateTest10.html')
        fo10.write(doc.toString());



	// ------------------------------------------------------
        def source3 = '''= testCreateTemplate() Test 11 - template with asciidoctor include file exists,
Community Documentation <Users@groovy.codehaus.org>
Version 1.1, 2014-09-02: Last Updated 2014-9-28, revision20
:linkcss:

Here we show you an example thats to be included: 

include::resources/Sample1.adoc[]

== Next Sample Include

Here is a groovy gstring var:{name} +++<% include file="resources/welcome.txt" %>+++

said Bill.

[source,groovy]
.Sample1.adoc
----
$buildDir/template.groovy
----

'''
        try{
            def doc3 = new DoctorTemplate(source3.toString()).make()
            fo5.append "\nDoctorTemplate step 11 :"+"-------------------------------------\n";
	    File fo11 = new File('resources/DoctorTemplateTest11.html')
    	    fo11.write(doc3.toString());
        }
        catch(any)
        {
            fo5.append "\nDoctorTemplate step 11 error :"+any.message+"\n-------------------------------------\n";
        } // end of catch

        fo5.append "DoctorTemplate end of testing\n\n"
        
    } // end of main
    
    // =========================================
    
    
} // end of class