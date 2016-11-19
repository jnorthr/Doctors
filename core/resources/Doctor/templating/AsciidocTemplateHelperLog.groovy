/*
 * Copyright 2003-2014 the original author or authors.
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
package groovyx.caelyf.templating;

import groovy.lang.*;
import java.io.File;
import java.text.SimpleDateFormat;

// for asciidoctorj 
import static org.asciidoctor.Asciidoctor.Factory.create;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.Options;  

public class AsciidocTemplateHelperLog 
{
    // declare necessaries
    Asciidoctor asciidoctor;

    //Print all the environment variables.
    def env = System.getenv()

    // options to govern translation process within asciidoctor
    org.asciidoctor.Options asciidoctorJOptions;
    org.asciidoctor.Attributes attributes;

    // desired output format of translate render; typically html5 or docbook45 though could be others but not coded for them
    String asciiDoctorBackendChoice = "html5";

    // temporary work variables
    StringBuffer payload = new StringBuffer(16384);

    String rendered = "";
    def dt = "";
    String logName = "resources/log${dt}.html"

    // class constructor
    public AsciidocTemplateHelperLog(String input, DocumentMetaData dmd)
    {
        asciidoctor = create();
        asciidoctorJOptions = new org.asciidoctor.Options();
        attributes = new org.asciidoctor.Attributes();

        dt =  new Date().format("yyyy.MM.d@kk.mm.ss"); 
	logName = "resources/log${dt}.html";

        String user= (env['USERNAME']) ? env['USERNAME'] : env['USER'];
	user = (user==null) ? "Unknown" : user;
        def processedDate = new Date();
        def dateString = processedDate.format("EEE. d MMM.yyyy hh.mm.ss a z "); 

            this.payload.append("= Asciidoctor Render Log");
            this.payload.append('\n');

            this.payload.append("${user} <${user}@googlemail.com>");
            this.payload.append('\n');

            this.payload.append("v1.0, ${dateString} : ${logName}");
            this.payload.append('\n');

            this.payload.append(" ");
            this.payload.append('\n');

            this.payload.append("== System Environment");
            this.payload.append('\n');


            this.payload.append("\nEnvironment Variables\n----\n");
            env.each{e-> 
                this.payload.append(e);
                this.payload.append('\n');            
            }

            this.payload.append('\n----\n');
            this.payload.append("== Document Metadata");
            this.payload.append('\n');

            this.payload.append("....");

  	      def lns = dmd.toString();
	      lns.eachLine{ dmdl ->          
                this.payload.append(dmdl);
                this.payload.append('\n');            
	      } // end of eachLine
            this.payload.append("....");

            this.payload.append("\n== Render Log");
            this.payload.append('\n');

            //this.payload.append("++++");

/*
  	      input.eachLine{ ln ->
	            this.payload.append(ln);
        	      this.payload.append('\n');
	      } // end of each
*/
            this.payload.append(input);
                this.payload.append('\n');            
            //this.payload.append("++++");
                this.payload.append('\n');            

            say "--- AsciidocTemplateHelperLog loaded " + input.size()+" bytes"
            def txt = render();
    } // end of constructor

    public void say(tx)
    {
       println tx;
    } // end of say
  
    // useful helper methods, wanted to return a writable but dunno how
    public Writer sendToWriter(String content)
    {
        def sw= new StringWriter();
        content.each{e->
              sw.write(e);        
      } // end of each

      return sw;
    } // end of Writer

    // send rendered document to an external file
    public writeToFile(StringWriter sw, String filename)
    {
        try 
        {
            if ( new File(filename).exists() ) { new File(filename).delete(); }
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
            out.println(sw.toString());
            out.close();
        } 
        catch (IOException e) 
        {
            def msg = "-> Failed to write file named "+filename+" beacuse "+e.message; 
            println msg;
        } // end of catch
    } // end of Writer


    // translate the log text from AsciidocTemplateHelper into the format declared by the 'backend', i.e. html,docbook
    // constructor will have placed text into 'payload' variable
    public render()
    {
        attributes.setBackend(asciiDoctorBackendChoice);
        attributes.setTableOfContents(true);
        attributes.setIcons(Attributes.IMAGE_ICONS);
        attributes.setDataUri(true);
        asciidoctorJOptions.setAttributes(attributes);
        
        // do you want extra html wrappers <html>...</html> around the result ?
        asciidoctorJOptions.setHeaderFooter(true);
        
        try
        {
            rendered = asciidoctor.render(payload.toString(), asciidoctorJOptions);
        }
        catch (Exception e)
        {
            def msg = "\n--------\nRender() failed with message:"+e.message;
            println msg;
        }
        
        say("\n--- rendered source has ${rendered.size()} bytes --");
        say(rendered);
        say("\n--- end of rendered source --");

        // get a StringWriter
        def ou = sendToWriter(rendered);

        // dump content of StringWriter to an output file
        say "send results to an output file named ${logName}"
        writeToFile(ou, logName)

        return rendered;
    } // end of setup


    // ----------------------------------------------
    //
    public static void main(String[] args)
    {
        println("--- Start Of Text For AsciidocTemplateHelperLog ---");    
        AsciidocTemplateHelperLog ate = new AsciidocTemplateHelperLog("Hi Kids");
        println("--- End Of Text For AsciidocTemplateHelperLog ---");    
    } // end of main

} // end of class