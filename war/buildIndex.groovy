// http://mrhaki.blogspot.nl/2016/05/groovy-goodness-creating-files-and.html
import groovy.io.FileType

String OS = System.getProperty("os.name").toLowerCase();
boolean win = (OS.substring(0,3)=='win')?true:false;
println "running on "+OS+" system; is it windows ? "+win;;
def suffixes = [".adoc",".ad",".asciidoc",".asc",".html",".htm",".txt"]
def dirs = []
def path = new File("C:/Users/Jim/Dropbox/Projects/Doctors/core/build").absolutePath

if (new File('./src/main/webapp/directory.adoc').exists())
{
    new File('./src/main/webapp/directory.adoc').delete()
    new File('./src/main/webapp/directory.html').delete()
}

// may need to chg this back to single '.' to parse current dir.
new File(path).eachFileRecurse(FileType.ANY)
{    
    def s = it.toString();
    def yn = false;
    print "[${s.toString()}]" 
    //if (s.toString().startsWith('./build')) { print " has ./build "; yn=true;}
    println ";"
    
    int i = s.lastIndexOf('.');    
    if (i>1)
    {        
        def t = s.substring(i).toLowerCase()        
        if (s.endsWith("directory.html")) {yn=true}
        if (s.endsWith("directory.adoc")) {yn=true}

        if (suffixes.contains( t ) && !yn) 
        { 
            s = it.toString();
            if (s.toString().startsWith('./src/main/webapp/')) { s = "./"+s.substring(18); }
            else  { s = "./../"+s.substring(2); }
            dirs << s; 
        }
    }    
}

//println "\n--------------------------------------------------\n"

def ct = 0;
def file = new File('./src/main/webapp/directory.adoc')
file.withWriter('UTF-8') {w->
    w.writeLine """= Index of Valid Documents
jnorthr <james.northrop@orange.fr>
v1.0, ${new Date()}: First version
:toc: right
:linkattrs:
:icons: font

TIP: Taken from folder *${path}*

''''

""".toString();
    
    dirs.each{x-> 
        ct+=1; 
	String na = (win) ? "file://c:"+x.toString().substring(5) : x.toString();
	if (win) { na = na.replaceAll("\\\\", "/") }
        println "found "+na;
        w.writeLine "link:"+na+"[${na}]\n";
    } // end of each
    
        w.writeLine "\n\n''''\n\nTIP: This directory has ${ct} valid files\n"
}

println "counted ${ct} files"

String ad = (win)?"asciidoctor.bat":"asciidoctor";
try{
    "${ad}  src/main/webapp/directory.adoc".execute()
} catch(any) {println any;}
println '--- the end ---'

/*  -Windows/7 test run 
C:\Users\Jim\Dropbox\Projects\Doctors\war>groovy buildIndex.groovy
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\main];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\main\DrTest.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\main\groovyx];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\main\groovyx\text];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\main\groovyx\text\Doctor.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\main\groovyx\text\DoctorHelper.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\main\groovyx\text\DoctorTemplate$_load_closure1.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\main\groovyx\text\DoctorTemplate$_main_closure3.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\main\groovyx\text\DoctorTemplate$_make_closure2.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\main\groovyx\text\DoctorTemplate.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\main\groovyx\text\DoctorTemplateEngine.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\main\groovyx\text\DoctorTemplateScanner.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\test];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\test\groovyx];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\test\groovyx\text];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\test\groovyx\text\CombinedTest$_run_closure1.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\test\groovyx\text\CombinedTest$_run_closure2.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\test\groovyx\text\CombinedTest.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\test\groovyx\text\DoctorHelperTest.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\test\groovyx\text\DoctorTemplateEngineTest1.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\test\groovyx\text\DoctorTemplateEngineTest3.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\test\groovyx\text\DoctorTemplateScannerTester.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\test\groovyx\text\DoctorTemplateTester$_main_closure1.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\test\groovyx\text\DoctorTemplateTester$_main_closure2.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\test\groovyx\text\DoctorTemplateTester$_main_closure3.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\test\groovyx\text\DoctorTemplateTester.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\classes\test\groovyx\text\DoctorTester.class];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\allclasses-frame.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\deprecated-list.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovy.ico];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx\text];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx\text\Doctor.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx\text\DoctorHelper.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx\text\DoctorTemplate.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx\text\DoctorTemplateEngine.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx\text\DoctorTemplateScanner.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx\text\DrTest.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx\text\package-frame.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx\text\package-summary.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\help-doc.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\index-all.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\index.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\inherit.gif];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\overview-frame.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\overview-summary.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\package-list];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\stylesheet.css];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\libs];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\libs\DoctorsProject-1.0-all.jar];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\libs\DoctorTemplateEngine-1.0.jar];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\reports];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\reports\tests];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\reports\tests\classes];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\reports\tests\classes\groovyx.text.DoctorHelperTest.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\reports\tests\classes\groovyx.text.DoctorTemplateEngineTest1.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\reports\tests\classes\groovyx.text.DoctorTemplateEngineTest3.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\reports\tests\css];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\reports\tests\css\base-style.css];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\reports\tests\css\style.css];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\reports\tests\index.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\reports\tests\js];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\reports\tests\js\report.js];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\reports\tests\packages];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\reports\tests\packages\groovyx.text.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\resources];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\resources\main];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\resources\main\asciidoctor.css];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\resources\main\DSL.adoc];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\resources\main\DSL.html];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\resources\main\header.txt];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\resources\main\log4j.properties];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\resources\main\Sample1.adoc];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\resources\main\Sample2.adoc];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\resources\main\StreamingMarkupBuilder.groovy];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\test-results];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\test-results\binary];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\test-results\binary\test];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\test-results\binary\test\output.bin];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\test-results\binary\test\output.bin.idx];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\test-results\binary\test\results.bin];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\test-results\TEST-groovyx.text.DoctorHelperTest.xml];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\test-results\TEST-groovyx.text.DoctorTemplateEngineTest1.xml];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\test-results\TEST-groovyx.text.DoctorTemplateEngineTest3.xml];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\compileGroovy];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\compileGroovy\groovy-java-stubs];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\compileTestGroovy];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\compileTestGroovy\groovy-java-stubs];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\groovydoc];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\groovydoc\groovyx];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\groovydoc\groovyx\text];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\groovydoc\groovyx\text\Doctor.groovy];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\groovydoc\groovyx\text\DoctorHelper.groovy];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\groovydoc\groovyx\text\DoctorTemplate.groovy];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\groovydoc\groovyx\text\DoctorTemplateEngine.groovy];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\groovydoc\groovyx\text\DoctorTemplateScanner.groovy];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\groovydoc\groovyx\text\DrTest.groovy];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\jar];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\jar\MANIFEST.MF];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\shadowJar];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\shadowJar\MANIFEST.MF];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\test];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\test\jar_extract_1186544080191249900_tmp];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\test\jar_extract_2492852711314597590_tmp];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\test\jar_extract_4915725062742770357_tmp];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\test\jar_extract_5710592449226285908_tmp];
[C:\Users\Jim\Dropbox\Projects\Doctors\core\build\tmp\test\jar_extract_6374857012812476325_tmp];
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\allclasses-frame.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\deprecated-list.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx\text\Doctor.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx\text\DoctorHelper.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx\text\DoctorTemplate.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx\text\DoctorTemplateEngine.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx\text\DoctorTemplateScanner.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx\text\DrTest.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx\text\package-frame.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx\text\package-summary.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\help-doc.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\index-all.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\index.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\overview-frame.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\overview-summary.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\reports\tests\classes\groovyx.text.DoctorHelperTest.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\reports\tests\classes\groovyx.text.DoctorTemplateEngineTest1.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\reports\tests\classes\groovyx.text.DoctorTemplateEngineTest3.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\reports\tests\index.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\reports\tests\packages\groovyx.text.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\resources\main\DSL.adoc
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\resources\main\DSL.html
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\resources\main\header.txt
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\resources\main\Sample1.adoc
found ./../\Users\Jim\Dropbox\Projects\Doctors\core\build\resources\main\Sample2.adoc
counted 25 files
java.io.IOException: Cannot run program "asciidoctor": CreateProcess error=2, The system cannot find the file specified
--- the end ---

C:\Users\Jim\Dropbox\Projects\Doctors\war>

NOTE: Windows back-slash and missing C:\ declaration before:

./../\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\allclasses-frame.html
./../\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\deprecated-list.html
./../\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx\text\Doctor.html
so use ->
file://c:\Users\Jim\Dropbox\Projects\Doctors\core\build\docs\groovydoc\groovyx\text\Doctor.html
and it works

*/