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
 
// if jsp tag is found and identifies a target file that actually exists, the <% include 'xxx.adoc' %> syntax is removed
// and replaced by the external file content

package groovyx.text;

/**
 * A parser to pre-parse asciidoctor payloads for signs of jsp-style include statements;
 * <p>
 * This builder supports the usual builder syntax made of nested method calls and closures,
 * but also some specific aspects of JSON data structures, such as list of values, etc.
 * Please make sure to have a look at the various methods provided by this builder
 * to be able to learn about the various possibilities of usage.
 * <p>
 * Example:
 * <pre><code>
 *        def payload = '''= Sample Asciidoctor Include
 *
 * <% include 'Sample1.adoc' %>'''
 *        DoctorTemplateScanner dts = new DoctorTemplateScanner();
 *        def results = dts.parse(payload);
 *
 *       // creates a string result from original payload including valid external text 
 *       assert results instanceof String
 *
 * </code></pre>
 * groovyx.text.DoctorTemplateScanner.groovy
 *
 * @author Jim Northrop
 * @since 2.3.0
 */
public class DoctorTemplateScanner
{
    /**
     * Processing continues while OK is true
     */
    def ok = true;
    
    /**
     * Write audit trail while debug is true
     */
    def debug = false;
    
    /**
     * Internal debug counter for debug msgs
     */
    def counter = 0;

    /**
     * Output variable holding original payload plus any included text from external files
     */
    def result = ""

    /**
     * Input variable holding stream to be worked on, set when parse() method begins
     */
    def payload = "";

    /**
     * Temporary store for text from any valid, existing 'included' external file
     */
    def includedText = "";
    

  /** 
    * Method to print log / debug messages 
    * 
    * @param text string to be printed if debug flag is tru
    * @return void
    */     
    public void say(def tx)
    {
        if (debug) println "DoctorTemplateScanner:"+tx;
    } // end of say

    
  /** 
    * Method to put state back to start
    * 
    * @return void
    */     
    public reset()
    {
        ok = true;
        counter = 0;
        result = ""
        payload = "";
        includedText = "";    
    } // end of method
    

  /** 
    * Method to set log debug flag true
    * 
    * @return void
    */     
    public setDebug()
    {
        debug = true;
    } // end of method
    
    
    
  /** 
    * Method to find first index for first <% in given input string   
    * 
    * @param text string to be examined for <%
    * @return int index of first location where the <% symbol exists in the given string or -1 if none found
    */     
    public int findFirst(text)
    {
        return text.indexOf("<%");
    } // end of method


  /** 
    * Method to find ending index for %> in given input string   
    * 
    * @param text string to be examined for %>  after initial <%
    * @return int index of location where the %> symbol exists in the given string or -1
    */     
    public int findNext(text)
    {
        return text.indexOf("%>");
    } // end of method



  /** 
    * Method to return first part of a given input string   
    * 
    * @param text string to be unstrung
    * @return piece of input string from first char. up to given index 
    */     
    public getFirst(ix, text)
    {
        return text.substring(0, ix);
    } // end of method


  /** 
    * Method to return remining part of a given input string   
    * 
    * @param text string to unstrung
    * @return piece of input string remaining after first piece starting before given index into char.string 
    */     
    public getAll(ix, text)
    {
        return text.substring(ix);
    } // end of method



  /** 
    * Method to see if a file by this name is present, if so get text from it
    * but could poss. get non-char files like .jpg, .mp3
    *
    * @param text string of name of a file to read
    * @return boolean true when the given file name actually exists or false if not
    */     
    public boolean fileExists(fn)
    {
	    say "fileExists(${fn}) ?"
        def fi = new File(fn);         
        if (!fi.exists()) 
    	{
        	say "fileExists(${fn})? No."
        	return false;
    	} // end of if

        try
    	{
            includedText = fi.text;
        	say "fileExists(${fn})? Yes."
            say "  -----> acquired ${includedText.size()} bytes from file ${fn}"
            return true;
        }
        catch(any)
        {
            say "failed to find file ${fn}:"+any.message;
            return false;
        } // end of catch

    } // end of method



  /** 
    * Method to identify name of file to be included, if it exists
    * 
    * see if a file by this name is present, if so return true, for example:
    *
    * decodeFilename( 'Sample1.adoc' )
    * ----- q=0 r=-1 s=0 fntl=|'Sample1.adoc'|
    * ----- qp=13 fntl=|Sample1.adoc|
    *
    * @param text string of name of a file to read
    * @return boolean true when the given file name actually exists or false if not
    */     
    public boolean decodeFilename(fn)
    {
        say "* decodeFilename(${fn})"

        /** trim lead&trail blanks */
        def fnt = fn.trim();
        
        /** ignore anything less than 3 chars */
        if (fnt.size() < 3) return false;
        
        /** locate either single ' or double " */
        int q = fnt.indexOf("'");
        int r = fnt.indexOf('"');

        /** set true if quote mark found */
        boolean qf = (q > -1)? true:false;
        boolean rf = (r > -1)? true:false;

        /** use single pointer */
        int s = (q>-1)?q:r;
        
        /** if either ' or " then keep tag bit else blank */
        def fntl = (s>-1) ? fnt.substring(s):"";
        
        say "  ----- q=$q r=$r s=$s fntl=|${fntl}|"
        /**    ----- q=0  r=-1 s=0  fntl=|'Sample1.adoc'|   */
    
        /** first single quote ' was found so find if it's pair */
        if (qf)
        {
            int qp = fnt.lastIndexOf("'");
            say "  ----- qp=$qp"
            if (qp > s+1) fntl = fnt.substring(s+1,qp).trim();

            say "  ----- qp=$qp fntl=|${fntl}|"
            /**    ----- qp=13 fntl=|Sample1.adoc|  */

            return fileExists(fntl);
        } // end of if
        
    
        /** first double quote " was found, find if it's pair */
        if (rf)
        {
            int rp = fnt.lastIndexOf('"');
            
            /** found " pair so get text between quotes */
            if (rp > s+1) { fntl = fnt.substring(s+1,rp).trim(); }
            say "  ----- rp=$rp fntl=|${fntl}|";
            
            /** go see if file exists, return true if so else false if not */
            return fileExists(fntl);
        } // end of if


        /** if no quotes found just take any text as a filename like */
        /** <%=  include Sample1.adoc %> */
        if (!qf && !rf)
        {
            fntl = fnt.substring(s+1)
            say "  ----- !qf && !rf fntl=|${fntl}|";
            
            return fileExists(fntl);
        } // end of if
        
        /** report failure to find */
        return false;
    } // end of method



  /** 
    * Method to see if possible <% %> tag is a valid  'include'
    * 
    * @param text string with potential 'include' tag of name of a file to read
    * @return boolean true when the given file name is a file that actually exists or false if not
    */     
    public boolean validInclude(text)
    {
        int ix = text.toLowerCase().indexOf('include');
        boolean flag = ix > -1;
        
        /** if lowercase input had keyword INCLUDE */
        if (flag)
        {
            counter += 1;
            def tag = text.substring(ix+7);        
            int jx = tag.indexOf('%')+7+ix;
            tag = text.substring(ix+7,jx);
            
            say "---> Loop #${counter}: tag |"+text+"| has an include at ${ix}\ntag=|${tag}| and jx=${jx}"
            /**  ---> Loop #21:         tag |<%= Hi kids <% include 'Sample1.adoc' %>| has an include at 15  */
            /**                         tag=| 'Sample1.adoc' | and jx=38  */
            
            /** return flag=true if tag was valid file */
            flag = decodeFilename(tag);
        } // end of if

        return flag;
    } // end of method
    
    
  /** 
    * Principle method to examine this text stream for any include stmts
    * 
    * @param text string with potential tags
    * @return string of all text plus text from included external files when the given file name actually exists or just filename only if not
    */     
    public parse(txt)
    {
        say "parse(txt)"
        reset();
        payload = txt;
        int ix = 0;
        
        
        // while true
        while(ok)
        {
            /** locate <% symbol if any */
            ix = findFirst(payload);
            
            /** no <% in stream so make result same as input */
            if (ix < 0)
            {
                result += payload;
                ok = false;
            
                say  "Bailout 1 - ix:${ix} and payload=|"+payload+"|\nresult=|"+result+"\n"            
                /**   Bailout 1 - ix:-1 and payload=||        */
                /**   result=|= Sample Asciidoctor Include    */
                /**   == Sample1.adoc Nice Tutorial            */
            } // end of if
    
    
            // found <% so process
            else
            {
                /** add 1st part of input up to <%      */
                result += getFirst(ix, payload)
                
                /** payload gets eveything after that */
                payload = getAll(ix, payload);
                
                /** find %>  and jx points to it */
                int jx = findNext(payload);
            
                say "Update 2 - jx:${jx}\nand result =|"+result+"|\nand payload=\n|"+payload+"|\n"
                /**  Update 2 - jx:26                                */
                /**                       and result =|= Sample Asciidoctor Include        */
                /**                                   |                        */
                /**  and payload=                                */
                /**  |<% include 'Sample1.adoc' %>|                        */

                /** stream had <% but no %> after that                        */
                if (jx < 0)
                {
                    say "\nBailout 3 - jx:${jx} and payload=|"+payload+"|\n"
                    result += payload;
                    ok = false;
                }
                
                else
                {
                    jx += 2;
                    def tag = getFirst(jx, payload);
                
                    say "Running 4 - \ntag=|"+tag+"|"
                    /**   Running 4 -                         */
                    /**  tag=|<% include 'Sample1.adoc' %>|    */

                    /** if its not a real file include just add that back into output stream    */
                    if (!validInclude(tag)) 
                    {
                        result+=tag;
                    } 
                    
                    /** Ho! was a valid file so put it's text into output stream !    */
                    else 
                    {
                        result+=includedText;
                    } // end of else
            
            
                    /** update payload with what has not been scanned    */
                    payload = getAll(jx, payload);
                
                    say "Running 5 - start reading payload at jx:${jx} and this gave payload=\n|"+payload+"|\n"
                    /**  Running 5 - start reading payload at jx:28 and this gave payload=||        */
                } // end of else
                
            } // end of else found <%    
        } // end of while
        
        return result;
    } // end of method
    
} // end of class