package ws;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import db.ControlSequenceTable;
import db.DatbaseConnection;
import mt.ReqRepository;
import req.ReqStatusQueryReq;
import res.RequestApproverRes;
import res.ReqStatusQueryRes;
import res.ReqStatusUpdateRes;

@Path("requestStatusQuery")
public class ReqStatusQuery {

//	  private static Connection connect = null;
//	  private Statement statement = null;
//	  private PreparedStatement preparedStatement = null;
//	  private ResultSet resultSet = null;
	  
	  ReqStatusQueryReq rq = new ReqStatusQueryReq();
	  ReqStatusQueryRes rs = new ReqStatusQueryRes();
	  
	  private String seq ="";
	  //private String statusReq="N";
	  private String refNo ="";
	  
	  private final Locale lc = new Locale("en","US");
	  private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",lc);
	  
	public ReqStatusQuery() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public ReqStatusQueryRes mainRequestStatusQuery(@Context HttpServletRequest req) {
		
		/*String temp = req;
		System.out.println(req);
		HashMap<String,String> data = new HashMap<String,String>();
		HashMap<Integer,String> key = new HashMap<Integer,String>();
		key.put(0, "ReqRefID");
		key.put(1, "RSARefCode");
		
		int s,l;
		for(int i=0;i<key.size();i++)
		{
			s = temp.indexOf(key.get(i))+key.get(i).length()+3;
			l = temp.indexOf("\"", s);
			data.put(key.get(i), temp.substring(s,l));
		}
		*/
		
		HashMap<String,String> data = new HashMap<String,String>();
		data.put("ReqRefID", (req.getParameter("ReqRefID")==null)?"":req.getParameter("ReqRefID").trim());
		data.put("RSARefCode", (req.getParameter("RSARefCode")==null)?"":req.getParameter("RSARefCode").trim());

		setReqStatusQueryReq(data);
		printtest();
		
		boolean flag = true;
		
		try{
			System.out.println("insertRequestStatusQueryRq");
			flag = insertRequestStatusQueryRq();
			
			System.out.println("setInformation");
			flag = setInformation(rq.getReqRefID());
			
			System.out.println("updateReqRepository");
			flag = updateReqRepository();
			
		 }catch(Exception ex){
			 rs.setErrorMsg("mainReqStatusQuery:"+ex.getMessage());
		 }finally{
			 return returnReqStatusQueryRes();
		 }
		
		
	}
	

	public void setReqStatusQueryReq(HashMap<String,String> data)
	{	
		rq.setReqRefID(data.get("ReqRefID"));
		rq.setRSARefCode(data.get("RSARefCode"));
	}
	
	public void printtest(){
		System.out.println(rq.getReqRefID());
		System.out.println(rq.getRSARefCode());
	}
	
	@SuppressWarnings("finally")
	public boolean insertRequestStatusQueryRq()  {
		
		Date curDate = new Date();
		
		
			seq = ControlSequenceTable.getSeqRequestStatusQuery();		
		if (seq == null) {
			SimpleDateFormat format2 = new SimpleDateFormat("YYMMdHms",lc);
			String date = format2.format(curDate);
			
			rs.setRefNo("ERR"+date);
			rs.setErrorMsg("Exception: get seq statusQuery error !!!");
			return false;
		}
		
		boolean flagResult = true;
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try{
		  // PreparedStatements can use variables and are more efficient
							
			PreparedStatement preparedStatement = connect
	          .prepareStatement("insert into  tbldt_wsreqstatusqueryrq values ( ?, ?, ?, ?)");
	      										

	      preparedStatement.setString(1, seq);
	      preparedStatement.setString(2, sdf.format(curDate));
	      preparedStatement.setString(3, rq.getReqRefID());
	      preparedStatement.setString(4, rq.getRSARefCode());

	      
	      preparedStatement.executeUpdate();
	      
			flagResult = true;
		}catch(Exception ex){
			rs.setErrorMsg("Exception:"+ex.getMessage());
			flagResult =  false;
		}
		finally{
			try {	connect.close();} catch (SQLException e) {	}
			
			return flagResult;
		}

	}
	
	public boolean setInformation(String ref){
		
		//String seq;
		try{
			
			ReqRepository rpstr = new ReqRepository();
			rpstr.setRepsitoryByRef(ref);
			
			if(rpstr.getCode().equals("000")){
				rs.setRefNo(rpstr.getRequestID());
				rs.setRequestor(rpstr.getRequestor());
				rs.setEmpID(rpstr.getEmpID());
			    rs.setStatusCode(rpstr.getStatus()); // N,A,C,F,R
			    rs.setReqSumbitDate(rpstr.getReqSubmitDate());		    
			    rs.setErrorMsg("000-OK");				
			}else{
				rs.setErrorMsg("900:"+ref+": not found");
			}
		    
		    
			} catch(Exception ex)
			{
				rs.setErrorMsg("800-Request Error");
				System.out.println("setInformation:"+ex.toString());
			}
		
		return true;
	}
	
	@SuppressWarnings("finally")
	public boolean updateReqRepository() {
		
		Date curDate = new Date();
		
		boolean flagResult = true;
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try{
		  // PreparedStatements can use variables and are more efficient
							
			PreparedStatement preparedStatement = connect
	          .prepareStatement("UPDATE tbldt_reqrepository SET RSAReferenceID = ? ,LastChange = ?"
	          		+ " WHERE RequestID like ?");
	      
	      preparedStatement.setString(1, rq.getRSARefCode());
	      preparedStatement.setString(2, sdf.format(curDate));
	      preparedStatement.setString(3, rq.getReqRefID());
	      
	      preparedStatement.executeUpdate();
	      //already check in setinformation 

			flagResult = true;
		}catch(Exception ex){
			rs.setErrorMsg("Exception:"+ex.getMessage());
			flagResult =  false;
		}
		finally{			
			try {	connect.close();} catch (SQLException e) {	}			
			return flagResult;
		}
	}
		
	@SuppressWarnings("finally")
	public ReqStatusQueryRes returnReqStatusQueryRes() {
		
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try{
			  Date curDate = new Date();
			  // PreparedStatements can use variables and are more efficient
			  				
			  PreparedStatement preparedStatement = connect
		          .prepareStatement("insert into  tbldt_wsreqstatusqueryrs values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		      preparedStatement.setString(1, seq);
		      preparedStatement.setString(2, sdf.format(curDate));
		      preparedStatement.setString(3, rs.getRefNo());
		      preparedStatement.setString(4, rs.getStatusCode());
		      preparedStatement.setString(5, rs.getReqSumbitDate());
		      preparedStatement.setString(6, rs.getEmpID());
		      preparedStatement.setString(7, rs.getRequestor());
		      preparedStatement.setString(8, rs.getLastChangeDate());
		      preparedStatement.setString(9, rs.getErrorMsg());
		      
		      preparedStatement.executeUpdate();
		      
		}catch(Exception ex){
				rs.setErrorMsg("returnRequestApproverRes:"+ex.getMessage());
		}finally{
				try {	connect.close();} catch (SQLException e) {	}
				return rs;
		}
	}
	
}
