package db;

import java.sql.DriverManager;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.sql.Connection;

import db.SEADatabase;

public class DatbaseConnection {
	private static Connection con = null;
	
//	private static String keyEnscrypt = Configuration.keyEnscrypt;
	
	private static String flagCon = Configuration.TYPE;
	
	public DatbaseConnection() {

	}
	
	public static Connection getConnectionMySQL()
	{
		if(flagCon.equals("")){
			flagCon = Configuration.getTYPE();
		}
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
	//		String password = "ZO1DmviAXTjwo1RoZxfCNFtWWMrD//I8";
	//		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
	//		encryptor.setPassword(keyEnscrypt);
	//		String decrypted = encryptor.decrypt(password);
	//		//System.out.println("Pass:"+decrypted);
			
			if("".equals(SEADatabase.getUserRB())||"".equals(SEADatabase.getPassRB())||
			   "".equals(SEADatabase.getUserHR())||"".equals(SEADatabase.getPassHR())){
				SEADatabase.setUserPass();
			}
			
//			System.out.println("MyBBB"+my.getUserRB()+my.getPassRB());
			String user = SEADatabase.getUserRB();
			String pass = SEADatabase.getPassRB();
	
//			System.out.println("MyAAA"+user+pass);
			
			String host ="";
			String dbname ="";
			if(flagCon.equals("DEV")){
				host = Configuration.hostnameDB;
				con = DriverManager.getConnection("jdbc:mysql://"+host+"/scb?useSSL=false&user="+user+"&password="+pass+"&useUnicode=true&characterEncoding=UTF-8");	
			}else if(flagCon.equals("SIT")){
				host = Configuration.hostnameDB;
				dbname = Configuration.dbnameRB;
				con = DriverManager.getConnection("jdbc:mysql://"+host+"/"+dbname+"?useSSL=false&user="+user+"&password="+pass+"&useUnicode=true&characterEncoding=UTF-8");
			}else if(flagCon.equals("UAT")){
				host = Configuration.hostnameDB;
				dbname = Configuration.dbnameRB;
				con = DriverManager.getConnection("jdbc:mysql://"+host+"/"+dbname+"?useSSL=false&user="+user+"&password="+pass+"&useUnicode=true&characterEncoding=UTF-8");
			}else if(flagCon.equals("PROD")){
				host = Configuration.hostnameDB;
				dbname = Configuration.dbnameRB;
//				System.out.println("urlCondb:"+"jdbc:mysql://"+host+"/"+dbname+"?useSSL=false&user="+user+"&password="+pass+"&useUnicode=true&characterEncoding=UTF-8");
				con = DriverManager.getConnection("jdbc:mysql://"+host+"/"+dbname+"?useSSL=false&user="+user+"&password="+pass+"&useUnicode=true&characterEncoding=UTF-8");
			}
			
		
		} catch(Exception ex)
		{
			System.out.println("CON:"+ex.toString());
		}
		
		return con;
	}
	
	
	public static void main(String [ ] args){
		
		String pass = "lk2b9i63";
		
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
//		encryptor.setPassword(keyEnscrypt);
		String encrypted= encryptor.encrypt(pass);
		System.out.println("enC:"+encrypted);
		
		String decrypted = encryptor.decrypt(encrypted);
		System.out.println("deC:"+decrypted);
		
	}
	
	
	
}
