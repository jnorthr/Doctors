//@Grab(group='org.slf4j', module='slf4j-simple', version='1.6.1')
//@Grab(group='org.asciidoctor', module='asciidoctorj', version='1.5.4.1')
package groovyx.text;
import groovyx.text.*

// Logging tut: http://www.javaworld.com/article/2074117/core-java/easy-groovy-logger-injection-and-log-guarding.html
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import groovy.util.logging.Slf4j

import org.asciidoctor.Attributes;

@Slf4j
public class DrTest
{ 
    public static void main(String[] args)
    {
        println "--- the start ---"
    String tl = (args.size()>0) ? args[0].replaceAll('\\\\','/') : "Servlets 3.0";  
        DoctorTemplateEngine dr = new DoctorTemplateEngine();
        def source = '''= Project Folder Location: {title}
Community Documentation <${name}@orange.fr>
v1.1, 2016-12-03 updated ${now}
:toc: left
:linkcss:
:title: ${title}
== Cloud Foundry Notes
Document Date:{docdate}

http://docs.gopivotal.com/pivotalcf/concepts/roles.html[Orgs, Spaces, Roles, and Permissions ]
'''.toString();

        def binding = [now: new Date(116, 12, 3), name: 'J.Northrop', title: tl]
        def output = dr.createTemplate(source).make(binding)

        println "->"+output.toString()

        println "--- the end ---"
    } // end of main
} // end of class