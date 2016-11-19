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
// Nice tut here: http://eddelbuettel.net/groovy/groovy-io.html
package groovyx.text;

import groovy.lang.*;
import groovyx.text.*
import groovy.transform.*;

import groovy.text.GStringTemplateEngine 
import groovy.text.Template; 
import groovy.util.logging.Slf4j

import org.slf4j.*
/** 
 * DoctorTemplate class description
 *
 * A class to construct an output, typically html, from a textfile styled in the asciidoctor syntax.
 */ 
@Slf4j
public class DoctorTemplate implements Template
{
    /** a specific boolean flag to write additional log stuff */ 
    boolean debug = true;
    
    
    /** the original text styled in asciidoctor syntax before any internal treatments */ 
    String originalPayload = "";


    /** the revised text styled in asciidoctor syntax as any internal treatments are applied */ 
    String payload = "";


    /** a specific key/value map of things to feed or influence the asciidoctor translation  process */ 
    Map binding = [:]

    
    /** a handle on the translation engine */ 
    DoctorHelper dh = new DoctorHelper();

    /** a handle to a JSP & GString converter */ 
    DoctorTemplateScanner dts = new DoctorTemplateScanner();


    /** a handle to write additional log stuff */ 
	def logger = LoggerFactory.getLogger('DoctorTemplate')


   /** 
    * Default Constructor 
    * 
    * @return DoctorTemplate object
    */     
    public DoctorTemplate()
    {
        say "DoctorTemplate() default constructor ...."
    } // end of default
 
 
   /** 
    * Debug Constructor 
    * 
    * @return DoctorTemplate object
    */     
    public DoctorTemplate(def tf)
    {
        debug = tf;
        say "DoctorTemplate(${tf}) debug constructor called"
    } // end of non-default
 
 
   /** 
    * Asciidoctor Loader method 
    * 
    * @param string text to be tranlated to html, etc
    */     
    public load(String tx)
    {
        say "DoctorTemplate Load Method 1 = load(String) method received "+tx.size()+" bytes";
        originalPayload = tx;        
    } // end of method


   /** 
    * Asciidoctor Loader method 
    * 
    * @param InputStreamReader bytes of text to be tranlated to char.s then strung into an html payload
    */     
    public load(InputStreamReader isr)
    {
		def br = new BufferedReader(isr)
		def eol = System.getProperty("line.separator");

		StringBuilder sb = new StringBuilder();

		def l = br.readLine()
		while(l!=null)
		{
    		sb.append(l);
    		sb.append(eol);
    		l = br.readLine()
		}
		
		//br.close();
		originalPayload = sb.toString();

        say "DoctorTemplate Load Method  = load(InputStreamReader) received "+originalPayload;        
    } // end of method




   /** 
    * Asciidoctor Loader method 
    * 
    * @param StringReader text to be tranlated to html, etc
    */     
    public load(StringReader sr)
    {
		StringWriter sw = new StringWriter();
		int data = sr.read();
		while (data != -1)
		{
    		sw.write(data);
    		data = sr.read();
		} // end of while
		
		//sr.close();
		sw.close();
		
		originalPayload = sw.toString();

        say "DoctorTemplate Load Method 2 = load(StringReader) received "+originalPayload;        
    } // end of method


   /** 
    * Asciidoctor Loader method 
    * 
    * @param string text to be tranlated to html, etc
    */     
    public load(File fi)
    {
        say "DoctorTemplate Load Method 3 = load(File) received filename "+fi.name;
        originalPayload = fi.text;        
    } // end of non-default


   /** 
    * Asciidoctor Loader method 
    * 
    * @param URL encoded address of text to be tranlated to html, etc
    */     
    public load(URL url)
    { 
		// Simple Integer enhancement to make
		// 10.seconds be 10 * 1000 ms.
		Integer.metaClass.getSeconds = { ->
    		delegate * 1000
		}
 
		// Get content of URL with parameters.
		String content = url.getText(connectTimeout: 10.seconds, readTimeout: 10.seconds,
                          useCaches: true, allowUserInteraction: false,
                          requestProperties: ['User-Agent': 'Doctor Template'])
                          
        say "DoctorTemplate Load Method 4 = load(URL) received "+content.size()+" bytes";        
        originalPayload = content.toString();        
    } // end of method


   /** 
    * Asciidoctor Loader method 
    * 
    * @param URL address as a string of text to be tranlated to html, etc
    */     
    public get(String address)
    {
        say "DoctorTemplate Load Method 5 (URL get) = load(url=${address}) text"; 
        try
        {
        	URL addr = address.toURL();       
			return load( addr );
		}
		catch(java.net.MalformedURLException ex)
		{
			String msg = "failed to convert ${address} to proper URL address:"+ex;
			throw new MalformedURLException(msg);
		} // end of catch
    } // end of method
 


   /** 
    * Method to set internal variables
    *
    * Can turn on/off the asciidoctor generation of html heading+trailer </body></html> bits
    * 
    * @param true flag to cause header/trailer components to be included in the translation while false omits them
    * @return void
    */     
    public void setWrapper(flag)
    {
        dh.setWrapper(flag);
    } // end of setWrapper


   /** 
    * Convenience Method to suppress generation of header and trailer components.
    *
    * Will turn off the asciidoctor generation of html heading+trailer </body></html> bits
    * 
    * @return void
    */     
    public void noWrapper()
    {
        dh.setWrapper(false);
    } // end of noWrapper


   /** 
    * Method to translate the original text styled in asciidoctor syntax after any internal treatments
    * 
    * @return writable stream of text rendered after translation
    */     
    Writable make() 
    {
        say "DoctorTemplate Make Method 1 = make() without a binding map"
        return make(binding); 
    } // end of method
     


   /** 
    * Method to translate the original text styled in asciidoctor syntax after any internal treatments
    * 
    * @param binding map of key/values to use for JSP replacements or asciidoctor attribute declarations
    * @return writable rendered content resulting from the translation
    */     
    Writable make(Map binder) 
    {
        say "DoctorTemplate Make Method 2 = make(Map) payload="+this.originalPayload.size()+" bytes"
		binding = binder;
		
/*
        def gstring = new GStringTemplateEngine()
*/
                        
        /** do GStringTemplateEngine ${xx} replacements first in originalPayload 
        try
        {
            this.originalPayload = gstring.createTemplate(originalPayload).make(binding).toString()
            say "DoctorTemplate make(Map) gstring.createTemplate(originalPayload).make(binding) worked ok"
        } // end of try
*/

        /** Trap any failures from the translation and return them as asciidoctor msgs, these are rendered into */
        /** the result designated by the backend, typically html 
        catch(Exception any)
        {
            badNews(any);
        } // end of catch
*/

        /** Passed all prior points that might have failed, so w our text in formatted in backend result 
        try
        {
            this.payload = dh.render(this.originalPayload);
        }
*/

        /** Trap any failures from the translation and return them as asciidoctor msgs, these are rendered into */
        /** the result designated by the backend, typically html */
        /** if cannot parse out ${} items, just leave the string as original 
        catch(any)
        {
            badNews(any);
        } // end of catch
*/        
        
        Closure c = { Writer out -> 
        	def gstring = new GStringTemplateEngine();
        	try{
		        // call scanner to convert jsp file includes
        		this.payload = dts.parse(originalPayload);
        		this.payload = gstring.createTemplate(this.payload).make(binding).toString();
        	}
        	catch(Exception ex)
        	{
        		this.payload = """= Failed To Translate
:icons: font

IMPORTANT: """.toString()
				this.payload += ex.message;        		
        	} // end of catch
        	
        	out << this.payload 
        };
        
        return make(binding,c);
    } // end of method
     
     
     
   /** 
    * Method to render input stream into requested backend format
    * 
    * @param binding a map
    * @param template the logic to be run
    * @return formatted content of the render process
    */     
    Writable make(Map binder = [:], Closure logic) 
    {
    	this.binding = binder;
    	
        /** first use of logic triggers the decode !!! */
        say "DoctorTemplate Make Method 3 = make(binding, closure)"

        /** Use asWritable() to make the closure implement the Writable interface. */
        def writableTemplate = logic.asWritable()
    
        /** Assign binding map as delegate so we can access the keys of the maps as properties in the closure context. */
        writableTemplate.delegate = binding
    	this.payload = writableTemplate;
    	
        /** Return as rendered Writable. */
        this.payload = dh.render(payload);
        
        return "${this.payload}"; // GString implements Writable 
    } // end of make
    
    
   /** 
    * Method to format bad news from catch failure
    * 
    * @param catch response any
    */     
    public void badNews(any)
    {
            def now = new Date().format('dd-MM-yyyy hh.mm.ss z')
            this.payload = """= DoctorTemplate Failed to Render Template

== groovyx.text.DoctorTemplate on $now

*groovyx.text.DoctorTemplate failed to render(text, asciidoctorJOptions)*

=== Failure message is:

""" + any.message; 
            this.payload = dh.render(this.payload);    
    } // end of badNews


   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String show()
    {
        return """debug=${debug}
originalPayload=${originalPayload}
payload.size()=${payload.size()}
binding=${binding}
DoctorHelper=${dh.toString()}
"""
    }  // end of string


   /** 
    * Method to display internal variable.
    * 
    * @return formatted content of current payload variable
    */     
    public String toString()
    {
    	return payload;
    }  // end of string


   /** 
    * Method to create Writable payload.
    * 
    * @return formatted content of current payload variable
    */     
    public Writable toWritable()
    {
    	return "${payload}"; // GString uses Writable interface
    }  // end of string


   /** 
    * Method to print audit log.
    * 
    * @param the text to be said
    * @return void
    */     
    public void say(String txt)
    {
        if (debug) logger.info txt;
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
        println("DoctorTemplate dr1 starting");
        println "-----------------------------------"
        DoctorTemplate dr1 = new DoctorTemplate()
        String addr = "https://raw.githubusercontent.com/jnorthr/anynines/master/Readme.adoc"
        println "get(${addr}) URL github address of an .adoc file"
		dr1.get(addr);
        dr1.setWrapper(false);
        println "use make() without parms"
        println dr1.make().toString()
        println "-----------------------------------"

        println("DoctorTemplate dr2 starting debug boolean");
        DoctorTemplate dr2 = new DoctorTemplate(true)
        println "turn off asciidoctor header/trailer wrapper"
        dr2.setWrapper(false);
        println "load nothing"
        try{
			dr2.load();
        	println "use make([:]) with empty binding"
        	def t2 = dr2.make([:]).toString()
		}
		catch(Exception x) { println "dr2 failed as "+x; }
		
/*		
        println "load null"
		dr2.load(null);
        println "use make() with empty binding"
        println dr2.make([:]).toString()
*/

        println "load empty string"
		dr2.load("");
        println "use make() with empty binding"
        println dr2.make([:]).toString()
        println "-----------------------------------"
        
        
        // Use toString() of Writable closure.
        println("DoctorTemplate dr3 starting String");
        DoctorTemplate dr3 = new DoctorTemplate();
        println "load string"
        dr3.load("= Make() with Binding and Closure\n\nHi kids\n")
        def ss = dr3.make([:]){ Writer out -> out << "= Hello world!\n\n== Title One\n\nHi kids" } 
        println ss;
        println "-----------------------------------"
        
        
        // try StringReader
        println("DoctorTemplate dr4 starting StringReader");        
        DoctorTemplate dr4 = new DoctorTemplate();
        println "load StringReader"
        dr4.load(new StringReader("Hi kids"))
        dr4.setWrapper(false);
        println dr4.make([:]).toString()
        println "-----------------------------------"
        
        // try another String
        println("DoctorTemplate dr5 starting String");
        DoctorTemplate dr5 = new DoctorTemplate();
        dr5.load("= DoctorTemplate\n:linkcss:\n\nThis is a test.\nAlias is \${name}");
        println dr5.make([name:'jnorthr']).toString()
        println "-----------------------------------\n"


        // try another String then make without a matching binding entry
        println("DoctorTemplate dr6 starting String");
        DoctorTemplate dr6 = new DoctorTemplate();
        dr6.load("= Simple String");
        def x = dr6.make([name:'jnorthr']).toString();
        x = (x.size()>200)? x.substring(x.size()-200) : x ;
        println x;
        println "-----------------------------------\n"
        
        
        // try a binding in the constructor
        println("DoctorTemplate dr7 starting constructor with binding to turn on debug");
        def hm = [debug:true]
        DoctorTemplate dr7 = new groovyx.text.DoctorTemplate(hm)

        println "--- DoctorTemplate end ---"
    } // end of main    
} // end of class

/*
String tx = "= Hi kids\n\n== Topic One\n\nThis is some text.\n-------------------\n"
println tx;
println "tx.size()+"+tx.size()

StringReader sr = new StringReader(tx);
StringWriter sw = new StringWriter();

int data = sr.read();
while (data != -1)
{
    sw.write(data);
    data = sr.read();
} // end of while
sw.close();
println"\n----------------------\nsw=\n-----------------------\n"+sw;


tx = sw.toString();
println "\ntx=\n"+tx;
*/