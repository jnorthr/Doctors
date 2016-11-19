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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import groovy.text.TemplateEngine;
import groovy.text.Template;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.runtime.InvokerHelper;

import groovyx.caelyf.templating.AsciidocTemplateHelper; 


/**
 * Processes template source files substituting variables and expressions into
 * placeholders in a template source text to produce the desired output.
 * <p>
 * The template engine uses JSP style &lt;% %&gt; script and &lt;%= %&gt; expression syntax
 * or GString style expressions. The variable '<code>out</code>' is bound to the writer that the template
 * is being written to.
 * <p>
 * Frequently, the template source will be in a file but here is a simple
 * example providing the template as a string:
 * <pre>
 * def binding = [
 *     firstname : "Grace",
 *     lastname  : "Hopper",
 *     accepted  : true,
 *     title     : 'Groovy for COBOL programmers'
 * ]
 * def engine = new groovy.text.AsciidocTemplateEngine()
 * def text = '''\
 * Dear &lt;%= firstname %&gt; $lastname,
 *
 * We &lt;% if (accepted) print 'are pleased' else print 'regret' %&gt; \
 * to inform you that your paper entitled
 * '$title' was ${ accepted ? 'accepted' : 'rejected' }.
 *
 * The conference committee.
 * '''
 *
 * def template = engine.createTemplate(text).make(binding)
 * println template.toString()
 * </pre>
 * This example uses a mix of the JSP style and GString style placeholders
 * but you can typically use just one style if you wish. Running this
 * example will produce this output:
 * <pre>
 * Dear Grace Hopper,
 *
 * We are pleased to inform you that your paper entitled
 * 'Groovy for COBOL programmers' was accepted.
 *
 * The conference committee.
 * </pre>
 * The template engine can also be used as the engine for {@link groovy.servlet.TemplateServlet} by placing the
 * following in your <code>web.xml</code> file (plus a corresponding servlet-mapping element):
 * <pre>
 * &lt;servlet&gt;
 *   &lt;servlet-name&gt;AsciidocTemplate&lt;/servlet-name&gt;
 *   &lt;servlet-class&gt;groovy.servlet.TemplateServlet&lt;/servlet-class&gt;
 *   &lt;init-param&gt;
 *     &lt;param-name&gt;template.engine&lt;/param-name&gt;
 *     &lt;param-value&gt;groovyx.caelyf.templating.AsciidocTemplateEngine&lt;/param-value&gt;
 *   &lt;/init-param&gt;
 * &lt;/servlet&gt;
 * </pre>
 * In this case, your template source file should be text composed in the asciidoc format along with the appropriate embedded placeholders.
 *
 * @author sam
 * @author Christian Stein
 * @author Paul King
 * @author Alex Tkachman
 * @author Jim Northrop - asciidoc bits
 */
public class AsciidocTemplateEngine extends TemplateEngine 
{
    private boolean verbose = true;
    private static int counter = 1;

    private GroovyShell groovyShell;
    private AsciidocTemplateHelper ath = new AsciidocTemplateHelper();
 

    // ===============================================
    // Class Constructors
    public AsciidocTemplateEngine() 
    {
        this(GroovyShell.class.getClassLoader());
    }

    public AsciidocTemplateEngine(boolean verbose) {
        this(GroovyShell.class.getClassLoader());
        setVerbose(verbose);
    }

    public AsciidocTemplateEngine(ClassLoader parentLoader) {
        this(new GroovyShell(parentLoader));
    }

    public AsciidocTemplateEngine(GroovyShell groovyShell) {
        this.groovyShell = groovyShell;
    }


    // =======================================================
    // Asciidoctor methods follow :
    /**
     * @param verbose true if you want the engine to display the template source file for debugging purposes
     */
    public renderAsciidoc(payload, binding)
    {
System.out.println("AsciidocTemplateEngine.renderAsciidoc(${payload.toString()})");
	ath.setIncludeHeaderFooter(true);
    	ath.loadContent(payload);
      ath.setNeedHeader(true);
      ath.setKeepUnknownPlaceholders(false);
      return ath.render();
    } // end of render


    // ========================================================
    // build
    public Template createTemplate(Reader reader) throws CompilationFailedException, IOException 
    {
System.out.println("AsciidocTemplateEngine.createTemplate()");
        AsciidocTemplate template = new AsciidocTemplate();
        String script = template.parse(reader);

        if (verbose) {
            System.out.println("\n-- script source --");
            System.out.print(script);
            System.out.println("\n-- script end --\n");
        }

        try {
            template.script = groovyShell.parse(script, "AsciidocTemplateScript" + counter++ + ".groovy");
	      if (verbose) {
      	      System.out.println("\n-- template.script source --");
            	System.out.print(template.script.toString());
            	System.out.println("\n-- template.script end --\n");
        	}

        } catch (Exception e) {
            throw new GroovyRuntimeException("Failed to parse template script (your template may contain an error or has an unsupported expression): " + e.getMessage());
        }
        return template;
    }

    /**
     * @param verbose true if you want the engine to display the template source file for debugging purposes;
     * also sets debug trace in the Helper class too
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
      ath.setVerbose(verbose);
    }

    public boolean isVerbose() {
        return verbose;
    }


    // ----------------------------------------------
    //
    public static void main(String[] args)
    {
	System.out.println("--- Start Of Text For AsciidocTemplateEngine ---");    

	String payloadx = '''= My Notes About AsciiDoctor\n:icons: font \njnorthr <doc.writer@asciidoctor.org>\nv1.0, 2014-01-01 \n\n== Heading One\n*This* is it. My name is ${name}. Hi {title}. here is an include statement:<% include '/WEB-INF/includes/header.gtpl' %> \n\nTIP: Eat Spinach regularly. \nicon:heart[2x] \n''';

	def payload = """= My Notes About AsciiDoctor
:icons: font 
jnorthr <doc.writer@asciidoctor.org>
v1.0, 2014-01-01 

== Heading One
*This* is it. My name is {name}. Hi {author}. here is an include statement:<% include '/WEB-INF/includes/header.gtpl' %> 

TIP: Eat Spinach regularly. 
icon:heart[2x] 
"""

	AsciidocTemplateEngine ate = new AsciidocTemplateEngine();

/* -- test with simple string first
	def fh = new File("resources/sample.txt")
    	if (fh.exists())
    	{
      	payload = fh.text;
    	} // end of if
*/
	def values = ['name' : 'fred']

	println "constructed AsciidocTemplateEngine - ready to render()"
    	def result = ate.renderAsciidoc(payload, values);

	println "render completed ok; results follow:"
    	println result;
    println "--------------------------------\ncreateTemplate() will run now:\n"

    Template t = ate.createTemplate(result);
    println "t is of class:"+t.getClass()+"<---\n";

    Writable txt = t.make(values);
    println txt.toString();
    println "\n---------------------\nEnd of txt.toString()\n--------------------------------\n"

    println "writing file resources/AsciidocTemplateEngineResults.html"
    new File("resources/AsciidocTemplateEngineResults.html").write(txt.toString());

    System.out.println("--- End Of Text For AsciidocTemplateEngine ---");    
    } // end of main

    // ========================================================
    private static class AsciidocTemplate implements Template 
    {
        protected Script script;
        public Writable make() 
        {
            return make(null);
        }

    // provide a binding map of key/values for template replacement placeholders
        public Writable make(final Map map) 
      { 
		println "AsciidocTemplateEngine.make(${map})";

            return new Writable(){
                /**
                 * Write the template document with the set binding applied to the writer.
                 *
                 * @see groovy.lang.Writable#writeTo(java.io.Writer)
                 */
                public Writer writeTo(Writer writer) {
                    Binding binding;
                    if (map == null)
                        binding = new Binding();
                    else
                        binding = new Binding(map);

   			  println "\n\n==================\nScript hit:\n--------------------" 
                    
			  try{   
					Script scriptObject = InvokerHelper.createScript(script.getClass(), binding); 
			  		println "\n--------------------\nAsciidocTemplateEngine.scriptObject=${scriptObject.toString()}\n--------------------";
                    		PrintWriter pw = new PrintWriter(writer);
		                  scriptObject.setProperty("out", pw);
            		      scriptObject.run();
                    		pw.flush();
			  		println "\n--------------------\nAsciidocTemplateEngine.pw.flush()\n--------------------";
			  }
			  catch(Exception x ) 
			  {
					println "AsciidocTemplateEngine bad news:"+x.message
			  }

	              return writer;
                } // end of writeTo

                /**
                 * Convert the template and binding into a result String.
                 *
                 * @see java.lang.Object#toString()
                 */
                public String toString() 
                {
                    StringWriter sw = new StringWriter();
                    writeTo(sw);
			  def tx2 = sw.toString();
	      if (verbose) {
      	      System.out.println("\n-- make().sw.toString() --");
            	System.out.print(tx2);
            	System.out.println("\n-- end --\n");
        	}

                    return tx2;
                } // toString()
            };
        }

        /**
         * Parse the text document looking for <% or <%= and then call out to the appropriate handler, otherwise copy the text directly
         * into the script while escaping quotes.
         *
         * @param reader a reader for the template text
         * @return the parsed text
         * @throws IOException if something goes wrong
         */
        protected String parse(Reader reader) throws IOException 
      {
  		println "AsciidocTemplateEngine.parse()";

            if (!reader.markSupported()) {
                reader = new BufferedReader(reader);
            }

            StringWriter sw = new StringWriter();

            startScript(sw);
            int c;

            while ((c = reader.read()) != -1) 
        {
                if (c == '<') 
            {
                    reader.mark(1);
                    c = reader.read();
                    if (c != '%') 
              {
                        sw.write('<');
                        reader.reset();
                    } 
              else 
              {
                        reader.mark(1);
                        c = reader.read();
                        if (c == '=') 
                {
                            groovyExpression(reader, sw);
                        }
 
                else 
                {
                            reader.reset();
                            groovySection(reader, sw);
                        }
                    } // end of else
                    continue; // at least '<' is consumed ... read next chars.
                } // end of if


                if (c == '$') 
            {
                    reader.mark(1);
                    c = reader.read();
                    if (c != '{') {
                        sw.write('$');
                        reader.reset();
                    } else {
                        reader.mark(1);
                        sw.write('${');
                        processGSstring(reader, sw);
                    }
                    continue; // at least '$' is consumed ... read next chars.
                } // end of if

                if (c == '\"') {
                    sw.write('\\');
                }

                /*
                 * Handle raw new line characters.
                 */
                if (c == '\n' || c == '\r') {
                    if (c == '\r') { // on Windows, "\r\n" is a new line.
                        reader.mark(1);
                        c = reader.read();
                        if (c != '\n') {
                            reader.reset();
                        }
                    }
                    sw.write('\n');
                    continue;
                }
                sw.write(c);
            }
            endScript(sw);
            return sw.toString();
        }

        /**
         * Builds prefix chars for GString script content writing beginning """ to string writer
       *
         * called from String parse(Reader reader)
         *
         * @param sw     a StringWriter to write expression content
         */
        private void startScript(StringWriter sw) 
      {
            sw.write("out.print(\"\"\"");
        } // end of startScript


        /**
         * Builds suffix chars for GString script so that it ends with """ to string writer
       *
         * called from String parse(Reader reader)
         *
         * @param sw     a StringWriter to write ending """ expression content
         */
        private void endScript(StringWriter sw) 
      {
            sw.write("\"\"\");\n");
            sw.write("\n/* Generated by AsciidocTemplateEngine */");
        } // end of endScript


        /**
         * Consumes GString script content writing it to string writer until it finds a } or EOF
       *
       * Used in the parse(Reader reader) logic above
         *
         * @param reader a reader for the template text
         * @param sw     a StringWriter to write expression content
         * @throws IOException if something goes wrong
         */
        private void processGSstring(Reader reader, StringWriter sw) throws IOException 
      {
  		println "AsciidocTemplateEngine.processGString()";

            int c;
            while ((c = reader.read()) != -1) {
                if (c != '\n' && c != '\r') {
                    sw.write(c);
                }
                if (c == '}') {
                    break;
                }
            }
        } // end of processGSstring


        /**
         * Closes the currently open write and writes out the following text as a GString expression until it reaches an end %>.
       *
       * Used in the parse(Reader reader) logic above
         *
         * @param reader a reader for the template text
         * @param sw     a StringWriter to write expression content
         * @throws IOException if something goes wrong
         */
        private void groovyExpression(Reader reader, StringWriter sw) throws IOException 
      {
            sw.write('${');
            int c;
            while ((c = reader.read()) != -1) {
                if (c == '%') {
                    c = reader.read();
                    if (c != '>') {
                        sw.write('%');
                    } else {
                        break;
                    }
                }
                if (c != '\n' && c != '\r') {
                    sw.write(c);
                }
            }
            sw.write('}');
        } // end of groovyExpression


        /**
         * Closes the currently open write and writes the following text as normal Groovy script code until it reaches an end %>.
       * Used in the parse(Reader reader) logic above
         *
         * @param reader a reader for the template text
         * @param sw     a StringWriter to write expression content
         * @throws IOException if something goes wrong
         */
        private void groovySection(Reader reader, StringWriter sw) throws IOException 
      {
            sw.write("\"\"\");");
            int c;
            while ((c = reader.read()) != -1) {
                if (c == '%') {
                    c = reader.read();
                    if (c != '>') {
                        sw.write('%');
                    } else {
                        break;
                    }
                }
                /* Don't eat EOL chars in sections - as they are valid instruction separators.
                 * See http://jira.codehaus.org/browse/GROOVY-980
                 */
                // if (c != '\n' && c != '\r') {
                sw.write(c);
                //}
            }
            sw.write(";\nout.print(\"\"\"");
        } // end of groovySection

    } // end of private static class AsciidocTemplate

} // end of class

/* tried to add metaClass but no joy
      AsciidocTemplate.metaClass.static.include << {->
          def sb = new StringBuffer()
          delegate.each 
        {
                   sb << (Character.isUpperCase(it as char) ? Character.toLowerCase(it as char) :  Character.toUpperCase(it as char))
          } // end of each

          sb.toString()
      }
*/