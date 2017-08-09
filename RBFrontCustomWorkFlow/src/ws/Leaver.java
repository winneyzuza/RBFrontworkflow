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
import mt.EmployeeInfo;
import req.LeaverReq;
import res.LeaverRes;

@Path("leaver")
public class Leaver {

//	  private static Connection connect = null;
//	  private Statement statement = null;
//	  private PreparedStatement preparedStatement = null;
//	  private ResultSet resultSet = null;
	
	  private LeaverRes rs = new LeaverRes();
	  private LeaverReq rq = new LeaverReq();
	  
	  private String seq ="";
	  private String statusReq="N";
	  private String refNo ="";
	  
	  private final Locale lc = new Locale("en","US");
	  private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",lc);

	  
	public Leaver() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public LeaverRes joiner(@Context HttpServletRequest req) throws SQLException{
		
		/*String temp = req;
		System.out.println(req);
		HashMap<String,String> data = new HashMap<String,String>();
		HashMap<Integer,String> key = new HashMap<Integer,String>();
		key.put(0, "EmpID");
		key.put(1, "EffectiveStartDate");
		key.put(2, "Branch");
		
		int s,l;
		for(int i=0;i<key.size();i++)
		{
			s = temp.indexOf(key.get(i))+key.get(i).length()+3;
			l = temp.indexOf("\"", s);
			data.put(key.get(i), temp.substring(s,l));
		}
		*/
		
		HashMap<String,String> data = new HashMap<String,String>();
		data.put("EmpID", (req.getParameter("EmpID")==null)?"":req.getParameter("EmpID").trim());
		data.put("EffectiveStartDate", (req.getParameter("EffectiveStartDate")==null)?"":req.getParameter("EffectiveStartDate").trim());
		data.put("Branch", (req.getParameter("Branch")==null)?"":req.getParameter("Branch").trim());
		
		setLeaverReq(data);
		printTest();
		
		boolean flag = true;
		
		try{
			System.out.println("insertJoinerRq");
			flag = insertLeaverRq();
			
			System.out.println("getStatusReq");
			flag = getStatusReq();
			
			System.out.println("insertReqRepository");
			flag = insertReqRepository();
			
		 }catch(Exception ex){
			 rs.setErrorMsg("mainJoiner:"+ex.getMessage());
		 }finally{
			 return returnLeaverRes();
		 }

	}
	
	public void setLeaverReq(HashMap<String,String> data)
	{	
		rq.setEmpID(data.get("EmpID"));
		rq.setEffectiveStartDate(data.get("EffectiveStartDate"));
		rq.setBranch(data.get("Branch"));
		
	}
	
	public void printTest(){
		System.out.println(rq.getEmpID());
		System.out.println(rq.getEffectiveStartDate());
		System.out.println(rq.getBranch());
	}
	
	@SuppressWarnings("finally")
	public boolean insertLeaverRq() throws SQLException
	{
		Date curDate = new Date();
		
		
			seq = ControlSequenceTable.getSeqFormValidate();		
		if (seq == null) {
			SimpleDateFormat format2 = new SimpleDateFormat("YYMMdHms",lc);
			String date = format2.format(curDate);
			
			rs.setStauts("N");
			rs.setRefNo("ERR"+date);
			rs.setErrorMsg("Exception: get seq leaver error !!!");
			return false;
		}
		
		boolean flagResult = false;
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try{
		  // PreparedStatements can use variables and are more efficient
							
			PreparedStatement preparedStatement = connect
	          .prepareStatement("insert into  tbldt_wsleaverrq values ( ?, ?, ?, ?, ?)");

	      preparedStatement.setString(1, seq);
	      preparedStatement.setString(2, sdf.format(curDate));
	      preparedStatement.setString(3, rq.getEmpID());
	      preparedStatement.setString(4, rq.getEffectiveStartDate());
	      preparedStatement.setString(5, rq.getBranch());
	      
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
	
	public boolean getStatusReq()
	{
		statusReq = "N";
		return true;
	}
	
	@SuppressWarnings("finally")
	public boolean insertReqRepository() throws SQLException{
		
		Date curDate = new Date();	
		SimpleDateFormat format2 = new SimpleDateFormat("YYYYMM",lc);
		String date = format2.format(curDate);			    
	    refNo = date+seq+statusReq;
	    rs.setRefNo(refNo);    
	      
	    boolean flag = true;
	    
	    EmployeeInfo user = new EmployeeInfo(rq.getEmpID());
	    
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try{
				
			PreparedStatement preparedStatement = connect
		    		  .prepareStatement("insert into  tbldt_reqrepository(RequestID,ReqSubmitDate,RSAReferenceID,Requestor,"
				          		+ "EmpID,EmpName,CorpTitleID,CorpTitleName,JobTitleID,JobTitleName,EffStartDate,EffEndDate,"
				          		+ "CurPosition,FwdPosition,RevPosition,CurLimit,FwdLimit,RevLimit,CurBranch,FwdBranch,"
				          		+ "RevBranch,Approver,Status,LastChange,Remark) "
				          		+ "values ( ?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?)");

		      preparedStatement.setString(1, refNo);
		      preparedStatement.setString(2, sdf.format(curDate)); //summit date
		      preparedStatement.setString(3, "RSAKey");
		      preparedStatement.setString(4, "HR"); // requestor
		      preparedStatement.setString(5, rq.getEmpID()); //EmpID
		      
		      preparedStatement.setString(6, "XXX"); //EmpName
		      preparedStatement.setString(7, "XXX"); // CorpTitleID
		      preparedStatement.setString(8, "XXX"); // corpTitleName
		      preparedStatement.setString(9, "XXX"); // jobTitleID
		      preparedStatement.setString(10, "XXX"); // jobtitleName
		      
		      preparedStatement.setString(11, rq.getEffectiveStartDate()); //EffStartDate
		      preparedStatement.setString(12, null); // EffEndDate
		      preparedStatement.setString(13, user.getTechnicalRole()); // CurPosition
		      preparedStatement.setString(14, ""); // FwdPosition
		      preparedStatement.setString(15, ""); // RevPosition
		      
		      preparedStatement.setString(16, user.getCurLimit()); //CurLimit
		      preparedStatement.setString(17, ""); // fwdLimit
		      preparedStatement.setString(18, ""); // revLimit
		      preparedStatement.setString(19, rq.getBranch()); // curBranch
		      preparedStatement.setString(20, ""); // FwdBrnach
		      
		      preparedStatement.setString(21, ""); //RevBranch
		      preparedStatement.setString(22, ""); // Approver
		      preparedStatement.setString(23, "A"); // Status
		      //preparedStatement.setDate(24, new java.sql.Date(curDate.getTime())); // LastChange
		      preparedStatement.setString(24, sdf.format(curDate));//(24, new java.sql.Date(curDate.getTime())); // LastChange
		      preparedStatement.setString(25, "leaver User"); // Remark
		      
		      preparedStatement.executeUpdate();
			  rs.setStauts("Y");
		      
			}catch(Exception ex){
				rs.setErrorMsg("insertReqRepository:"+ex.getMessage());
			    rs.setStauts("N");
				flag = false;
				ex.printStackTrace();
			}finally{
				try {	connect.close();} catch (SQLException e) {	}
				return flag;	
			}
	}	
	
	@SuppressWarnings("finally")
	public LeaverRes returnLeaverRes() throws SQLException{
		
		Connection connect = DatbaseConnection.getConnectionMySQL();		
		try{
			  Date curDate = new Date();
			  // PreparedStatements can use variables and are more efficient
			  			
			  PreparedStatement preparedStatement = connect
		          .prepareStatement("insert into  tbldt_wsleaverrs values ( ?, ?, ?, ?, ?)");

		      preparedStatement.setString(1, seq);
		      preparedStatement.setString(2, sdf.format(curDate.getTime()));
		      preparedStatement.setString(3, rs.getStauts());
		      preparedStatement.setString(4, rs.getErrorMsg());
		      preparedStatement.setString(5, rs.getRefNo());
		      
		      preparedStatement.executeUpdate();
		      
			}catch(Exception ex){
				rs.setErrorMsg("returnJoinerRes:"+ex.getMessage());
			}finally{
				try {	connect.close();} catch (SQLException e) {	}
				return rs;	
			}
	}
	
}
