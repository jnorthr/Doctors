@GrabResolver('https://oss.sonatype.org/content/groups/public')
@Grab(group='org.jruby', module='jruby-complete', version='1.6.5')
@Grab(group='org.asciidoctor', module='asciidoctor-java-integration', version='0.1.4')  
@GrabConfig(systemClassLoader=true)

import groovy.text.GStringTemplateEngine
import static org.asciidoctor.Asciidoctor.Factory.create;
import org.asciidoctor.Asciidoctor;
import javax.swing.*;

 //Asciidoctor asciidoctor;
 
 def binding = [
     firstname : "Grace",
     lastname  : "Hopper",
     accepted  : true,
     title     : 'Groovy for COBOL programmers'
 ]
 
 def engine = new groovy.text.GStringTemplateEngine()
 
 def text = '''= My Notes About AsciiDoctor
jnorthr <doc.writer@asciidoctor.org>
v1.0, 2014-01-01

:source-highlighter: pygments

http://groovy.codehaus.org/api/groovy/text/GStringTemplateEngine.html[GStringTemplateEngine]
This GStringTemplate example uses a mixture of the JSP style and GString style placeholders but you can typically use just one style if you wish. Running this example will produce this output:

Dear <%= firstname %> $lastname,
We <% if (accepted) {out << 'are pleased'} else {out << 'regret';} %> \
 to inform you that your paper entitled
'$title' was ${ accepted ? 'accepted' : 'rejected' }.

 The conference committee.

Add the following to your web.xml file

++++
  <servlet>
   <servlet-name>GStringTemplate</servlet-name>
   <servlet-class>groovy.servlet.TemplateServlet</servlet-class>
   <init-param>
     <param-name>template.engine</param-name>
     <param-value>groovy.text.GStringTemplateEngine</param-value>
   </init-param>
 </servlet>
++++
 
// A single-line comment.
:version: 0.1.4

[source,xml]
[subs="verbatim,attributes"]
----
<dependency>
  <groupId>org.asciidoctor</groupId>
  <artifactId>asciidoctor-java-integration</artifactId>
  <version>{version}</version>
</dependency>
---- 

*Processed by Asciidoctor version {asciidoctor-version}*
'Document Title: {doctitle}'
'Traduction engine: {backend}'
Document Date:{docdate}
Document Path:{docdir}
Document Type:{doctype}
Date 1:{localdate}
Date 2:{localtime}
'''
 
 def template = engine.createTemplate(text).make(binding)
 String decodedTemplate = template.toString()
 println decodedTemplate; //  template.toString()
 println "\n\n--------------------------------"
        
 asciidoctor = create();
 String rendered = asciidoctor.render("*This* is it.", binding);
        
 // render a file template: String rendered = asciidoctor.renderFile(new File("resources/rendersample.asciidoc"), Collections.EMPTY_MAP);
 println rendered;
        
 println "\ntry again :\n--------------------------------"
 Map<String, Object> attributes = new HashMap<String, Object>();
attributes.put("backend", "html5");

Map<String, Object> asciidoctorJOptions = new HashMap<String, Object>();
asciidoctorJOptions.put("header_footer", true);
asciidoctorJOptions.put("attributes", attributes);

rendered = asciidoctor.render(decodedTemplate, asciidoctorJOptions);
println rendered;
process();
println "--- that's all folks ---"

Writable process()
{
    def output = asciidoctor.render(text, asciidoctorJOptions);
    { w -> w.println output }.asWritable()
}

        