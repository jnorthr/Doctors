import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;

public class Samples
{
// Transforming Reader Input to Writer Output
def reader = new StringReader('''\
Groovy's support
for transforming reader input to writer output.
''')

public Samples()
{
def writer = new StringWriter()

reader.transformLine(writer) { line ->  
    if (line.matches(~/^Groovy.*/)) {
        line = '>>' + line.replaceAll('Groovy', 'GROOVY') + '<< '
    }
    line
}

def resultTransformLine = writer.toString()

reader = new StringReader(resultTransformLine)
writer = new StringWriter()
reader.transformChar(writer) { ch ->
    ch in ['\n', '\r'] ? '' : ch
}
println writer.toString()
assert writer.toString() == ">>GROOVY's support<< for transforming reader input to writer output."

// http://mrhaki.blogspot.fr/2009/11/groovy-goodness-readers-and-writers.html
// withReader and withWriter
def file = new File('sample.txt')

file.withWriter('UTF-8') {
    it.writeLine 'Adding this text to the file.'
}

def s 
file.withReader('UTF-8') {
    s = it.readLine()
}
println "withReader and withWriter:"+s
assert 'Adding this text to the file.' == s

// http://mrhaki.blogspot.fr/2009/10/groovy-goodness-reading-url-content.html
// reading URL content
// Contents of http://www.mrhaki.com/url.html:
// Simple test document
// for testing URL extensions
// in Groovy.

def url = "http://www.mrhaki.com/url.html".toURL()

assert '''\
Simple test document
for testing URL extensions
in Groovy.
''' == url.text

def result = []
url.eachLine {
    if (it =~ /Groovy/) {
        result << it
    }
}
assert ['in Groovy.'] == result

url.withReader { urlReader ->
    def payload = urlReader.readLine()
    println "data from url:"+payload
    assert 'Simple test document' == payload
}
} // end of constructor
    // ----------------------------------------------
    //
    public static void main(String[] args)
    {
        System.out.println("--- Start Of Text For Samples ---"); 
        Samples sam = new Samples();
        println "============================"
        SimpleTemplateEngine ate = new SimpleTemplateEngine();
        Template txt = ate.createTemplate(new File("C:/Software/Groovy/AsciidocTemplates/AsciidocTemplates/resources/sample.txt")); 
        def values = ['name' : 'fred']
        Writable made = txt.make(values);
        println made.toString();
        
        println "--- the end --- \n============================\n"
    } // end of main
    
} // end of class