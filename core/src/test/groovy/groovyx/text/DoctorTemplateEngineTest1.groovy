package groovyx.text

import groovy.text.*
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DoctorTemplateEngineTest1  extends GroovyTestCase 
{
    DoctorTemplateEngine dr;
    def binding = [:]

    @Before
    public void setUp() {
        super.setUp();
        this.println("@Before setUp");
        dr = new DoctorTemplateEngine();
        println "DoctorTemplateEngineTest1 setUp()"
    } // end of before

    @After
      public void tearDown() throws IOException {
        this.println("@After tearDown");
      }

    @Test
    void testBindingPresent() {
        println "DoctorTemplateEngineTest1 testBindingPresent()"
        assert binding != null
    }


    @Test
    void testEnginePresent() {
        println "DoctorTemplateEngineTest1 testEnginePresent()"
        assert dr != null
        assert (dr instanceof TemplateEngine)
    }    


    // ==================================================
    @Test
    void testCreateTemplate1()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 1"
        String source = '''= testCreateTemplate() Test 1
Community Documentation <Fred@groovy.codehaus.org>
v1.1 2016-07-21, updated 17 Aug. 2016
:linkcss:

== Sample 1 Notes

This is a simple sample.
'''.toString()

        dr = new DoctorTemplateEngine();
        assert source instanceof String 
        def output = dr.createTemplate(source).make()

        println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
    } // end of test class


    // ==================================================
    @Test
    void testCreateTemplate2()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 2"
        def source = '''= testCreateTemplate() Test 2
Community Documentation <Fred@groovy.codehaus.org>
v1.1, 2016-07-21, updated 17 Aug. 2016
:linkcss:

== Sample 2 Notes

This is a simple sample a test 1 but uses a binding even though not needed.
'''.toString()
        dr = new DoctorTemplateEngine();
        def output = dr.createTemplate(source).make(binding)
        println "\n\n->.createTemplate(source).make(binding) output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"

    } // end of test class



    // ==================================================
    @Test
    void testCreateTemplate3()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 3"
        def source = '''= testCreateTemplate() Test 3
'''.toString()
        dr = new DoctorTemplateEngine();
        def output = dr.createTemplate(source).make()
        println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
    } // end of test class


    // ==================================================
    @Test
    void testCreateTemplate4()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 4"
        def source = '''= testCreateTemplate() Test 4
Community Documentation 
'''.toString()
        dr = new DoctorTemplateEngine();
        def output = dr.createTemplate(source).make()
        println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
    } // end of test class

    // ==================================================
    @Test
    void testCreateTemplate5()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 5"
        def source = '''= testCreateTemplate() Test 5
Community Documentation <Users@groovy.codehaus.org>
'''.toString()
        dr = new DoctorTemplateEngine();
        def output = dr.createTemplate(source).make()
        println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
    } // end of test class

    // ==================================================
    @Test
    void testCreateTemplate6()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 6"
        def source = '''= testCreateTemplate() Test 6
Community Documentation <Users@groovy.codehaus.org>
V1.1,2016-08-17
'''.toString()
        dr = new DoctorTemplateEngine();
        def output = dr.createTemplate(source).make()
        println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
    } // end of test class

    // ==================================================
    @Test
    void testCreateTemplate7()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 7"
        def source = '''= testCreateTemplate() Test 7
Community Documentation <Users@groovy.codehaus.org>
V1.1, 2016-08-01  2016-08-22
'''.toString()
        dr = new DoctorTemplateEngine();
        def output = dr.createTemplate(source).make()
        println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
    } // end of test class

    // ==================================================
    @Test
    void testCreateTemplate8()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 8"
        def source = '''= testCreateTemplate() Test 8
Community Documentation <Users@groovy.codehaus.org>
V1.1, Dated 2016-08-17, 2016-08-17
'''.toString()
        dr = new DoctorTemplateEngine();
        def output = dr.createTemplate(source).make()
        println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
    } // end of test class

    // ==================================================
    @Test
    void testCreateTemplate9()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 9"
        def source = '''= testCreateTemplate() Test 9 Out-Of-Mem Test
Community Documentation <Users@groovy.codehaus.org>
V1.1, Dated 2016-08-17, 2016-08-17
'''.toString()
        dr = new DoctorTemplateEngine();
        def output = dr.createTemplate(source).make()
        println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
    } // end of test class

/* causes out-of-memory */
    // ==================================================
    @Test
    void testCreateTemplate10()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 10"
        def source = '''= testCreateTemplate() Test 10
Community Documentation <Users@groovy.codehaus.org>
V1.1, 2016-08-02, 2016-8-28, revision9
:linkcss:

'''.toString()
        dr = new DoctorTemplateEngine();
        def output = dr.createTemplate(source).make()
        println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
    } // end of test class


    // ==================================================
    @Test
    void testCreateTemplate11()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 11"
        def source = '''testCreateTemplate() Test 11 - no title ,author or text

'''.toString()
        dr = new DoctorTemplateEngine();
        println "... after create new object"
        def output = dr.createTemplate(source).make()
        println "... after createTemplate make()"
        println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
        println "... end of test 11"
    } // end of test class


    // ==================================================
    @Test
    void testCreateTemplate12()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 12"
        def source = '''= testCreateTemplate() Test 12
Community Documentation <Users@groovy.codehaus.org>
Version 1.1, 2016-08-02: Last Updated 2016-8-28, revision12
:linkcss:

'''.toString()
        dr = new DoctorTemplateEngine();
        def output = dr.createTemplate(source).make()
        println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
    } // end of test class

    // ==================================================
    @Test
    void testCreateTemplate13()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 13"
        def source = '''= testCreateTemplate() Test 13
Community Documentation <Users@groovy.codehaus.org>
Version 1.1, 2016-08-02: Last Updated 2016-8-28, revision12
:linkcss:

Hi {author}, said Bill.'''.toString()
        dr = new DoctorTemplateEngine();
        def output = dr.createTemplate(source).make()
        println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
    } // end of test class

    // ==================================================
    @Test
    void testCreateTemplate14()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 14"
        def source = '''= testCreateTemplate() Test 14 - template with missing groovy var but same name as asciidoctor uses leaves the $,
Community Documentation <Users@groovy.codehaus.org>
Version 1.1, 2016-08-02: Last Updated 2016-8-28, revision12
:linkcss:

Hi ${author}, said Bill.
'''.toString()
        dr = new DoctorTemplateEngine();
        def output = dr.createTemplate(source).make()
        println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
    } // end of test class

    // ==================================================
    @Test
    void testCreateTemplate15()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 15"
        def source = '''= testCreateTemplate() Test 15 - template with missing groovy var not same name as asciidoctor uses,
Community Documentation <Users@groovy.codehaus.org>
Version 1.1, 2016-08-02: Last Updated 2016-8-28, revision12
:linkcss:

Hi ${nickname}, said Bill.
'''.toString()
        dr = new DoctorTemplateEngine();
        def output = dr.createTemplate(source).make()
        println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
    } // end of test class



    // ==================================================
    @Test
    void testCreateTemplate16()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 16"
        def source = '''= testCreateTemplate() Test 16 - template with jsp include,
Community Documentation <Users@groovy.codehaus.org>
Version 1.1, 2016-08-02: Last Updated 2016-8-28, revision12
:linkcss:

Hi <%= name %>, said Bill.
'''.toString()
        dr = new DoctorTemplateEngine();
        def output = dr.createTemplate(source).make()
        println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
    } // end of test class

    // ==================================================
    @Test
    void testCreateTemplate17()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 17"
        def source = '''= testCreateTemplate() Test 17 - template with jsp include,
Community Documentation <Users@groovy.codehaus.org>
Version 1.1, 2016-08-02: Last Updated 2016-8-28, revision17
:linkcss:

Hi <%= name %>, said Bill.
'''.toString()
        def binding = [name: 'Jnorthr']
        def output = dr.createTemplate(source).make(binding)
        println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
    } // end of test class

    // ==================================================
    @Test
    void testCreateTemplate18()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 18"
        def source = '''= testCreateTemplate() Test 18 - template with jsp missing include,
Community Documentation <Users@groovy.codehaus.org>
Version 1.1, 2016-08-02: Last Updated 2016-8-28, revision18
:linkcss:

Here we show you an example: <% include "sample.adoc" %>, said Bill.
'''.toString()
        def output = dr.createTemplate(source).make()
        println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
    } // end of test class

    // ==================================================
    @Test
    void testCreateTemplate19()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 19"
        def source = '''= testCreateTemplate() Test 19 - template with jsp included file exists,
Community Documentation <Users@groovy.codehaus.org>
Version 1.1, 2016-08-02: Last Updated 2016-8-28, revision19
:linkcss:

Here we show you an example: <% include file="./war/sample6.adoc" %>, said Bill.
'''.toString()
        def output = dr.createTemplate(source).make()
        println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
    } // end of test class


    // ==================================================
    @Test
    void testCreateTemplate20()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 20"
        def source = '''= testCreateTemplate() Test 20 - template with asciidoctor include file exists

Here we show you an example thats to be included: 

//include::/media/jnorthr/LINKS/asciidoctor-project/war/asciidocs/sample6.adoc[] 

said Bill.

'''.toString()
        //DoctorTemplateEngine dr = new DoctorTemplateEngine();
        try{
            def output = dr.createTemplate(source).make()
            println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
        }
        catch(any)
        {
            println "DoctorTemplateEngineTest1 testCreateTemplate() Test 20 failed with message:"+any.message;
        }
    } // end of test class


    // ==================================================
    @Test
    void testCreateTemplate21()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 21"
        String source = '''= testCreateTemplate() Test 21 - template with asciidoctor include file exists,
Community Documentation <Users@groovy.codehaus.org>
Version 1.1, 2016-08-02: Last Updated 2016-8-28, revision21
:linkcss:

Here we show you an example thats to be included from war/sample6.adoc: 


include::war/sample6.adoc[] 



said Bill.
'''.toString();

        DoctorTemplateEngine dr21 = new DoctorTemplateEngine();
        def output = dr21.createTemplate(source).make()
        println "\n\n->.createTemplate(source).make() output w/o binding:"
        println "|" + output.toString() + "|"
        println "\nsource:\n|" + source + "|\n"
    } // end of test class


    // ==================================================
    @Test
    void testCreateTemplate22()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 22"
        def source = '''= Servlets 3.0
Community Documentation <${name}@groovy.codehaus.org>
version 1.1, Written 2016-07-21 :  Rev.V1.1
:toc: left
:linkcss:

== Cloud Foundry Notes
Document Date:{docdate}

http://docs.gopivotal.com/pivotalcf/concepts/roles.html[Orgs, Spaces, Roles, and Permissions ]
'''.toString()
        DoctorTemplateEngine dr = new DoctorTemplateEngine();
        def binding = [now: new Date(), name: 'Jnorthr']
        def output = dr.createTemplate(source).make(binding)
        println "\n\n->.createTemplate(source).make(binding) output w/binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"

    } // end of test class


    // ==================================================
    @Test
    void testCreateTemplate23()
    {
        println "\n\nDoctorTemplateEngineTest1 testCreateTemplate() Test 23"
        def source = '''include::resources/Sample1.adoc[] 

== testCreateTemplate() Test 23 - include asciidoctor file exists first,
Community Documentation <Users@groovy.codehaus.org>
Version 1.1, 2016-08-02: Last Updated 2016-8-28, revision21

Here we show you an example thats to be included from war/sample6.adoc: 


include::resources/Sample2.adoc[] 



said Bill.
'''.toString();
        DoctorTemplateEngine dr = new DoctorTemplateEngine();
        def output = dr.createTemplate(source).make()
        println "\n\n->.createTemplate(source).make() output w/o binding:\n|"+output.toString()+"|\n\nsource:\n|"+source+"|\n"
    } // end of test class

} // end of class