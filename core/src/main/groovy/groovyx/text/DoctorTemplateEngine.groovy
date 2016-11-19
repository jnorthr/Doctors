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
import groovyx.text.DoctorTemplate;
import groovy.text.*
import groovy.transform.*;

import groovy.util.logging.Slf4j

import org.slf4j.*

/** 
 * DoctorTemplateEngine class description
 *
 * A class to construct and managed a template instance from a textfile styled in the asciidoctor syntax.
 */ 
@Canonical
@Slf4j  
public class DoctorTemplateEngine extends TemplateEngine
{
    /** a handle to write additional log stuff */ 
	def logger = LoggerFactory.getLogger('DrTemplateEngine')

    /** a specific boolean flag to write additional log stuff */ 
    boolean debug = true;
    
    
    /** a handle to the template text styled in asciidoctor syntax */ 
    DoctorTemplate template;
    
   /** 
    * Default Constructor 
    * 
    * @return DoctorTemplateEngine object
    */     
    public DoctorTemplateEngine()
    {
        super();
        say "DoctorTemplateEngine() def constructor"
    } // end of constructor
    
    
    
   /** 
    * Method to construct a DoctorTemplate from text provided in a Reader.
    * 
    * @param reader contains text composed in the asciidoctor markup  syntax.
    * @return DoctorTemplate instance
    */         
    public DoctorTemplate createTemplate(Reader reader)
    {        
        println "DoctorTemplateEngine() createTemplate(Reader reader)"
        try{
	        template = new DoctorTemplate()
	        template.load(reader);
	    }
	    catch(Exception ex) 
	    {
	    	println "... failed to load DoctorTemplate in createTemplate(Reader reader):"+ex;
	    }
        return template;
    } // end of create


   /** 
    * Method to construct a DoctorTemplate from text provided in a File.
    * 
    * @param reader is a file that contains text composed in the asciidoctor markup  syntax.
    * @return DoctorTemplate instance
    */         
    public DoctorTemplate createTemplate(File reader)
    {        
        say "DoctorTemplateEngine() createTemplate(File reader)";
        this.template = new groovyx.text.DoctorTemplate()
        template.load(reader.text);
        return this.template;
    } // end of create


   /** 
    * Method to construct a DoctorTemplate from text provided in a File.
    * 
    * @param reader is a file that contains text composed in the asciidoctor markup  syntax.
    * @param wrapperFlag can turn on/off the asciidoctor generation of html headings+ending </body></html> bits
    * @return DoctorTemplate instance
    */         
    public DoctorTemplate createTemplate(File reader, boolean wrapperFlag)
    {        
        say "DoctorTemplateEngine() createTemplate(File reader, boolean wrapperFlag)"
        template = new DoctorTemplate()
        template.load(reader.text);
    	template.setWrapper(wrapperFlag);
        return template;
    } // end of create


   /** 
    * Method to construct a DoctorTemplate from text provided thru a Reader.
    * 
    * @param reader contains text composed in the asciidoctor markup syntax.
    * @param wrapperFlag can turn on/off the asciidoctor generation of html headings+ending </body></html> bits
    * @return DoctorTemplate instance
    */         
    public DoctorTemplate createTemplate(Reader reader, boolean wrapperFlag)
    {        
        say "DoctorTemplateEngine() createTemplate(Reader reader, boolean wrapperFlag)"

        template = new DoctorTemplate()
    	template.setWrapper(wrapperFlag);
        template.load(reader.text);

        return template;
    } // end of method



   /** 
    * Method to construct a DoctorTemplate from string text provided using text string.
    * 
    * @param reader contains text string composed in the asciidoctor markup  syntax.
    * @param wrapperFlag can turn on/off the asciidoctor generation of html headings+ending </body></html> bits
    * @return DoctorTemplate instance
    */         
    public DoctorTemplate createTemplate(String reader)
    {        
        say "DoctorTemplateEngine() createTemplate(String reader)"
        java.lang.Boolean b = true
        template = new groovyx.text.DoctorTemplate(b)
        template.load(reader);
        return template;
    } // end of method



   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String toString()
    {
        return """debug=${debug}
DoctorTemplate content=${template.toString()}
"""
    }  // end of method


   /** 
    * Method to print audit log.
    * 
    * @param the text to be said
    * @return void
    */     
    public void say(txt)
    {
        if (debug) logger.info txt;
        if (debug) println txt;
    }  // end of method
    
   // =====================================================================
   /** 
    * Method to run class tests.
    * 
    * @param args Value is string array - possibly empty - of command-line values. 
    * @return void
    */    
    public static void main(String[] args)
    {
        println("DoctorTemplateEngine starting");
        println "-----------------------------------"
        def dr = new DoctorTemplateEngine()
        def payload = dr.createTemplate("= Simple Engine Test").make().toString();
        println "payload size() =" + payload.size() + " bytes";
        println "payload=" + payload.substring( payload.size() - 189 );
        println "-----------------------------------"
        assert 30271 == payload.size()
        
        println("DoctorTemplateEngine ending");
	}
} // end of class