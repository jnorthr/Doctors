package groovy.text
import groovy.text.*

public class DoctorTemplateEngine extends TemplateEngine
{
    DoctorTemplate template;
    
    public DoctorTemplateEngine()
    {
        super();
        println "DoctorTemplateEngine() def constructor"
    } // end of constructor
    
    public Template createTemplate(Reader reader)
    {        
        println "DoctorTemplateEngine().createTemplate(Reader)"
        template = new DoctorTemplate(reader.text);
        return template;
    } // end of create
    

    // ==================================================
    public static void main(String[] args)
    {
        DoctorTemplateEngine dr = new DoctorTemplateEngine();
        def source = '''= Servlets 3.0
Community Documentation <${name}@groovy.codehaus.org>
v1.1, 2014-07-21 updated ${now}
:toc: left
:linkcss:

== Cloud Foundry Notes
Document Date:{docdate}

http://docs.gopivotal.com/pivotalcf/concepts/roles.html[Orgs, Spaces, Roles, and Permissions ]
'''
        def binding = [now: new Date(114, 11, 1), name: 'Hubert Klein Ikkink']
        def output = dr.createTemplate(source).make(binding)

        println "->"+output.toString()

        def output2 = dr.createTemplate(source).make()

        println "->"+output2.toString()

        File fi = new File('/Users/jim/Google Drive/DoctorTemplate/war/asciidocs/complexsample.adoc');
        def output3 = dr.createTemplate(fi).make(binding)

	File fo = new File('/Users/jim/Google Drive/DoctorTemplate/war/asciidocs/complexsample.html')
        fo.write(output3.toString());
        
        println "--- the end ---"
    } // end of main

} // end of class