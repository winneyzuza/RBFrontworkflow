package db;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64; 
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import mt.EmployeeInfo;
import mt.NormalFunction;
import validate.CheckFile;

public class SEADatabase {
	
    private static SecretKeySpec secretKey;
    private static byte[] key;
    
    private final static String secretKeyRB = "scb@baycoms@rsa";
    private final static String secretKeyHR = "scb@baycoms@rsa@HR";
    
    private static String userRB = "";
    private static String passRB = "";
    private static String userHR = "";
    private static String passHR = "";
    
	public static String getPassRB() {
		return passRB;
	}

	public static String getPassHR() {
		return passHR;
	}

	public static String getUserRB() {
		return userRB;
	}

	public static String getUserHR() {
		return userHR;
	}

	public SEADatabase() {
		// TODO Auto-generated constructor stub
	}
    
    public static void setKey(String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    public static String encrypt(String strToEncrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
 
    public static String decrypt(String strToDecrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
    
    public static void setUserPass(){
//		System.out.println("Start Update user pass DB ");
//		al.setModule("AutoUploadUserlist3");		

		InputStream fileInputStream = null;
		String path = "";
		if(Configuration.TYPE.equals("DEV")){
			path = Configuration.pathDEV;
		}else if(Configuration.TYPE.equals("SIT")){
			path = Configuration.pathPROD;
		}else if(Configuration.TYPE.equals("UAT")){
			path = Configuration.pathPROD;
		}else if(Configuration.TYPE.equals("PROD")){
			path = Configuration.pathPROD;
		}		
		
		String filename = Configuration.filenameDBConfig;//"usrDB.property";
		System.out.println("setUserPass:"+path+filename);
        try {
        	fileInputStream = new FileInputStream(path+filename);
			
//			System.out.println("Total file size to read (in bytes) : "
//					+ fileInputStream.available());

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
//			  if(!insertTerminal(file))
//				  System.out.println("insert terminal auto error");
			  try{					  				  				  			  
				  	
					int s1 = 0;
					int l1 = file.indexOf("\n",s1);
					String lineAll[] = file.split("\n");
					
					for(int i=0;i<lineAll.length;i++){
						String line = lineAll[i];
//						System.out.println(i+":"+line);
						
						int s = 0;
						int l = 0;
						
						if(line.indexOf("userRB")>-1){
							userRB = decrypt(line.substring(6,line.length()),secretKeyRB);							
						}
						if(line.indexOf("passRB")>-1){
							passRB = decrypt(line.substring(6,line.length()),secretKeyRB);
						}
						if(line.indexOf("userHR")>-1){
							userHR = decrypt(line.substring(6,line.length()),secretKeyHR);
						}
						if(line.indexOf("passHR")>-1){
							passHR = decrypt(line.substring(6,line.length()),secretKeyHR);
						}
													
						////////////////////////////////////
							
						      
						}// end for
									
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					
					e.printStackTrace();
				}finally{
							
				}

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			al.setStatus("E");
//			success = "ไม่่พบ file:"+path+filename;
		}
		
//        al.setDesc(success);
//        al.insertAuditLog();
//		System.out.println("success:"+success);

	}
    
    public void writeFileDB(String userRB,String passRB,String userHR,String passHR){
		String path = "";//"C:\\file\\"; // "/apps/";
    	
		if(Configuration.TYPE.equals("DEV")){
			path = Configuration.pathDEV;
		}else if(Configuration.TYPE.equals("SIT")){
			path = Configuration.pathPROD;
		}else if(Configuration.TYPE.equals("UAT")){
			path = Configuration.pathPROD;
		}else if(Configuration.TYPE.equals("PROD")){
			path = Configuration.pathPROD;
		}
    	
		String filename = Configuration.filenameDBConfig;//"usrDB.property";
		
		try {
			System.out.println("pathUpdateDB:"+path+filename);
			String file = path+filename;
			FileWriter writer = new FileWriter(file, false);
			String info = "";

//			userRB/Z3bh0ad4mZQFmpyqk7cyQ==
//					passRBAJv8tG/t1BW78/+dC3OddA==
//					userHRO25Jrct1Q/zAjXW5iwSj/w==
//					passHR0TUpyzFGDMzIIiLhh8sJNA==
			
			info = info+Configuration.TYPE+"\n";		
			info = info+"userRB"+encrypt(userRB,this.secretKeyRB)+"\n";
			info = info+"passRB"+encrypt(passRB,this.secretKeyRB)+"\n";
			info = info+"userHR"+encrypt(userHR,this.secretKeyHR)+"\n";
			info = info+"passHR"+encrypt(passHR,this.secretKeyHR)+"\n";
			
			writer.write(info);
			writer.flush();
	        writer.close();		
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
					
		}
		
	}
    
    public static void main(String[] args) 
    {
        final String secretKey = "scb@baycoms@rsa@HR";
        //final String secretKey = "scb@baycoms@rsa";
         
//        String originalString = "lk2b9i63";
//        String encryptedString = SEADatabase.encrypt(originalString, secretKey) ;
//        String decryptedString = SEADatabase.decrypt(encryptedString, secretKey) ;
//         
//        System.out.println(originalString);
//        System.out.println(encryptedString);
//        System.out.println(decryptedString);
    	SEADatabase a = new SEADatabase();
    	a.setUserPass();
    	
    }
	
}
