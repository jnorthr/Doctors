// https://github.com/hansd/livedemos/blob/master/testing/build-template.gradle
// see: http://www.javabeat.net/templates-in-groovy/

// to make asciidoc work you will also need the jruby jar from here: http://mvnrepository.com/artifact/org.jruby/jruby-complete/1.6.5
// or in gradle dependency from 'org.jruby:jruby-complete:1.6.5' in maven central

import groovy.text.SimpleTemplateEngine
import groovy.text.GStringTemplateEngine

// https://github.com/asciidoctor/asciidoctorj
import static org.asciidoctor.Asciidoctor.Factory.create;
import org.asciidoctor.Asciidoctor;

@GrabResolver('https://oss.sonatype.org/content/groups/public')
@Grab(group='org.asciidoctor', module='asciidoctor-java-integration', version='0.1.4')  
@GrabConfig(systemClassLoader=true)
public class Test1
{
    Asciidoctor asciidoctor;
    def data = [id:'100',name:'Neptune',color:'Green']

    // render a string
    String rendered

    // ---------------------------------------
    // Define our template
    def template = '''
<planet id="${planet.id}">
    <name>${planet.name}</name>
    <color>${planet.color}</color>
</planet>

i like planet ${planet.name} as it is so ${planet.color.toLowerCase()} and almost like earth.


== Nice Tutorial
http://saltnlight5.blogspot.fr/2013/08/how-to-convert-asciidoc-text-to-html.html

== Sample Groovy Script

// filename: RunAsciidoc.groovy +
@Grab('org.asciidoctor:asciidoctor-java-integration:0.1.3') +
import org.asciidoctor.* +
def asciidoctor = Asciidoctor.Factory.create() +
def output = asciidoctor.renderFile(new File(args[0]),  [:]) +
println(output); +


Now you may run it

+groovy RunAsciidoc.groovy myarticle.txt+


<% include '/WEB-INF/includes/header.gtpl' %>

<h1>Date / time</h1>

<p>

    The current date and time: <%= request.getAttribute('datetime') %>
</p>

'''  // end of template
// --------------------------------------------



    // GString template follows:
    def binding = [
        firstname : "Grace  ",
        lastname  : "Hopper",
        accepted  : true,
        title     : 'Groovy for COBOL programmers',
        map       : ['name':'fred']
     ]

    public Test1()
    {
        asciidoctor = create();
        rendered = asciidoctor.render("*This* is it.", Collections.EMPTY_MAP);
        
        // render a file template: String rendered = asciidoctor.renderFile(new File("resources/rendersample.asciidoc"), Collections.EMPTY_MAP);
        println rendered;
     
        // ---------------------
        // SimpleTemplateEngine
        template = template.trim().replaceAll('<%','±');
 
        // Define our data
        // Bring the template and data together
        def engine = new SimpleTemplateEngine().createTemplate(template)
        def result = engine.make([planet:data]).toString()
        result = result.replaceAll('±', '<%');
 
        println result
/*
<planet id="100">
    <name>Neptune</name>
    <color>Green</color>
</planet>
*/

        // ---------------------
        // SimpleTemplateEngine
        def map = ['day' : 'Holiday']
        def message = "Today is ${map.day} ";
        def templateEngine = new SimpleTemplateEngine()
        def template2 = templateEngine.createTemplate(message)

        Writable object = template2.make(map)
        println object
        // Today is Holiday 

     } // end of constructor


    // ---------------------
    // GStringTemplateEngine 
    public void doGS()
    {    
        def gengine = new groovy.text.GStringTemplateEngine()
        def text = '''
 
 Dear <%= firstname.trim() %> $lastname,

 We <%= accepted ? 'are pleased' : 'regret' %> \
 to inform you that your paper entitled
 '$title' was ${ accepted ? 'accepted' : 'rejected' }.

 The conference committee.
 
 ----------------------------
 
 '''

        def template3 = gengine.createTemplate(text).make(binding)
        println template3.toString()
 
/*
 Dear Grace Hopper,

 We are pleased  to inform you that your paper entitled
 'Groovy for COBOL programmers' was accepted.

 The conference committee.
*/
    } // end of doGS

    public void doFile()
    {
        def adtemplate = new File("/Volumes/Media/Users/jim/Desktop/asciidoctor.txt").text

        println """
The adtemplate follows :
------------------------
${adtemplate}
// ---------------------
"""
        adtemplate = adtemplate.replaceAll('<%','±');
        println "151 -------------------------" 
        def template3 = gengine.createTemplate(adtemplate).make(binding)
        println "153 -------------------------" 

        template3 = template3.toString()
        template3 = template3.replaceAll('±', '<%');
        println template3
    } // end of doFile

    public static void main(String[] args)
    {
        def t1 = new Test1();
    } // end of main
} // end of class