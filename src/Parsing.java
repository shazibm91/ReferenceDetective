import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException; 
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult; 

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Parsing {

public List<String> objs2parse;


//Main was made only for unit testing
	
public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, XMLStreamException, FactoryConfigurationError{
	
List<String> selectedobjectslist=new ArrayList<String>();
selectedobjectslist.add("Account");
findtheirfields(selectedobjectslist);
	
	
	
	
	
	
	
}






public static void editpackagexml(List<String> newmemberlist, String metadatatype) throws SAXException, IOException, ParserConfigurationException {
	

	
	
	
	
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse("package.xml");
		doc.getDocumentElement().normalize();
		
		Node n = doc.getFirstChild();
		NodeList nl = n.getChildNodes();
		Node an=null,an2=null,an3= null;
	    outerloop:
		for (int i=0; i < nl.getLength(); i++) {
		    an = nl.item(i);
		    if(an.getNodeType()==Node.ELEMENT_NODE) {
		        NodeList nl2 = an.getChildNodes();

		        for(int i2=0; i2<nl2.getLength(); i2++) {
		            an2 = nl2.item(i2);
		            
		            NodeList nl3 = an2.getChildNodes();
		            
		            
			        for(int i3=0; i3<nl3.getLength(); i3++) {
			            an3 = nl3.item(i3);
	            
				            
			    		if (an3.getTextContent().contains(metadatatype)){
				    		break outerloop;

				            }

			        }

		        }
		    }
		}
		System.out.println(an3.getTextContent());
		an2=an3.getParentNode();

		for (String newmember:newmemberlist){
	        Node member = doc.createElement("members");
	        Node st = doc.createTextNode(newmember);
	        member.appendChild(st);
	        an.appendChild(member);
	        

		}
        try 
        {            
            Source source = new DOMSource(doc); 
            File xmlFile = new File("Package.xml");            
            StreamResult result = new StreamResult(new OutputStreamWriter(new FileOutputStream(xmlFile), "ISO-8859-1"));
            Transformer xformer = TransformerFactory.newInstance().newTransformer();                        
            xformer.transform(source, result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

	}

	public static void findtheirfields(List<String> selectedobjectslist) throws IOException, XMLStreamException, FactoryConfigurationError, ParserConfigurationException, SAXException {

		
		
		FileSystemView filesys = FileSystemView.getFileSystemView();
		filesys.getHomeDirectory();
		
		File theDir = new File(filesys.getHomeDirectory()+"\\"+"Results");

		// if the directory does not exist, create it
		if (!theDir.exists()) {
		    try{
		        theDir.mkdir();
		    } 
		    catch(SecurityException se){
		    }      

		}
		
		
		

		
		
	    ZipFile zipFile = new ZipFile("components.zip");

	    for (String objectanalysis:selectedobjectslist){
	    	Pattern RELATIONTAG_REGEX = Pattern.compile("<fullName>([a-zA-Z_]{3,100})</fullName>((?!fullName).)*<referenceTo>"+objectanalysis);
	    	Pattern INSTANCE_REGEX = Pattern.compile(objectanalysis+"[ \t]+?([a-zA-Z0-9_\\-]{1,75})[ \t]*?[:=;\\{]");    	

	    	FileWriter filewriter= new FileWriter(filesys.getHomeDirectory()+"\\Results\\"+objectanalysis+".csv");
		    
			String COMMA_DELIMITER = ",";
			String NEW_LINE_SEPARATOR = "\n";
			
			//CSV file header
			final String FILE_HEADER = "Object Name ,Field Name ,Found in Metadata type, Found in File";
			//Write the CSV file header
			filewriter.append(FILE_HEADER.toString());
			
			//Add a new line separator after the header
			filewriter.append(NEW_LINE_SEPARATOR);
	    	
	    	
	    	
			//System.out.println("started fieldtheirfields parser"+" for "+objectanalysis);
			
	    	//to get list of all fields
			ZipEntry zipEntry = zipFile.getEntry("unpackaged/objects/"+objectanalysis+".object");
	        InputStream stream = zipFile.getInputStream(zipEntry);  

			String objectstring=convertStreamToString(stream).replaceAll("\n ", " ");
			List<String> fieldslist=getTagValues(objectstring,FIELDFULLNAMETAG_REGEX);
			fieldslist.add("Name");
			fieldslist.add("CreatedBy");
			fieldslist.add("LastModifiedBy");
			fieldslist.add("Owner");
			fieldslist.add("Id");
			
			List<String> customfieldslist= new ArrayList<String>();
			for (String field:fieldslist){
				if (field.contains("__c")){
					customfieldslist.add(field);
				}
			}
			//have all fields of the object under analysis now
			System.out.println(fieldslist);			
			//to grab different possible pre-words for this objects fields, then will prepare clones of field list with the diff possible prefixes.
			List<String> oldPrefixes=new ArrayList<String>();
			oldPrefixes.add(objectanalysis+"__c");
			oldPrefixes.add(objectanalysis);
		    //to grab relationship fields.
			List<String> RelationNames = new ArrayList<String>();

	    	  for (Enumeration<? extends ZipEntry> e = zipFile.entries();
		    	        e.hasMoreElements();) {
		        ZipEntry zipEntry2 = e.nextElement();
		        
		        if (zipEntry2.getName().contains("object")){
		        InputStream stream2 = zipFile.getInputStream(zipEntry2);
				String specialmetadata=(convertStreamToString(stream2).replaceAll("\n ", " "));
		        //add to prefix pattern


				List<String> prefixfieldwithc = getTagValues(specialmetadata,RELATIONTAG_REGEX);


				
				if (!prefixfieldwithc.isEmpty()){
				RelationNames.addAll(prefixfieldwithc);
				}
				
				
				oldPrefixes.addAll(prefixfieldwithc);			
    
				}
	    	  }
	    	  
	    	  System.out.println(RelationNames+"Prefixes list is ready");

			
			
			
			List<String> Prefixes=new ArrayList<String>();
			Prefixes.addAll(oldPrefixes);
			String relationnamewithr;
			for (String relationname:oldPrefixes){
				if(relationname.contains("__c")){
					relationnamewithr=relationname.replace("__c","__r");
					Prefixes.add(relationnamewithr);
				}
			}
			Set<String> hs = new HashSet<>();
			hs.addAll(Prefixes);
			Prefixes.clear();
			Prefixes.addAll(hs);

			
			
			
			List<String> prefixedfields=new ArrayList<String>();			
			//multiply prefix with fields list
			for (String prefix:Prefixes){
				for (String field:fieldslist){
					prefixedfields.add(prefix+"."+field);
				}
			}
			//System.out.println(3);
			System.out.println(prefixedfields);			
			

			//PARSE most here for prefixed fields except object and workflow (aim is for other object's components)
			System.out.println("Initiating field search in metadata archive");
	    	  for (Enumeration<? extends ZipEntry> e1 = zipFile.entries();
		    	        e1.hasMoreElements();) {
		        ZipEntry zipEntry21 = e1.nextElement();
		        if (zipEntry21.getName().contains("approvalProcesses") || zipEntry21.getName().contains("assignmentRules")
		        				        		||  zipEntry21.getName().contains("applications") || zipEntry21.getName().contains("pages")
		        				|| zipEntry21.getName().contains("class") || zipEntry21.getName().contains("/flows")
		        		
		        		
		        		
		        		){
		        InputStream stream2 = zipFile.getInputStream(zipEntry21);
				String findquery=(convertStreamToString(stream2).replaceAll("\n ", " "));   

				for (String prefixfield:prefixedfields){

					
							if (findquery.contains(prefixfield)) {
								String fieldwithoutprefix=prefixfield;
								
								if(prefixfield.contains(".")){
									String[] parts = prefixfield.split("\\.");
									fieldwithoutprefix = parts[1];
									
								}

							String filematch1=zipEntry21.getName();
							//System.out.println(filematch1);
							if (filematch1.contains("approvalProcesses")){
								filematch1=filematch1.replaceFirst("\\.", "-");
								//System.out.println(filematch1);
							}

							String filetype = filematch1.replaceAll("unpackaged\\/|\\/.*", "");
							//System.out.println(filetype);
							String filematch = filematch1.replaceAll(".*\\/|\\..*", "");
							//System.out.println(filematch);
							
							filewriter.append(objectanalysis);
							filewriter.append(COMMA_DELIMITER);
							filewriter.append(fieldwithoutprefix);
							filewriter.append(COMMA_DELIMITER);
							filewriter.append(filetype);
							filewriter.append(COMMA_DELIMITER);
							filewriter.append(filematch);
							filewriter.append(NEW_LINE_SEPARATOR);							
													
							}
											
					}
		        }
		        
		        
		         
		        
		        
		        
		        
		        //searching own object containers for reference except object & workflow
		        if (( zipEntry21.getName().contains("layouts")||zipEntry21.getName().contains("approvalProcesses"))&&((zipEntry21.getName().contains("/"+objectanalysis+"."))||(zipEntry21.getName().contains("/"+objectanalysis+"-")))
		        		
		        		){
		        InputStream stream2 = zipFile.getInputStream(zipEntry21);
				String findquery=(convertStreamToString(stream2).replaceAll("\n ", " "));   

				for (String field:fieldslist){

					
							if (findquery.contains(field)) {


							String filematch1=zipEntry21.getName();
							//System.out.println(field);
							if (filematch1.contains("approvalProcesses")){
								filematch1=filematch1.replaceFirst("\\.", "-");
								System.out.println(filematch1);								
							}

							String filetype = filematch1.replaceAll("unpackaged\\/|\\/.*", "");
							//System.out.println(filetype);
							String filematch = filematch1.replaceAll(".*\\/|\\..*", "");
							//System.out.println(filematch);
							
							filewriter.append(objectanalysis);
							filewriter.append(COMMA_DELIMITER);
							filewriter.append(field);
							filewriter.append(COMMA_DELIMITER);
							filewriter.append(filetype);
							filewriter.append(COMMA_DELIMITER);
							filewriter.append(filematch);
							filewriter.append(NEW_LINE_SEPARATOR);							
													
							}
											
					}
		        } 
		        
		        
		        //searching own object containers for reference layout
		        if ((zipEntry21.getName().contains("layouts"))&& (zipEntry21.getName().contains("/"+objectanalysis+"-"))
		        		
		        		){
		        InputStream stream2 = zipFile.getInputStream(zipEntry21);
				String findquery=(convertStreamToString(stream2).replaceAll("\n ", " "));   
				
				

				for (String field:customfieldslist){

					
							if (findquery.contains(field)) {


							String filematch1=zipEntry21.getName();
							//System.out.println(field);							

							String filetype = filematch1.replaceAll("unpackaged\\/|\\/.*", "");
							//System.out.println(filetype);
							String filematch = filematch1.replaceAll(".*\\/|\\..*", "");
							//System.out.println(filematch);
							
							filewriter.append(objectanalysis);
							filewriter.append(COMMA_DELIMITER);
							filewriter.append(field);
							filewriter.append(COMMA_DELIMITER);
							filewriter.append(filetype);
							filewriter.append(COMMA_DELIMITER);
							filewriter.append(filematch);
							filewriter.append(NEW_LINE_SEPARATOR);							
													
							}
											
					}
		        } 
		        
		        
		        
		        //other objects related list sometimes doesnt make reference like account.field__c.

		        if ((zipEntry21.getName().contains("layouts"))&& !(zipEntry21.getName().contains("/"+objectanalysis+"-"))
       		
		        		){
		        InputStream stream2 = zipFile.getInputStream(zipEntry21);
				String codebody=(convertStreamToString(stream2).replaceAll("\n ", " "));   

				for (String field:fieldslist){
					//actual parsing for field reference in apex
					
					Pattern RELATEDLIST_REGEX = Pattern.compile("<relatedLists>[ \t]+<fields>.*?"+field+".*?</fields>.*?<relatedList>"+objectanalysis+"\\."+".*?</relatedList>[ \t]+</relatedLists>");

					Matcher matcher = RELATEDLIST_REGEX.matcher(codebody);
		
					
					
					
					if (matcher.find()==true){
							
							String filematch1=zipEntry21.getName();
							//System.out.println(field);							
							//System.out.println(field+" match found in "+zipEntry2.getName());
							String filetype = filematch1.replaceAll("unpackaged\\/|\\/.*", "");
							//System.out.println(filetype);
							String filematch = filematch1.replaceAll(".*\\/|\\..*", "");
							//System.out.println(filematch);

							filewriter.append(objectanalysis);
							filewriter.append(COMMA_DELIMITER);
							filewriter.append(field);
							filewriter.append(COMMA_DELIMITER);
							filewriter.append(filetype);
							filewriter.append(COMMA_DELIMITER);
							filewriter.append(filematch);
							filewriter.append(NEW_LINE_SEPARATOR);
							
						}
					
				
					}
		        } 
		        
		        
		        
		        
		        
		        
		        
		      
		        
		        
		        //searching major apex
		        if (zipEntry21.getName().contains("class") || zipEntry21.getName().contains("trigger")){
			        InputStream stream2 = zipFile.getInputStream(zipEntry21);
					String codebody=(convertStreamToString(stream2).replaceAll("\n ", " ")); 
					
					List<String> Instances=getTagValues(codebody,INSTANCE_REGEX);
					List<String> Instancefields = new ArrayList<String>();
					for (String Instance:Instances){
						for (String fieldx:fieldslist){
							Instancefields.add(Instance+"."+fieldx);
						}
					}
					
					for (String instancefield:Instancefields){
						if (codebody.contains(instancefield)){
							String field=instancefield;
							if(instancefield.contains(".")){
								String[] parts = instancefield.split("\\.");
								field = parts[1];
								
							}
							
							
							String filematch1=zipEntry21.getName();

							String filetype = filematch1.replaceAll("unpackaged\\/|\\/.*", "");

							String filematch = filematch1.replaceAll(".*\\/|\\..*", "");


							filewriter.append(objectanalysis);
							filewriter.append(COMMA_DELIMITER);
							filewriter.append(field);
							filewriter.append(COMMA_DELIMITER);
							filewriter.append(filetype);
							filewriter.append(COMMA_DELIMITER);
							filewriter.append(filematch);
							filewriter.append(NEW_LINE_SEPARATOR);
							
							
							
						}
					}
					

					for (String field:fieldslist){
						//actual parsing for field reference in apex
						Pattern QUERYTAG_REGEX = Pattern.compile("\\[[ \t]*(?:SELECT|select|Select)((?!\\]).)*?"+field+"((?!\\]).)*?(?:FROM|From|from)[ \t]*"+objectanalysis);


						Matcher matcher = QUERYTAG_REGEX.matcher(codebody);
			
						Pattern INSTANCENEW_REGEX = Pattern.compile(objectanalysis+"[ \t]+[a-zA-Z0-9_\\-]{1,75}[ \t]*=[ \t]*?new[ \t]+"+objectanalysis+"\\(((?!\\;).)+"+field);

						Matcher matcher2 = INSTANCENEW_REGEX.matcher(codebody);						
						
						
						if (matcher.find()==true || matcher2.find()==true){
								
								String filematch1=zipEntry21.getName();
								//System.out.println(field);							
								//System.out.println(field+" match found in "+zipEntry2.getName());
								String filetype = filematch1.replaceAll("unpackaged\\/|\\/.*", "");
								//System.out.println(filetype);
								String filematch = filematch1.replaceAll(".*\\/|\\..*", "");
								//System.out.println(filematch);

								filewriter.append(objectanalysis);
								filewriter.append(COMMA_DELIMITER);
								filewriter.append(field);
								filewriter.append(COMMA_DELIMITER);
								filewriter.append(filetype);
								filewriter.append(COMMA_DELIMITER);
								filewriter.append(filematch);
								filewriter.append(NEW_LINE_SEPARATOR);
								
							}
						}

					}
		        
		        //Add code to review own object and workflow
		        if (((zipEntry21.getName().contains("workflows"))||(zipEntry21.getName().contains("objects"))) && (zipEntry21.getName().contains("/"+ objectanalysis +"."))) {
			        InputStream stream2 = zipFile.getInputStream(zipEntry21);      	
		    		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		    		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		    		Document doc = docBuilder.parse(stream2);
		    		doc.getDocumentElement().normalize();
		    	    String fulllabel=null;		    		
		    		Node n = doc.getFirstChild();
		    		NodeList nl = n.getChildNodes();
		    		Node an=null,an2=null,an3= null;

		    		
		    	//    outerloop:
		    	   // return 
		    	   for (String field:fieldslist) {
		    		   Pattern FIELDREGEX = Pattern.compile("[^a-zA-Z_]"+field+"[^a-zA-Z_]");
		    		//   System.out.println("FIELD");
		    	    //	System.out.println(field);
		    		for (int i=0; i < nl.getLength(); i++) {
		    		
		    		    an = nl.item(i);
		    		    
		    		   
		    		    if(an.getNodeType()==Node.ELEMENT_NODE) {
		    		    //	System.out.println("Second Node:");
			    		//    System.out.println(an.getNodeName());
		    		        NodeList nl2 = an.getChildNodes();

		    		        for(int i2=0; i2<nl2.getLength(); i2++) {
		    		            an2 = nl2.item(i2);
		    		        //    if(an2.getNodeType()==Node.ELEMENT_NODE){
		    		        //    System.out.println("Third Node:");
				    		//    System.out.println(an2.getTextContent());
				    		    if(an2.getTextContent()!=null  && an2.getTextContent().contains(field) && !an2.getNodeName().equals("description")){
				    		    System.out.println(an.getTextContent());
				    		   System.out.println("BINGO");
				    		    	String nodetext=an2.getTextContent().replaceAll("\n ", " ");
				    		    	Matcher matcher = FIELDREGEX.matcher(nodetext);
				    		    	if (matcher.find()){
				    		    for(int i4=0; i4<nl2.getLength(); i4++) {
		    			            an3 = nl2.item(i4);
		    			            
	    			            
		    			            
		    			            if (an3.getNodeName().equals("fullName")){
		    			            	fulllabel=an3.getTextContent();
    			            }				    			            
		    			        }	
				    		    an2=an3.getParentNode();
    				    		String nodetype=an2.getNodeName();	
    				    		
    				    		if (nodetype=="webLinks"){
    				    			nodetype="Button/Link";
    				    		}
    				    		


    				    		if (!fulllabel.equals(field) && !nodetype.equals("recordTypes") && !nodetype.equals("")){
     				    			
    									String filematch1=zipEntry21.getName();

    									String filetype = filematch1.replaceAll("unpackaged\\/|\\/.*", "");

    									String filematch = filematch1.replaceAll(".*\\/|\\..*", "");


    									filewriter.append(objectanalysis);
    									filewriter.append(COMMA_DELIMITER);
    									filewriter.append(field);
    									filewriter.append(COMMA_DELIMITER);
    									filewriter.append(filetype+"."+nodetype);
    									filewriter.append(COMMA_DELIMITER);
    									filewriter.append(filematch+"."+fulllabel);
    									filewriter.append(NEW_LINE_SEPARATOR);
    				    		}	 									
    									
    				    	//	break outerloop;
				    		    	}
		    				        }
		    		   //     }
		    			        }

		    		        
		    		    }
		    		}



						}
						
		        }	
		        
		        //Add code to review other's object and workflow
		        
//		        if (((zipEntry21.getName().contains("workflows"))||(zipEntry21.getName().contains("objects")))) {
//			        InputStream stream2 = zipFile.getInputStream(zipEntry21);      	
//		    		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//		    		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//		    		Document doc = docBuilder.parse(stream2);
//		    		doc.getDocumentElement().normalize();
//		    	    String fulllabel=null;		    		
//		    		Node n = doc.getFirstChild();
//		    		NodeList nl = n.getChildNodes();
//		    		Node an=null,an2=null,an3= null;
//
//		    		
//		    	//    outerloop:
//		    	   // return 
//		    	   for (String field:prefixedfields) {
//		    		//   System.out.println("FIELD");
//		    	    //	System.out.println(field);
//		    		for (int i=0; i < nl.getLength(); i++) {
//		    		
//		    		    an = nl.item(i);
//		    		    
//		    		   
//		    		    if(an.getNodeType()==Node.ELEMENT_NODE) {
//		    		    //	System.out.println("Second Node:");
//			    		//    System.out.println(an.getNodeName());
//		    		        NodeList nl2 = an.getChildNodes();
//
//		    		        for(int i2=0; i2<nl2.getLength(); i2++) {
//		    		            an2 = nl2.item(i2);
//		    		            if(an2.getNodeType()==Node.ELEMENT_NODE){
//		    		      //      System.out.println("Third Node:");
//				    	//	    System.out.println(an2.getNodeName());
//				    		    if(an2.getTextContent()!=null && an2.getTextContent().contains(field)){
//				    	//	    System.out.println(an2.getTextContent());
//				    		//    System.out.println("BINGO");
//				    		    for(int i4=0; i4<nl2.getLength(); i4++) {
//		    			            an3 = nl2.item(i4);
//		    			            if (an3.getNodeName()=="fullName"){
//		    			            	fulllabel=an3.getTextContent();
//    			            }				    			            
//		    			        }	
//				    		    an2=an3.getParentNode();
//    				    		String nodetype=an2.getNodeName();	
//    				    		
//    				    		if (nodetype=="webLinks"){
//    				    			nodetype="Button/Link";
//    				    		}
//
//    				    		if (!fulllabel.equals(field) && !nodetype.equals("recordTypes") && !nodetype.equals("")){
//     				    			
//    									String filematch1=zipEntry21.getName();
//
//    									String filetype = filematch1.replaceAll("unpackaged\\/|\\/.*", "");
//
//    									String filematch = filematch1.replaceAll(".*\\/|\\..*", "");
//
//    									if (field.contains(".")){
//    										String[] parts = field.split("\\.");
//    									//	String part1 = parts[0]; 
//    										field = parts[1]; 
//    									}
//    									filewriter.append(objectanalysis);
//    									filewriter.append(COMMA_DELIMITER);
//    									filewriter.append(field);
//    									filewriter.append(COMMA_DELIMITER);
//    									filewriter.append(filetype+"."+nodetype);
//    									filewriter.append(COMMA_DELIMITER);
//    									filewriter.append(filematch+"."+fulllabel);
//    									filewriter.append(NEW_LINE_SEPARATOR);
//    				    		}	 									
//    									
//    				    	//	break outerloop;
//				    		     
//		    				        }
//		    		        }
//		    			        }
//
//		    		        
//		    		    }
//		    		}
//
//
//
//						}
//						
//		        }		        
		        
		        
						
					}

		        
		        
		        
		       
		        
		        
		 		        
	    	  			
			



	    	  
		        
			
	    	 
	    	  filewriter.flush();
	    	  filewriter.close();

	    	  System.out.println("job done for "+objectanalysis);	

	  	    stripDuplicatesFromFile(filesys.getHomeDirectory()+"\\Results\\"+objectanalysis+".csv");
	    	//new tab or new csv for next object?			 	
		}

		
		zipFile.close();	
		}
	
	//http://stackoverflow.com/questions/12410499/using-sax-parser-on-xml-file-inside-a-zA-Zip

	static String convertStreamToString(java.io.InputStream is) {
	    @SuppressWarnings("resource")
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	private static final Pattern FIELDFULLNAMETAG_REGEX = Pattern.compile("<fields>        <fullName>([a-zA-Z_]{3,75})</fullName>");

	
	private static List<String> getTagValues(final String str, Pattern regex) {
	    final List<String> tagValues = new ArrayList<String>();
	    final Matcher matcher = regex .matcher(str);
	    while (matcher.find()) {
	        tagValues.add(matcher.group(1));
	    }
	    return tagValues;
	}
	
    public static void stripDuplicatesFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        Set<String> lines = new LinkedHashSet<String>(10000); // maybe should be bigger
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (String unique : lines) {
            writer.write(unique);
            writer.newLine();
        }
        writer.close();
    }
		
	}

    

