package groovyx.text;

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

import groovyx.text.*
import groovy.transform.*;

/** 
 * DoctorTester class description
 *
 * A wrapper class to construct and managed a doctor template engine instance 
 * using a set of sample textfiles and strings styled in the asciidoctor syntax.
 */ 
@Canonical 
public class DoctorTester
{
   /** 
    * Method to run class tests.
    * 
    * @param args Value is string array - possibly empty - of command-line values. 
    * @return void
    */    
    public static void main(String[] args)
    {
        // write a log of everything as fo5 output file handle
        File fo5 = new File('resources/DoctorTester.log')
        fo5.write("DoctorTemplateEngine starting\n");

        DoctorTemplateEngine dr = new DoctorTemplateEngine();
        
        def source = """= Servlets 3.0
Community Documentation <\${name}@groovy.codehaus.org>
v1.1, 2015-09-21 updated \${now}
:toc: left
:linkcss:

== Cloud Foundry Notes
Document Date:{docdate}

http://docs.gopivotal.com/pivotalcf/concepts/roles.html[Orgs, Spaces, Roles, and Permissions ]
""".toString();

    // test simple asciidoctor generation plus binding for the make() method
        def binding = [now: new Date(114, 11, 1), name: 'Hubert Klein Ikkink']
        def output = dr.createTemplate(source).make(binding);
        
        // dump engine internals
        fo5.append(dr.toString());

        File fo1 = new File('resources/DoctorTemplateEngineTest1.html')
        fo1.write(output.toString());
        fo5.append(output.toString());2

        // test no wrapper generation
        StringReader sr = new StringReader(source);
        output = dr.createTemplate(sr, false).make(binding)

        // dump engine internals
        fo5.append(dr.toString());

        File fo2 = new File('resources/DoctorTemplateEngineTest2.html')
        fo2.write(output.toString());
        fo5.append(output.toString());


    // a bigger test
        File fi = new File('resources/Sample1.adoc');
        def output3 = dr.createTemplate(fi).make(binding)

        // dump engine internals
        fo5.append(dr.toString());

        File fo = new File('resources/DoctorTemplateEngineTest3.html')
        fo.write(output3.toString());
        fo5.append(output3.toString());


    // another test with fresh Engine
    dr = new DoctorTemplateEngine();
    
    // dump engine internals
        fo5.append(dr.toString());

    // a more complex test
        File fi4 = new File('resources/DSL.adoc');
        def output4 = dr.createTemplate(fi4).make(binding)

        // dump engine internals
        fo5.append(dr.toString());
        
        File fo4 = new File('resources/DoctorTemplateEngineTest4.html')
        fo4.write(output4.toString());
        fo5.append(output4.toString());
        
        fo5.append "--- the end ---"
    } // end of main

} // end of class