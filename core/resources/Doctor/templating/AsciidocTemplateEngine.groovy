package groovyx.caelyf.templating;

import groovy.lang.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import groovy.text.TemplateEngine;
import groovy.text.Template;
import groovy.lang.Binding;

import groovyx.caelyf.templating.AsciidocTemplateHelper; 
import groovyx.caelyf.templating.TemplateTranslator; 


public class AsciidocTemplateEngine extends TemplateEngine 
{
    private AsciidocTemplate template;
 
    // ===============================================
    // Class Constructors
    public AsciidocTemplateEngine() 
    {
    }

    public AsciidocTemplateEngine(boolean verbose) {
        setVerbose(verbose);
    }

    public AsciidocTemplateEngine(ClassLoader parentLoader) {
    }

    public AsciidocTemplateEngine(GroovyShell groovyShell) {
    }


     // ========================================================
    // build
    public Template createTemplate(Reader reader) throws IOException 
    {
	template = new AsciidocTemplate();
        template.parse(reader);
        println "createTemplate().parse(reader)";
        return template;
    } // end of createTemplate


    // ========================================================
    private static class AsciidocTemplate implements Template 
    {
	String script =null;
	AsciidocTemplateHelper helper = new AsciidocTemplateHelper();
 
    // =======================================================
    // Asciidoctor methods follow :
    /**
     * @param verbose true if you want the engine to display the template source file for debugging purposes
     */
     public render(payload, binding)
     {
	helper.setIncludeHeaderFooter(true);
    	helper.loadContent(payload);
	helper.loadBinding(binding);
        helper.setNeedHeader(true);
        helper.setKeepUnknownPlaceholders(false);
        return helper.getRenderedPayload();
     } // end of render

        public Writable make() 
        {
            return make([:]);
        }

        // provide a binding map of key/values for template replacement placeholders
        public Writable make(final Map map) 
        {
		Binding binding = new Binding(map);

		return new Writable() {
                	public Writer writeTo(Writer writer) 
			{
        			TemplateTranslator tt = new TemplateTranslator();
        			tt.setBinding(binding);
	 			def temp = tt.process(script);
				temp = render(temp, map);
				def reader = new StringReader(temp)
				reader.transformChar(writer) { ch ->
    					ch in ['\n', '\r'] ? ch : ch
				}
				return writer
	            } // end of writeTo

                	public String toString() 
                	{
				StringWriter sw = new StringWriter();
        	            writeTo(sw);
			    	return sw.toString();
				//return tx2;
        	      } // toString()

            } // end of return new Writable method

        } // end of public Writable make


        protected String parse(Reader reader) throws IOException {

/*
		println "parse(reader) hit ----"
		if (!(reader)) 
		{
                	reader = new BufferedReader(reader);
           	} // end of if

		StringWriter writer = new StringWriter();
		reader.transformChar(writer) { ch ->
    			ch in ['\n', '\r'] ? '|' : ch
		}
-- */
		script = reader.getText();

            return script;   //writer.toString();
        } // end of protected String parse

    } // end of private static class AsciidocTemplate



    // ----------------------------------------------
    //
    public static void main(String[] args)
    {
	System.out.println("--- Start Of Text For AsciidocTemplateEngine ---");    

	String payload = "";
	String payload1 = '''= My Notes About AsciiDoctor\n:icons: font \njnorthr <doc.writer@asciidoctor.org>\nv1.0, 2014-01-01 \n\n== Heading One\n*This* is it. My name is ${name}. Hi {title}. here is an include statement:<% include '/WEB-INF/includes/header.gtpl' %> \n\nTIP: Eat Spinach regularly. \nicon:heart[2x] \n''';

	def payload2 = '''= My Notes About AsciiDoctor
:icons: font 
jnorthr <doc.writer@asciidoctor.org>
v1.0, 2014-01-01 

== Heading One
*This* is it. My name is {name}. Hi {author}. here is an include statement:<% include '/WEB-INF/includes/header.gtpl' %> 

TIP: Eat Spinach regularly. 
icon:heart[2x] 
'''
	//------------------------------------------------------------------------------------------
	AsciidocTemplateEngine ate = new AsciidocTemplateEngine();

	println "constructed AsciidocTemplateEngine - ready to ate.createTemplate()"
	Template result = ate.createTemplate(new File("resources/sample.txt"));   

	println "render completed ok; time to make:"


	def values = ['name' : 'fred']
	Writable made = result.make(values);
    	println made.toString();
    	println "\n---------------------\nEnd of made.toString()\n--------------------------------\n"


/*  ----
	println "--------------------------------\ncreateTemplate() will run now:\n"

	Template t = ate.createTemplate(payload1);
    	println "Template t is of class:"+t.getClass()+"<---\n";

	Writable txt = t.make(values);
    	println txt.toString();
    	println "\n---------------------\nEnd of txt.toString()\n--------------------------------\n"
  
	println "writing file resources/AsciidocTemplateEngineResults.html"
    	new File("resources/AsciidocTemplateEngineResults.html").write(txt.toString());
--- */

	System.out.println("--- End Of Text For AsciidocTemplateEngine ---");    
    } // end of main


} // end of class
