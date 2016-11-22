package groovyx.text;
import groovyx.text.DoctorTemplate;
import groovyx.text.DoctorTemplateEngine;

		String home = System.getProperty("user.home");

        // write a log of everything as flog output file handle    
        File flog = new File('resources/CombinedTest.log')
        
        def msg = "DoctorTemplate starting\nwriting log messages to "+flog.getCanonicalPath(); 
        println(msg);
        flog.write(msg+'\n');
		println "\n\n-----------------------------------------------------\n";
        msg = "step 1 : construct default template";
        println msg;
        flog.append msg;
        
        // construct default template        
        def dr = new DoctorTemplate()
        
        // Use toString() of Writable closure.
		println "\n\n-----------------------------------------------------\n";
        msg = "\nstep 2 : send 'Hello World!' to default template";
        println msg;
        flog.append msg+"\n";

		try {  
        	def xxx = dr.make { Writer out -> out << "= Hello world!" }.toString();
        	assert xxx.contains("<h1>Hello world!</h1>") == true
        	println "step 2 size()="+xxx.size();
        	assert 30000 <  xxx.size();
		} 
		catch(AssertionError e) 
		{  
    		println e.getMessage()  
	        flog.append "\n"+e.getMessage()+'\n'
		} 

        msg = "\nDoctorTemplate step 2 completed\n"
        flog.append(msg) 
        println msg;
		println "\n\n-----------------------------------------------------\n";


		println "\n\n-----------------------------------------------------\n";
        msg = "step 3 : request a 'make' offering a map and closure to build html response";
        println msg;
        flog.append msg+"\n";
        
		// request a 'make' offering a map and closure to build html response
        final writable = dr.make(user:'mrhaki', { out ->
            out.println "= Welcome ${user},\n"
            out.print "Today on ${new Date().format('dd-MM-yyyy')}, "
            out.println "we have a Groovy party!"
        });
		println "\n\n-----------------------------------------------------\n";
      
        // show what we produced
        msg = "step 4 : request produced the following :\n";
        flog.append msg
        println msg
        def tx = writable.toString();
        i = tx.size();
        println "step 4 result size = "+i;
        tx = (i>200)?tx.substring(i - 200):i;
        flog.append writable.toString()+'\n';
        println tx;
        assert i > 30000;
		println "\n\n-----------------------------------------------------\n";
        
                        
		println "\n\n-----------------------------------------------------\n";
        msg = "step 5 : get text from a reader";
        println msg;
        flog.append msg+"\n";
        String source = "= Doctor\n\n== Missing Filename\n\nThe first command line parameter needs to be the name of a text file that exists.\n"
        
        
        // try completely new template object
        DoctorTemplateEngine drs = new DoctorTemplateEngine();
        def output = drs.createTemplate(source).make();
        String s = output.toString();
        def i = s.size()
        println "produced ${i} bytes of output \n"
        i = (i > 200) ? i - 200 : i;
        println s.substring(i);
        flog.append "produced ${s.size()} bytes of output \n"+s.substring(i)+'\n';
        assert s.size() > 30000;
		println "\n\n-----------------------------------------------------\n";


		// -----------------------------------------------------------------------------------
        msg = "\n\n--------------------\nstep 6 : read text from a File\n------------------------------------\n";
        println msg;
        flog.append msg+"\n";
		String f = "resources/Sample1.adoc";
		
        File fin = new File(f);
        
        msg = "filename:"+fin.getCanonicalPath();
        println msg;
        flog.append(msg+"\n");
		println "\n${f} has "+fin.text.size()+" bytes";


        drs = new DoctorTemplateEngine();
        boolean flag  = false; // forces asciidoctor to produce html reply without any header/trailer
		
        output = drs.createTemplate(fin, flag ).make().toString();
                
        
        msg = "drs.createTemplate(fin, ${flag} ).make() worked ok\n--------------------\nstep 6 : produced ${output.size()} bytes of text from a File";
        println msg;
        flog.append msg+"\n";
		println "step 6 size()="+output.size()+" bytes"
        msg ="first 200 bytes=["+output.toString().substring(0,200)+"]";
        println msg;
        flog.append msg+"\n";
        assert output.size() > 900;
		println "\n\n-----------------------------------------------------\n";
        
		println "\n--- the end of CombinedTest ---"
		flog.append "\n--- the end of CombinedTest ---\n";
		