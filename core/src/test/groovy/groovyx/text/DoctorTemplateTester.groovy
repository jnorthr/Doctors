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
        // write a log of everything as flog output file handle    
        File flog = new File('resources/DoctorTemplateTester.log')
        flog.write("DoctorTemplate starting\n");
                
        def dr = new DoctorTemplate()
        
        println "\n------------------------\nDoctorTemplate Start Test 1 :\n-------------------------------------\n";
        // Use toString() of Writable closure.
        def xx = dr.make { Writer out -> out << "= Hello world!\n:linkcss:\n" }.toString() 
        
        flog.append "\nDoctorTemplate step 1"+xx;
        File fo1 = new File('resources/DoctorTemplateTest1.html')
        fo1.write(xx.toString());
        println "\n------------------------\nDoctorTemplate End Test 1 :\n-------------------------------------\n";


    // ------------------------------------------------------------
        // Provide data for the binding.
        // The closure is not executed when the make method is finished.
        println "\n------------------------\nDoctorTemplate Start Test 2 :\n-------------------------------------\n";
         dr = new DoctorTemplate()
        final writable = dr.make(user:'mrhaki', { out ->
            out.println "= Welcome ${user},\n:linkcss:\n"
            out.print "Today on ${new Date(year: 115, month: 9, date: 4).format('dd-MM-yyyy')}, "
            out.println "we have a Groovy party!"
        });
        
        File fo2 = new File('resources/DoctorTemplateTest2.html')
        // We invoke toString() and now the closure is executed.
        fo2.write(writable.toString());
        flog.append "\nDoctorTemplate step 2 :"+writable.toString()
        println "\n------------------------\nDoctorTemplate End Test 2 :\n-------------------------------------\n";
        

        // ------------------------------------------------------------
        // Append contents to a file.
        // NOTE: The leftShift (<<) operator on File is implemented in Groovy to use the File.append() method.
        // The append() method creates a new Writer and invokes the write() method which is re-implemented in Groovy if the argument
        // is a Writable object. Then the writeTo() method is invoked:
        // Writer.write(Writable) becomes Writable.writeTo(Writer).
        println "\n------------------------\nDoctorTemplate Start Test 3 :\n-------------------------------------\n";
        new File('resources/DoctorTemplateTest3.html').delete()
        
        // So Groovy magic allows us to use the following one-liner and still the writeTo() method is used on Writable.
        new File('resources/DoctorTemplateTest3.html') << writable;
        flog.append "\nDoctorTemplate step 3"
        println "\n------------------------\nDoctorTemplate End Test 3 :\n-------------------------------------\n";

         
        // step 4
        println "\n------------------------\nDoctorTemplate Start Test 4 :\n-------------------------------------\n";
        def dr4 = new DoctorTemplate()
        def m = ['user':'jnorthr']
        final writable2 = dr4.make(m, { out ->
            out.println "= Welcome ${user},\n:linkcss:\n"
            out.println "Today on ${new Date(year: 116, month: 10, date: 4).format('dd-MM-yyyy')}, "
            out.println "we have a Groovy party!"
        })
        
        flog.append "\nDoctorTemplate step 4 :"+writable2.toString();
        
        File fo4 = new File('resources/DoctorTemplateTest4.html')
        fo4.write(writable2.toString());
        println "\n------------------------\nDoctorTemplate End Test 4 :\n-------------------------------------\n";


        // ------------------------------------------------------------
        println "\n------------------------\nDoctorTemplate Start Test 5 :\n-------------------------------------\n";
        flog.append "\nDoctorTemplate step 5 :";
        def dr5 = new DoctorTemplate()
        File fo5 = new File('resources/DoctorTemplateTest5.html')
        def x5 = dr5.make().toString();
        fo5.write(x5)
        println "\n------------------------\nDoctorTemplate End Test 5 :\n-------------------------------------\n";


        // ------------------------------------------------------------
        println "\n------------------------\nDoctorTemplate Start Test 6 :\n-------------------------------------\n";
        flog.append "\nDoctorTemplate step 6 :";
        def dr6 = new DoctorTemplate()
    
        dr6.setPayload("= DoctorTemplate step 6\n:linkcss:\n\nThis test uses the alternative code to define the payload to be rendered.")
        File fo6 = new File('resources/DoctorTemplateTest6.html')
        fo6.write(dr6.make(['user':'jim']).toString());
        println "\n------------------------\nDoctorTemplate End Test 6 :\n-------------------------------------\n";


    // ------------------------------------------------------------
        println "\n------------------------\nDoctorTemplate Start Test 7 :\n-------------------------------------\n";
        def payload7 = '''= Welcome ${user},
:linkcss:
Today on 04-09-2015, we have a Groovy party!
'''.toString();
        def doc7 =  new DoctorTemplate();
        doc7.load(payload7);
        def result7 = doc7.make()
        println "payload7 created document result of "+result7.size()+" bytes"
        flog.append "\nDoctorTemplate step 7 - did not provide user in binding so it's not populated in the result :"+"-------------------------------------\n";
        File fo7 = new File('resources/DoctorTemplateTest7.html')
        fo7.write(doc7.toString());
        println "payload7 created toString() of "+doc7.toString().size()+" bytes"
        println "\n------------------------\nDoctorTemplate End Test 7 :\n-------------------------------------\n";



        // ------------------------------------------------------------
        println "\n------------------------\nDoctorTemplate Start Test 8 :\n-------------------------------------\n";
        def source8 = """= Servlets 3.0
Community Documentation <\${user}@groovy.codehaus.org>
v1.1, 2015-07-21 updated 17 July 2016
:toc: left
:allow-uri-read:
:linkcss:
:fred: 6
:name: jnorthr
:town: Norman

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

        DoctorTemplate doc8 = new DoctorTemplate()

        doc8.load(source8)
        doc8.make([user:'jimbo'])
        flog.append "\nDoctorTemplate step 8 :"+"-------------------------------------\n";
        File fo8 = new File('resources/DoctorTemplateTest8.html')
        fo8.write(doc8.toString());
        println "source8 created toString() of "+doc8.toString().size()+" bytes"
        println "\n------------------------\nDoctorTemplate End Test 8 :\n-------------------------------------\n";



        // -----------------------------------------------------
        println "\n------------------------\nDoctorTemplate Start Test 9 :\n-------------------------------------\n";
        def source9 = '''<% include resources/header.txt %>
:linkcss:

+++<% include resources/welcome.txt %>+++        

== Cloud Foundry Notes

The document title is called {doctitle} and has {fred} parts with groovy +++<%= user %>+++ users!
'''        
        DoctorTemplate doc9 = new DoctorTemplate()
        doc9.load(source9)
        doc9.make([user:'jimbo'])
        flog.append "\nDoctorTemplate step 9 :"+"-------------------------------------\n";
        File fo9 = new File('resources/DoctorTemplateTest9.html')
        fo9.write(doc9.toString());
        println "source9 created toString() of "+doc9.toString().size()+" bytes"
        println "\n------------------------\nDoctorTemplate End Test 9 :\n-------------------------------------\n";


    // ------------------------------------------------------
        println "\n------------------------\nDoctorTemplate Start Test 10 :\n-------------------------------------\n";
        def source10 = '''= testCreateTemplate() Test 10 - template with asciidoctor include file exists,
Community Documentation <Users@groovy.codehaus.org>
Version 1.1, 2014-09-02: Last Updated 2016-9-28, revision20
:linkcss:

Here we show you an example thats to be included: 

include::resources/Sample1.adoc[]

== Next Sample Include

Here is a groovy gstring var:{name} +++<% include file="resources/welcome.txt" %>+++

said Bill.

[source,groovy]
.Sample1.adoc
----
buildDir/template.groovy
----

'''
        try{
            def doc10 = new DoctorTemplate()
            doc10.load(source10.toString())
            doc10.make()
            flog.append "\nDoctorTemplate step 10 :\n-------------------------------------\n";
            File fo10 = new File('resources/DoctorTemplateTest10.html')
            fo10.write(doc10.toString());
            println "source10 created toString() of "+doc10.toString().size()+" bytes"
        }
        catch(any)
        {
            flog.append "\nDoctorTemplate step 10 error :"+any.message+"\n-------------------------------------\n";
        } // end of catch
        println "\n------------------------\nDoctorTemplate End Test 10\n-------------------------------------\n";


        flog.append "DoctorTemplate end of testing\n\n"
        
    } // end of main
    
    // =========================================
    
    
} // end of class