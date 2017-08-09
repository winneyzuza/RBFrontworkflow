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
import req.RequestApproverReq;
import req.ReqStatusQueryReq;
import req.ReqStatusUpdateReq;
import res.RequestApproverRes;
import res.ReqStatusQueryRes;
import res.ReqStatusUpdateRes;

@Path("requestStatusUpdate")
public class ReqStatusUpdate {

//	  private static Connection connect = null;
//	  private Statement statement = null;
//	  private PreparedStatement preparedStatement = null;
	  private ResultSet resultSet = null;
	  
	  ReqStatusUpdateReq rq = new ReqStatusUpdateReq();
	  ReqStatusUpdateRes rs = new ReqStatusUpdateRes();
	  
	  private String seq ="";
	  //private String statusReq="N";
	  private String refNo ="";
	  
	  private final Locale lc = new Locale("en","US");
	  private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",lc);
	  
	public ReqStatusUpdate() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public ReqStatusUpdateRes mainrequestStatusUpdate(@Context HttpServletRequest req){
		

		/*String temp = req;
		System.out.println(req);
		HashMap<String,String> data = new HashMap<String,String>();
		HashMap<Integer,String> key = new HashMap<Integer,String>();
		key.put(0, "ReqRefID");
		key.put(1, "CurrStatus");
		key.put(2, "NewStatus");
		key.put(3, "ApproverID");
		
		
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
		data.put("CurrStatus", (req.getParameter("CurrStatus")==null)?"":req.getParameter("CurrStatus").trim());
		data.put("NewStatus", (req.getParameter("NewStatus")==null)?"":req.getParameter("NewStatus").trim());
		data.put("ApproverID", (req.getParameter("ApproverID")==null)?"":req.getParameter("ApproverID").trim());
		
		setReqStatusUpdateReq(data);
		printtest();
		
		boolean flag = true;
		
		try{
			System.out.println("insertRequestStatusQueryRq");
			flag = insertReqStatusUpdateRq();		
//			if(!flag)
//				return returnReqStatusUpdateRes();
			
			System.out.println("updateReqRepository");
			flag = updateReqRepository(rq.getReqRefID());
//			if(!flag)
//				return returnReqStatusUpdateRes();
//			System.out.println("BBBB:"+flag);
			
		 }catch(Exception ex){
			 rs.setResultCode("mainReqStatusQuery:"+ex.getMessage());
		 }finally{
			 return returnReqStatusUpdateRes();
		 }
		
	}

	
	public void setReqStatusUpdateReq(HashMap<String,String> data)
	{	
		rq.setReqRefID(data.get("ReqRefID"));
		rq.setCurrStatus(data.get("CurrStatus"));
		rq.setNewStatus(data.get("NewStatus"));
		rq.setApproverID(data.get("ApproverID"));
	}
	
	public void printtest(){
		System.out.println(rq.getReqRefID());
		System.out.println(rq.getCurrStatus());
		System.out.println(rq.getNewStatus());
		System.out.println(rq.getApproverID());
	}
	
	@SuppressWarnings("finally")
	public boolean insertReqStatusUpdateRq() {
		
		Date curDate = new Date();
		
		
			seq = ControlSequenceTable.getSeqRequestStatusUpdate();		
		if (seq == null) {
			
			rs.setResultCode("999-ERROR exception");
			return false;
		}
		
		boolean flagResult = true;			
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try{
		  // PreparedStatements can use variables and are more efficient
				
			PreparedStatement preparedStatement = connect
	          .prepareStatement("insert into  tbldt_wsreqstatusupdaterq values ( ?, ?, ?, ?, ?, ?)");
	      										

	      preparedStatement.setString(1, seq);
	      preparedStatement.setString(2, sdf.format(curDate));
	      preparedStatement.setString(3, rq.getReqRefID());
	      preparedStatement.setString(4, rq.getCurrStatus());
	      preparedStatement.setString(5, rq.getNewStatus());
	      preparedStatement.setString(6, rq.getApproverID());

	      
	      preparedStatement.executeUpdate();
	      rs.setResultCode("000-OK");
	      
	      flagResult = true;
		}catch(Exception ex){
			rs.setResultCode("999-ERROR exception");
			flagResult =  false;
		}
		finally{
			try {	connect.close();} catch (SQLException e) {	}
			
			return flagResult;
		}

	}
	
	
	@SuppressWarnings("finally")
	public boolean updateReqRepository(String ref){
		
		Date curDate = new Date();
		
		boolean flagResult = true;
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try{
		  // PreparedStatements can use variables and are more efficient
							
			PreparedStatement preparedStatement = connect
	          .prepareStatement("UPDATE tbldt_reqrepository SET Status = ? ,LastChange = ?"
	          		+ " WHERE RequestID like ?");
	      
	      preparedStatement.setString(1, rq.getNewStatus());
	      preparedStatement.setString(2, sdf.format(curDate));
	      preparedStatement.setString(3, ref);
	      
	      if(preparedStatement.executeUpdate()<1){
	    	  rs.setResultCode("900:"+ref+": not found");
	      };

			flagResult = true;
		}catch(Exception ex){
			rs.setResultCode("999-ERROR exception");
			System.out.println("updateReqRepository:"+ex.getMessage());
			flagResult =  false;
		}
		finally{			
			try {	connect.close();} catch (SQLException e) {	}			
			return flagResult;
		}
	}
	
	
	@SuppressWarnings("finally")
	public ReqStatusUpdateRes returnReqStatusUpdateRes(){
		
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try{
			  Date curDate = new Date();
			  // PreparedStatements can use variables and are more efficient
			  				
			  PreparedStatement preparedStatement = connect
		          .prepareStatement("insert into  tbldt_wsreqstatusupdaters values ( ?, ?, ?)");

		      preparedStatement.setString(1, seq);
		      preparedStatement.setString(2, sdf.format(curDate));
		      preparedStatement.setString(3, rs.getResultCode());

		      preparedStatement.executeUpdate();
		      
			}catch(Exception ex){
				rs.setResultCode("999-ERROR exception");
				System.out.println("returnReqStatusUpdateRes:"+ex.getMessage());
			}finally{

				try {	connect.close();} catch (SQLException e) {	}
				return rs;
			}
	}
	
}
