import com.sforce.soap.metadata.FileProperties;
import com.sforce.soap.metadata.ListMetadataQuery;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.ConnectionException;
import com.sforce.soap.metadata.MetadataConnection;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;


public class SFDC {
	public static List<String> list;
	public boolean success=false;
    static MetadataConnection MetadataConnection = null;
    static PartnerConnection PartnerConnection = null;
    private static BufferedReader reader =
        new BufferedReader(new InputStreamReader(System.in));
    
    public static void main(String[] args) {


    } 
    
  
    public MetadataConnection login(String username,String password,String URL) {
        success = false;

        try {
          ConnectorConfig config = new ConnectorConfig();
          config.setUsername(username);
          config.setPassword(password);
          
          config.setAuthEndpoint(URL);
          config.setTraceMessage(true);
          config.setPrettyPrintXml(true);
          PartnerConnection = new PartnerConnection(config);
          LoginResult LoginResult=(new PartnerConnection(config)).login(username, password);
          config.setServiceEndpoint(LoginResult.getMetadataServerUrl());
          MetadataConnection = new MetadataConnection(config);          
          success = true;
          
        } catch (ConnectionException ce) {
          ce.printStackTrace();
        }
        
        return MetadataConnection;
      }
    
    public static List<String> listMetadata(MetadataConnection Connection, String metadatatype) {
    	  try {
    	    ListMetadataQuery query = new ListMetadataQuery();
    	    query.setType(metadatatype);
    	    //query.setFolder(null);
    	    double asOfVersion = 34.0;
    	    list = new ArrayList<String>();
    	    // Assuming that the SOAP binding has already been established.
    	    FileProperties[] lmr = Connection.listMetadata(new ListMetadataQuery[] {query}, asOfVersion);
    	    if (lmr != null) {
    	      for (FileProperties n : lmr) {
    	    	  list.add(n.getFullName());


    	      }
    	    }            
    	  } catch (ConnectionException ce) {
    	    ce.printStackTrace();
    	  }
    	  return list;
    	}

    
}
    // 
    // Add your methods here.
    //
