package db;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.Context;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.DirContext;
import javax.naming.directory.Attributes;
import javax.naming.NamingException;


public class LDAPConnection {

   // final String ldapAdServer = "ldap://seahawk.scbcorpiam.local:389";
    //final String ldapAdServer = "ldap://10.21.17.81:389";
    // String ldapSearchBase = "ou=users,DC=scbcorpiam,DC=local";
    
    //final String ldapUsername = "iambaycoms";
    //final String ldapPassword = "8;k,x]vf4yp";
    
    //final String ldapAccountToLookup = "myOtherLdapUsername";
    
	public LDAPConnection() {
		// TODO Auto-generated constructor stub
	}

	/*public boolean connectLAP(String user,String pass){
		
		
		Map<String, String> env1 = new HashMap<String, String>();
		env1.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env1.put(Context.PROVIDER_URL, "ldap://10.21.17.81:389/dc=scbcorpiam,dc=local");
		env1.put(Context.SECURITY_AUTHENTICATION, "simple");
		env1.put(Context.SECURITY_PRINCIPAL, "cn=iambaycoms,ou=Users"); // replace with user DN
		env1.put(Context.SECURITY_CREDENTIALS, "8;k,x]vf4yp");
		
		//env1.put(Context.SECURITY_PRINCIPAL, "cn="+user+",ou=Users"); // replace with user DN
		//env1.put(Context.SECURITY_CREDENTIALS, pass);
		System.out.println(":"+"cn="+user+",ou=Users"+":");
		System.out.println(":"+pass+":");
		DirContext ctx = null;
		boolean flag = true;
		try {
		   ctx = new InitialDirContext();
		} catch (NamingException e) {
			flag = false;
		   System.out.println("exception initial LDAP:"+e.getMessage());
		}
		try {
		   SearchControls controls = new SearchControls();
		   controls.setSearchScope( SearchControls.SUBTREE_SCOPE);
		   ctx.search( "", "(objectclass=person)", controls);
		   // no need to process the results
		} catch (NameNotFoundException e) {
			flag = false;
			   System.out.println("exception initial LDAP NameNotFoundException :"+e.getMessage());
		} catch (NamingException e) {
			flag = false;
			   System.out.println("exception initial LDAP NamingException :"+e.getMessage());
		} finally {
		   // close ctx or do Java 7 try-with-resources http://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html			
		}
		return flag;
	}
	*/
	
	//private final static String ldapURI = "ldaps://ldap.server.com/dc=ldap,dc=server,dc=com";
	private static String ldapURI = "";//ldaps://10.21.17.81:636/dc=scbcorpiam,dc=local";
	//private final static String ldapURI = "ldap://10.21.17.81:389/DC=scbcorpiam,DC=local";
	
	private final static String contextFactory = "com.sun.jndi.ldap.LdapCtxFactory";
	
	private static boolean testBind (String dn, String password) throws Exception {
		Hashtable<String,String> env = new Hashtable <String,String>();
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, dn);
		env.put(Context.SECURITY_CREDENTIALS, password);

		try {
			ldapContext(env);
		}
		catch (javax.naming.AuthenticationException e) {
			return false;
		}
		return true;
	}

	private static DirContext ldapContext (Hashtable <String,String>env) throws Exception {
		System.out.println("ldapURI:"+ldapURI);
		env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
		env.put(Context.PROVIDER_URL, ldapURI);
		DirContext ctx = new InitialDirContext(env);
		return ctx;
	}
	
	private static DirContext ldapContext () throws Exception {
		Hashtable<String,String> env = new Hashtable <String,String>();
		return ldapContext(env);
	}
	
	private static String getUid (String user) throws Exception {
		DirContext ctx = ldapContext();

		String filter = "(uid=" + user + ")";
		SearchControls ctrl = new SearchControls();
		ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
		NamingEnumeration answer = ctx.search("", filter, ctrl);

		String dn;
		if (answer.hasMore()) {
			SearchResult result = (SearchResult) answer.next();
			dn = result.getNameInNamespace();
		}
		else {
			dn = null;
		}
		answer.close();
		return dn;
		//return ctx.toString();
	}
	
	public boolean getLDAP(String userParam,String passParam) throws Exception {
		
		if(Configuration.TYPE.equals("")){
			Configuration.TYPE = Configuration.getTYPE();
		}
		
		String user = userParam; //"iambaycoms";
		String password = passParam; //"8;k,x]vf4yp";
//		System.out.println("Start:"+user);
		//String dn = user+"@scbcorpiam.local";//getUid(user);
		String dn = user+"@"+Configuration.hostAD;
//		System.out.println("dn:"+dn);
		
		if(Configuration.TYPE.equals("DEV")){
			ldapURI = "not use";
		}else if(Configuration.TYPE.equals("SIT")){
			ldapURI = Configuration.ldapURI;
		}else if(Configuration.TYPE.equals("UAT")){
			ldapURI = Configuration.ldapURI;
		}else if(Configuration.TYPE.equals("PROD")){
			ldapURI = Configuration.ldapURI;
		}
		
		System.out.println("Configuration.TYPE:"+Configuration.TYPE+":"+ldapURI);
		if (dn != null) {
			/* Found user - test password */
			if ( testBind( dn, password ) ) {
				System.out.println( "user dn '" + dn + "' authentication succeeded" );
				return true;
			}
			else {
				System.out.println( "user dn '" + dn + "' authentication failed" );
				return false;
			}
		}
		else {
			System.out.println( "user '" + user + "' not found" );
			return false;
		}
		
	}
	
	public static void main(String[] args) throws NamingException{
		
		/*LdapContext ctx = new InitialLdapContext();
        
		LDAPConnecction ldap = new LDAPConnecction();
        
        //1) lookup the ldap account
        SearchResult srLdapUser = ldap.findAccountByAccountName(ctx, ldap.ldapSearchBase, ldap.ldapAccountToLookup);
        */
		//
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://10.21.17.81:389");
		env.put(Context.SECURITY_AUTHENTICATION,"simple");
		env.put(Context.SECURITY_PRINCIPAL,"cn=iambaycoms"); // specify the username
		env.put(Context.SECURITY_CREDENTIALS,"8;k,x]vf4yp");           // specify the password
		DirContext ctx = new InitialDirContext(env);

	}
	
}
