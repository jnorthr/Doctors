//@GrabResolver('https://oss.sonatype.org/content/groups/public')
//@Grab(group='org.asciidoctor', module='asciidoctor-java-integration', version='0.1.4')   

package groovyx.caelyf.templating;
import java.io.*;
import java.util.*;
import groovy.text.*;
import groovy.lang.*;
import groovy.lang.Writable;
import static org.asciidoctor.Asciidoctor.Factory.create;
import org.asciidoctor.Asciidoctor;

/**
 * TemplateTranslator is used to pre-process text streams containing text composed in the asciidoctor format.
 * The pre-processing handles the groovy GString replacement syntax ${..} and JSP style tags to include external text
 * fragments. 
 *
 * Only the JSP include syntax <% include 'something.txt' %> is supported. Other JSP tags are sent thru the asciidoctorj engine
 * for rendering or post-processing by the groovy GStringTemplateEngine().
 *
 * @author      jnorthr
 * @version     0.1
 * @since       2014-02-10  
 */
public class TemplateTranslator 
{
    /**
     * true to issue further debug audit trails
     */
    boolean verbose = true;

    /**
     * Binding map of replacement variables for this template. 
     * These would be external business type key/val pairs, 
     * not asciidoc options or attributes. No declared keys by default unless setBinding() is called
     */
     private binding = [:]

    /**
     * Text within the current <%..%> or ${...} or {...} working tag.
     */
     StringBuffer tag = new StringBuffer(16384);


/**
 * A convenience method to produce further debug info when verbose flag is true
 *
 * @param  tx - value to write to sysout
 */
     def say(tx) {if (verbose) println tx;}
     // sysout a fragment of text  not a complete line
     def say(tx,flag) {if (verbose && flag) print tx;}

/**
 * One full complete tag such as JSP includes and groovy gstrings is processed here.
 *
 * @param  val -  string of full tag content: ${name}  {author}  <% include 'something.txt' %>
 *
 * @return boolean - false asks the calling method to use the current content of the 'tag' and insert it into the output stream 
 *                while true asks calling method to consume this tag
 */
    void setBinding(bindmap)
    {
        this.binding = bindmap;
    } // end of setBinding

/**
 * One full complete tag such as JSP includes and groovy gstrings is processed here.
 * <p>
 * Processes three kinds of text replacement variables
 * here after the trailing delimiter is found for known tags.
 *
 * <p>
 * A global binding map is used to obtain a value for a given key. The key is found within the ${...} and {...} tags. Other JSP
 * tags like <%= author.name %> are NOT processed here and are returned to the output stream.
 *
 * @param  mode - int value identifies the condition causing this tag to be collected
 * @param  val -  string of full tag content: ${name}  {author}  <% include 'something.txt' %>
 *
 * @return boolean - false asks the calling method to use the current content of the 'tag' and insert it into the output stream 
 *                while true asks calling method to consume this tag
 */
    boolean decodeTag(int mode, StringBuffer val)
    {
        // silently consume the tag
        def ans = false;
        
        say "\n|${val}| mode:"+mode;
        // find index of right-most delimiter either > or }        
        int right = val.indexOf('}');   
        // didn't find trailing } so try trailing >
        if (right < 0) {right = val.indexOf('>');}
        
        // no } or > found, bail out
        if (right < 0) return false;
        
        // handle key depending on which tag format
        String ky = "";
        switch(mode)
        {
            // asciidoctor variable: {...}
            // pull key from tag stream
            case 6 :    ky = val.substring(1,right).trim();
            
                        // is key in map ?
                        def ok = (binding[ky])?true:false;
                        say "[${ky}]=[${binding[ky]}] and ok:${ok}";
                        
                        // yes, key in map, so replace this tag with map value
                        if (ok) 
                        {
                            tag.setLength(0);
                            tag.append(binding[ky]);
                        } // end of if

                        // tell calling method to re-insert 'tag' content into output stream
                        ans = true;
                        break;
                        
            // groovy GString: ${...}
            // pull key from tag stream
            case 7 :    ky = val.substring(2,right).trim();

                        // is key in map ?
                        def ok2 = (binding[ky])?true:false;
                        say "[${ky}]=[${binding[ky]}] and ok2:${ok2}";
                        
                        // yes, key in map, so replace this tag with map value
                        if (ok2) 
                        {
                            tag.setLength(0);
                            tag.append(binding[ky]);
                        } // end of if
                        
                        // tell calling method to re-insert 'tag' content into output stream
                        ans = true;
                        break;
                        
             // <% include 'fred.txt' %> handled here          
            // pull key from tag stream
             case 56 :
             case 72 :  ky = val.substring(2,right-1);

                        // get pointer to 'include' keyword
                        int left = ky.toLowerCase().indexOf('include');
                        say "[${ky}] left=${left}"

                        // was not an 'include' JSP tag, so just re-insert tag into output stream
                        if (left < 0) return false;

                        // extract key from tag
                        ky = val.substring(left+9,right-1).trim();
                        say "[${ky}] left+9=${left+9}"

                        // unstring using single quote mark
                        if (ky[0]=="'")
                        {
                            right = ky.lastIndexOf("'")
                            if (right < 0) return false;
                            ky = ky.substring(1,right)
                            try{
                                ans = new File(ky).exists();
                            }
                            catch(IOException iox)
                            {
                                say "IOException trying to read file $ky : "+iox.message
                                ans = false;
                            } // end of catch
                            
                            say "[${ky}] exists?"+ans
                            if (ans)
                            {
                                tag.setLength(0);
                                tag.append(new File(ky).text);
                                say "[${ky}] text size ?"+tag.size();
                                ans = true;
                            } // end of if
                        } // end of if

                        // unstring using double quote mark
                        if (ky[0]=="\"")
                        {
                            right = ky.lastIndexOf("\"")
                            if (right < 0) return false;
                            
                            ky = ky.substring(1,right);
                            try{
                                ans = new File(ky).exists();
                            }
                            catch(IOException iox)
                            {
                                say "IOException trying to read file $ky : "+iox.message
                                ans = false;
                            } // end of catch
                            
                            say "[${ky}] exists?"+ans
                            if (ans)
                            {
                                tag.setLength(0);
                                tag.append(new File(ky).text);
                                ans = true;
                            } // end of if
                        } // end of if
                        break;
                                                
             // if no other use was found for the tag, allow it to flow back out to the output string buffer - i.e. false
             default:   ans = true;
             
        } // end of switch
        
        return ans;
    } // end of decodeTag
    
    
/**
 * Parse template text stream finding known tag formats.
 * <p>
 * Identify three tag formats that we can handle here.
 * <ul>
 * <li> JSP style 'include' tags like: <% include 'something.txt' %>
 * <li> Groovy GString replacements like: ${keyname}
 * <li> Asciidoctor replacement syntax where key is in the binding like: {keyname}
 * </ul>
 *  
 * <p>
 * call decodeTag() for each tag. That method will include external text fragments from other sources or replace template  
 * variables with values if/when the key is known. Unknown tag formats and any unknown keys not in binding map are retained
 * are re-written to the output stream
 *
 * @param  payload - a string of asciidoc formatted text.
 * @return sb - the parsed template ready for the asciidoctorj engine to reformat it. External text fragments that exists are inserted
 *        where appropriate. Known keys that are in the global binding map have their value used in place of the ${...} and {...} components.
 */    
    def process(String payload)
    {
        // setup and clear method components
        StringBuffer sb = new StringBuffer(16384);
        int mode = 0;
        boolean flag = false;
        StringBuffer tag = new StringBuffer(16384);
                
        // examine each character in the payload
        payload.each{ ch ->       
            switch(ch)
            {
                // start of known tag format, dump any existing tag into the sb output stream
                case '$' :  if (mode>0) {mode=0;sb.append(tag);tag.setLength(0);}
                            mode +=1;
                            tag.append(ch);
                            break;                                 
                
                case '{' :  mode +=2;
                            tag.append(ch);
                            break;                                 

                case '}' :  mode +=4;
                            tag.append(ch);
                            break;                                 

                // start of known tag format, dump any existing tag into the sb output stream
                case '<' :  if (mode>0) {mode=0;sb.append(tag);tag.setLength(0);}
                            mode +=8; 
                            tag.append(ch);
                            say ("(found < tag=${tag})",true);
                            break;                                 
            
                case '%' :  mode +=16;
                            tag.append(ch);
                            break;                                 

                case '>' :  mode +=32;
                            tag.append(ch);
                            say ("(found > tag=${tag})",true);
                            break;                                 
            
            
                default:    if (mode==0)
                            {
                                sb.append(ch);
                                say (ch,true);
                            }
                            else
                            {
                                say (ch,true);
                                tag.append(ch);
                            } // end of else
                            
                            break;            
            
            } // end of switch    
        
            switch(mode)
            {
                // asciidoc replacement variables {...}
                case 6:     flag =  decodeTag(mode, tag);
                            if (flag) {sb.append(tag);}
                            tag.setLength(0);
                            mode = 0;
                            break;

                // GString ${...}                                
                case 7:     flag =  decodeTag(mode, tag);
                            if (flag) {sb.append(tag);}
                            tag.setLength(0);
                            mode = 0;
                            break;
                                
                case 56:    flag =  decodeTag(mode, tag);
                            if (flag) {sb.append(tag);}
                            tag.setLength(0);
                            mode = 0;
                            break;

                // <% ... %>
                case 72:    flag =  decodeTag(mode, tag);
                            if (flag) {sb.append(tag);}
                            tag.setLength(0);
                            mode = 0;
                            break;

                default:    //what about all those rogue combos of chars that nick thru ? > %%
                            break;
            } // end of switch
                
                
        } // end of each
        
        if (tag.size() > 0) { say "\n---> tag:"+tag; sb.append(tag); }
        say "\n---> sb :"+sb;
            
        return sb.toString();
    } // end of find
    
    
/**
 * static main method
 * <p>
 *
 * @param  args runtime values - not used here
 */    public static void main(String[] args) throws IOException, ClassNotFoundException {
    
        println "--- starting ---"
        TemplateTranslator tt = new TemplateTranslator();
        tt.setBinding(['name':'jnorthr is the name']);
        def temp = '''\n= AsciiDoctor Sample Document\nfred <fred@asciidoctor.org>\nv1.1, 2014-01-31\n\nabc<% include 'short.adoc' %>de{f<% forward 'sample.gtpl'%>ghi{author}jkl${ name }mno${town}pqr|%stu$vwx>y<z<'''
        def ans = tt.process(temp);
               
        print ans;
        println "\n\n----------------------\n\n";
        
    def sample = '''
= AsciiDoctor Sample Document
fred <fred@asciidoctor.org>
v1.1, 2014-01-31

== Link To A Nice Tutorial

http://saltnlight5.blogspot.fr/2013/08/how-to-convert-asciidoc-text-to-html.html
abc${name}def{author}ghi${town}jkl<%= author.toLowercase() %>mnop

:description: AsciiDoc is a text document format for writing notes, +
              documentation, articles, books, slideshows, web pages +
              and man pages. This is the sample.adoc test script.

== Try A JSP Fragment

+++<% include 'header.gtpl' %>+++        
    
'''        
        tt.setBinding(['town':'Paris','name':'Jim', 'author':'jnorthr']);
        def ans2 = tt.process(sample);

        println "\n\n----------------------\n\n"+ans2;
        
        String templatename = "mail.gtpl";
        tt.setBinding(['town':'Paris','name':'Jim','greeting':'Bonjour']);
        def ans3 = tt.process(new File(templatename).text);
        println "\nFile ${templatename} rendered as :"+ans3

        println "\n\n----------------------\n\n"  

        // Create Map for binding objects to template.
        Map binding = ["greeting":"Kind regards,\n\nThe Company", "person":new PersonObject()]
        def out;
        
        // Initialize template.
        final TemplateEngine engine = new GStringTemplateEngine();
        try
        {
            final Template template = engine.createTemplate(ans3);
            // Bind Map to template.
            out = template.make(binding);
        }
        catch(Exception x)
        {
            println "GStringTemplateEngine.createTemplate() failed :"+x.message;
        }

        // We use a Writer object, can be any Writer e.g. FileWriter.
        final StringWriter writer = new StringWriter();
        out.writeTo(writer);
        String txt = writer.toString();

        println "\n\n----------------------\n\n"


        def amap=[:] 
        
        Asciidoctor asciidoctor = create();
        String rendered = asciidoctor.render(txt, amap);
        println """
        =======================================================
${rendered}
        =======================================================
        
        """
        println "--- ending ---"

    } // end of main
} // end of class