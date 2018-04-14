package tests;

import java.io.File;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hthurow.tomcatjndi.TomcatJNDI;

@RunWith(Suite.class)
@SuiteClasses({TestEmployeeDAO.class})
public class AllTests {

	 private static TomcatJNDI tomcatJNDI;

	    @BeforeClass
	    public static void setUpClass() {
	    	File jspFolder = Paths.get("/WEB-INF/").toFile();
	        for (File jsp : jspFolder.listFiles())
	            System.out.println(jsp.getName());
	        
	        tomcatJNDI = new TomcatJNDI();
	        tomcatJNDI.processContextXml(new File("src/main/webapp/META-INF/context.xml"));
	        //tomcatJNDI.processContextXml(new File("web\\META-INF\\context.xml"));
	        //tomcatJNDI.processWebXml(new File("web\\WEB-INF\\web.xml"));
	        tomcatJNDI.start();
	    }

	    @AfterClass
	    public static void tearDownClass() {
	        tomcatJNDI.tearDown();
	    }
}
