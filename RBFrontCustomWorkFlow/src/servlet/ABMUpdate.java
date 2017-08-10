package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Session;

import db.Configuration;
import db.DatbaseConnection;
import db.DatbaseConnectionMsSQL;

public class ABMUpdate extends HttpServlet{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String msg = ""; 
public ABMUpdate() {
	// TODO Auto-generated constructor stub
	msg = "";
	System.out.println("ABMUpdate class initializing");
	System.out.flush();
//	Session session; 
}

protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException 
{ 
	String msg2 = "";
	System.out.println("ABMUpdate servlet initializing");
	boolean abm_result = false; 
	Date curDate = new Date();
	Locale lc = new Locale("en","US");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",lc);
	
	  	Configuration.setConfig();
	  	ABMUpdate abm = new ABMUpdate();
		abm_result = abm.update();
		msg2 = abm.msg;
		if (abm_result) {
			msg2 = "ABM Update Success with " + msg2;
		} else {
			msg2 = "ABM Update Fail with " + msg2;
		}
	Date curDate2 = new Date();
	System.out.println("ABMUpdate servlet  >>>>>>>>>>>>>> doGet END");
	System.out.println("start:"+sdf.format(curDate)+" end:"+sdf.format(curDate2));
    request.setAttribute("message", msg2); // This will be available as ${message}
    request.getRequestDispatcher("/jsp/dispmsg.jsp").forward(request, response);
}  

public boolean update() {
	String flagCon = Configuration.TYPE;
	boolean debugging = true;
	
	if(flagCon.equals("")){
		flagCon = Configuration.getTYPE();
	}
		
	Connection msconnect = (!Configuration.TYPE.equals("DEV")) ? DatbaseConnectionMsSQL.getConnectionMsSQL() : DatbaseConnection.getConnectionMySQL();
	Connection connect = DatbaseConnection.getConnectionMySQL();
	
	PreparedStatement mspreparedStatement = null;
	PreparedStatement preparedStatement = null;
	PreparedStatement preparedStatement2 = null;
	ResultSet msresultSet = null;
	boolean first = true;
	boolean success = true;
	int count = 0;
	int fail = 0;
	if (msconnect != null && connect != null) {
		try {
			mspreparedStatement = msconnect.prepareStatement("select distinct oc_code from IAM where job_title_en like '%Assistant Branch Manager%'");
			msresultSet = mspreparedStatement.executeQuery();
			if (debugging) System.out.println("ABMUpdate class - Feteched Row from IAM view");
		} catch(SQLException se) {
			System.out.println("ABMUpdate class - Exception while query ABM from IAM view"+se.getMessage());
			msg += "<br>'HRMS IAM view query error'<br>";
		}
		// start flush "N" into MySQL
		// start update branch info in MySQL
		try {
			preparedStatement = connect.prepareStatement("update tblmt_branchinfo set ABM = ? where OcCode = ?");
			//
			while(msresultSet.next()) {
				count++;
				if (first) {
					try {
						preparedStatement2 = connect.prepareStatement("update tblmt_branchinfo set ABM = 'N'");
						success = success & (preparedStatement2.executeUpdate() > 0);
					} catch(SQLException se) {
						System.out.println("ABMUpdate class - Exception while update ABM field to NO for all rows : "+se.getMessage());
						fail++;
					}
					first = false;
				}
				preparedStatement.setString(1, "Y");
				preparedStatement.setString(2, msresultSet.getString(1));
				success = success & (preparedStatement.executeUpdate() > 0);
				if (debugging) System.out.println("ABMUpdate class - Found ABM in OC=" + msresultSet.getString(1));
			}
			msg += "<br>Updated ABM column in " + Integer.toString(count) + " Branches<br>";
			//System.out.println("Message = " + msg);
		} catch (SQLException se) {
			System.out.println("ABMUpdate class - Exception while updating ABM in tblmt_branchinfo");
			success = false;
		}
		try {
			msconnect.close();
			connect.close();
		} catch (SQLException se) {
			System.out.println("ABMUpdate class - Exception while closing connection");
			success = false;
		}
	} else {
		System.out.println("ABMUpdate class - Error in getting connection");
		success = false;
	}
	if (fail > 0) msg += "Found " + Integer.toString(fail) + " Fails in update Branch database<br>";
	System.out.println("Message = " + msg);
	return success;
}

}