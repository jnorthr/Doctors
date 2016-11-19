package groovyx.caelyf.templating;
public decode(def txt)
{
    int at = 0
    flag = true
    println txt;
    int loop = 0 ;
    
    while(flag)
    {
        def ssx = txt.substring(at); println "\nloop:${loop}:"+ssx;
        def ix1 = ssx.indexOf("<%"); 
        def ix2 = -1;
        if (ix1 > -1)
        {
            def ss = txt.substring(ix1)
            ix2 = ss.indexOf("%>");
            println "-->ix1="+ix1+"; ix2=${ix2} : #${ss}#"
            if (ix2 > -1)
            {
                def token = txt.substring(ix1,ix1+ix2+2);
                at = ix1+ix2+1     
                println "-->token = #${token}# and at becomes:"+at;   
            } // end of if
            else
            {
                flag = false;
            } // end of else
            
            //flag = false
        } // end of if
        else
        {
            flag = false;
        } // end of else
    
        flag = ( ++loop < 4 ) ? true : false;    
    } // end of while
    
} // end of decode

def txt = "abc<% include 'fred.txt'%>def<% include pre 'WEB-INF/privacy.adoc'%>ghi+++<% include 'WEB-INF/notes.gtpl' %>+++jkl<! hi kids %>mno\$\$<%pqr\$\$"
decode(txt)
println "--- the end ---"