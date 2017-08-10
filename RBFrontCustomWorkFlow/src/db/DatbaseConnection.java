package db;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import com.mysql.jdbc.Statement;

import java.sql.Connection;

import db.SEADatabase;

public class DatbaseConnection {
	private static Connection con = null;
	
//	private static String keyEnscrypt = Configuration.keyEnscrypt;
	
	private static String flagCon = Configuration.TYPE;
	
	public DatbaseConnection() {

	}
	
	public static Connection localConnect() throws SQLException, ClassNotFoundException {
		String driverName = "com.mysql.jdbc.Driver";
	    Class.forName(driverName);

	    String serverName = "localhost";
	    String mydatabase = "rbfworkflow";
	    String url = "jdbc:mysql://" + serverName + "/" + mydatabase; 

	    String username = "root";
	    String password = "";
	    Connection connection = DriverManager.getConnection(url, username, password);
	    
	    return connection;
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
			System.out.println("flagCon " + flagCon);
			if(flagCon.equals("DEV")){
				//host = Configuration.hostnameDB;
				//con = DriverManager.getConnection("jdbc:mysql://"+host+"/scb?useSSL=false&user="+user+"&password="+pass+"&useUnicode=true&characterEncoding=UTF-8");
				String driverName = "com.mysql.jdbc.Driver";
			    Class.forName(driverName);

			    String serverName = "localhost";
			    String mydatabase = "rbfworkflow";
			    String url = "jdbc:mysql://" + serverName + "/" + mydatabase; 

			    String username = "root";
			    String password = "";
			    Connection connection = DriverManager.getConnection(url, username, password);
			    con = connection;
			}else if(flagCon.equals("SIT")){
				/*host = Configuration.hostnameDB;
				dbname = Configuration.dbnameRB;
				con = DriverManager.getConnection("jdbc:mysql://"+host+"/"+dbname+"?useSSL=false&user="+user+"&password="+pass+"&useUnicode=true&characterEncoding=UTF-8");*/
				String driverName = "com.mysql.jdbc.Driver";
			    Class.forName(driverName);

			    String serverName = "localhost";
			    String mydatabase = "rbfworkflow";
			    String url = "jdbc:mysql://" + serverName + "/" + mydatabase; 

			    String username = "root";
			    String password = "";
			    Connection connection = DriverManager.getConnection(url, username, password);
			    con = connection;
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
	
	
	public static void main(String [ ] args) throws ClassNotFoundException, SQLException{
		/*
		String pass = "lk2b9i63";
		
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
//		encryptor.setPassword(keyEnscrypt);
		String encrypted= encryptor.encrypt(pass);
		System.out.println("enC:"+encrypted);
		
		String decrypted = encryptor.decrypt(encrypted);
		System.out.println("deC:"+decrypted);
		*/
		
		Connection connect = DatbaseConnection.localConnect();
		try {
			String module="";
			PreparedStatement preparedStatement = connect
			          .prepareStatement("select * from tblmt_authen a where a.EmpID like ? ");
			preparedStatement.setString(1, "iambaycoms");
			ResultSet resultSet = preparedStatement.executeQuery();
			
			//System.out.println("checkBranch result: "+resultSet.getInt(1));
			while( resultSet.next()){
				module = module+resultSet.getString("Module");
			}
			System.out.println("Module:"+module);			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
	}
	
	
	
}
