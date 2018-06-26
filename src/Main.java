import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.SAXException;

import com.sforce.soap.metadata.MetadataConnection;



public class Main {

	public static String theURL;
	public static String thepassword ;
	public static String theusername;
	static boolean successSFDClogin;
	static boolean successwindowone;
	static boolean successwindowtwo;
	static boolean successwindowthree;
	static MetadataConnection connection;
	static SFDC session;
	static List<String> objectslist;
	static List<String> selectedobjectslist;
	static List<String> workflowslist;
	
//https://developer.salesforce.com/docs/atlas.en-us.api.meta/api/sforce_api_partner_examples.htm	
	public static void main(String[] args) throws Exception {
		Launchwindowone();
		connect();
		System.out.println("windowonelaunched");
		if (successSFDClogin==true){
			System.out.println("going to get metadata list");
			
			
		//What metadata types to append in manifest file during t1 thread
		objectslist=SFDC.listMetadata(connection,"CustomObject");
		java.util.Collections.sort(objectslist);
		workflowslist=SFDC.listMetadata(connection,"Workflow");
		
		
		Launchwindowtwo(objectslist);
		selectedobjectslist=windowtwo.returnselectedobjslist();
		if (successwindowtwo==true){
//	    Files.copy(Paths.get("src\\main\\resources\\Package.xml"), Paths.get("src\\Package.xml"), StandardCopyOption.REPLACE_EXISTING);


			URL inputUrl = Main.class.getResource("/main/resources/package.xml");
			File packagexml = new File("package.xml");
			FileUtils.copyURLToFile(inputUrl, packagexml);




	
		Thread t1 = new Thread(new Runnable() {
		     public void run() {
		 		FileBasedDeployAndRetrieve retrievesession=new FileBasedDeployAndRetrieve();
				retrievesession.metadataConnection=connection;
				try {
					Parsing.editpackagexml(objectslist,"CustomObject");
					Parsing.editpackagexml(workflowslist,"Workflow");

					
					
				} catch (SAXException | IOException
						| ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					retrievesession.retrieveZip();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					Parsing.findtheirfields(selectedobjectslist);
				} catch (IOException | XMLStreamException
						| FactoryConfigurationError | ParserConfigurationException | SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		     }
		});  
		t1.start();
		t1.setPriority(10);;
		Launchwindowthree(t1);
		Launchwindowfour();		

		System.gc();
		 if (packagexml.exists()) {
			 FileDeleteStrategy.FORCE.delete(packagexml);     
		    }
		
		File components=new File("components.zip");
		
		try {
			Files.deleteIfExists(components.toPath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		
		
		}
		}  
	}
	
    private static void Launchwindowfour() {
    	windowfour.main(null);
	}
    private static void Launchwindowthree(Thread t1) {
    	windowthree.main(t1);
	}


	private static void connect() {
    	if (successwindowone==true){
    	try {
    		SFDC session=new SFDC();
    		connection=session.login(theusername,thepassword,theURL);
        	successSFDClogin= session.success;
			System.out.println(successSFDClogin);
        	
        	if (successSFDClogin==false){
        		Launchloginfailed();
        	}
        } catch (Exception e) {
        	Launchloginfailed();
        }
    	}
	
	}

    public static void Launchwindowone() {
		windowone.main();
		theURL = windowone.URL;
		thepassword = windowone.password;
		theusername = windowone.username;
		successwindowone=false;
		successwindowone=windowone.successwindowone;
    }	
    
    public static void Launchloginfailed() {
		loginfailed.main();
    }	
    public static void Launchwindowtwo(List<String> olist) {
		windowtwo.main(olist);
		successwindowtwo=false;
		successwindowtwo=windowtwo.windowtwosuccess;
    }	 
    
   
}
