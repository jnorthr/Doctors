//@Grab('org.asciidoctor:asciidoctorj:1.5.0')
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

package groovyx.text;

import groovy.transform.*;

// for asciidoctorj 
import static org.asciidoctor.Asciidoctor.Factory.create;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.Options;  
import org.asciidoctor.SafeMode;  
import org.asciidoctor.ast.DocumentHeader;

import org.apache.log4j.*
import groovy.util.logging.*  
import org.asciidoctor.ast.*

/** 
 * DoctorHelper class description
 *
 * A class to support output, typically html, from a textfile styled in the asciidoctor syntax.
 */ 
@Canonical 
@Log4j
public class DoctorHelper
{        
    /** a handle on the translation engine */ 
    Asciidoctor asciidoctor;

    /** options to govern the translation process within asciidoctor */ 
    org.asciidoctor.Options asciidoctorJOptions;

    /** attributes or data values to feed the document during the translation process within asciidoctor */ 
    org.asciidoctor.Attributes attributes;

    /** a specific boolean flag to write additional stuff to the document header and trailer */ 
    boolean includeHeaderFooter = true;

	DocumentHeader header; 
	String title =  "";
	Author author;
    String email = "";
    String fullname = "";
    
    RevisionInfo revisionInfo;
    String date = "";
    String version = "";
    String remarks = "";
    
   /** 
    * Default Constructor 
    * 
    * @return DoctorHelper object
    */     
    public DoctorHelper()
    {
        say "DoctorTemplate() default constructor called"
        setup();
    } // end of default
 
 
   /** 
    * Method to set internal variables.
    * can turn on/off the asciidoctor generation of html heading+trailer </body></html> bits
    * 
    * @param true flag to cause header/trailer components to be included in the translation
    * @return void
    */     
    public setWrapper(flag)
    {
    	log.warn "setting includeHeaderFooter to "+includeHeaderFooter 
        includeHeaderFooter = flag;
        asciidoctorJOptions.setHeaderFooter(includeHeaderFooter);
        return includeHeaderFooter;
    } // end of setWrapper

 
   /** 
    * Convenience Method to suppress generation of asciidoctor header and trailer components.
    *
    * will turn off the asciidoctor generation of html heading+trailer </body></html> bits
    * 
    * @return void
    */     
    public noWrapper()
    {
        return setWrapper(false);
    } // end of noWrapper


   /** 
    * Method to construct handles and set internal variables.
    *
    * Prepare org.asciidoctor.Attributes and org.asciidoctor.Options to make an html output by default
    * 
    * @return void
    */     
    private void setup()
    {
        asciidoctor = create();
        asciidoctorJOptions = new org.asciidoctor.Options();

        attributes = new org.asciidoctor.Attributes();
        attributes.setIgnoreUndefinedAttributes(true);
        attributes.setIcons(Attributes.FONT_ICONS);
        attributes.setBackend("html");
        asciidoctorJOptions.setAttributes(attributes);
        asciidoctorJOptions.setHeaderFooter(includeHeaderFooter);
        asciidoctorJOptions.setSafe(SafeMode.UNSAFE);
    } // end of setup
    


   /** 
    * Method to translate the original text styled in asciidoctor syntax 
    * 
    * @return writable stream of text rendered after translation
    */     
    def render(def payload) 
    {
    	def reply = "";
        try
        {
        	header = asciidoctor.readDocumentHeader(payload);
        	title = header.getDocumentTitle().getMain();
        	log.warn "header title="+title 

            author = header.getAuthor();
        	log.warn "header author="+author; 

			if (author!=null)
			{
            	email = author.getEmail();
        		log.warn "header email="+email

            	fullname = author.getFullName();
        		log.warn "header fullname="+fullname 
			}
			
            revisionInfo = header.getRevisionInfo();

			if (revisionInfo!=null)
			{
	            date = revisionInfo.getDate();
    	    	log.warn "header date="+date 

	            version = revisionInfo.getNumber();
    	    	log.warn "header version="+version 

	            remarks = revisionInfo.getRemark();
    	    	log.warn "header remarks="+remarks
			}
			
			if (payload!=null)
			{
	            reply = asciidoctor.render(payload, asciidoctorJOptions); 
	        }
	        else
	        {
	        	def msg = "DoctorHelper could not render NULL payload";
	        	log.fatal  msg;
	        	reply = msg;
	        }
        }
        
        /** Trap failures from translation & return them formatted as asciidoctor msgs, these are rendered into */
        /** the result designated by the backend, typically html */
        catch(any)
        {
        	reply = "asciidoctor render failed:"+any.message; 
        	log.fatal "DoctorHelper render failed b/c of "+any.message;
        } // end of catch
        
        log.info "DoctorHelper rendered "+reply.size()+" bytes"
        return reply;
    } // end of method
     

   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String toString()
    {
        return """includeHeaderFooter=${includeHeaderFooter}
asciidoctor=${asciidoctor.toString()}
asciidoctorJOptions=${asciidoctorJOptions}
attributes=${attributes}
"""
    }  // end of string


   /** 
    * Method to print audit log.
    * 
    * @param the text to be said
    * @return void
    */     
    public void say(String txt)
    {
        println txt;
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
        println "-----------------------------------"
        println("DoctorHelper starting");

        DoctorHelper dr = new DoctorHelper();
        println dr.toString();        
        println "-----------------------------------\n"
		
		def sample = """= DoctorHelper

== First Title

This is a sample of text.
""".toString();

		def ans = dr.render(sample);
		println "Render size="+ans.size()
		println ans.substring( ans.size() - 300 );
		
        println "-----------------------------------\n"
        println dr.toString();
        
        dr.noWrapper();
        println "-----------------------------------\n"
        println dr.toString();
		ans = dr.render(sample);
		println ans;
        println "-----------------------------------\n"
        
        dr.setWrapper(false);
        sample = """= bad news\n:frog: true\n= More News\n""".toString();
		ans = dr.render(sample);
		println ans;
        println "-----------------------------------\n"

        println "--- DoctorHelper end ---"
    } // end of main    
} // end of class