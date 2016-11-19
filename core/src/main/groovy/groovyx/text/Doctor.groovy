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

import groovyx.text.*
import groovy.transform.*;

/** 
 * Doctor class description
 *
 * A wrapper class to construct and managed a sample doctor template engine instance 
 * using a textfile styled in the asciidoctor syntax. It produces a print of the conversion
 */ 
@Canonical 
public class Doctor
{
   /** 
    * Method to run class tests.
    * 
    * @param args Value is string array - possibly empty - of command-line values. 
    * @return void
    */    
    public static void main(String[] args)
    {
        println "Doctor starting";
        String source = "= Doctor\n\n== Missing Filename\n\nThe first command line parameter needs to be the name of a text file that exists.\n"
        
        if (args.size() < 1)
        {
            println "Please provide the name of a file full of text composed using the asciidoctor syntax"
            DoctorTemplateEngine dr = new DoctorTemplateEngine();
            def output = dr.createTemplate(source).make();
			println output.toString();
        }
        else
        {
            String fn = args[0].trim();
            int i = fn.lastIndexOf('.');
        	
            File fi = new File(fn);
            try{
                source = fi.text;
                DoctorTemplateEngine dr = new DoctorTemplateEngine();
                def output = dr.createTemplate(source).make();
				if (i<1)
			    {
			    	// report the bad news composed from the 'source' string
					println output.toString();
				}			
				else				
				{
		        	String fno = fn.substring(0,i)+".html"
					println "writing output to :"+fno;
                	File fo1 = new File(fno)
                	fo1.write(output.toString());
                } // end of else
                
            } // end of try
            catch(Exception x)
            {
			    // report the bad news composed from the 'source' string that says we cannot find the input file
                println "Please provide the name of a file that exists as i cannot find a file named [${args[0]}] or "+x.message;
            } // end of catch
            
        } // end of else
        
        println "Doctor ending";
    } // end of main

} // end of class