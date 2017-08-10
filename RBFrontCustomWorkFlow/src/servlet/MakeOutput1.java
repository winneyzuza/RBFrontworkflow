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
				System.out.println("Makeoutput1.class:getJobTitle Error closing connection");
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
		FileWriter writer = null;
		String file = path+filename;
		try { 
			writer = new FileWriter(file, false);
			writer.write("'UserID'	'CurBr'	'Curpos'	'Limit'	'TermID'	'Action'	'NewBr'	'NewPos'	'NewLimit'	'NewTermID'	'Mode' \n");
		} catch (Exception e) {
			System.out.println("MakeOutput1:generateFile - Cannot create output file (New FileWriter");
		}
		
		
		try {
			preparedStatement = connect.prepareStatement(sql);

			//System.out.println(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			String info = "";
			while(resultSet.next()){
				//System.out.println(resultSet.getString("UserID")+","+resultSet.getString("CurBr")+","+resultSet.getString("Curpos"));
				info = info+"'"+resultSet.getString("UserID")+"'\t'"+resultSet.getString("CurBr")+"'\t'"+resultSet.getString("Curpos")+"'\t'"
				 + resultSet.getString("CurLimit")+"'\t'"+resultSet.getString("TermID")+"'\t'"+resultSet.getString("Action")+"'\t'"
				 + resultSet.getString("NewBr")+"'\t'"+resultSet.getString("NewPos")+"'\t'"+resultSet.getString("NewLimit")+"'\t'"
				 + resultSet.getString("NewTermID")+"'\t'"+resultSet.getString("ModeOutput")+"' \n";
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
	
	private boolean DateBetween(String sDate, String eDate, String checkDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
		boolean result = false; 
		try {
			Date SearchstartDate = (Date)formatter.parse(sDate);
			Date SearchendDate = (Date)formatter.parse(eDate);
			Date check1Date = (Date)formatter.parse(checkDate + " 00:01:01");
			if ((!check1Date.before(SearchstartDate)) && (!check1Date.after(SearchendDate))) { // effective date is in between search start and end date
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			System.out.println("MakeOutput1.class: DateBetween - Exception in date transforms & comparison");
		}
		System.out.println("MakeOutput1.class: DateBetween - sDate=" + sDate + " eDate=" + eDate + " checkDate=" + checkDate + " Result=" + result);
		return result; 
	}
	
	@SuppressWarnings("resource")
	public void startProcessOutput2(String sDate,String eDate,String type,String complete){
		System.out.println("ApproveProcess.getData");
//		AuditLog al = new AuditLog();
//		al.setModule("AutoMakeOutput1");
		
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		  
		connect = DatbaseConnection.getConnectionMySQL();
		
		boolean debugging = true;
		
		try{
			preparedStatement = connect.prepareStatement("delete from tbldt_output2 ");		
			preparedStatement.executeUpdate();
			System.out.println("delete  tbldt_output2 success");
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("MakeOutput1.class:Exception delete tbldt_output2:"+e.getMessage());
		}
		// forward query statement
		String sql1 = "SELECT rq.RequestID, rq.ReqSubmitDate, rq.RSAReferenceID, rq.Requestor, rq.FwdLimit, rq.RevLimit, rq.FwdBranch, rq.RevBranch, rq.EmpID, rq.EmpName, rq.CorpTitleID, rq.CorpTitleName, rq.JobTitleID, rq.JobTitleName, rq.EffStartDate, rq.EffEndDate, rq.CurPosition, rq.FwdPosition, rq.RevPosition, rq.Approver, rq.Status, rq.LastChange, rq.CompleteF, rq.CompleteR, rq.Remark, e.BranchID, e.TechnicalRole, e.CurLimit, e.TerminalID  " 
					 +" FROM tbldt_reqrepository rq JOIN tblmt_employeeinfo e ON rq.EmpID = e.EmpID WHERE (rq.Status = 'A') "
					 +" AND (rq.EffStartDate  BETWEEN ? AND ?) ";
					if (complete.equals("Y")) { // Forward Complete case
						sql1 = sql1 +" AND (rq.CompleteF = 'Y')"; 
					} else {
						if (complete.equals("N")) { // Not complete case
							sql1 = sql1 +" AND (rq.CompleteF = 'N')";
						} else {
							// do nothing
							sql1 = sql1 + " AND (rq.CompleteF like '%')";
						}
					}
					 
					
		// backward query statement
		String sql2 = "SELECT rq.RequestID, rq.ReqSubmitDate, rq.RSAReferenceID, rq.Requestor, rq.FwdLimit, rq.RevLimit, rq.FwdBranch, rq.RevBranch, rq.EmpID, rq.EmpName, rq.CorpTitleID, rq.CorpTitleName, rq.JobTitleID, rq.JobTitleName, rq.EffStartDate, rq.EffEndDate, rq.CurPosition, rq.FwdPosition, rq.RevPosition, rq.Approver, rq.Status, rq.LastChange, rq.CompleteF, rq.CompleteR, rq.Remark, e.BranchID, e.TechnicalRole, e.CurLimit, e.TerminalID  " 
				     +" FROM tbldt_reqrepository rq JOIN tblmt_employeeinfo e ON rq.EmpID = e.EmpID  WHERE NOT (rq.Status = 'R' OR rq.Status = 'C') "
				     +" AND (rq.EffEndDate  BETWEEN ? AND ?) ";
					if (complete.equals("Y")) { // Backward Complete case
						sql2 = sql2 +" AND (rq.CompleteF = 'Y' AND rq.CompleteR = 'Y')";
					} else {
						if (complete.equals("N")) {
							sql2 = sql2 + " AND (rq.CompleteF = 'Y' AND rq.CompleteR = 'N')";
						} else {
							sql2 = sql2 + " AND rq.CompleteF = 'Y'";
						}
					}
		// forward + backward query statement
		String sql3 = "SELECT rq.RequestID, rq.ReqSubmitDate, rq.RSAReferenceID, rq.Requestor, rq.FwdLimit, rq.RevLimit, rq.FwdBranch, rq.RevBranch, rq.EmpID, rq.EmpName, rq.CorpTitleID, rq.CorpTitleName, rq.JobTitleID, rq.JobTitleName, rq.EffStartDate, rq.EffEndDate, rq.CurPosition, rq.FwdPosition, rq.RevPosition, rq.Approver, rq.Status, rq.LastChange, rq.CompleteF, rq.CompleteR, rq.Remark, e.BranchID, e.TechnicalRole, e.CurLimit, e.TerminalID  " 
				     +" FROM tbldt_reqrepository rq JOIN tblmt_employeeinfo e ON rq.EmpID = e.EmpID WHERE (rq.Status = 'A' OR rq.Status = 'F') "
				     +" AND ((rq.EffStartDate  BETWEEN ? AND ?)  "
				     +" OR (rq.EffEndDate  BETWEEN ? AND ? ))";
					if (complete.equals("Y")) {
						sql3 = sql3 + " AND (rq.CompleteF = 'Y' OR rq.CompleteR = 'Y')";
					} else {
						if (complete.equals("N")) {
							sql3 = sql3 + " AND (rq.CompleteF = 'N' OR rq.CompleteR = 'N')";
						}
					}
		
		sDate = sDate + " 00:00:00";
		eDate = eDate + " 23:59:59";
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
				//preparedStatement.setString(5, complete);
			}
			
//			System.out.println("AA:"+preparedStatement);
			//preparedStatement.setString(1, ref);
			boolean querypass = false;
			ResultSet resultSet = null;
			try {
				resultSet = preparedStatement.executeQuery();
				querypass = true;
			} catch (SQLException se) {
				System.out.println("MakeOutput1.class: Query exception on Type=" + type);
			}
//			String NewBranch =  "";
//			String NewPosition = "";
//			String NewLimit = "";
//			String NewTerminalID = "";
//			String Action = "";
//			String Mode = "";
//			String Complete = "";
			String Checked ="";
//			String DoubleRecords = "No";
			String FwdBranch, RwdBranch = "";
			String FwdRole, RwdRole, FwdPosition, RwdPosition = "";
			String FwdLimit, RwdLimit, FwdTerminal,RwdTerminal = "";
			String FwdAction,RwdAction, FwdComplete, RwdComplete = "";
			String FwdEffDate, RwdEffDate = "";
			boolean FwdPublish, RwdPublish = false;

			while(resultSet.next() & querypass){
				HashMap<String,String> dataFwd = new HashMap<String,String>();
				HashMap<String,String> dataRwd = new HashMap<String,String>();
				FwdBranch = "";
				RwdBranch = "";
				FwdPosition = "";
				RwdPosition = "";
				FwdLimit = "";
				RwdLimit = "";
				FwdTerminal = "";
				RwdTerminal = "";
				FwdAction = "";
				RwdAction = "";
				FwdComplete = "";
				RwdComplete = "";
				FwdEffDate = "";
				RwdEffDate = "";
				FwdRole = "";
				RwdRole = "";
				FwdPublish = false;
				RwdPublish = false;
				
				String EmpID = resultSet.getString("EmpID");
				String BranchID = resultSet.getString("BranchID");
				String TechnicalRole = resultSet.getString("TechnicalRole");
				String Limit = resultSet.getString("CurLimit");
				String TerminalID = resultSet.getString("TerminalID");
				String completeF = resultSet.getString("CompleteF");
				String completeR = resultSet.getString("CompleteR");

				// data populate section start **********************************
				FwdBranch = resultSet.getString("FwdBranch");
				RwdBranch = resultSet.getString("RevBranch");
				FwdPosition = resultSet.getString("FwdPosition");
				RwdPosition = resultSet.getString("RevPosition");
				FwdLimit = resultSet.getString("FwdLimit");
				RwdLimit = resultSet.getString("RevLimit");
				FwdEffDate = resultSet.getString("EffStartDate");
				RwdEffDate = resultSet.getString("EffEndDate");
				// data populate section end **********************************
				
			// computation logic ********************************************** 
				// check type of query (F-Forward, B-Backward, A-All)
				if (type.equals("F")) { 
					FwdPublish = true; 
				} else if (type.equals("B")) {
					RwdPublish = true;
				} else if (type.equals("A")) { 
					FwdPublish = true; RwdPublish = true; 
				} else {
					FwdPublish = false; 
					RwdPublish = false;
				}
				if (debugging) System.out.println("MakeOutput1.class: Type evalute FWD=" + FwdPublish + " RWD=" + RwdPublish);
				
				// check date 
				FwdPublish = FwdPublish & DateBetween(sDate, eDate, FwdEffDate);
				RwdPublish = RwdPublish & DateBetween(sDate, eDate, RwdEffDate);
				if (debugging) System.out.println("MakeOutput1.class: Date evalute FWD=" + FwdPublish + " RWD=" + RwdPublish);
				
				// check completion query flag
				if (complete.equals("Y")) {
					FwdPublish = FwdPublish & completeF.equals("Y");
					RwdPublish = RwdPublish & completeR.equals("Y");
				} else if (complete.equals("N")) {
					FwdPublish = FwdPublish & completeF.equals("N");
					RwdPublish = RwdPublish & completeR.equals("N");
				} else if (complete.equals("%")) {
					FwdPublish = FwdPublish & true;
					RwdPublish = RwdPublish & true;
				} else {
					System.out.println("MakeOutput1.class: Complete query flag invalid =" + complete);
					FwdPublish = false; 
					RwdPublish = false;
				}
				if (debugging) System.out.println("MakeOutput1.class: Complete evalute FWD=" + FwdPublish + " RWD=" + RwdPublish);
				
				// TO-DO
				// Action Change/Move in output1
				// 
				
/* ************************ OLD CODE & LOGIC ***************************** */				
//				if (type.equals("F")){
//					FwdBranch = resultSet.getString("FwdBranch");
//					FwdPosition = resultSet.getString("FwdPosition");
//					FwdLimit = resultSet.getString("FwdLimit");
//					FwdAction = "Forward";
//					FwdComplete = resultSet.getString("CompleteF");
//				}else if(type.equals("B")){
//					RwdBranch = resultSet.getString("RevBranch");
//					RwdPosition = resultSet.getString("RevPosition");
//					RwdLimit = resultSet.getString("RevPosition");
//					RwdAction = "Backward";
//					RwdComplete = resultSet.getString("CompleteR");
//				}else if(type.equals("A")){
					// One request need to be check whether it needs 2 separate records or not
//					if(completeF.toUpperCase().equals("Y") && completeR.toUpperCase().equals("N")) { // forward done - show backward
//						NewBranch = resultSet.getString("RevBranch");
//						NewPosition = resultSet.getString("RevPosition");
//						NewLimit = resultSet.getString("RevPosition");
//						Action = "Backward";
//						Complete = resultSet.getString("CompleteR");
//						DoubleRecords = "No";
//					} else {
//						if(completeF.toUpperCase().equals("N")) { // forward 
//							NewBranch = resultSet.getString("FwdBranch");
//							NewPosition = resultSet.getString("FwdPosition");
//							NewLimit = resultSet.getString("FwdLimit");
//							Action = "Forward";
//							Complete = resultSet.getString("CompleteF");
//							DoubleRecords = "Maybe";
//						} else {
//							if(completeR.toUpperCase().equals("Y")) {
//								NewBranch = BranchID;
//								NewPosition = TechnicalRole;
//								NewLimit = Limit;
//								System.out.println("MakeOutput1.class: CompleteF & CompleteR both are YES");
//								DoubleRecords = "No";
//							}
//						}
//					}
//					if (DoubleRecords.equals("Maybe")) {
//						// check sDate & eDate with search range
//						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
//						try {
//							Date SearchstartDate = (Date)formatter.parse(sDate);
//							Date SearchendDate = (Date)formatter.parse(eDate);
//							Date effStartDate = (Date)formatter.parse(resultSet.getString("EffStartDate"));
//							Date effEndDate = (Date)formatter.parse(resultSet.getString("EffEndDate"));
//							System.out.println("MakeOutput1.class: sDate=" + sDate + " eDate=" + eDate + " EffStart=" + resultSet.getString("EffStartDate") + " EffEnd=" + resultSet.getString("EffEndDate"));
//							if ((!effStartDate.before(SearchstartDate)) && (!effEndDate.after(SearchendDate))) { // effective date is in between search start and end date
//								DoubleRecords = "Yes";
//							} else {
//								DoubleRecords = "Maybe No";
//							}
//						} catch (Exception e) {
//							DoubleRecords = "No";
//							System.out.println("MakeOutput1.class:StartProcessOutput2 - Exception in date transforms & comparison");
//						}
//					}
//				}
				
//				Mode = Action;
//				Checked = Complete;
//				if(BranchID.equals(NewBranch)){
//					Action = "Change";
//				}else{
//					Action = "Move";
//				}
				
				if (debugging) System.out.println("output2:"+EmpID+":"+BranchID+":"+TechnicalRole+":"+Limit+" TerminalID :"+TerminalID);
				dataFwd = convertBusToTecRole(EmpID,FwdPosition,FwdBranch,FwdLimit);
				dataRwd = convertBusToTecRole(EmpID,RwdPosition,RwdBranch,RwdLimit);
				//System.out.println("behigh:"+data.get("role")+":"+data.get("limit"));
				FwdPosition = dataFwd.get("role");
				RwdPosition = dataRwd.get("role");
				FwdLimit = dataFwd.get("limit");
				RwdLimit = dataRwd.get("limit");
				if (debugging) System.out.println("MakeOutput1.class: FwdPosition=" + FwdPosition + " RwdPosition=" + RwdPosition + " FwdLimit=" + FwdLimit + " RwdLimit=" + RwdLimit);
				//System.out.println("ApproveProcess.getData.getNewTerminal");
				FwdTerminal = (getNewTerminal(EmpID,FwdBranch) == null) ? "NULL" : getNewTerminal(EmpID,FwdBranch);
				RwdTerminal = (getNewTerminal(EmpID,RwdBranch) == null) ? "NULL" : getNewTerminal(EmpID,RwdBranch);
				String RequestID = resultSet.getString("RequestID");
				
				// get new terminal if remark exist (Add 2017-07-23)
				if (!"".equals(resultSet.getString("Remark")) && resultSet.getString("Remark").length() > 0) {
					// remark exist & length more than 0
					String rem = resultSet.getString("Remark").trim();
					if (!"new request".equals(rem)) {
						// not default remark phrase
						int dash = rem.indexOf("-", 0);
						if (dash > 0) {
							// found dash (-) then extract FWD & BWD
							// start extract FWD
							FwdTerminal = rem.substring(0, dash);
							// start extract RWD/BWD
							RwdTerminal = rem.substring(dash+1, rem.length());
						} else {
							if (dash == -1 && rem.length() > 4) {
								// -1 mean not found & remark exist with length more than 4
								// start extract FWD
								FwdTerminal = rem;
							} else {
								// not normal case
								System.out.println("MakeOutput1.class: Warning - Remark column invalid");
								FwdTerminal = "";
								RwdTerminal = "";
							}
						}
					} else {
						System.out.println("MakeOutput1.class: Info - Remark column value is default");
						FwdTerminal = "";
						RwdTerminal = "";
					}
				}
				// get new terminal end
				boolean flag = true;
				String Action = "";
				// 
				if (debugging) System.out.println("MakeOutput1.class: Start to publish to tbldt_output2");
				// Start Forward request check
				if (FwdPublish) {
					if (debugging) System.out.println("MakeOutput1.class: Start Forward request check");
					if(BranchID.equals(FwdBranch)){
						Action = "Change";
					}else{
						Action = "Move";
					}
					if (RwdPublish) RequestID = RequestID + "-1"; // if backward request will be publish too need to distinguish by add -1
					Checked = completeF;
					if (debugging) System.out.println("Fwd RequestID " + RequestID);
					flag = insertOutput2(EmpID,BranchID,TechnicalRole,Limit,TerminalID,Action,FwdBranch,FwdPosition,FwdLimit,FwdTerminal,"Forward",completeF,Checked,RequestID);
					if (debugging) System.out.println("MakeOutput1.class: Finish Forward request check");
					if (debugging) System.out.println("MakeOutput1.class: Value published Emp= " + EmpID + " :Branch=" + BranchID + " :TechRole=" + TechnicalRole + " :Limit=" + Limit + " :Term=" + TerminalID + " :Action=" + Action + " :NextBranch=" + FwdBranch + " :NextPosition=" + FwdPosition + " :NextLimit=" + FwdLimit + " :NextTerm=" + FwdTerminal + " :Complete=" + completeF + " :Checked=" + Checked + " :RequestID=" + RequestID);
				}
				
				// Start Backward request check
				if (RwdPublish) {
					if (debugging) System.out.println("MakeOutput1.class: Start Backward request check");
					if(FwdBranch.equals(RwdBranch)){
						Action = "Change";
					}else{
						Action = "Move";
					}
					if (FwdPublish) RequestID = RequestID + "-2"; // if forward request will be publish too need to distinguish by add -2
					Checked = completeR;
					if (completeF.equalsIgnoreCase("Y")) {
						if (debugging) System.out.println("Rwd1 RequestID " + RequestID);
						flag = flag & insertOutput2(EmpID,FwdBranch,FwdPosition,FwdLimit,FwdTerminal,Action,RwdBranch,RwdPosition,RwdLimit,RwdTerminal,"Backward",completeR,Checked,RequestID);
					} else {
					//	if (FwdPublish) {
					//		if (debugging) System.out.println("Rwd2 RequestID " + RequestID);
					//		flag = flag & insertOutput2(EmpID,FwdBranch,FwdPosition,FwdLimit,FwdTerminal,Action,RwdBranch,RwdPosition,RwdLimit,RwdTerminal,"Backward",completeR,Checked,RequestID);
					//	} else {
							System.out.println("MakeOutput1.class: Forward request is not publish OR not complete - Skip backward request");
							flag = false;
					//	}
					}
					if (debugging) System.out.println("MakeOutput1.class: Finish Backward request check");					
					if (debugging) System.out.println("MakeOutput1.class: Value published Emp= " + EmpID + " :Branch=" + FwdBranch + " :TechRole=" + FwdPosition + " :Limit=" + FwdLimit + " :Term=" + FwdTerminal + " :Action=" + Action + " :NextBranch=" + RwdBranch + " :NextPosition=" + RwdPosition + " :NextLimit=" + RwdLimit + " :NextTerm=" + RwdTerminal + " :Complete=" + completeR + " :Checked=" + Checked + " :RequestID=" + RequestID);
				}
				if (debugging) System.out.println("MakeOutput1.class: Result from insertOutput2 is = " + flag);
				//System.out.println("ApproveProcess.getData.inseartOutput1");
				/* OLD LOGIC & CODE ********************************************* 
//				boolean flag = false; 
//				if (DoubleRecords.equals("Yes")) {
					// Forward request
					String CurPosOnFWD = "";
					String CurLimitOnFWD = "";
					String CurBranchOnFWD = "";
					String CurTerminalOnFWD = "";
					NewBranch = resultSet.getString("FwdBranch");
					NewPosition = resultSet.getString("FwdPosition");
					NewLimit = resultSet.getString("FwdLimit");
					if(BranchID.equals(NewBranch)){
						Action = "Change";
					}else{
						Action = "Move";
					}
					Complete = resultSet.getString("CompleteF");
					data = convertBusToTecRole(EmpID,NewPosition,NewBranch,NewLimit);
					NewPosition = data.get("role");
					CurPosOnFWD = NewPosition;
					NewLimit = data.get("limit");
					CurLimitOnFWD = NewLimit;
					NewTerminalID = getNewTerminal(EmpID,NewBranch);
					CurBranchOnFWD = NewBranch;
					CurTerminalOnFWD = NewTerminalID;
					RequestID = resultSet.getString("RequestID") + "-1";
					flag = insertOutput2(EmpID,BranchID,TechnicalRole,Limit,TerminalID,Action,NewBranch,NewPosition,NewLimit,NewTerminalID,Mode,Complete,Checked,RequestID);
					// Backward request
					NewBranch = resultSet.getString("RevBranch");
					NewPosition = resultSet.getString("RevPosition");
					NewLimit = resultSet.getString("RevPosition");
					if(CurBranchOnFWD.equals(NewBranch)){
						Action = "Change";
					}else{
						Action = "Move";
					}
					Complete = resultSet.getString("CompleteR");
					data = convertBusToTecRole(EmpID,NewPosition,NewBranch,NewLimit);
					Mode = "Backward"; // set mode to Backward
					Limit = CurLimitOnFWD;
					BranchID = CurBranchOnFWD;
					TerminalID = CurTerminalOnFWD;
					TechnicalRole = CurPosOnFWD; // Tech role from forward request
					NewPosition = data.get("role");
					NewLimit = data.get("limit");
					NewTerminalID = getNewTerminal(EmpID,NewBranch);
					RequestID = resultSet.getString("RequestID") + "-2";
					flag = flag && insertOutput2(EmpID,BranchID,TechnicalRole,Limit,TerminalID,Action,NewBranch,NewPosition,NewLimit,NewTerminalID,Mode,Complete,Checked,RequestID);
					System.out.println("MakeOutput1.class:StartProcessOutput2 - double records found");
				} else {
					if (DoubleRecords.equalsIgnoreCase("Maybe No")) {
						String direction = "Forward";
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
						try {
							Date SearchstartDate = (Date)formatter.parse(sDate);
							Date SearchendDate = (Date)formatter.parse(eDate);
							Date effStartDate = (Date)formatter.parse(resultSet.getString("EffStartDate"));
							Date effEndDate = (Date)formatter.parse(resultSet.getString("EffEndDate"));
							System.out.println("MakeOutput1.class: sDate=" + sDate + " eDate=" + eDate + " EffStart=" + resultSet.getString("EffStartDate") + " EffEnd=" + resultSet.getString("EffEndDate"));
							
							if (!(effStartDate.before(SearchstartDate) || effStartDate.after(SearchendDate))) {	// effstartdate in range
								direction = "Forward";
								System.out.println("MakeOutput1.class: (Maybe No) EffStartDate is in search range");
								if (!(effEndDate.before(SearchstartDate) || effEndDate.after(SearchendDate))) { // effenddate in range
									System.out.println("MakeOutput1.class: (Maybe No) EffEndDate is in search range");
									direction = "Both";
								} else {
									System.out.println("MakeOutput1.class: (Maybe No) EffStartDate is in range but EffEndDate is out of range");
								}
							} else {
								if (!(effEndDate.before(SearchstartDate) || effEndDate.after(SearchendDate))) { // effenddate in range
									System.out.println("MakeOutput1.class: (Maybe No) EffEndDate is in search range");
									direction = "Backward";
								} else {
									System.out.println("MakeOutput1.class: (Maybe No) Both EffStartDate and EffEndDate is out of range");
								}
							}
						} catch (Exception e) {
							DoubleRecords = "No";
							System.out.println("MakeOutput1.class:StartProcessOutput2 - Exception in date transforms & comparison");
						}
						switch(direction) {
						case "Forward":
							NewBranch = resultSet.getString("FwdBranch");
							NewPosition = resultSet.getString("FwdPosition");
							NewLimit = resultSet.getString("FwdLimit");
							if(BranchID.equals(NewBranch)){
								Action = "Change";
							}else{
								Action = "Move";
							}
							Complete = resultSet.getString("CompleteF");
							data = convertBusToTecRole(EmpID,NewPosition,NewBranch,NewLimit);
							NewPosition = data.get("role");
							NewLimit = data.get("limit");
							NewTerminalID = getNewTerminal(EmpID,NewBranch);
							RequestID = resultSet.getString("RequestID");
							break;
						case "Backward":
							NewBranch = resultSet.getString("RevBranch");
							NewPosition = resultSet.getString("RevPosition");
							NewLimit = resultSet.getString("RevLimit");
							if(BranchID.equals(NewBranch)){
								Action = "Change";
							}else{
								Action = "Move";
							}
							Complete = resultSet.getString("CompleteR");
							data = convertBusToTecRole(EmpID,NewPosition,NewBranch,NewLimit);
							NewPosition = data.get("role");
							NewLimit = data.get("limit");
							NewTerminalID = getNewTerminal(EmpID,NewBranch);
							RequestID = resultSet.getString("RequestID");
							break;
						case "Both":
							System.out.println("MakeOutput1.class: Get into BOTH switch condition - something goes wrong");
							break;
						}
						flag = insertOutput2(EmpID,BranchID,TechnicalRole,Limit,TerminalID,Action,NewBranch,NewPosition,NewLimit,NewTerminalID,Mode,Complete,Checked,RequestID);
						System.out.println("MakeOutput1.class:StartProcessOutput2 - maybe double records");
					} else {
						flag = insertOutput2(EmpID,BranchID,TechnicalRole,Limit,TerminalID,Action,NewBranch,NewPosition,NewLimit,NewTerminalID,Mode,Complete,Checked,RequestID);
						System.out.println("MakeOutput1.class:StartProcessOutput2 - not double records");
					} 
				} */
			}			
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Exception setOutput2:"+e.getMessage());
			
		}finally{
			System.out.println("setOutput2 success.");
			try { connect.close();	} catch (SQLException e) {	e.printStackTrace(); }
		}
		System.out.flush();
	}
	
	public boolean insertOutput2(String EmpID,String BranchID,String TechnicalRole,String Limit,String TerminalID
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
				System.out.println("Exception insertOutput2:"+e.getMessage());
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
