package groovyx.text
import groovyx.text.*;

// To support Spock Test Framework - uncomment @Grab lines if compiling directly with groovyc and not gradle build tool
//@Grab('org.spockframework:spock-core:1.0-groovy-2.4')
import spock.lang.*

// To support the feature to copy stdout and stderr module output as a redirect back to the spock framework
//@Grab('org.springframework.boot:spring-boot:1.2.1.RELEASE')
import org.springframework.boot.test.OutputCapture


public class DoctorTemplateEngineTest3  extends spock.lang.Specification 
{
    DoctorTemplateEngine test;

	@org.junit.Rule
	OutputCapture capture = new OutputCapture()

 
//  ---------------------------------------------
//  Before the firt and after the last feature method, do these things:

	// run before the first feature method
	def setupSpec() 
	{
        def source = '''= DoctorTemplateEngineTest3 Test 1
Community Documentation <Users@groovy.codehaus.org>
V1.1, 2014-08-02, 2014-8-28, revision9
:linkcss:

== Section One

This is a sample.
'''.toString()
	} // end of setupSpec()     

	// run after the last feature method	
	def cleanupSpec() 
	{
		println "end of testing for DoctorHelper"
	}   
 
//  ---------------------------------------------
//  Before and after each feature method, do these things:

	// run before every feature method
	def setup() 
	{
		test = new DoctorTemplateEngine();
	}          

	// run after every feature method
	def cleanup() 
	{

	}        

 
//  ---------------------------------------------
//  Feature method 1, do these things:
  	def "Build default DoctorTemplateEngine - Feature 1"() {
  		when:     'default DoctorTemplateEngine has been built'
		then:     test != null;
				  test.getClass() == DoctorTemplateEngine;
  	} // end of feature method

//  ---------------------------------------------
//  Feature method 2, do these things:
  	def "DoctorTemplateEngine.createTemplate(Reader) - Feature 2"() {
  		when:    'DoctorTemplateEngine.createTemplate(Reader)'
  				  Reader r = new StringReader("= Hi kids\n")
  				  def t;
  				  t = test.createTemplate(r);
  				  
		then:     t != null;
				  t.getClass() == DoctorTemplate;
  	} // end of feature method

//  ---------------------------------------------
//  Feature method 3, do these things:
  	def "Build default DoctorTemplateEngine - Feature 3"() {
  		when:     'default DoctorTemplateEngine has been built'
		then:     test != null;
				  test.getClass() == DoctorTemplateEngine;
  	} // end of feature method


} // end of class