// this script to be tested from within the groovyConsole; you will need to 'add Jar(s) to classpath' of the Doctor-all-1.0.jar
import groovyx.text.DoctorTemplate;
import groovyx.text.DoctorTemplateEngine;
DoctorTemplateEngine dr = new DoctorTemplateEngine();
        def source = """= Groovy Console Test
Community Documentation <\${name}@groovy.codehaus.org>
v1.1, 2015-09-21 updated \${now}
:toc: left
:linkcss:

== Notes
Document Date:{docdate}

Here is an html link to Cloud Foundry concepts: http://docs.gopivotal.com/pivotalcf/concepts/roles.html[Orgs, Spaces, Roles, and Permissions ]

""".toString();

    // test simple asciidoctor generation plus binding for the make() method
        def binding = [now: new Date(116, 1, 31), name: 'Jnorthr']
        def output = dr.createTemplate(source).make(binding);

println "--- the end --"