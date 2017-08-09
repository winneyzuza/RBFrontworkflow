package ws;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import req.JoinerReq;
import res.JoinerRes;;

@Path("joiner")
public class Joiner {

//	  private static Connection connect = null;
	  //private Statement statement = null;
	  private PreparedStatement preparedStatement = null;
	  //private ResultSet resultSet = null;
	 
	  private JoinerReq rq = new JoinerReq();
	  private JoinerRes rs = new JoinerRes();
	  
	  private String seq ="";
	  private String statusReq="N";
	  private String refNo ="";
	  
	  private final Locale lc = new Locale("en","US");
	  private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",lc);
	  
	public Joiner() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public JoinerRes mainjoiner(@Context HttpServletRequest req) throws SQLException{	
		//{"EmpID":"90008","EmpJobTitle":"Senior Teller","EffectiveStartDate":"20160703",
		//"Position":"AT+CA","Branch":"0122"}/
		
		/*String temp = req;
		System.out.println(req);
		HashMap<String,String> data = new HashMap<String,String>();
		HashMap<Integer,String> key = new HashMap<Integer,String>();
		key.put(0, "EmpID");
		key.put(1, "EmpJobTitle");
		key.put(2, "EffectiveStartDate");
		key.put(3, "Position");
		key.put(4, "Branch");
		
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
		data.put("EmpJobTitle", req.getParameter("EmpJobTitle"));
		data.put("EffectiveStartDate", (req.getParameter("EffectiveStartDate")==null)?"":req.getParameter("EffectiveStartDate").trim());
		data.put("Position", req.getParameter("Position").replace(" ", "+"));
		data.put("Branch", (req.getParameter("Branch")==null)?"":req.getParameter("Branch").trim());
		
		setJoinerReq(data);
		printtest();
		
		boolean flag = true;
		
		try{
			System.out.println("insertJoinerRq");
			flag = insertJoinerRq();
			
//			System.out.println("getStatusReq");
//			flag = getStatusReq();
			
			System.out.println("insertReqRepository");
			flag = insertReqRepository();

			
		 }catch(Exception ex){
			 rs.setErrorMsg("mainJoiner:"+ex.getMessage());
		 }finally{
			 return returnJoinerRes();
		 }
		
	}
	
	public void setJoinerReq(HashMap<String,String> data)
	{	
		rq.setEmpID(data.get("EmpID"));
		rq.setEmpJobTitle(data.get("EmpJobTitle"));
		rq.setEffectiveStartDate(data.get("EffectiveStartDate"));
		rq.setPosition(data.get("Position"));
		rq.setBranch(data.get("Branch"));
		
	}
	public void printtest(){
		System.out.println(rq.getEmpID());
		System.out.println(rq.getEmpJobTitle());
		System.out.println(rq.getEffectiveStartDate());
		System.out.println(rq.getPosition());
		System.out.println(rq.getBranch());
	}
	
	@SuppressWarnings("finally")
	public boolean insertJoinerRq() throws SQLException
	{
		Date curDate = new Date();
		
		
			seq = ControlSequenceTable.getSeqFormValidate();		
		if (seq == null) {
			SimpleDateFormat format2 = new SimpleDateFormat("YYMMdHms",lc);
			String date = format2.format(curDate);
			
			rs.setStauts("N");
			rs.setRefNo("ERR"+date);
			rs.setErrorMsg("Exception: get seq joiner error !!! ");
			return false;
		}
		
		boolean flagResult = true;
		Connection connect = DatbaseConnection.getConnectionMySQL();	
		try{
		  // PreparedStatements can use variables and are more efficient			
	      preparedStatement = connect
	          .prepareStatement("insert into  tbldt_wsjoinerrq values ( ?, ?, ?, ?, ?, ?, ?)");

	      preparedStatement.setString(1, seq);
	      preparedStatement.setString(2, sdf.format(curDate));
	      preparedStatement.setString(3, rq.getEmpID());
	      preparedStatement.setString(4, rq.getEmpJobTitle());
	      preparedStatement.setString(5, rq.getEffectiveStartDate());
	      preparedStatement.setString(6, rq.getPosition());
	      preparedStatement.setString(7, rq.getBranch());
	      
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
		statusReq= "N"; 
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
	    Connection connect = DatbaseConnection.getConnectionMySQL();
		try{
							
		      preparedStatement = connect
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
		      preparedStatement.setString(10, rq.getEmpJobTitle()); // jobtitleName
		      
		      preparedStatement.setString(11, rq.getEffectiveStartDate()); //EffStartDate
		      preparedStatement.setString(12, null); // EffEndDate
		      preparedStatement.setString(13, ""); // CurPosition
		      preparedStatement.setString(14, rq.getPosition()); // FwdPosition
		      preparedStatement.setString(15, ""); // RevPosition
		      
		      preparedStatement.setString(16, ""); //CurLimit
		      preparedStatement.setString(17, "LOW"); // fwdLimit
		      preparedStatement.setString(18, ""); // revLimit
		      preparedStatement.setString(19, ""); // curBranch
		      preparedStatement.setString(20, rq.getBranch()); // FwdBrnach
		      
		      preparedStatement.setString(21, ""); //RevBranch
		      preparedStatement.setString(22, ""); // Approver
		      preparedStatement.setString(23, "A"); // Status
		      //preparedStatement.setDate(24, new java.sql.Date(curDate.getTime())); // LastChange
		      preparedStatement.setString(24, sdf.format(curDate));//(24, new java.sql.Date(curDate.getTime())); // LastChange
		      preparedStatement.setString(25, "new User"); // Remark
		      
		      preparedStatement.executeUpdate();
		      rs.setStauts("Y");
			}catch(Exception ex){
				rs.setErrorMsg("insertReqRepository:"+ex.getMessage());
				rs.setStauts("N");
				flag = false;
			}finally{
				try {	connect.close();} catch (SQLException e) {	}
				return flag;	
			}
	}
	
	@SuppressWarnings("finally")
	public JoinerRes returnJoinerRes() throws SQLException{
		
		Connection connect = DatbaseConnection.getConnectionMySQL();	
		try{
			  Date curDate = new Date();
			  // PreparedStatements can use variables and are more efficient			
		      preparedStatement = connect
		          .prepareStatement("insert into  tbldt_wsjoinerrs values ( ?, ?, ?, ?, ?)");

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
