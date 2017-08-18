package db;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

public class Configuration {

//	public static final String TYPE = "DEV"; //DEV,SIT,UAT
//	public static final String TYPE = "SIT"; //DEV,SIT,UAT
	public static String TYPE = ""; //"UAT"; //DEV,SIT,UAT
	public static String logType = "info";
	
	public static String keyEnscrypt = "AES512";
	
//	public static final String path = "C:\\file\\";
//	public static final String path = "/apps/";
	
	public static final String filenameDBConfig = "usrDB.property";
	public static final String fileSuperUser = "superUser.property";
	public static final String fileConfiguration = "configuration.property";
	
	public static final String pathDEV = "C:\\FILE\\";
	public static final String pathPROD = "/apps/rbfrontcustom/";
//	public static final String pathDBPropertyUAT = "/apps/";
	
	public static String superUser1 = "";
	public static String superUser2 = "";
	
//	public static final String hostnameDEV = "localhost:3306";
//	public static final String hostnameSIT = "10.21.18.48:3310";
//	public static final String hostnameUAT = "10.21.18.50:3310";
	public static String hostnameDB = "";
	
//	public static final String dbnameSIT = "rbfworkflow";
//	public static final String dbnameUAT = "rbfworkflow_uat";
	public static String dbnameRB = "";
	
//	public static final String ldapSIT = "ldaps://10.21.17.81:636/dc=scbcorpiam,dc=local";
//	public static final String ldapUAT = "ldaps://10.21.17.81:636/dc=scbcorpiam,dc=local";
	public static String ldapURI = "";
	public static String hostAD = "";
	
//	public static final String hostnameHrSIT = "10.21.16.118:1440";
//	public static final String hostnameHrUAT = "10.21.16.118:1440";
	public static String hostnameHr = "";
	
//	public static final String dbnameHrSIT = "HRMS_PROD_NEW";
//	public static final String dbnameHrUAT = "HRMS_IAM";
	public static String dbnameHr = "";
	
	public static HashMap<String,String> data = new HashMap<String,String>();
	
	public Configuration(){
		String name  = "ApproveEx";
		String value = "NM";
		this.data.put(name, value);
	}
	
	public static void setConfig(){
		System.out.println("setConfig");
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		  
		connect = DatbaseConnection.getConnectionMySQL();
		try {
			preparedStatement = connect
			          .prepareStatement("select * from tblmt_config c ");

			ResultSet resultSet = preparedStatement.executeQuery();
	
			while(resultSet.next()){
				String name  = resultSet.getString("Name");
				String value = resultSet.getString("Value");
				data.put(name, value);	
			}		
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
	}
	
	public static String getParam(String name){
		
		String result = null;
		//setConfig();
//		if(data.get(name)==null){
//			data.put("Type", "DEV");
//			System.out.println("AAA");
//			setConfig();
			System.out.println(data);
//		}
		result = data.get(name);
		return result;	
	}
	
	public static String getHierarchyLevel(){
		String path1 = pathDEV; //"C:\\file\\";
		String path2 = pathPROD; //"/apps/";
		
		Properties prop = new Properties();
		InputStream input = null;
		String hierarchyLevel = "";
		try {
			String filename = Configuration.fileConfiguration;
			
			try{
				input = new FileInputStream(path2+filename);
		 	}catch(Exception e){
		 		input = new FileInputStream(path1+filename);
		 	}

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			//System.out.println("hierarchyLevel " + prop.getProperty("hierarchyLevel"));
			
			hierarchyLevel = prop.getProperty("hierarchyLevel");
			

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return hierarchyLevel;
	}
	
	public static String getTYPE(){
		
		InputStream fileInputStream = null;
		String path1 = pathDEV; //"C:\\file\\";
		String path2 = pathPROD; //"/apps/";
		
		String filename = Configuration.filenameDBConfig;
		
		 try {
			 	try{
			 		fileInputStream = new FileInputStream(path2+filename);
			 	}catch(Exception e){
			 		fileInputStream = new FileInputStream(path1+filename);
			 	}
				
//				System.out.println("Total file size to read (in bytes) : "
//						+ fileInputStream.available());

				BufferedReader br = new BufferedReader( new InputStreamReader(fileInputStream, StandardCharsets.UTF_8 ));
				   
				StringBuilder sb = new StringBuilder();
				String OriLine;
				while(( OriLine = br.readLine()) != null ) {
				    sb.append( OriLine );
				    sb.append( '\n' );
				}
				br.close();
				//System.out.println(sb.toString());
				
				  String file = sb.toString();
//				  if(!insertTerminal(file))
//					  System.out.println("insert terminal auto error");				  				  				  			  
					  	
						int s1 = 0;
						int l1 = file.indexOf("\n",s1);
						String lineAll[] = file.split("\n");
						
						for(int i=0;i<lineAll.length;i++){
							String line = lineAll[i];
//							System.out.println(i+":"+line);
							
							int s = 0;
							int l = 0;
							
							if(line.indexOf("PROD")>-1){
								Configuration.TYPE = "PROD"; 							
							}
							if(line.indexOf("UAT")>-1){
								Configuration.TYPE = "UAT"; 
							}
							if(line.indexOf("SIT")>-1){
								Configuration.TYPE = "SIT"; 
							}
							if(line.indexOf("DEV")>-1){
								Configuration.TYPE = "DEV"; 
							}
							
							if(!Configuration.TYPE.equals(""))
							      break;
						}// end for

				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Exception forget change path usrDB.property");
				e.printStackTrace();
//				al.setStatus("E");
//				success = "เน�เธกเน�เน�เธ�เธ� file:"+path+filename;
			}
		 return Configuration.TYPE;
	}
	
	public static String getSuperUser(){
		System.out.println("getSuperUser");
		
		InputStream fileInputStream = null;
		String path1 = pathDEV; //"C:\\file\\";
		String path2 = pathPROD; //"/apps/";
		
		String filename = Configuration.fileSuperUser;//"getSuperUser.property";
		
		 try {
			 	try{
			 		fileInputStream = new FileInputStream(path2+filename);
			 	}catch(Exception e){
			 		fileInputStream = new FileInputStream(path1+filename);
			 	}

				BufferedReader br = new BufferedReader( new InputStreamReader(fileInputStream, StandardCharsets.UTF_8 ));
				   
				StringBuilder sb = new StringBuilder();
				String OriLine;
				while(( OriLine = br.readLine()) != null ) {
				    sb.append( OriLine );
				    sb.append( '\n' );
				}
				br.close();
				//System.out.println(sb.toString());
				
				String file = sb.toString();		  				  				  			  
					  	
				String lineAll[] = file.split("\n");
						
				Configuration.superUser1 = lineAll[0];
//				Configuration.superUser2 = lineAll[1];


				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Exception forget change path getSuperUser.property");
				e.printStackTrace();
//				al.setStatus("E");
//				success = "เน�เธกเน�เน�เธ�เธ� file:"+path+filename;
			}
		 return Configuration.superUser1;
	}
	
	public static void setConfigFromFile(){
		System.out.println("setConfigFromFile");
		
		InputStream fileInputStream = null;
		String path1 = pathDEV; //"C:\\file\\";
		String path2 = pathPROD; //"/apps/";
		
		String filename = Configuration.fileConfiguration;
		
		 try {
			 	try{
			 		fileInputStream = new FileInputStream(path2+filename);
			 	}catch(Exception e){
			 		fileInputStream = new FileInputStream(path1+filename);
			 	}
				
//				System.out.println("Total file size to read (in bytes) : "
//						+ fileInputStream.available());

				BufferedReader br = new BufferedReader( new InputStreamReader(fileInputStream, StandardCharsets.UTF_8 ));
				   
				StringBuilder sb = new StringBuilder();
				String OriLine;
				while(( OriLine = br.readLine()) != null ) {
				    sb.append( OriLine );
				    sb.append( '\n' );
				}
				br.close();
				//System.out.println(sb.toString());
				
				  String file = sb.toString();
//				  if(!insertTerminal(file))
//					  System.out.println("insert terminal auto error");				  				  				  			  
					  	
						int s1 = 0;
						int l1 = file.indexOf("\n",s1);
						String lineAll[] = file.split("\n");
						
						for(int i=0;i<lineAll.length;i++){
							String line = lineAll[i];
//							System.out.println(i+":"+line);							
							
							if(line.indexOf("hostnameDB")>-1){
								Configuration.hostnameDB = line.substring(11); 							
							}
							if(line.indexOf("dbnameRB")>-1){
								Configuration.dbnameRB = line.substring(9);
							}
							if(line.indexOf("ldapURI")>-1){
								Configuration.ldapURI = line.substring(8);
							}
							if(line.indexOf("hostnameHr")>-1){
								Configuration.hostnameHr = line.substring(11);
							}
							if(line.indexOf("dbnameHr")>-1){
								Configuration.dbnameHr = line.substring(9);
							}
							if(line.indexOf("hostAD")>-1){
//								System.out.println("HHHAD");
								Configuration.hostAD = line.substring(7);
//								System.out.println(Configuration.hostAD+"HHHAD2"+line.substring(7));
							}
						}
						System.out.println("$$$:"+Configuration.hostnameDB+":"+Configuration.dbnameRB+":"+Configuration.ldapURI+":"+Configuration.hostnameHr+":"+Configuration.dbnameHr);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Exception forget change path fileConfiguration.property");
				e.printStackTrace();
			}

	}
}
