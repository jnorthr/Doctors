package groovyx.text;
import groovyx.text.DoctorTemplateEngine;

// testing Reader logic
import java.io.StringReader;

def say(txt){ println txt};
DoctorTemplateEngine dte = new DoctorTemplateEngine();
def payload = """= Payload Test
:icons: font
:toc: right
Jim Northrop <james.northrop@orange.fr>
V1.0, 2016-12-05 : This is a joke.

== Topic Title

Type some stuff here.

''''

TIP: This is a tip!
""".toString();

// make using a String
def tem = dte.createTemplate(payload);
def os = tem.make();

def file3 = new File('sample.html')
// Or a writer object:
file3.withWriter('UTF-8') { writer ->
    writer.write(os.toString())
}

tem = dte.createTemplate(new StringReader(payload))
os = tem.make();
file3 = new File('Reader.html')
// Or a writer object:
file3.withWriter('UTF-8') { writer ->
    writer.write(os.toString())
}

// make a File object to ck that createTemplate(File) works
file3 = new File('sample.adoc')
// Or a writer object:
file3.withWriter('UTF-8') { writer ->
    writer.write(payload.toString())
}

tem = dte.createTemplate(new File('sample.adoc'))
os = tem.make();
file3 = new File('adoc.html')
file3.withWriter('UTF-8') { writer ->
    writer.write(os.toString())
}

// try URL createTemplate
def url = "https://raw.githubusercontent.com/jnorthr/Doctors/master/README.adoc".toURL()
tem = dte.createTemplate(url)
os = tem.make([name:'jnorthr']);
file3 = new File('url.html')
file3.withWriter('UTF-8') { writer ->
    writer.write(os.toString())
}

say "--- the end ---"