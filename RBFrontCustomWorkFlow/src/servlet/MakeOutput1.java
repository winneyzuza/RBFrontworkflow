package servlet;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.Configuration;
import db.DatbaseConnection;
import db.DatbaseConnectionMsSQL;

public class MakeOutput1 extends HttpServlet{
	private final String BM = "Branch Manager";
	private final String ABM = "Assistant Branch Manager";
	private final String BWRM = "Branch Wealth Relationship Manager";
	private final String BWRO = "Branch Wealth Relationship Officer";
	private final String BWROF = "Branch Wealth Relationship Officer (FAT)";
	private final String LOO = "Lending Operation Officer";
	private final String LRM = "Lending Relationship Manager-SME";
	
	private final String PB = "Personal Banker";             // เธ�เธ�เธฑเธ�เธ�เธฒเธ�เธ�เธธเธฃเธ�เธดเธ�
	private final String PBS = "Personal Banker Supervisor"; // เธซเธฑเธงเธซเธ�เน�เธฒเธ�เธธเธฃเธ�เธดเธ�
	private final String SPB = "Senior Personal Banker";     // เน€เธ�เน�เธฒเธซเธ�เน�เธฒเธ—เธตเน�เธ�เธธเธฃเธ�เธดเธ�
	private final String ST = "Senior Teller";
	private final String SPO = "Service Planner Officer";
	private final String Teller = "Teller";
	private final String TS = "Teller Supervisor";
	
	private final String TT = "Teller Trainee";
	private final String WRT = "Wealth RM Trainee";
	
	
	public MakeOutput1() {
		// TODO Auto-generated constructor stub
	}
	
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException 
	{ System.out.println("MakeOutput1 Servlet");
	
		Date curDate = new Date();
		Locale lc = new Locale("en","US");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",lc);
		
		  	Configuration.setConfig();
		  	MakeOutput1 test = new MakeOutput1();
			test.startProcessOutput1();
			
		Date curDate2 = new Date();
		System.out.println("Make Output 1 >>>>>>>>>>>>>> doGet");
		System.out.println("start:"+sdf.format(curDate)+" end:"+sdf.format(curDate2));
		  
	}  
	
	public void startProcessOutput1(){
		System.out.println("ApproveProcess.getData");
		AuditLog al = new AuditLog();
		al.setModule("AutoMakeOutput1");
		
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		  
		connect = DatbaseConnection.getConnectionMySQL();
		
		try{
			preparedStatement = connect.prepareStatement("delete from tbldt_output1 ");		
			preparedStatement.executeUpdate();
			System.out.println("delete  tbldt_output1 success");
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("Exception delete tbldt_output1:"+e.getMessage());
		}

		
		String sql = "(select t.EmpID,e.BranchID,e.TechnicalRole,e.CurLimit,e.TerminalID,'Change',t.FwdBranch,t.FwdPosition,FwdLimit,'newTerminl','Forward',t.RequestID,ifnull(e.Exam,'N') exam " 
			 +" from tbldt_reqrepository t"
			 +" join tblmt_employeeinfo e on t.EmpID = e.EmpID"
			 +" where t.Status like 'A' and e.exam = 'Y' "
			 +" and t.EffStartDate = DATE_ADD(CURDATE(),INTERVAL 1 DAY) )"
			 +" UNION"
			 +" (select t.EmpID,e.BranchID,e.TechnicalRole,e.CurLimit,e.TerminalID,'Change2',t.RevBranch,t.RevPosition,RevLimit,'newTerminl','Backward',t.RequestID,ifnull(e.Exam,'N') exam "
			 +" from tbldt_reqrepository t"
			 +" join tblmt_employeeinfo e on t.EmpID = e.EmpID"
			 +" where t.Status like 'A' and e.exam = 'Y' "
			 +" and (t.EffEndDate = CURDATE())) ";
//		System.out.println(sql);
		boolean flag=true;
		try {
			preparedStatement = connect
			          .prepareStatement(sql);
			System.out.println("makeOutput1:"+preparedStatement);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				HashMap<String,String> data = new HashMap<String,String>();
				
				String EmpID = resultSet.getString(1);
				String BranchID = resultSet.getString(2);
				String TechnicalRole = resultSet.getString(3);
				String Limit = resultSet.getString(4);
				String TerminalID = resultSet.getString(5);
				String Action  = resultSet.getString(6);
				String NewBranch = resultSet.getString(7);
				String NewPosition = resultSet.getString(8);
				String NewLimit = resultSet.getString(9);
				String NewTerminalID = resultSet.getString(10);
				String Mode = resultSet.getString(11);
				String ReqID = resultSet.getString(12);
				String Exam = resultSet.getString(13);
				
				if(BranchID.equals(NewBranch)){
					Action = "Change";
				}else{
					Action = "Move";
				}
				
//				System.out.println("output1:"+EmpID+":"+BranchID+":"+TechnicalRole+":"+Limit+":"+TerminalID+":"+Action+":"+NewBranch+":"+NewPosition+":"+NewLimit+":"+Mode+":"+ReqID);
				data = convertBusToTecRole(EmpID,NewPosition,NewBranch,NewLimit);
				//System.out.println("behigh:"+data.get("role")+":"+data.get("limit"));
				NewPosition = data.get("role");
				NewLimit = data.get("limit");
				
				//System.out.println("ApproveProcess.getData.getNewTerminal");
				NewTerminalID = getNewTerminal(EmpID,NewBranch);
				
				if("N".equals(Exam)){
					AuditLog aEx = new AuditLog();
					aEx.setModule("Not pass exam yet ");
					aEx.setStatus("EE");
					aEx.setDesc("Employee:"+EmpID+" not pass exam. ");
					aEx.insertAuditLog();
				}
				if("".equals(NewTerminalID)){
					AuditLog aT = new AuditLog();
					aT.setModule("Insufficient termial available ");
					aT.setStatus("ET");
					aT.setDesc("Branch: "+BranchID+" Employee:"+EmpID+" not have terminal avalible. ");
					aT.insertAuditLog();
				}
				//System.out.println("ApproveProcess.getData.inseartOutput1");
				flag = inseartOutput1(EmpID,BranchID,TechnicalRole,Limit,TerminalID,Action,NewBranch,NewPosition,NewLimit,NewTerminalID,Mode,ReqID);			
			}
			
			if(flag){
				generateFile();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Exception MakeOutput1:"+e.getMessage());
			al.setDesc("Exception MakeOutput1");
			al.setStatus("E");
		}finally{
			System.out.println("MakeOutput1.getData success.");
			al.setDesc("Generate output1.csv success.");
			al.setStatus("C");
			al.insertAuditLog();
			
			try {	connect.close();} catch (SQLException e) {	}			
		}

	}
	
	public HashMap<String,String> convertBusToTecRole(String EmpID,String BRole,String BranchID,String LimitHL){
		HashMap<String,String> data = new HashMap<String,String>();

		if(BRole.equals("SSC")){
			String job = getJobTitle(EmpID);
			if(job.equals(BM)){
				data.put("role","BM");
			}else if(job.equals(ABM)){
				data.put("role","ABM");
			}else{
				data.put("role","ABM");//** เธ�เธ�เธฑเธ�เธ�เธฒเธ�เธ�เธฃเธฃเธกเธ”เธฒเธ�เธญSSC
			}
			data.put("limit","SUPERVISOR");
			
		}else if(BRole.equals("SC")){
			String job = getJobTitle(EmpID);
			data.put("role","SBAO");
			data.put("limit","SUPERVISOR");
			
		}else if(BRole.equals("SSC+CA")){
			data.put("role","ABM+CA");
			data.put("limit","ABM+CA");
			
		}else if(BRole.equals("SC+CA")){
			data.put("role","SBO+CA");
			data.put("limit","SBO+CA");	
			
		}else if(BRole.equals("SSC+AT")){
			data.put("role","ABM+BAO1");
			
			String job = getJobTitle(EmpID);
			String limtB = "";
			
			if(job.equals(TT)){
				data.put("limit","ABM+BAO1 0.1");	
			}else{
				if(LimitHL.equals("LOW"))
					limtB = getLimitBranch(BranchID).get(1);
				else
					limtB = getLimitBranch(BranchID).get(2);
				data.put("limit","ABM+BAO1 "+limtB);
			}
			
		}else if(BRole.equals("SC+AT")){
			data.put("role","BAO1+SBAO");
			
			String job = getJobTitle(EmpID); String limtB = "";
			
			if(job.equals(TT)){
				data.put("limit","AT+SUP 0.1");	
			}else{
				if(LimitHL.equals("LOW"))
					limtB = getLimitBranch(BranchID).get(1);
				else
					limtB = getLimitBranch(BranchID).get(2);
				data.put("limit","AT+SUP "+limtB);
			}
			
		}else if(BRole.equals("AT+CA")){
			data.put("role","AT+CA");
			String job = getJobTitle(EmpID); String limtB = "";
			
			if(job.equals(TT)){
				data.put("limit","TELLER 0.1");	
			}else{
				if(LimitHL.equals("LOW"))
					limtB = getLimitBranch(BranchID).get(1);
				else
					limtB = getLimitBranch(BranchID).get(2);
				data.put("limit","TELLER "+limtB);
			}			
		}else if(BRole.equals("AT")){
			data.put("role","BAO1");
			String job = getJobTitle(EmpID); String limtB = "";
			
			if(job.equals(TT)){
				data.put("limit","TELLER 0.1");
			}else if(job.indexOf("Wealth")>0){
				data.put("limit","TELLER WM WO");
			}else{
				if(LimitHL.equals("LOW"))
					limtB = getLimitBranch(BranchID).get(1);
				else
					limtB = getLimitBranch(BranchID).get(2);
				data.put("limit","TELLER "+limtB);
			}			
		}else if(BRole.equals("CA")){
			data.put("role","CA");
			data.put("limit","TELLER 0.1");				
		}
		
		return data;
	}
	
	public String getJobTitle(String EmpID){
		String jobTitle=null;
		
		Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
		
		try {
			System.out.println(" EmpID "+  EmpID);
			PreparedStatement preparedStatement = connect.prepareStatement("select Job_Title_Code, Job_Title_EN from IAM where employeeID like ?");
			preparedStatement.setString(1,EmpID);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next())
				jobTitle = rs.getString("Job_Title_EN");//Job_Title_Code
			else{
				jobTitle = "NotFound";
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Exception getJobTitle from HRMS:"+e.getMessage());
			jobTitle = "Exception getEmpID";
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
		
		return jobTitle;
	}
	
	public HashMap<Integer,String> getLimitBranch(String branchID){
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		HashMap<Integer,String> limit = new HashMap<Integer,String>();
			limit.put(1, "");
			limit.put(2, "");
		
		connect = DatbaseConnection.getConnectionMySQL();
		try {
			preparedStatement = connect.prepareStatement("select CounterLimit1,CounterLimit2 from tblmt_branchinfo where OrgCode = ? ");
			preparedStatement.setString(1, branchID);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				limit.put(1, rs.getString(1));
				limit.put(2, rs.getString(2));
			}				
			//System.out.println("limit:"+branchID+limit.get(1)+limit.get(2));			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Exception getJobTitle from HRMS:"+e.getMessage());
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}
		
		return limit;
	}
	
	@SuppressWarnings("finally")
	public boolean inseartOutput1(String EmpID,String BranchID,String TechnicalRole,String Limit,String TerminalID
			,String Action,String NewBranch,String NewPosition,String NewLimit,String NewTerminalID,String Mode,String ReqID){
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		boolean flag =true;
	      try {
			preparedStatement = connect
			      .prepareStatement("insert into  tbldt_output1(UserID,CurBr,Curpos,CurLimit,TermID,Action,NewBr,NewPos,NewLimit,NewTermID,ModeOutput,ReqID) "
			    		  +"values ( ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?)");
			
		      preparedStatement.setString(1, EmpID);
		      preparedStatement.setString(2, BranchID);
		      preparedStatement.setString(3, TechnicalRole);
		      preparedStatement.setString(4, Limit);
		      preparedStatement.setString(5, TerminalID);
		      preparedStatement.setString(6, Action);
		      preparedStatement.setString(7, NewBranch);
		      preparedStatement.setString(8, NewPosition);
		      preparedStatement.setString(9, NewLimit);
		      preparedStatement.setString(10, NewTerminalID);
		      preparedStatement.setString(11, Mode);
		      preparedStatement.setString(12, ReqID);
//		      System.out.println(preparedStatement);
		      preparedStatement.executeUpdate();
		      
		      flag = true;

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Exception inseartOutput1:"+e.getMessage());
				flag = false;
			} finally{
				try {	connect.close();} catch (SQLException e) {	}			
				return flag;
			}
	}
	
	public static String getNewTerminal(String EmpID,String NewBranch){
		
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		String Newterminal="";
		
		
		connect = DatbaseConnection.getConnectionMySQL();
		try {
			preparedStatement = connect.prepareStatement("select TerminalID from tblmt_terminallist where EmpID = ? ");
			preparedStatement.setString(1, EmpID);
			ResultSet rs = preparedStatement.executeQuery();
			
			if(rs.next()){
				Newterminal = rs.getString(1);
			}else{
				preparedStatement = connect.prepareStatement("select TerminalID from tblmt_terminallist where OrgCode = ? and trim(EmpID) like ''");
				preparedStatement.setString(1, NewBranch);
				rs = preparedStatement.executeQuery();
				
				if(rs.next()){
					Newterminal = rs.getString(1);
				}

			}
			System.out.println("Newterminal:"+Newterminal+"BranchNew:"+NewBranch);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Exception getJobTitle from HRMS:"+e.getMessage());
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}
		
		return Newterminal;
	}
	
	public void generateFile(){
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		String path = Configuration.getParam("PathOutput1");
		
//		if(Configuration.Step.equals("DEV")){
//			path = Configuration.pathFileDEV;
//		}else{
//			path = Configuration.pathFileSIT;
//		}
		System.out.println("path:"+path);
		
		String filename = Configuration.getParam("FileNameOutput1");
		 System.out.println("approveExportFile");
		 
		 String sql = "select * from tbldt_output1 t where t.Complete not like 'Y' order by t.CurBr,t.UserID ";
		try {
			preparedStatement = connect.prepareStatement(sql);

		//System.out.println(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		String file = path+filename;
		FileWriter writer = new FileWriter(file, false);
		String info = "";
		while(resultSet.next()){
			//System.out.println(resultSet.getString("UserID")+","+resultSet.getString("CurBr")+","+resultSet.getString("Curpos"));
			info = info+"'"+resultSet.getString("UserID")+"','"+resultSet.getString("CurBr")+"','"+resultSet.getString("Curpos")+"','"
				 + resultSet.getString("CurLimit")+"','"+resultSet.getString("TermID")+"','"+resultSet.getString("Action")+"','"
				 + resultSet.getString("NewBr")+"','"+resultSet.getString("NewPos")+"','"+resultSet.getString("NewLimit")+"','"
				 + resultSet.getString("NewTermID")+"','"+resultSet.getString("ModeOutput")+"' \n";
		}
		writer.write(info);
		writer.flush();
        writer.close();		
	    
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}
		
	}
	
	@SuppressWarnings("resource")
	public void startProcessOutput2(String sDate,String eDate,String type,String complete){
		System.out.println("ApproveProcess.getData");
//		AuditLog al = new AuditLog();
//		al.setModule("AutoMakeOutput1");
		
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		  
		connect = DatbaseConnection.getConnectionMySQL();
		
		try{
			preparedStatement = connect.prepareStatement("delete from tbldt_output2 ");		
			preparedStatement.executeUpdate();
			System.out.println("delete  tbldt_output2 success");
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("Exception delete tbldt_output2:"+e.getMessage());
		}
		// forward query statement
		String sql1 = "SELECT rq.RequestID, rq.ReqSubmitDate, rq.RSAReferenceID, rq.Requestor, rq.FwdLimit, rq.FwdBranch, rq.RevBranch, rq.EmpID, rq.EmpName, rq.CorpTitleID, rq.CorpTitleName, rq.JobTitleID, rq.JobTitleName, rq.EffStartDate, rq.EffEndDate, rq.CurPosition, rq.FwdPosition, rq.RevPosition, rq.Approver, rq.Status, rq.LastChange, rq.CompleteF, rq.CompleteR, e.BranchID, e.TechnicalRole, e.CurLimit, e.TerminalID  " 
					 +" FROM tbldt_reqrepository rq JOIN tblmt_employeeinfo e ON rq.EmpID = e.EmpID WHERE (rq.Status = 'A') "
					 +" AND (rq.EffStartDate  BETWEEN ? AND ?) "
					 +" AND (rq.CompleteF = 'N' AND rq.CompleteR = 'N')";
		// backward query statement
		String sql2 = "SELECT rq.RequestID, rq.ReqSubmitDate, rq.RSAReferenceID, rq.Requestor, rq.FwdLimit, rq.FwdBranch, rq.RevBranch, rq.EmpID, rq.EmpName, rq.CorpTitleID, rq.CorpTitleName, rq.JobTitleID, rq.JobTitleName, rq.EffStartDate, rq.EffEndDate, rq.CurPosition, rq.FwdPosition, rq.RevPosition, rq.Approver, rq.Status, rq.LastChange, rq.CompleteF, rq.CompleteR, e.BranchID, e.TechnicalRole, e.CurLimit, e.TerminalID  " 
				     +" FROM tbldt_reqrepository rq JOIN tblmt_employeeinfo e ON rq.EmpID = e.EmpID  WHERE NOT (rq.Status = 'R' OR rq.Status = 'C') "
				     +" AND (rq.EffEndDate  BETWEEN ? AND ?) "
				     +" AND rq.CompleteF = 'Y'";
		// forward + backward query statement
		String sql3 = "SELECT rq.RequestID, rq.ReqSubmitDate, rq.RSAReferenceID, rq.Requestor, rq.FwdLimit, rq.FwdBranch, rq.RevBranch, rq.EmpID, rq.EmpName, rq.CorpTitleID, rq.CorpTitleName, rq.JobTitleID, rq.JobTitleName, rq.EffStartDate, rq.EffEndDate, rq.CurPosition, rq.FwdPosition, rq.RevPosition, rq.Approver, rq.Status, rq.LastChange, rq.CompleteF, rq.CompleteR, e.BranchID, e.TechnicalRole, e.CurLimit, e.TerminalID  " 
				     +" FROM tbldt_reqrepository rq JOIN tblmt_employeeinfo e ON rq.EmpID = e.EmpID WHERE (rq.Status = 'A' OR rq.Status = 'F') "
				     +" AND ((rq.EffStartDate  BETWEEN ? AND ?)  "
				     +" OR (rq.EffEndDate  BETWEEN ? AND ? )) ";
		 
		
		/* old condition
		String sql1 = "select t.EmpID,e.BranchID,e.TechnicalRole,e.CurLimit,e.TerminalID,'Change',t.FwdBranch,t.FwdPosition,FwdLimit,'newTerminl','Forward',t.CompleteF " 
			 +" from tbldt_reqrepository t"
			 +" join tblmt_employeeinfo e on t.EmpID = e.EmpID"
			 +" where t.Status like 'A' "
			 +" and t.EffStartDate between ? and ? "
			 +" and t.CompleteF like ";
		
		String sql2 = " select t.EmpID,e.BranchID,e.TechnicalRole,e.CurLimit,e.TerminalID,'Change2',t.RevBranch,t.RevPosition,RevLimit,'newTerminl','Backward',t.CompleteR "
				 +" from tbldt_reqrepository t"
				 +" join tblmt_employeeinfo e on t.EmpID = e.EmpID"
				 +" where t.Status like 'A' "
				 +" and t.EffEndDate between ? and ? "
				 +" and t.CompleteR like ";
		
		String sql3 = "(select t.EmpID,e.BranchID,e.TechnicalRole,e.CurLimit,e.TerminalID,'Change',t.FwdBranch,t.FwdPosition,FwdLimit,'newTerminl','Forward',t.CompleteF " 
				 +" from tbldt_reqrepository t"
				 +" join tblmt_employeeinfo e on t.EmpID = e.EmpID"
				 +" where t.Status like 'A' "
				 +" and t.EffStartDate between ? and ? "
				 +" and t.CompleteF like ? ) "
				 +" UNION"
				 +" (select t.EmpID,e.BranchID,e.TechnicalRole,e.CurLimit,e.TerminalID,'Change2',t.RevBranch,t.RevPosition,RevLimit,'newTerminl','Backward',t.CompleteR "
				 +" from tbldt_reqrepository t"
				 +" join tblmt_employeeinfo e on t.EmpID = e.EmpID"
				 +" where t.Status like 'A' "
				 +" and t.EffEndDate between ? and ? "
				 +" and t.CompleteR like ? ) ";
		//System.out.println(sql);
		 
		 */
		try {
			
			if(type.equals("F")){
				System.out.println("Forward");
				preparedStatement = connect.prepareStatement(sql1);
				preparedStatement.setString(1, sDate);
				preparedStatement.setString(2, eDate);
				//preparedStatement.setString(3, complete);
			}else if(type.equals("B")){
				System.out.println("Backward");
				preparedStatement = connect.prepareStatement(sql2);
				preparedStatement.setString(1, sDate);
				preparedStatement.setString(2, eDate);
				//preparedStatement.setString(3, complete);
			}else if(type.equals("A")){
				System.out.println("All");
				preparedStatement = connect.prepareStatement(sql3);
				preparedStatement.setString(1, sDate);
				preparedStatement.setString(2, eDate);
				//preparedStatement.setString(3, complete);
				preparedStatement.setString(3, sDate);
				preparedStatement.setString(4, eDate);
				//preparedStatement.setString(6, complete);
			}
			
//			System.out.println("AA:"+preparedStatement);
			//preparedStatement.setString(1, ref);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			String NewBranch =  "";
			String NewPosition = "";
			String NewLimit = "";
			String NewTerminalID = "";
			String Action = "";
			String Mode = "";
			String Complete = "";
			String Checked ="";
			
			while(resultSet.next()){
				HashMap<String,String> data = new HashMap<String,String>();
				
				String EmpID = resultSet.getString("EmpID");
				String BranchID = resultSet.getString("BranchID");
				String TechnicalRole = resultSet.getString("TechnicalRole");
				String Limit = resultSet.getString("CurLimit");
				String TerminalID = resultSet.getString("TerminalID");
				String completeF = resultSet.getString("CompleteF");
				String completeR = resultSet.getString("CompleteR");
				
				if (type.equals("F")){
					NewBranch = resultSet.getString("FwdBranch");
					NewPosition = resultSet.getString("FwdPosition");
					NewLimit = resultSet.getString("FwdLimit");
					Action = "Forward";
					Complete = resultSet.getString("CompleteF");
				}else if(type.equals("B")){
					NewBranch = resultSet.getString("RevBranch");
					NewPosition = resultSet.getString("RevPosition");
					NewLimit = resultSet.getString("RevPosition");
					Action = "Backward";
					Complete = resultSet.getString("CompleteR");
				}else if(type.equals("A")){
					if(completeF.toUpperCase().equals("Y") && completeR.toUpperCase().equals("N")) { // forward done - show backward
						NewBranch = resultSet.getString("RevBranch");
						NewPosition = resultSet.getString("RevPosition");
						NewLimit = resultSet.getString("RevPosition");
						Action = "Backward";
						Complete = resultSet.getString("CompleteR");						
					} else {
						if(completeF.toUpperCase().equals("N")) { // forward 
							NewBranch = resultSet.getString("FwdBranch");
							NewPosition = resultSet.getString("FwdPosition");
							NewLimit = resultSet.getString("FwdLimit");
							Action = "Forward";
							Complete = resultSet.getString("CompleteF");
						}
					}
					
				}
				
				Mode = Action;
				Checked = Complete;
				if(BranchID.equals(NewBranch)){
					Action = "Change";
				}else{
					Action = "Move";
				}
				
				System.out.println("output2:"+EmpID+":"+BranchID+":"+TechnicalRole+":"+Limit+" TerminalID :"+TerminalID+" Action :"+Action+":"+NewBranch+":"+NewPosition+":"+NewLimit+":"+Mode);
				data = convertBusToTecRole(EmpID,NewPosition,NewBranch,NewLimit);
				//System.out.println("behigh:"+data.get("role")+":"+data.get("limit"));
				NewPosition = data.get("role");
				NewLimit = data.get("limit");
				
				//System.out.println("ApproveProcess.getData.getNewTerminal");
				NewTerminalID = getNewTerminal(EmpID,NewBranch);
				String RequestID = resultSet.getString("RequestID");
				System.out.println("RequestID " + RequestID);
				//System.out.println("ApproveProcess.getData.inseartOutput1");
				boolean flag = inseartOutput2(EmpID,BranchID,TechnicalRole,Limit,TerminalID,Action,NewBranch,NewPosition,NewLimit,NewTerminalID,Mode,Complete,Checked,RequestID);
			
			}			
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Exception setOutput2:"+e.getMessage());
			
		}finally{
			System.out.println("setOutput2 success.");
			try { connect.close();	} catch (SQLException e) {	e.printStackTrace(); }
		}
	}
	
	public boolean inseartOutput2(String EmpID,String BranchID,String TechnicalRole,String Limit,String TerminalID
			,String Action,String NewBranch,String NewPosition,String NewLimit,String NewTerminalID,String Mode,String Complete, String Checked, String RequestID){
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		connect = DatbaseConnection.getConnectionMySQL();
		boolean flag =true;
	      try {
			preparedStatement = connect
			      .prepareStatement("insert into  tbldt_output2(UserID, CurBr, CurPos, CurLimit, TermID, Action, NewBr, NewPos, NewLimit, NewTermID, ModeOutput, Complete, Checked, RequestID) values ( ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			  preparedStatement.setString(1, EmpID);
			  preparedStatement.setString(2, BranchID);
		      preparedStatement.setString(3, TechnicalRole);
		      preparedStatement.setString(4, Limit);
		      preparedStatement.setString(5, TerminalID);
		      preparedStatement.setString(6, Action);
		      preparedStatement.setString(7, NewBranch);
		      preparedStatement.setString(8, NewPosition);
		      preparedStatement.setString(9, NewLimit);
		      preparedStatement.setString(10, NewTerminalID);
		      preparedStatement.setString(11, Mode);
		      preparedStatement.setString(12, Complete);
		      preparedStatement.setString(13, Checked);
		      preparedStatement.setString(14, RequestID);
		      preparedStatement.executeUpdate();
		      
		      flag = true;

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Exception inseartOutput1:"+e.getMessage());
				flag = false;
			}finally{
				try {	connect.close();} catch (SQLException e) {	}			
			}
	      	
	      return flag;
	}
	
	public static void main(String [] args)
	{
		MakeOutput1 test = new MakeOutput1();
		test.startProcessOutput1();
	}
}
