package groovyx.text;

import groovyx.text.*;

// To support Spock Test Framework - uncomment @Grab lines if compiling directly with groovyc and not gradle build tool
//@Grab('org.spockframework:spock-core:1.0-groovy-2.4')
import spock.lang.*

// To support the feature to copy stdout and stderr module output as a redirect back to the spock framework
//@Grab('org.springframework.boot:spring-boot:1.2.1.RELEASE')
import org.springframework.boot.test.OutputCapture

import org.asciidoctor.SafeMode;  

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

/*

The Spock framework needs some external jars to compile and run. Since we are using gradle build tool to make this all work, the build.gradle script will need:

// groovy plugin needs these declarations
repositories {
    mavenCentral()
}

dependencies {
    compile 'org.codehaus.groovy:groovy-all:2.4.7'
    compile 'org.apache.ivy:ivy:2.3.0'

    testCompile 'org.spockframework:spock-core:1.0-groovy-2.4'
    testCompile 'org.springframework.boot:spring-boot:1.2.1.RELEASE'
}

Or you can include these lines at the top of this source code when directly compiling this script using groovyc

@Grab('org.spockframework:spock-core:1.0-groovy-2.4')
import spock.lang.*

@Grab('org.springframework.boot:spring-boot-test:1.4.1.RELEASE')
import org.springframework.boot.test.OutputCapture

NOTE: If you try to compile this source using gradle and the @Grab stmts are NOT commented out, you will get an ivy failure like:

* What went wrong:
Execution failed for task ':compileTestGroovy'.
> org/apache/ivy/core/report/ResolveReport

so just // comment out the @Grab lines to compile correctly.

See: http://mrhaki.blogspot.fr/2015/02/spocklight-capture-and-assert-system.html

*/

class DoctorTemplateEngineTest extends spock.lang.Specification {
    DoctorTemplateEngine test;
    String s = "= Hi Kids\n\n== Title One\n\nNews is next.\n";
    
    @org.junit.Rule
    OutputCapture capture = new OutputCapture()

    // run before the first feature method
    def setupSpec() 
    {
    } // end of setupSpec()     

    // run before every feature method
    def setup() 
    {
        println "\nStart of testing for DoctorTemplateEngine feature"
        test = new DoctorTemplateEngine();
    }          

    // run after every feature method
    def cleanup() 
    {
        println "end of testing for DoctorTemplateEngine feature\n"
    }        

    // run after the last feature method    
    def cleanupSpec() 
    {
        println "\nend of all testing for DoctorTemplateEngine\n"
    }   
 
 
    def "Build default DoctorTemplateEngine"() {
        when:     'default DoctorTemplateEngine has been built'
        then:     test != null;
                  test.getClass() == DoctorTemplateEngine;
    } // end of feature method

 
    def "Build non-default DoctorTemplateEngine"() {
        when:     'non-default DoctorTemplateEngine has been built'
                  test = new DoctorTemplateEngine(true);
        then:     test != null;
                  test.getClass() == DoctorTemplateEngine;
                  test.debug == true;
    } // end of feature method


    def "createTemplate(Reader)"() {
        when:     'createTemplate set to get input from Reader'
                  StringReader sr = new StringReader(s);
                  DoctorTemplate dt = test.createTemplate(sr)
                  
        then:     test != null;
                  dt != null;
                  dt.getClass() == DoctorTemplate;
    } // end of feature method
 

    def "createTemplate()"() {
        when:     'createTemplate set to get no input from method call'
                  DoctorTemplate dt = test.createTemplate()
                  
        then:     thrown(Exception);
    } // end of feature method
 
 
} // end of class