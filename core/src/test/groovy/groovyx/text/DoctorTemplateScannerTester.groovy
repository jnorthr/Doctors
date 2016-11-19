/*
 * Copyright 2016 the original author or authors.
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
 
// 
// if found and identified target file exists, the <% include 'xxx.adoc' %> syntax is removed
// and replaced by the external file content

package groovyx.text;
import groovy.transform.*;

/**
 * A test wrapper for the parser to pre-parse asciidoctor payloads for signs of JSP-style include statements;
 * groovyx.text.DoctorTemplateScannerTester.groovy
 *
 * @author Jim Northrop
 * @since 2.4.4
 */
@Canonical 
public class DoctorTemplateScannerTester
{
    // =============================
    public static void main(String[] args)
    {

    	// write a log of everything as fo5 output file handle
        File fo5 = new File('resources/DoctorTemplateScannerTester.log')
        fo5.write("DoctorTemplateScanner arrives\n");

	// invent a complex and broken set of syntax
        def title = ''' 0   0    1    1    2    2    3    3    4    5    5    5    6    6    7    7    8
012345678901234567890123456789012345678901234567890123456789012345678901234567890'''
        def payload = 
'''ab<>cd<%%>ef<% include %>gh<% include %>ij<% include %>kl<% include %>mn<%=include %>op<% include%>qr<%include%>st<%= name %>uv<%include'%>wx<%include''%>yz<%include"%>aa<%include""%>bb<%include=''%>cc<%include='fred'%>dd<%include 'fred'%>ee<%include file='fred'%>ff<%  include file="sample.txt"%>gg<%=  include Sample1.adoc %>hh<%=  include sample.txt%>II<%=  include /media/jnorthr/LINKS/asciidoctor-project/war/asciidocs/sample1.adoc %>JJ<%=  include   /home/jnorthr/Google Drive/DoctorTemplate/war/asciidocs/sample6.adoc    %>KK;
<%= Hi kids <% include 'Sample1.adoc' %>wxy($buildDir/template.groovy)The End'''


        fo5.append('--- the start ---\n');
        
        DoctorTemplateScanner dts = new DoctorTemplateScanner();
        dts.parse(payload);
        fo5.append "\n============================\nResult=\n|" + dts.result + "|\n-----------------------------------\n";
        fo5.append title+'\n';
        fo5.append "------------------------------\n"
        fo5.append payload+'\n';
        fo5.append "\n------------------------------\nPayload2 follows:"+'\n'
        def payload2 = '''= Sample Asciidoctor Include

<% include 'Sample1.adoc' %>'''
        fo5.append title+'\n';
        fo5.append "------------------------------"+'\n'
        fo5.append payload2+'\n';
        fo5.append "------------------------------"+'\n'

        fo5.append dts.parse(payload2)+'\n';
        fo5.append "\n------------------------------"+'\n'
        
        payload2 = '''<% include heading.txt %>
== Cloud Foundry Notes

The document title is called {doctitle} and has {fred} parts with groovy +++<%= user %>+++ users!
'''        
        fo5.append title+'\n';
        fo5.append "------------------------------"+'\n'
        fo5.append payload2+'\n';
        fo5.append "------------------------------"+'\n'
        //dts.setDebug()
        fo5.append dts.parse(payload2)+'\n';
        fo5.append "\n------------------------------"

        def payload3 = '''= Note 3.0
Community Documentation <${user}@groovy.codehaus.org>
v1.1, 2014-07-21 updated 17 July 2015
:toc: left
:allow-uri-read:
:linkcss:

== Cloud Foundry Notes

The document title is called {doctitle} and has {fred} parts with groovy +++<%= user %>+++ users!
<%= user %> 
Here is a groovy gstring var:{name} +++<% include file="includes/header.jsp" %>+++
http://docs.gopivotal.com/pivotalcf/concepts/roles.html[Orgs, Spaces, Roles, and Permissions ]

The include directive works like this:

[source,groovy]
.Sample1.adoc
----
include::Sample1.adoc[]
----

'''.toString();

        fo5.append title+'\n';
        fo5.append "------------------------------"+'\n'
        fo5.append payload3+'\n';
        fo5.append "------------------------------"+'\n'
        //dts.setDebug();
        fo5.append dts.parse(payload3)+'\n';
        fo5.append "\n------------------------------\n"
        
        
def payload4 = '''= My Notes About AsciiDoctor
fred <fred.writer@asciidoctor.org>
v1.0, 2014-02-01
:toc:
:homepage: http://asciidoctor.org
:nofooter:

== Nice Tutorial
http://asciidoctor.org/docs/asciidoc-writers-guide/[Writer's Guide]

== Date / time

The current date and time: <%= request.getAttribute('datetime') %>.

My name is <%= request.getAttribute('name') %>

<% include '/WEB-INF/includes/footer.gtpl' %>
'''.toString();

        fo5.append title+'\n';
        fo5.append "------------------------------"+'\n'
	dts = new DoctorTemplateScanner();
        fo5.append payload4+'\n';
        //dts.setDebug();
        fo5.append dts.parse(payload4)+'\n';
        fo5.append "------------------------------\n"

        fo5.append  "--- the end ---"+'\n'
    } // end of main
    
} // end of class