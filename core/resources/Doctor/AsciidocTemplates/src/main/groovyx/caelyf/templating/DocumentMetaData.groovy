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

    // ----------------------------------------
    // a document metadata holder
    public class DocumentMetaData 
    {
	// true to get debug info
        boolean verbose = false;

	// true to force asciidoctorj to produce both header and footer wrappers rather than using
    	// <% include 'header.gtpl' %> etc
    	boolean includeHeaderFooter = false;

	// captures document metadata after document has been parsed
    	boolean needHeader = false;

	// tells parser to retain unknown place holders 
	boolean keepUnknownPlaceholders = true;

	// desired output format of translate render; typically html5 or docbook45 though could be others but not coded for them
    	String asciiDoctorBackendChoice = "html5";

        boolean available = false;
        String documentTitle = "";
        String authorName = "";
        String authorEmail = "";
        String revisionDate = "";
        String revisionNumber = "";
        String revisionReason = "";
        Date processedDate = new Date();
        def dateString = processedDate.format("EEE, d MMM.yyyy HH:mm:ss z");

        public String toString()
        {            
            def yn = (this.available) ? "yes" : "no" ;
            def ans = """
Document Title : ${documentTitle}
Author Email   : ${authorEmail}
Author Name    : ${authorName}
Revision Date  : ${revisionDate}
Revision Number: ${revisionNumber}
Revision Reason: ${revisionReason}
Header Avail.? : ${yn}
Rendered Date  : ${dateString}  
""" // end of toString
    
            return ans.toString();
        } // end of toString()
        
    } // end of DocumentMetadata