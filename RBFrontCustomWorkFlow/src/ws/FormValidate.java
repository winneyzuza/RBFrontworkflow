package ws;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;


import db.DatbaseConnection;
import db.DatbaseConnectionMsSQL;
import mt.EmployeeInfo;
import mt.IAM;
import mt.NormalFunction;
import mt.ReqRepository;
import db.ControlSequenceTable;
import res.FormValidateRes;
import servlet.AuditLog;
import validate.ProcessCA;
import validate.ProcessGroup;
import validate.ProcessNumRole;
import req.FormValidateReq;

@Path("formValidate")
public class FormValidate {

//	  private static Connection connect = null;
//	  private Statement statement = null;
//	  private PreparedStatement preparedStatement = null;
//	  private ResultSet resultSet = null;
	  
	  private FormValidateRes rs = new FormValidateRes();
	  private FormValidateReq rq = new FormValidateReq();
	  private String seq ="";
	  private String statusReq="N";
	  private String refNo ="";
	  private String approver = "";
	  private String msgError ="";
	  private String running = "";
	  
	  private final Locale lc = new Locale("en","US");
	  private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",lc);
	  private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd",lc);
	  private final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM",lc);
	  
	public FormValidate() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("finally")
	@GET
	//@Path("Ws_FormValidate/{req}")
	@Produces(MediaType.APPLICATION_XML)
	//public FormValidateRes mainformValidate(@PathParam("req") String req) throws Exception{
	public FormValidateRes mainformValidate(@Context HttpServletRequest req) throws Exception{
		
		try{
			
			HashMap<String,String> data = new HashMap<String,String>();
			data.put("EmpID", req.getParameter("EmpID").trim());
			data.put("EmpJobTitle", req.getParameter("EmpJobTitle"));
			data.put("FwdEffectiveStartDate", req.getParameter("FwdEffectiveStartDate").trim());
			data.put("FwdEffectiveEndDate", req.getParameter("FwdEffectiveEndDate").trim());
			data.put("FwdPosition", req.getParameter("FwdPosition").replace(" ", "+"));
			data.put("FwdBranch", req.getParameter("FwdBranch").trim());
			data.put("FwdLimit", req.getParameter("FwdLimit"));
			data.put("RevLimit", req.getParameter("RevLimit"));
			data.put("RevPosition", req.getParameter("RevPosition").replace(" ", "+"));
			data.put("RevBranch", req.getParameter("RevBranch").trim());
			data.put("RequestorID", req.getParameter("RequestorID").trim());
			data.put("Escalation", req.getParameter("Escalation"));
			System.out.println("\n*** Requestor ID = " + req.getParameter("RequestorID") + "\n");
			setValidateReq(data);
			//printTest(data);
			printTest2();
			
			boolean flag = true;
		
			System.out.println(flag+" insertFormValidagteRq");
			flag = flag && insertFormValidateRq(data);
			
			System.out.println(flag+" validate");
			flag = flag && validate(rq);
			
			System.out.println(flag+" check vacation/leave day");
			flag = flag && weekendDay(rq);
			
			System.out.println(flag+" check holiday");
			flag = flag && holiDay(rq);
			
			//System.out.println("dumpReq");
			//flag = flag && dumpReq(rq);									
			
			System.out.println(flag+" check CA");
			ProcessCA processCA = new ProcessCA();
			flag = flag & processCA.checkCA(rq);
			if(!flag){
				msgError = msgError + processCA.getMsgError();			
			}else{
				this.approver = NormalFunction.chooseApprover(this.approver, processCA.getApprover());
			}
			
			System.out.println(flag+" validate checkNumRole");
			ProcessNumRole processNumR = new ProcessNumRole();
			flag = flag & processNumR.checkNumRole(rq);
			if(!flag){
				msgError = msgError + processNumR.getMsgError();			
			}else{
				this.approver = NormalFunction.chooseApprover(this.approver, processNumR.getApprover());
			}
			
			System.out.println(flag+" validate checkGroup");
			ProcessGroup processGroup = new ProcessGroup();
			flag = flag & processGroup.checkGroup(rq);
			if(!flag){
				msgError = msgError + processGroup.getMsgError();				
			}else{
				this.approver = NormalFunction.chooseApprover(this.approver, processGroup.getApprover());
			}			
			
			System.out.println(flag+" insertReqRepository");
			flag = flag & insertReqRepository(flag);			  
			
			/////////// Appprover //////////
			System.out.println(flag+" Approver validation start");
			RequestApprover ra = new RequestApprover();
			flag = flag & ra.mainRequestApprover(data.get("EmpID"),this.refNo,rq.getEscalation());
			
			if(!flag){
				msgError = msgError + ra.getMsgError();				
			}
			
			if(!flag){
				rs.setRefNo("ERR");
				rs.setErrorMsg("ผลการตรวจสอบพบข้อผิดพลาด (ERROR):"+msgError);

			}else{
				rs.setErrorMsg("000=OK");				
			}
			
		}catch(Exception ex){
			this.msgError = this.msgError+" parameter not complete. ";
			ex.printStackTrace();
			//rs.setErrorMsg("mainformValidate:"+ex.getMessage());
		}  
		finally{	
			
		return returnFormValidateRes();
		}
	}	
	
	public void printTest(HashMap<String,String> data)
	{
		System.out.println(data.get("EmpID"));
		System.out.println(data.get("EmpJobTitle"));
		System.out.println(data.get("FwdEffectiveStartDate"));
		System.out.println(data.get("FwdEffectiveEndDate"));
		System.out.println(data.get("FwdPosition"));
		System.out.println(data.get("FwdBranch"));
		System.out.println(data.get("FwdLimit"));
		System.out.println(data.get("RevLimit"));
		System.out.println(data.get("RevPosition"));
		System.out.println(data.get("RevBranch"));
		
		/*s = temp.indexOf(key.get(0))+key.get(0).length()+3;
		l = temp.indexOf("\"", s);		
		EmpID = temp.substring(s,l);
		System.out.println(EmpID);
		*/
	}
	
	public void printTest2()
	{
		System.out.println("getEmpID:"+rq.getEmpID());
		System.out.println("getEmpJobTitle:"+rq.getEmpJobTitle());
		System.out.println("getFwdBranch:"+rq.getFwdBranch());
		System.out.println("getFwdLimit:"+rq.getFwdLimit());
		System.out.println("getFwdPosition:"+rq.getFwdPosition());
		System.out.println("getRevBranch:"+rq.getRevBranch());
		System.out.println("getRevLimit:"+rq.getRevLimit());
		System.out.println("getRevPosition:"+rq.getRevPosition());
		System.out.println("getFwdEffectiveStartDate:"+rq.getFwdEffectiveStartDate());
		System.out.println("getFwdEffectiveEndDate:"+rq.getFwdEffectiveEndDate());
		System.out.println("getRequestorID:"+rq.getRequestorID());
		System.out.println("getEscalateion:"+rq.getEscalation());
		
		/*s = temp.indexOf(key.get(0))+key.get(0).length()+3;
		l = temp.indexOf("\"", s);		
		EmpID = temp.substring(s,l);
		System.out.println(EmpID);
		*/
	}
	
	public void setValidateReq(HashMap<String,String> data) throws ParseException
	{     
        
		rq.setEmpID(data.get("EmpID"));
		rq.setEmpJobTitle(data.get("EmpJobTitle"));
		rq.setFwdEffectiveStartDate( data.get("FwdEffectiveStartDate") );
		rq.setFwdEffectiveEndDate( data.get("FwdEffectiveEndDate") );
		rq.setFwdPosition(data.get("FwdPosition"));
		rq.setFwdBranch(data.get("FwdBranch"));
		rq.setFwdLimit(data.get("FwdLimit"));		
		rq.setRevPosition(data.get("RevPosition"));
		rq.setRevBranch(data.get("RevBranch"));
		rq.setRevLimit(data.get("RevLimit"));
		rq.setRequestorID(data.get("RequestorID"));
		rq.setEscalation(data.get("Escalation"));	
		
	}
	
	
	
	@SuppressWarnings("finally")
	public boolean insertFormValidateRq(HashMap<String,String> data) 
	{		
		Date curDate = new Date();
		boolean flagResult = true;
		
		seq = ControlSequenceTable.getSeqFormValidate();		
		if(seq==null) {
			SimpleDateFormat format2 = new SimpleDateFormat("YYMMdHms",lc);
			String date = format2.format(curDate);
			
			rs.setRefNo("ERR"+date);
			rs.setErrorMsg("Exception: get sql Validate error !!!");
			flagResult = false;
			this.msgError = this.msgError+"Seq ";
		}
		running = ControlSequenceTable.getRunningFormValidate();
		
		Connection connect = DatbaseConnection.getConnectionMySQL();	
		try{
		  // PreparedStatements can use variables and are more efficient			
			PreparedStatement preparedStatement = connect
	          .prepareStatement("insert into  tbldt_wsformvalidaterq(PKey,TransDate,EmpID,EmpJobTitle,FwdEffectiveStartDate,FwdEffectiveEndDate,Fwdposition,FwdBranch,FwdLimit,RevLimit,RevPosition,RevBranch,RequestorID,Escalation) values ( ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			preparedStatement.setString(1, running);
			preparedStatement.setString(2, sdf.format(curDate));
			preparedStatement.setString(3, data.get("EmpID"));
			//preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
			preparedStatement.setString(4, data.get("EmpJobTitle"));
	      
	      //String string = "January 2, 2010";
//	      	DateFormat format = new SimpleDateFormat("yyyyMMd", Locale.ENGLISH);
//	      	Date FwdEffectiveStartDate = format.parse(data.get("FwdEffectiveStartDate"));
//	      	preparedStatement.setDate(5, new java.sql.Date(FwdEffectiveStartDate.getTime()));
//	      	Date FwdEffectiveEndDate = format.parse(data.get("FwdEffectiveEndDate"));
//	      	preparedStatement.setDate(6, new java.sql.Date(FwdEffectiveEndDate.getTime()));
	      
			preparedStatement.setString(5, data.get("FwdEffectiveStartDate"));
			preparedStatement.setString(6, data.get("FwdEffectiveEndDate"));
	      
			preparedStatement.setString(7, data.get("FwdPosition"));
			preparedStatement.setString(8, data.get("FwdBranch"));
			preparedStatement.setString(9, data.get("FwdLimit"));
			preparedStatement.setString(10, data.get("RevLimit"));
			preparedStatement.setString(11, data.get("RevPosition"));
			preparedStatement.setString(12, data.get("RevBranch"));
			preparedStatement.setString(13, data.get("RequestorID"));
			preparedStatement.setString(14, data.get("Escalation"));
			preparedStatement.executeUpdate();
	      
			flagResult = true;
//	      	System.out.println(preparedStatement);

		}catch(Exception ex){
			rs.setErrorMsg("insertFormValidagteRq:"+ex.getMessage());
			flagResult = false;
			this.msgError = this.msgError+"insertRq ";
			ex.printStackTrace();
		}
		finally{
			try {	connect.close();} catch (SQLException e) {	}
			return flagResult;
		}
	}

	public boolean validate(FormValidateReq rq) {
		
		boolean result = true;
		boolean finalResult = true;
		String msg = "";
		result  = checkEmpID(rq.getEmpID());
		if(!result){  msg = msg+"EmpID "; finalResult = false; }
		
		result  = checkPosition(rq.getFwdPosition());
		if(!result){  msg = msg+"FwdPosition "; finalResult = false; }
		
		result  = checkPosition(rq.getRevPosition());
		if(!result){  msg = msg+"RevPosition "; finalResult = false; }
		
		result  = checkBranch(rq.getFwdBranch());
		if(!result){  msg = msg+"FwdBranch "; finalResult = false; }
		
		result  = checkBranch(rq.getRevBranch());
		if(!result){  msg = msg+"RevBranch "; finalResult = false; }
		
		result  = checkDate(rq.getFwdEffectiveStartDate(),rq.getFwdEffectiveEndDate());
		if(!result){  msg = msg+"StartDate,EndDate "; finalResult = false; }
		
		result  = checkEmpID(rq.getRequestorID());
		if(!result){  msg = msg+"RequestorID "; finalResult = false; }
			
		System.out.println("validate result : "+finalResult);		
		if(!finalResult){  this.msgError = this.msgError +msg+" ค่าไม่ถูกต้อง"; }
		return finalResult;
	}
	
	public boolean checkEmpID(String empID){
		if(empID.trim().length()<1){
//			this.msgError = this.msgError +"empID ";
			return false;
		}else{
			IAM i = new IAM(empID);
			if("Y".equals(i.getErr())){
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("finally")
	public boolean checkPosition(String position) {
		boolean flag = true;
		//System.out.println(position);
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			
			PreparedStatement preparedStatement = connect
			          .prepareStatement("select * from tblmt_role t where t.RoleName like ? ");
			preparedStatement.setString(1, position);
			ResultSet resultSet = preparedStatement.executeQuery();
			//System.out.println(preparedStatement);
			if(resultSet.next()){
				flag = true;
			}else{
//				this.msgError = this.msgError + "Position ";
				flag = false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {	connect.close();} catch (SQLException e) {	}
			return flag;
		}
	}	
	
	@SuppressWarnings("finally")
	public boolean checkBranch(String branch) {
		// This function will return the result of branch existence (T/F)
		boolean flag = true;
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			
			PreparedStatement preparedStatement = connect
			          .prepareStatement("select count(*) from tblmt_branchinfo b where b.OrgCode like ? ");
			preparedStatement.setString(1, branch);
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			//System.out.println("checkBranch result: "+resultSet.getInt(1));
		//	if( resultSet.getInt(1) > 0)
		//		flag = true;
		//	else 
		//		flag = false;
			flag = (resultSet.getInt(1) > 0)? true : false;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {	connect.close();} catch (SQLException e) {	}
			return flag;
		}
	}
	
	@SuppressWarnings("finally")
	public FormValidateRes returnFormValidateRes() {
		
		Connection connect = DatbaseConnection.getConnectionMySQL();	
		try{
			  Date curDate = new Date();
			  seq = ControlSequenceTable.getRunningFormValidate();		
			  if(seq==null) {
				  rs.setErrorMsg(this.msgError+"-returnFromValidateRes: cannot get sequence id");
			  } else {
			  // PreparedStatements can use variables and are more efficient
//			  System.out.println(rs.getErrorMsg().length());			
				  PreparedStatement preparedStatement = connect
						  .prepareStatement("insert into tbldt_wsformvalidaters(PKey,TransDate,RefNo,ErrorMsgType) values ( ?, ?, ?, ?)");
				  String errmsg = "";
				  preparedStatement.setString(1, seq);
				  preparedStatement.setString(2, sdf.format(curDate.getTime()));
				  preparedStatement.setString(3, rs.getRefNo());
				  errmsg = rs.getErrorMsg().trim()+"111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
				  if (errmsg.length() > 254) errmsg = errmsg.substring(0, 255);
				  preparedStatement.setString(4, errmsg);
				  System.out.println("errmsg >>> " + errmsg.length());
				  if (preparedStatement.executeUpdate() > 0) {
					  System.out.println("returnFormValidateRes(): Insert into tbldt_wsformvalidaters succeed");
				  } else {
					  System.out.println("returnFormValidateRes(): Insert into tbldt_wsformvalidaters failed");
				  }
			  }
			}catch(Exception ex){
				rs.setErrorMsg(this.msgError+"returnFormValidateRes: "+ex.getMessage()+" in table tbldt_wsformvalidaters");
			}finally{
				
				try {	connect.close();} catch (SQLException e) { System.out.println("returnFormValidateRes() error in connection closing");	}
				return rs;	
			}
	}
	
	@SuppressWarnings("finally")
	public boolean insertReqRepository(boolean flag) 
	{
		Date curDate = new Date();
		//Locale lc = new Locale("en","US");		
		SimpleDateFormat format2 = new SimpleDateFormat("YYYYMM",lc);
		String date = format2.format(curDate);			    
	    refNo = date+seq+statusReq.substring(0,1);
	    rs.setRefNo(refNo);
	    System.out.println("FormValidate.class: insertReqRepository - RequestID=" + refNo);
	    refNo = refNo.substring(0, 12); // new add 2017-07-12
	    
	    EmployeeInfo empInfo = new EmployeeInfo(rq.getEmpID());
	    String status = "N";
	    
	    IAM empHR = new IAM(rq.getEmpID());
	    
	    Connection connect = DatbaseConnection.getConnectionMySQL();
		try{
							
			PreparedStatement  preparedStatement = connect
		          .prepareStatement("insert into  tbldt_reqrepository(RequestID,ReqSubmitDate,RSAReferenceID,Requestor,"
		          		+ "EmpID,EmpName,CorpTitleID,CorpTitleName,JobTitleID,JobTitleName,EffStartDate,EffEndDate,"
		          		+ "CurPosition,FwdPosition,RevPosition,CurLimit,FwdLimit,RevLimit,CurBranch,FwdBranch,"
		          		+ "RevBranch,Approver,Status,LastChange,Remark) "
		          		+ "values ( ?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?)");

		      preparedStatement.setString(1, refNo);
		      preparedStatement.setString(2, sdf.format(curDate));
		      preparedStatement.setString(3, "RSAKey");
		      //preparedStatement.setString(4, ""); // requestor
		      preparedStatement.setString(4, rq.getRequestorID()); // requestor
		      preparedStatement.setString(5, rq.getEmpID()); //EmpID
		      
		      preparedStatement.setString(6, empHR.getFull_Name_TH()); //EmpName
		      preparedStatement.setString(7, ""); // CorpTitleID
		      preparedStatement.setString(8, empHR.getCorporateTitleEN()); // corpTitleName
		      preparedStatement.setString(9, ""); // jobTitleID
		      preparedStatement.setString(10, rq.getEmpJobTitle()); // jobtitleName
		      
		      preparedStatement.setString(11, rq.getFwdEffectiveStartDate()); //EffStartDate
		      preparedStatement.setString(12, rq.getFwdEffectiveEndDate()); // EffEndDate
		      preparedStatement.setString(13, empInfo.getBusinessRole()); // CurPosition
		      preparedStatement.setString(14, rq.getFwdPosition()); // FwdPosition
		      preparedStatement.setString(15, rq.getRevPosition()); // RevPosition
		      
		      preparedStatement.setString(16, empInfo.getCurLimit()); //CurLimit
		      preparedStatement.setString(17, rq.getFwdLimit()); // fwdLimit
		      preparedStatement.setString(18, rq.getRevLimit()); // revLimit
		      preparedStatement.setString(19, empInfo.getBranchID()); // curBranch
		      preparedStatement.setString(20, rq.getFwdBranch()); // FwdBrnach
		      
		      preparedStatement.setString(21, rq.getRevBranch()); //RevBranch
		      preparedStatement.setString(22, this.approver); // Approver
		      preparedStatement.setString(23, status); // Status
		      //preparedStatement.setDate(24, new java.sql.Date(curDate.getTime())); // LastChange
		      preparedStatement.setString(24, sdf.format(curDate));//(24, new java.sql.Date(curDate.getTime())); // LastChange
		      preparedStatement.setString(25, ""); // Remark
		      
		      preparedStatement.executeUpdate();
		      
			}catch(SQLException ex){
				rs.setErrorMsg("insertReqRepository:"+ex.getMessage());
				this.msgError = this.msgError + "insert Repository not complete. ";
				flag = false;
				ex.printStackTrace();
			}finally{
				try {	connect.close();} catch (SQLException e) {	}
				return flag;	
			}
	}
	//ok
//	@SuppressWarnings("finally")
//	public boolean ContralView(){
//
//		boolean flag = true;
//		  // PreparedStatements can use variables and are more efficient
//		Connection connect = DatbaseConnection.getConnectionMySQL();				
//	    try {
//	    	PreparedStatement preparedStatement = connect.prepareStatement("delete from tblmt_controlview ");
//			preparedStatement.executeUpdate();
//			
//	    	preparedStatement = connect.prepareStatement("select e.EmpID,e.TechnicalRole from tblmt_employeeinfo e where e.TechnicalRole not like 'BLANK' ORDER BY e.EmpID ,e.TechnicalRole ");
//			//preparedStatement.setString(1, position);
//			resultSet = preparedStatement.executeQuery();
//			
//			while(resultSet.next()){
//				String temp1 = null,temp2 = null,temp3 = null;
//				String EmpID = resultSet.getString("EmpID");
//				String BRole = resultSet.getString("TechnicalRole");
//				//System.out.println(EmpID+":"+BRole);
//				
//				int s=0,l1=0,l2=0,f=0;
//				l1 = BRole.indexOf("+");
//				
//				f = BRole.length();
//				if(l1==-1){
//						temp1 = BRole.substring(s, f);
//						insertContralView(EmpID,temp1);
//				}else{
//					temp1 = BRole.substring(s, l1);
//					insertContralView(EmpID,temp1);
//					l2 = BRole.indexOf("+",l1+1);
//					if(l2 == -1){
//						temp2 = BRole.substring(l1+1, f);
//						insertContralView(EmpID,temp2);
//					}else {
//						temp2 = BRole.substring(l1+1, l2);
//						temp3 = BRole.substring(l2+1, f);
//						insertContralView(EmpID,temp2);
//						insertContralView(EmpID,temp3);
//					}
//				}
//				//System.out.println("return:"+temp1+":"+temp2+":"+temp3);	
//				
//			}
//	    	
//		}catch(SQLException ex){
//			rs.setErrorMsg("ControlView:"+ex.getMessage());
//			flag = false;
//		}finally{
//			try {	connect.close();} catch (SQLException e) {	}
//			return flag;	
//		}
//	}
	//ok
//	private void insertContralView(String EmpID,String TRole) {
//		Date curDate = new Date();
//
//		Connection connect = DatbaseConnection.getConnectionMySQL();
//		try {
//			PreparedStatement preparedStatement = connect
//			          .prepareStatement("insert into  tblmt_controlview values ( ?, ?, ?, ?)");
//			      preparedStatement.setString(1, EmpID);
//			      preparedStatement.setString(2, converRoleTecnToBus(TRole));
//			      preparedStatement.setString(3, TRole);
//			      preparedStatement.setDate(4, new java.sql.Date(curDate.getTime()));		      
//			preparedStatement.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {	connect.close();} catch (SQLException e) {	}			
//		}
//		
//	}
	//ok
//	public String converRoleTecnToBus(String TRole){
//		if(TRole.equals("BAO1"))
//			return "AT";
//		else if(TRole.equals("SBAO"))
//			return "SC";
//		else if(TRole.equals("ABM"))
//			return "SSC";
//		else if(TRole.equals("BM"))
//			return "SSC";
//		else if(TRole.equals("SBM"))
//			return "SSC";
//
//		return TRole;
//	}
	//ok	
	public String AddDate1(String dd){

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar b = Calendar.getInstance();
		try {
		    b.setTime(sdf.parse(dd));
		} catch (ParseException e) {
		    e.printStackTrace();
		} 
		
		b.add(Calendar.DATE, 1);

		return sdf.format(b.getTime());
	}
	//ok
	public boolean checkDate(String s,String e){
		boolean flag =true;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
		try {
			System.out.println(s+":"+e);
			Date ss = sdf.parse(s);
			Date ee = sdf.parse(e);
			System.out.println(ss.getTime()+":"+ee.getTime());
			if(ss.getTime() > ee.getTime()){
				flag = false;
//				this.msgError = this.msgError + "Date ";
			}
				
		} catch (ParseException e1) {
			e1.printStackTrace();
//			this.msgError = this.msgError + "Date ";
			flag = false;
		}
		
		return flag;
	}
	//ok
	public boolean dumpReq(FormValidateReq rq) {
		boolean dump = true;
		
		ReqRepository rpstr = new ReqRepository();

		List<ReqRepository> LR = new ArrayList<ReqRepository>();  
		
		LR = rpstr.getRepsitoryByEmpID(rq.getEmpID());
		
		for(ReqRepository rr : LR) {
			String rrS = rr.getEffStartDate();
			String rrE = rr.getEffEndDate();
				   rrE = AddDate1(rrE);
			
			while(!rrS.equals(rrE)){
				String S = rq.getFwdEffectiveStartDate();
				String E = AddDate1(rq.getFwdEffectiveEndDate());
				while(!S.equals(E)){
					if(S.equals(rrS)){
						this.msgError = this.msgError +"Dump req ";
						dump = false;
						break;
					}
					S = AddDate1(S);
				}
				if(!dump)
					break;
				rrS = AddDate1(rrS);
			}
			if(!dump)
				break;
		}
		
		return dump;
	}
	//ok
	public int getOperDaybyBranch(String branch){
		int num = 0;
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			
			PreparedStatement preparedStatement = connect
			          .prepareStatement("select b.OperationDay from tblmt_branchinfo b "+
			        		  "where b.OrgCode = ? ");
			preparedStatement.setString(1, branch);
			ResultSet resultSet = preparedStatement.executeQuery();
			//System.out.println(preparedStatement);
			if(resultSet.next()){
				num = (int)resultSet.getInt(1);
			}
			
			System.out.println("getOperDayBranch : "+num);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}
		return num;
	}
	//ok
	public boolean weekendDay(FormValidateReq rq){
		boolean restD = true;
		
		int OperDayB = getOperDaybyBranch(rq.getFwdBranch());
		
		if(OperDayB<6){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
			Calendar b = Calendar.getInstance();
			try {			
			    b.setTime(sdf.parse(rq.getFwdEffectiveStartDate()));
			    int dayOfWeek = b.get(Calendar.DAY_OF_WEEK);
			    System.out.println(rq.getFwdEffectiveStartDate()+":"+dayOfWeek+":"+Calendar.SATURDAY+":"+Calendar.SUNDAY);
			    if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
			    	restD = false;
			    	this.msgError = this.msgError + ("วันเริ่มคำขออยู่ในช่วงวันหยุดของสาขา 5วัน ");
			    }
			    
			} catch (ParseException e) {
			    e.printStackTrace();
			} 				
		}else if(OperDayB==6){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
			Calendar b = Calendar.getInstance();
			try {			
			    b.setTime(sdf.parse(rq.getFwdEffectiveStartDate()));
			    int dayOfWeek = b.get(Calendar.DAY_OF_WEEK);
			    System.out.println(rq.getFwdEffectiveStartDate()+":"+dayOfWeek+":"+Calendar.SUNDAY);
			    if(dayOfWeek == Calendar.SUNDAY){
			    	restD = false;
			    	this.msgError = this.msgError + ("วันเริ่มคำขออยู่ในช่วงวันหยุดของสาขา 6วัน ");
			    }
			    
			} catch (ParseException e) {
			    e.printStackTrace();
			} 				
		}
		
		return restD;
	}
	public boolean holiDay(FormValidateReq rq){
		boolean restD = true;
		
		int OperDayB = getOperDaybyBranch(rq.getFwdBranch());
		
		restD = logicHoliday(OperDayB,rq.getFwdEffectiveStartDate());
		
//		if(OperDayB==6){
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
//			Calendar b = Calendar.getInstance();
//			try {			
//			    b.setTime(sdf.parse(rq.getFwdEffectiveStartDate()));
//			    int dayOfWeek = b.get(Calendar.DAY_OF_WEEK);
//			    System.out.println(rq.getFwdEffectiveStartDate()+":"+dayOfWeek+":"+Calendar.SATURDAY+":"+Calendar.SUNDAY);
//			    if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
//			    	restD = false;
//			    	this.msgError = this.msgError + ("startEffectDate in Weekend by branch 5 day ");
//			    }
//			    
//			} catch (ParseException e) {
//			    e.printStackTrace();
//			} 				
//		}else if(OperDayB==6){
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
//			Calendar b = Calendar.getInstance();
//			try {			
//			    b.setTime(sdf.parse(rq.getFwdEffectiveStartDate()));
//			    int dayOfWeek = b.get(Calendar.DAY_OF_WEEK);
//			    System.out.println(rq.getFwdEffectiveStartDate()+":"+dayOfWeek+":"+Calendar.SUNDAY);
//			    if(dayOfWeek == Calendar.SUNDAY){
//			    	restD = false;
//			    	this.msgError = this.msgError + ("startEffectDate in Sunday by branch 6 day ");
//			    }
//			    
//			} catch (ParseException e) {
//			    e.printStackTrace();
//			} 				
//		}
		
		return restD;
	}
	
	public boolean logicHoliday(int BranchDay,String startDay){
		boolean result = true;
		Connection connect = DatbaseConnection.getConnectionMySQL();

		try {
			
			PreparedStatement preparedStatement = connect
			          .prepareStatement("select * from tblmt_holiday "+
			        		  "where BranchDay =? and Holiday=? ");
			
			preparedStatement.setString(1, String.valueOf(BranchDay));
			preparedStatement.setString(2, startDay);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			//System.out.println(preparedStatement);
			if(resultSet.next()){
				result = false;
				this.msgError = this.msgError + "วันเริ่มคำขออยู่ในช่วงวันหยุดธนาคารของสาขา "+BranchDay+"วัน";
			}		
			
		} catch (SQLException e) {
			this.msgError = this.msgError + "// Error in getting Holiday logic from DB (logicHoliday)";
			e.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}
		
		
		return result;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		FormValidate a = new FormValidate();
//		FormValidateReq test = new FormValidateReq();
//		test.setEmpID("90051");
//		test.setFwdBranch("0001");
//		test.setFwdEffectiveStartDate("2016-09-25");
//		test.setFwdEffectiveEndDate("2016-09-25");
////		
////		a.checkCA(test);
//		
//		a.restDay(test);
		
//		String a = "AT+CA+SSC";
//		System.out.println(a.split("\\+").length);
//		String Operation = "+";
//		switch(Operation){
//		case "+":{
//			if(10 < 11){ System.out.println("AAh");	}
//			break;}
//		}
		
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
		SimpleDateFormat a = new SimpleDateFormat();
		Calendar b = Calendar.getInstance();
		try {

		    b.setTime(sdf.parse("2016-10-03"));
		    int dayOfWeek = b.get(Calendar.DAY_OF_WEEK);
		    System.out.println("2016-10-03"+":"+dayOfWeek+":"+Calendar.SATURDAY+":"+Calendar.SUNDAY);
		    if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
//		    	restD = false;
//		    	this.msgError = this.msgError + ("startEffectDate is RestDay ");
		    }
		    
		} catch (ParseException e) {
		    e.printStackTrace();
		} 				
	}
}
