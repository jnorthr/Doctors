package groovyx.text;
import groovyx.text.DoctorTemplate;
import groovyx.text.DoctorTemplateEngine;

		String home = System.getProperty("user.home");

        // write a log of everything as fo5 output file handle    
        File fo5 = new File('resources/CombinedTest.log')
        println("DoctorTemplate starting\nwriting log messages to "+fo5.getCanonicalPath());
        fo5.write("DoctorTemplate starting\nwriting log messages to "+fo5.getCanonicalPath()+'\n');

        println "step 1 : construct default template";
        fo5.append "step 1 : construct default template\n";
        
        // construct default template        
        def dr = new DoctorTemplate()
        
        // Use toString() of Writable closure.
        println "step 2 : send 'Hello World!' to default template";
        fo5.append "step 2 : send 'Hello World!' to default template\n";

		try {  
        	assert dr.make { Writer out -> out << "Hello world!" }.toString() == 'Hello world*'
		} 
		catch(AssertionError e) 
		{  
    		println e.getMessage()  
	        fo5.append "\n"+e.getMessage()+'\n'
		} 

        fo5.append "\nDoctorTemplate step 2 completed\n"
        println "\nDoctorTemplate step 2 completed"


        println "step 3 : request a 'make' offering a map and closure to build html response";
        fo5.append "step 3 : request a 'make' offering a map and closure to build html response\n";
		// request a 'make' offering a map and closure to build html response
        final writable = dr.make(user:'mrhaki', { out ->
            out.println "Welcome ${user},"
            out.print "Today on ${new Date().format('dd-MM-yyyy')}, "
            out.println "we have a Groovy party!"
        });
      
        // show what we produced
        println "step 4 :"+writable.toString();
        fo5.append "step 4 : request produced the following :\n";
        fo5.append writable.toString()+'\n';
        
                        
        println "step 5 : get text from a reader";
        fo5.append "step 5 : get text from a reader\n";
        String source = "= Doctor\n\n== Missing Filename\n\nThe first command line parameter needs to be the name of a text file that exists.\n"
        
        
        // try completely new template object
        DoctorTemplateEngine drs = new DoctorTemplateEngine();
        def output = drs.createTemplate(source).make();
        String s = output.toString();
        def i = s.size()
        println "produced ${i} bytes of output \n"
        i = (i > 200) ? i - 200 : 03;
        println s.substring(i);
        fo5.append "produced ${s.size()} bytes of output \n"+s.substring(i)+'\n';


		// -----------------------------------------------------------------------------------
        println "\n\n--------------------\nstep 6 : read text from a File";
        fo5.append "\n\n--------------------\nstep 6 : get text from a file\n";
		String f = "resources/Sample1.adoc";
        File fin = new File(f);
        
        println("filename:"+fin.getCanonicalPath());
        fo5.append("filename:"+fin.getCanonicalPath()+'\n');


        drs = new DoctorTemplateEngine();
        boolean flag  = false; // forces asciidoctor to produce html reply without any header/trailer
		
        output = drs.createTemplate(fin, flag ).make().toString();
                
        
        println "drs.createTemplate(fin, ${flag} ).make() worked ok\n--------------------\nstep 6 : produced ${output.size()} bytes of text from a File";
        fo5.append "\ndrs.createTemplate(fin, ${flag} ).make() worked ok\n--------------------\nstep 6 : produced ${output.size()} bytes of text from a File\n";
		
        println "first 200 bytes=["+output.toString().substring(0,200)+"]";
        fo5.append "first 200 bytes=["+output.toString()+"]\n";
        
		println "\n--- the end of CombinedTest ---"
		fo5.append "\n--- the end of CombinedTest ---\n";
		