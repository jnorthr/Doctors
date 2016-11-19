@Grapes([
    @Grab(group='org.slf4j', module='slf4j-api', version='1.6.1'),
    @Grab(group='ch.qos.logback', module='logback-classic', version='0.9.28'),
    @Grab(group='org.spockframework', module='spock-core', version='0.7-groovy-2.0')
])

import spock.lang.*

import java.util.logging.*;
// File: LogSlf4j.groovy
// Add dependencies for Slf4j API and Logback
import org.slf4j.*
import groovy.util.logging.Slf4j
import groovy.text.*;

@Slf4j
public class AsciidocTemplateTest extends Specification
{
    def static tasks = 0;
    def setup() { tasks+=1; println "setup() running for test ${tasks}";  }          // run before every feature method

    def cleanup() {println "cleanup running\n\n";  }        // run after every feature method

    def setupSpec() {  println "// run before the first feature method" }     // run before the first feature method
    def cleanupSpec() { println "// run after the last feature method; task count was ${tasks}\n\n" }   // run after the last feature method

    def "construct empty Asciidoc Template instance"()
    {
        given: "make an empty template instance"
            AsciidocTemplate adt;
            
        when: "create object"
            adt = new AsciidocTemplate();
            
        then: "instance should be created"
            assert adt != null, "instance was null";
            assert adt instanceof Template, "instance was not a Template";
    } // end of fixture


    def "construct Asciidoc Template instance with text"()
    {
        given: "make a template instance with text"
            AsciidocTemplate adt;
            
        when: "create object with text"
            adt = new AsciidocTemplate("hi kids");
            
        then: "instance should be created"
            assert adt != null, "template not created";
            assert adt instanceof Template, "instance was not a Template";
    } // end of fixture

    def "make AsciidocTemplate instance with text"()
    {
        given: "make a template instance with text"
            AsciidocTemplate adt = new AsciidocTemplate("hi kids");;
            Writable tem = null;
             
        when: "make a template with text"
            tem = adt.make();

        then: "instance should be created"
            assert tem != null, "Writable not created";
            assert tem instanceof Writable, "instance was not a Writable object";
    } // end of fixture

    def "make AsciidocTemplate instance with no text"()
    {
        given: "make a template instance with text"
            AsciidocTemplate adt = new AsciidocTemplate("");;
            Writable tem = null;
            
        when: "make a template with text"
            tem = adt.make();

        then: "instance should be created"
            assert tem != null, "Writable not created";
            assert tem instanceof Writable, "instance was not a Writable object";
    } // end of fixture

    def "make AsciidocTemplate instance with no text and a map"()
    {
        given: "make a template instance without but with a map"
            AsciidocTemplate adt = new AsciidocTemplate("");
            Map map = ['name':'jnorthr']
            Writable tem = null;
             
        when: "make a template with no text but a map"
            tem = adt.make(map);
            
        then: "empty template created"
            assert tem != null, "Writable not created";
            assert tem instanceof Writable, "instance was not a Writable object";
            assert tem.toString()!=" ","template should not have any text"
    } // end of fixture


    def "make AsciidocTemplate instance with one blank as text and a map"()
    {
        given: "make a template instance with one space and with a map"
            AsciidocTemplate adt = new AsciidocTemplate(" ");
            Map map = ['name':'jnorthr']
            Writable tem = null;
             
        when: "make a template with no text but a map"
            tem = adt.make(map);
            
        then: "empty template created"
            assert tem != null, "Writable not created";
            assert tem instanceof Writable, "instance was not a Writable object";
            assert tem.toString()==" ","template should have one blank space"
    } // end of fixture


    // ---------------------
    def "make AsciidocTemplate instance with some simple text and a map"()
    {
        given: "make a template instance with simple text and a map"
            AsciidocTemplate adt = new AsciidocTemplate("fred");
            Map map = ['name':'jnorthr']
            Writable tem = null;
             
        when: "make a template with text and a map"
            tem = adt.make(map);
            String ans = tem.toString(); println "<${ans}>"
            
        then: "non-empty template created"
            assert tem != null, "Writable not created";
            assert tem instanceof Writable, "instance was not a Writable object";
            assert ans=="fred","template should have one blank space"
    } // end of fixture


    // ---------------------
    def "test Template generation with text values"()
    {
        AsciidocTemplate adt;
        setup:
            Map map = ['name':'fred']
            adt = new AsciidocTemplate(value);
            Writable tem = adt.make(map);
            println "Task ${AsciidocTemplateTest.tasks} expects new AsciidocTemplate(${value})==<${result}>"

        expect:
            assert result == tem.toString();
                    
        where:
            value       |    result
            ""          |    ""
            " "         |    " "
            "fred"      |    "fred"
            " fred"     |    " fred"
            "fred "     |    "fred "
            " fred "    |    " fred "
            " hi kids "    |    " hi kids "
            " name is fred "    |    " name is fred "
    } // end of fixture


    def "test Template generation with \$binding"()
    {
        given:  "make a template with a binding variable in the text and a map"
            AsciidocTemplate adt = new AsciidocTemplate("My name is \${name}"); // escape dollar sign to get thru groovy compiler
            Map map = ['name':'jnorthr']
            Writable tem = null;
             
        when: "binding variable is passed to make method"
            tem = adt.make(map);
            String ans = tem.toString(); 
            println "Test ${AsciidocTemplateTest.tasks} found ans="+ans;

        then: "result template should contain replaced value from binding map"
            assert ans=="My name is ${map.name}","template should have one replacement value"
        
    } // end of fixture

    def "test Template generation with JSP binding"()
    {
        given:  "make a template with a binding variable in the text and a map"
            AsciidocTemplate adt = new AsciidocTemplate("My name is <%= name %>"); // escape dollar sign to get thru groovy compiler
            Map map = ['name':'jnorthr']
            Writable tem = null;
             
        when: "binding variable is passed to make method"
            tem = adt.make(map);
            String ans = tem.toString(); 
            println "Test ${AsciidocTemplateTest.tasks} found ans="+ans;

        then: "result template should contain replaced value from binding map"
            assert ans=="My name is jnorthr","template should have one replacement value"
        
    } // end of fixture


    def "test Template generation with JSP binding for a more complex name"()
    {
        given:  "make a template with a JSP binding variable in the text and a map"
            AsciidocTemplate adt = new AsciidocTemplate("My name is <%= name %>"); // escape dollar sign to get thru groovy compiler
            Map map = ['name':'Le Roy Brown']
            Writable tem = null;
             
        when: "binding variable is passed to make method"
            tem = adt.make(map);
            String ans = tem.toString(); 
            println "Test ${AsciidocTemplateTest.tasks} found ans="+ans;

        then: "result template should contain replaced value from binding map"
            assert ans=="My name is Le Roy Brown","template should have one replacement value"
        
    } // end of fixture

    // test template conversion for replacement of both JSP-style and groovy-style variables
    def "test Template generation with JSP binding plus normal groovy binding for a more complex name"()
    {
        given:  "make a template with a JSP binding variable in the text and a map"
            AsciidocTemplate adt = new AsciidocTemplate("My name is <%= name %> aka \${name}"); // escape dollar sign to get thru groovy compiler
            Map map = ['name':'Le Roy Brown']
            Writable tem = null;
             
        when: "one binding variable is passed to fill template variables"
            tem = adt.make(map);
            String ans = tem.toString(); 
            println "Test ${AsciidocTemplateTest.tasks} found ans="+ans;

        then: "result template should contain replaced value from binding map"
            assert ans=="My name is Le Roy Brown aka Le Roy Brown","template should have two replacement values, not this:<${ans}>"
        
    } // end of fixture


    // test template conversion for replacement of both JSP-style and groovy-style variables
    def "test Template generation with JSP binding plus normal groovy binding for a more complex name"()
    {
        given:  "make a template with a JSP binding variable in the text and a map with 2 entries"
            AsciidocTemplate adt = new AsciidocTemplate("My name is <%= name %> aka \${name}"); // escape dollar sign to get thru groovy compiler
            Map map = ['name':'Le Roy Brown','age':'21']
            Writable tem = null;
             
        when: "one binding variable is passed to fill template variables"
            tem = adt.make(map);
            String ans = tem.toString(); 
            println "Test ${AsciidocTemplateTest.tasks} found ans="+ans;

        then: "result template should contain replaced value from binding map"
            assert ans=="My name is Le Roy Brown aka Le Roy Brown","template should have two replacement values, not this:<${ans}>"
        
    } // end of fixture


    // test template conversion for replacement of both JSP-style and groovy-style variables
    def "test Template generation with JSP binding plus normal groovy binding for a more complex name but an empty map"()
    {
        given:  "make a template with a JSP binding variable in the text and an empty map"
            AsciidocTemplate adt = new AsciidocTemplate("My name is <%= name %> aka \${name}"); // escape dollar sign to get thru groovy compiler
            Map map = [:]
            Writable tem = null;
             
        when: "one binding variable is passed to fill template variables"
            tem = adt.make(map);
            String ans = tem.toString(); 
            println "Test ${AsciidocTemplateTest.tasks} found ans="+ans;

        then: "result template should contain replaced value from binding map"
            //thrown(MissingPropertyException)
            MissingPropertyException e = thrown();
            println "-> Expected failure cause:"+e.message // gives: -> failure cause:No such property: name for class: SimpleTemplateScript743
    } // end of fixture


} // end of class