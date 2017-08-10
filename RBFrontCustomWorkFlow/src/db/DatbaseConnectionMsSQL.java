package db;

import java.sql.DriverManager;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import java.sql.Connection;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64; 
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DatbaseConnectionMsSQL {
	private static Connection con = null;
	
	private static String keyEnscrypt = Configuration.keyEnscrypt;
	
	private static String flagCon = Configuration.TYPE;
	
	public DatbaseConnectionMsSQL() {

	}
	
	public static Connection getConnectionMsSQL()
	{
		if(flagCon.equals("")){
			flagCon = Configuration.getTYPE();
		}
		
		try{
//			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String userName = "iamsit";
			String password = "ZO1DmviAXTjwo1RoZxfCNFtWWMrD//I8";
			
//			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
//			encryptor.setPassword(keyEnscrypt);
//			String decrypted = encryptor.decrypt(password);

			String url = "";//"jdbc:sqlserver://10.21.16.118:1440;databaseName=HRMS_PROD_NEW";
			
			if("".equals(SEADatabase.getUserRB())||"".equals(SEADatabase.getPassRB())||
			   "".equals(SEADatabase.getUserHR())||"".equals(SEADatabase.getPassHR())){
				SEADatabase.setUserPass();
			}
//			System.out.println("BBB"+my.getUserRB()+my.getPassRB());
			String user = SEADatabase.getUserHR();
			String pass = SEADatabase.getPassHR();
//			System.out.println("AAA"+user+pass);
			userName = user;
			password = pass;
			
			String host = "";
			String dbName = "";
			
			if(flagCon.equals("DEV") ){
				//Class.forName("com.mysql.jdbc.Driver");
				//con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tempscbmssql?useSSL=false&user="+user+"&password="+pass+"&useUnicode=true&characterEncoding=UTF-8");
				
				String driverName = "com.mysql.jdbc.Driver";
			    Class.forName(driverName);

			    String serverName = "localhost";
			    String mydatabase = "rbfworkflow";
			    url = "jdbc:mysql://" + serverName + "/" + mydatabase; 

			    String username = "root";
			    password = "";
			    Connection connection = DriverManager.getConnection(url, username, password);
			    con = connection;
			}else if(flagCon.equals("SIT") ){
//				System.out.println("MsSQL");
				
				host = Configuration.hostnameHr;
				dbName = Configuration.dbnameHr;
				url = "jdbc:sqlserver://"+host+";databaseName="+dbName;
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				con = DriverManager.getConnection(url, userName, password);
				
//				Class.forName("com.mysql.jdbc.Driver");
//				con = DriverManager.getConnection("jdbc:mysql://10.21.18.48:3310/rbfworkflow?useSSL=false&user=rbfrontusr&password="+decrypted+"&useUnicode=true&characterEncoding=UTF-8");
			}else if(flagCon.equals("UAT") ){
				host = Configuration.hostnameHr;
				dbName = Configuration.dbnameHr;
				url = "jdbc:sqlserver://"+host+";databaseName="+dbName;
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				con = DriverManager.getConnection(url, userName, password);
				
			}else if(flagCon.equals("PROD") ){
				host = Configuration.hostnameHr;
				dbName = Configuration.dbnameHr;
				url = "jdbc:sqlserver://"+host+";databaseName="+dbName;
//				System.out.println("HRMShost+dbname:"+host+dbName);
//				System.out.println("url"+url);
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				con = DriverManager.getConnection(url, userName, password);
			}
			
			
		} catch(Exception ex)
		{
			System.out.println("CON:"+ex.toString());
//			flagCon ="DEV";
//			con = getConnectionMySQL();
		}
		
		return con;
	}
	     
	
	    public static void main(String[] args) 
	    {
	        final String secretKey = "scb@baycoms@rsa@HR";
	        //final String secretKey = "scb@baycoms@rsa";
	         
	        String originalString = "lk2b9i63";
	        String encryptedString = SEADatabase.encrypt(originalString, secretKey) ;
	        String decryptedString = SEADatabase.decrypt(encryptedString, secretKey) ;
	         
	        System.out.println(originalString);
	        System.out.println(encryptedString);
	        System.out.println(decryptedString);
	    }
}
