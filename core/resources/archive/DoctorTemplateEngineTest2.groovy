package groovyx.text

import groovyx.text.*
//import DoctorTemplateEngine

public class DoctorTemplateEngineTest2  extends GroovyTestCase 
{
    DoctorTemplate template;
    DoctorTemplateEngine dr;
    def binding = [:]

    protected void setUp() {
        super.setUp();
		dr = new DoctorTemplateEngine();
		template = new DoctorTemplate();
		println "DoctorTemplateEngineTest2 setUp()"
    }

    void testBindingPresent() {
		println "DoctorTemplateEngineTest2 testBindingPresent()"
        assert binding != null
    }
    void testEnginePresent() {
		println "DoctorTemplateEngineTest2 testEnginePresent()"
        assert dr != null
    }

    void testTemplatePresent() {
    	println "DoctorTemplateEngineTest2 testTemplatePresent()"
        assert template != null
    }

    // ==================================================
    void testCreateTemplate()
    {
		println "\nDoctorTemplateEngineTest2 testCreateTemplate() Test 1"
        def source = """= Servlets 3.0
Community Documentation <${name}@groovy.codehaus.org>
v1.1 2014-07-21 updated 26Oct.2016
:toc: left
:linkcss:

== Cloud Foundry Notes
Document Date:{docdate}

http://docs.gopivotal.com/pivotalcf/concepts/roles.html[Orgs, Spaces, Roles, and Permissions ]
"""

        binding = [now: new Date(), name: 'JNorthr']
        dr = new DoctorTemplateEngine();

        try{
	        def output = dr.createTemplate(source).make(binding)
    	    println "->.createTemplate(source).make(binding) output w/binding:|"+output.toString()+"| source:|"+source+"|"
		}
		catch(any)
		{
			println "dr.createTemplate(source).make(binding) failed on :"+any.message;
		}
		
		println "\nDoctorTemplateEngineTest2 testCreateTemplate() Test 2"
        println "dr.createTemplate(source).make() <--- no binding but has gstring var."
        dr = new DoctorTemplateEngine();
        def output2 = dr.createTemplate(source).make()

        println "->dr.createTemplate(source).make() output2:|"+output2.toString()+"|"
    } // end of test

    void testWriteFile()
    {
		println "\nDoctorTemplateEngineTest2 testWriteFile() Test 1"
        File fi = new File('resources/Sample1.adoc');
		println("========================================");
		println("          name:" + fi.getName());
		println("  is directory:" + fi.isDirectory());
		println("        exists:" + fi.exists());
		println("          path:" + fi.getPath());
		println(" absolute file:" + fi.getAbsoluteFile());
		println(" absolute path:" + fi.getAbsolutePath());
		println("canonical file:" + fi.getCanonicalFile());
		println("canonical path:" + fi.getCanonicalPath());        
		println("========================================");
        def output3 = dr.createTemplate(fi)
        def out3 = output3.make(binding).toString()
        println "dr.createTemplate(fi).make(binding)=|"+output3+"|"
    } // end of test

    void testCreateFile()
    {
		println "\nDoctorTemplateEngineTest2 testCreateFile() Test 1"
		println new File(".").absolutePath+ " is current directory !!"
		File file = new File('./sample5.html')
        file.write("sample5.html");
		println("========================================");
		println("          name:" + file.getName());
		println("  is directory:" + file.isDirectory());
		println("        exists:" + file.exists());
		println("          path:" + file.getPath());
		println(" absolute file:" + file.getAbsoluteFile());
		println(" absolute path:" + file.getAbsolutePath());
		println("canonical file:" + file.getCanonicalFile());
		println("canonical path:" + file.getCanonicalPath());        
        println "--- the end ---"
    } // end of test

} // end of class