package ws;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import db.Configuration;
import db.ControlSequenceTable;
import db.DatbaseConnection;
import db.DatbaseConnectionMsSQL;
import mt.Authen;
import mt.BranchInfo;
import mt.IAM;
import mt.ReqRepository;
import req.RequestApproverReq;
import res.RequestApproverRes;;

@Path("requestApprover")
public class RequestApprover {

	
	  private String msgError="";
	  private String approver="";
	  private String escalate="";
			
	  static RequestApproverReq rq = new RequestApproverReq();
	  RequestApproverRes rs = new RequestApproverRes();
	  
	  private String seq ="";	  
	  
	  private final Locale lc = new Locale("en","US");
	  private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",lc);

	  
	public RequestApprover() {
		// TODO Auto-generated constructor stub
	}
	
	public String getApprover() {
		return approver;
	}
	
	public String getMsgError() {
		return msgError;
	}
	
	@SuppressWarnings("finally")
	@GET
	//@Path("Ws_RequestApprover/{req}")
	@Produces(MediaType.APPLICATION_XML)
	//public RequestApproverRes mainRequestApprover(@PathParam("req") String req){
	public RequestApproverRes mainRequestApprover(@Context HttpServletRequest req){	
		
		
		HashMap<String,String> data = new HashMap<String,String>();
		data.put("EmpID", (req.getParameter("EmpID")==null)?"":req.getParameter("EmpID").trim());
		data.put("RefNo", (req.getParameter("RefNo")==null)?"":req.getParameter("RefNo").trim());
		
		setRequestApproverReq(data);
		RequestApproverRes temp = new RequestApproverRes(rq.getRefNo());
		return temp;
		
		/*seq = ControlSequenceTable.getSeqRequestApprover();
		//printtest();
		
		boolean flag = true;
		
		try{
			System.out.println("insertRequestApproverRq");
			flag = flag && insertRequestApproverRq();
			
			System.out.println("setApprover");
			flag = flag && processApprover();
			
			System.out.println("setVerifyAO NW");
			flag = flag && setVerify();
			
			System.out.println("updateReqRepository");
			flag = flag && updateReqRepository();
			
		 }catch(Exception ex){
			 rs.setErrorMsg("mainRequestApprover:"+ex.getMessage());
		 }finally{
			 return returnRequestApproverRes();
		 }
		 */
			
	}
	
	public boolean mainRequestApprover(String EmpID,String RefNo,String Escalate){	
		HashMap<String,String> data = new HashMap<String,String>();
		data.put("EmpID", (EmpID==null)?"":EmpID.trim());
		data.put("RefNo", (RefNo==null)?"":RefNo.trim());
		this.escalate = Escalate;
		
		setRequestApproverReq(data);
		seq = ControlSequenceTable.getSeqRequestApprover();
		
		boolean flag = true;		
		
		try{
			System.out.println("insertRequestApproverRq");
			flag = flag && insertRequestApproverRq();
					
			System.out.println("setApprover");
			flag = flag && processApprover(); System.out.println("processApprover()  >> " + processApprover() + " flag " + flag);
			msgError = "ไม่สามารถอนุมัติคำขอได้เนื่องจากผิดกฎในการระบุตัวผู้อนุมัติ (Cannot identify approver)";
			
			System.out.println("setVerifyAO NW");
			flag = flag && setVerify(); System.out.println("setVerify() flag >> " + setVerify() + " flag " + flag);
			
			System.out.println("updateReqRepository");
			flag = flag && updateReqRepository();
			
			
			 RequestApproverRes rar = returnRequestApproverRes();
			 if(rar.getApproverEmpID1().equals("AveksaAdmin")){
				 msgError = "ไม่พบผู้อนุมัติที่ระดับ  "+rar.getApproverLevel1()+" ในระบบ (Authorization level not found)";
				 flag =  false;
			 }
			 else if (rar.getApproverEmpID2().equals("AveksaAdmin")){
				 msgError = "ไม่พบผู้อนุมัติที่ระดับ "+rar.getApproverLevel2()+" ในระบบ (Authorization level not found)";
				 flag =  false;
			 }
			 else if (rar.getApproverEmpID3().equals("AveksaAdmin")){
				 msgError = "ไม่พบผู้อนุมัติที่ระดับ "+rar.getApproverLevel3()+" ในระบบ (Authorization level not found)";
				 flag =  false;
			 }else{
				 flag = true;
			 }
			 
		 }catch(Exception ex){
			 rs.setErrorMsg("mainRequestApprover:"+ex.getMessage());
		 }finally{
			 
		 }
		return flag;
				
	}
	
	public void setRequestApproverReq(HashMap<String,String> data)
	{	
		rq.setEmpID(data.get("EmpID"));
		rq.setRefNo(data.get("RefNo"));		
	}
	
	public void printtest(){
		System.out.println("getEmpID:"+rq.getEmpID());
		System.out.println("rq.getRefNo():"+rq.getRefNo());
	}
	
	public boolean insertRequestApproverRq()
	{
		Date curDate = new Date();
		boolean flagResult = true;
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try{
		  // PreparedStatements can use variables and are more efficient
							
			PreparedStatement preparedStatement = connect
	          .prepareStatement("insert into  tbldt_wsrequestapproverrq values ( ?, ?, ?, ?)");
	      
	      preparedStatement.setString(1, seq);
	      preparedStatement.setString(2, sdf.format(curDate));
	      preparedStatement.setString(3, rq.getEmpID());
	      preparedStatement.setString(4, rq.getRefNo());

	      preparedStatement.executeUpdate();
	      
			flagResult = true;
		}catch(Exception ex){
			System.out.println(ex.getMessage());
			rs.setErrorMsg("Exception:"+ex.getMessage());
			flagResult =  false;
		}
		finally{
			try { connect.close();} catch (SQLException e) {	}		
		}
		return flagResult;
	}
	
	public boolean processApprover() {
		boolean flag = true;
		
		ReqRepository rpstr = new ReqRepository();
		rpstr.setRepsitoryByRef(rq.getRefNo());
		
		System.out.println("code " + rpstr.getCode() + "  Remark " + rpstr.getRemark() + " RefNo " + rq.getRefNo());
		
		if(rpstr.getCode().equals("000")){
			System.out.println("rpstr.getCode() 0 " + rpstr.getCode());
			rs.setRefNo(rpstr.getRequestID());
			System.out.println("rpstr.getCode() 1 " + rpstr.getCode());
			
			boolean AdminSystem = Authen.isAdmin(rq.getEmpID());
			
			
		
			if(!AdminSystem){
				System.out.println(" AdminSystem  >> " + AdminSystem); 
				
				String rsMoveBranch = checkMoveBranch(rpstr);
				
				String typeApprove;
				String typeApproveF = getTypeApprove(rpstr.getFwdPosition(),rpstr.getEmpID(),rpstr.getRequestor());
				System.out.println("processApprover():typeApproveF:"+typeApproveF);
				String typeApproveR = getTypeApprove(rpstr.getRevPosition(),rpstr.getEmpID(),rpstr.getRequestor());
				
				if (!rsMoveBranch.equals("NO")) {
					System.out.println("processApprover():typeApproveR:"+typeApproveR);
					System.out.println("processApprover():typeApproveForValidate:"+rpstr.getApprover());
					if(typeApproveF.equals("EVP")||typeApproveR.equals("EVP")||"EVP".equals(rpstr.getApprover())||"EVP".equals(rsMoveBranch))
						typeApprove = "EVP";
					else if(typeApproveF.equals("NM")||typeApproveR.equals("NM")||"NM".equals(rpstr.getApprover())||"NM".equals(rsMoveBranch) )
						typeApprove = "NM";
					else if(typeApproveF.equals("AM")||typeApproveR.equals("AM")||"AM".equals(rpstr.getApprover())||"AM".equals(rsMoveBranch) )
						typeApprove = "AM";
					else
						typeApprove = "BM";												
					System.out.println("processApprover():typeApprove:"+typeApprove);
					setApprover(rq.getEmpID(),typeApprove);
				} else {
					System.out.println("processApprover(): checkMoveBranch returns NO - do not allow moving");
					typeApprove = "NOT OK";
					flag=false;
				}
					//approveEx
//					if(!approveEx.equals("NM")){
//						rs.setApproverEmpID3(approveEx);
//						rs.setApproverLevel3("Special");
//					}
				if(typeApproveF.equals("EVP")||typeApproveR.equals("EVP")){
					String approverEVP = getEVP(rq.getEmpID());
					rs.setApproverLevel3("EVP");
					rs.setApproverEmpID3(approverEVP);
				}					
				System.out.println("typeApprove "+ typeApprove);
			}	else {
				
				System.out.println("Adnin "+ AdminSystem);
			}									
							
		}else{
			rs.setRefNo("NA");
			rs.setErrorMsg("900:"+rpstr.getRequestID()+": not found");
			flag = false;
		}
		return flag;	
	}
	
	public String getManager(String empID,String ocType){
		String temp=empID;
		String m=empID;
		String o=null;
		String j="";
		
			Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
			PreparedStatement preparedStatement;
			int i =0;
			boolean loopGo = true;
			try {
				do{preparedStatement = connect
				          .prepareStatement("select h.manager,h.OC_TYPE,h.Job_Title_EN from IAM h where h.employeeID = ?");
				preparedStatement.setString(1, m);
				ResultSet resultSet = preparedStatement.executeQuery();
				
					if(resultSet.next()){
						temp = m;
						m = resultSet.getString("manager");
						o = resultSet.getString("OC_TYPE");
						j = resultSet.getString("Job_Title_EN");
						
					}else if(i>20){
						m = "AveksaAdmin";
						break;
					}else{
						m = "AveksaAdmin";
						break;
					}
					i++;
					
					if("Branch".equals(ocType)){
						if("Branch Manager".equals(j)){
							m=temp;
							loopGo = false;
						}
					}else if("Area".equals(ocType)){ // OC Type is Region OR Area AND Job Title is Area Manager									
						if( (o.equals("Region")||o.equals("Area")) 
							&& j.equals("Area Manager")	){
							m=temp;
							loopGo = false;
						}
					}else if("Region".equals(ocType)){ 
						if( (o.equals("Region")||o.equals("Area")) //OC Type is Region OR Area AND Job Title Contains "Regional Manager"
							&& j.indexOf("Regional Manager")>=0 ){
							m=temp;
							loopGo = false;
						}
						
						if( (o.equals("Division")||o.equals("Region")) //OC Type is Division OR Region AND Job Title starts with "EVP, Branch Network"
								&& j.indexOf("EVP, Branch Network")==0 ){
								m=temp;
								loopGo = false;
						}
					}else{
						loopGo = true;
					}
				}while(loopGo);
				
//				if(i!=1){
//					m=temp;
//				}
					
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					connect.close();
				} catch (SQLException e) {
				}			
			}
				
			//System.out.println("getManager result: "+m);
		
		return m;
	}
	
	public String getTypeApprove(String role,String empID,String requester){
		String jobNameEmp = getJobName(empID);
		//String TypeOCRequester = convertLongToShortByOcType(getOcTypebyEmpID(requester));// BM AO NW HQ
		String TypeOCRequester = getOcTypebyEmpID(requester);
		int	lvCorpEmp	= IAM.getlvCorpByEmp(empID);
		
		String RoleDB;
		String RequesterDB;
		String ApproverDB;
		int CorpStaffDB;
		String DefaultAppDB;
		String resultApp="";
		int RowCount=0;

		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			PreparedStatement preparedStatement = connect
						.prepareStatement("select a.Role,a.Requester,a.Approver,CorpStaff,a.DefaultApp from tblmt_approver a "+
			        		  " where a.job like ? and a.Active = 'Y' order by a.Requester desc ");			
			preparedStatement.setString(1, jobNameEmp);
			ResultSet resultSet = preparedStatement.executeQuery();
			
//			System.out.println("getTypeApprove:"+preparedStatement);
			System.out.println("getTypeApprove():typeJob,role="+jobNameEmp+":"+role);
			while(resultSet.next()){
				RowCount++;
				RoleDB = resultSet.getString("Role");
				RequesterDB = resultSet.getString("Requester");
				ApproverDB = resultSet.getString("Approver");
				CorpStaffDB = Integer.parseInt(resultSet.getString("CorpStaff"));
				DefaultAppDB = resultSet.getString("DefaultApp");
				
				boolean check = role.equals(RoleDB) &&  // role match
								lvCorpEmp <= CorpStaffDB && // corpstaff < corp from condition 
								("All".equals(RequesterDB)||RequesterDB.equals(TypeOCRequester)); //request match
				
				if(check){
					resultApp = ApproverDB;
					break;
				}else{
					resultApp = DefaultAppDB;
				}
			}
			if(RowCount == 0){
				resultApp = "";
				System.out.println("RequestApprover:getTypeApprove - No Approver rule match-Return 0 row");
			}
			/// check process role CA CA+AT  CoprStaff>3
//			if(!checkCorpStaff(empID,role))
//				resultApp = getDefaultApp(typeJob);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("RequestApprover:getTypeApprove - SQL execution error - jobNameEmp=" + jobNameEmp);
			
		}finally{
			try { connect.close();} catch (SQLException e) {	System.out.println("RequestApprover:getTypeApprove - SQL Error: connection close");}
			//System.out.println("ApproveEx:"+Configuration.getParam("ApproveEx"));
		}
		return resultApp;
	}
	
	public String getDefaultApp(String typeJob){
		String DefaultApp="";
		
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			PreparedStatement preparedStatement = connect
						.prepareStatement("select a.Requester,a.Approver,a.DefaultApp from tblmt_approver a "+
			        		  " where a.job like ? and a.Active = 'Y' ");
			
			preparedStatement.setString(1, typeJob);		
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				DefaultApp = resultSet.getString("DefaultApp");
			}else{
				DefaultApp = Configuration.getParam("ApproveEx");//"NM";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}
		
		return DefaultApp;
	}
	
	
	public String getJobName(String empID){
		
		String typeEmp = null;
		Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
		
		try {
			
			PreparedStatement preparedStatement = connect
			          .prepareStatement("select h.Job_Title_Code TITLE,h.Job_Title_EN JOBNAME,h.OC_TYPE OC_TYPE from IAM h where h.employeeID = ?");
			preparedStatement.setString(1, empID);
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			//System.out.println("aaa:"+resultSet.toString());
			String jobTitleCode = resultSet.getString("TITLE");
			String jobTitleName = resultSet.getString("JOBNAME");
			String ocType		= resultSet.getString("OC_TYPE");
			
			//System.out.println("ocType: "+ocType);
			//System.out.println("jobTitleCode: "+jobTitleCode);
			
			typeEmp = jobTitleName;//jobTitleCode
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}
			//System.out.println("getTypeEmp:"+typeEmp);
		}
		return typeEmp;
		
	}
		
	public boolean updateReqRepository(){
		
		Date curDate = new Date();
		
		boolean flagResult = true;
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try{
		  // PreparedStatements can use variables and are more efficient
							
//			PreparedStatement preparedStatement = connect
//	          .prepareStatement("UPDATE tbldt_reqrepository SET Requestor = ? ,LastChange = ?"
//	          		+ " WHERE RequestID like ?");

	      
//	      preparedStatement.setString(1, rq.getEmpID());
//	      preparedStatement.setString(2, sdf.format(curDate));
//	      preparedStatement.setString(3, rq.getRefNo());

	      	PreparedStatement preparedStatement = connect
			          .prepareStatement("UPDATE tbldt_reqrepository SET LastChange = ?"
			          		+ " WHERE RequestID like ?");
			      
			      preparedStatement.setString(1, sdf.format(curDate));
			      preparedStatement.setString(2, rq.getRefNo());

	      
			int count = preparedStatement.executeUpdate();
	      //System.out.println(count);
	      
	      	flagResult = (count > 0) ? true : false;
		}catch(Exception ex){
			rs.setErrorMsg("WS.RequestApprover:updateReqRepository - Exception:"+ex.getMessage());
			flagResult =  false;
		}
		finally{			
			try {	connect.close();} catch (SQLException e) {	}			
			
		}
		return flagResult;
	}
		
	public RequestApproverRes returnRequestApproverRes(){
		System.out.println("returnRequestApproverRes");	
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try{
			  Date curDate = new Date();
			  // PreparedStatements can use variables and are more efficient
			  				
			  PreparedStatement preparedStatement = connect
		          .prepareStatement("insert into  tbldt_wsrequestapproverrs values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			  
		      preparedStatement.setString(1, seq);
		      preparedStatement.setString(2, sdf.format(curDate));
		      preparedStatement.setString(3, rs.getRefNo());
		      preparedStatement.setString(4, rs.getApproverLevel1());
		      preparedStatement.setString(5, rs.getApproverEmpID1());
		      preparedStatement.setString(6, rs.getApproverLevel2());
		      preparedStatement.setString(7, rs.getApproverEmpID2());
		      preparedStatement.setString(8, rs.getApproverLevel3());
		      preparedStatement.setString(9, rs.getApproverEmpID3());
		      preparedStatement.setString(10, rs.getVerifyUserAO().toString());
		      preparedStatement.setString(11, rs.getVerifyUserNO().toString());
		      preparedStatement.setString(12, rs.getErrorMsg());
		      preparedStatement.setString(13, rs.getEscalation());
		      
		      System.out.println("\n*** seq = " + seq + "\n");
		      System.out.println("\n*** sdf.format(curDate) = " + sdf.format(curDate) + "\n");
		      System.out.println("\n*** rs.getRefNo() = " + rs.getRefNo() + "\n");
		      System.out.println("\n*** rs.getApproverLevel1() = " + rs.getApproverLevel1() + "\n");
		      System.out.println("\n*** rs.getApproverEmpID1() = " + rs.getApproverEmpID1() + "\n");
		      System.out.println("\n***  rs.getApproverLevel2() = " +  rs.getApproverLevel2() + "\n");
		      System.out.println("\n***  rs.getApproverEmpID2() = " +  rs.getApproverEmpID2() + "\n");
		      System.out.println("\n*** rs.getApproverLevel3() = " + rs.getApproverLevel3() + "\n");
		      System.out.println("\n*** rs.getApproverEmpID3() = " + rs.getApproverEmpID3() + "\n");
		      System.out.println("\n*** rs.getVerifyUserAO().toString() = " + rs.getVerifyUserAO().toString() + "\n");
		      System.out.println("\n*** rs.getVerifyUserNO().toString() = " + rs.getVerifyUserNO().toString() + "\n");
		      System.out.println("\n*** rs.getErrorMsg()= " + rs.getErrorMsg() + "\n");
		      System.out.println("\n*** rs.getEscalation()= " + rs.getEscalation() + "\n");
		      
		      
		      preparedStatement.executeUpdate();
		      
		      System.out.println("returnRequestApproverRes statement >>  "  + preparedStatement);
			}catch(Exception ex){
				rs.setErrorMsg("returnRequestApproverRes:"+ex.getMessage());
			}finally{
				try {	connect.close();} catch (SQLException e) {	}				
			}
		
			return rs;
	}

	public static String checkMoveBranch(ReqRepository rps ){
		String boundary = "";
		String sfwdEffDate = "";
		String srevEffDate = "";
		
		System.out.println(" rps.getFwdDiff() >>" + rps.getFwdDiff());
		System.out.println(" rps.getRevDiff() >>" + rps.getRevDiff());
		
		
		 
		if(null == rps.getFwdDiff()){
			sfwdEffDate = "9999";
		}else{
			sfwdEffDate = rps.getFwdDiff();
		}
		
		if(null == rps.getRevDiff()){
			srevEffDate = "9999";
		}else{
			srevEffDate = rps.getRevDiff();
		}
		
		System.out.println("1 sfwdEffDate " + sfwdEffDate + " srevEffDate " + srevEffDate + " rps.getCompleteF() " + rps.getCompleteF());
		
		int fwdEffDate = Integer.parseInt(sfwdEffDate) ;
		int revEffDate = Integer.parseInt(srevEffDate);
		System.out.println("2 sfwdEffDate " + sfwdEffDate + " srevEffDate " + srevEffDate + " rps.getCompleteF() " + rps.getCompleteF());
		Locale lc = new Locale("en","US");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",lc);
		String chkBranch = "";
		String curBranch = rps.getCurBranch();
		String result="BM";
		
		System.out.println("1 chkBranch " + chkBranch + " rps.getFwdBrandch() " + rps.getFwdBrandch() + " rps.getRevBranch() " + rps.getRevBranch());
		if(fwdEffDate < 1 && rps.getCompleteF().equals("N")){
			chkBranch = rps.getFwdBrandch();
		}else if(revEffDate < 1 && rps.getCompleteF().equals("Y")){
			chkBranch = rps.getRevBranch();
		}
		System.out.println("2 chkBranch " + chkBranch);
		System.out.println("RequestApprover:CheckMoveBranch start");
		System.out.println("ReqID : " + rps.getRequestID());
		System.out.println("CurrentDate : " + sdf.format(new Date()));
		System.out.println("fwdEffDate : " + rps.getEffStartDate());
		System.out.println("revEffDate : " + rps.getEffEndDate());
		System.out.println("DifffwdEffDate : " + fwdEffDate);
		System.out.println("DiffrevEffDate : " + revEffDate);
		System.out.println("CompleteF : " + rps.getCompleteF());
		System.out.println("chkBranch : " + chkBranch);
		System.out.println("curBranch : " + curBranch);
		System.out.println("fwd Branch : " + rps.getFwdBrandch());
		System.out.println("rev Branch : " + rps.getRevBranch());
		
		boundary = GetBoundary(rps.getRequestor()); 
		if(chkBranch.equals(curBranch)){ // if change is in same branch
			if(boundary.equals("Branch")) { 
				result="BM";
			} else { 
				// not branch user but moving in same branch
				if(boundary.equals("Area")) { // if support officer/area manager request?
					result="AM";
				} else {
					if(boundary.equals("Network")) { // if support officer/network manager request?
						result="NM";
					} else {
						// no match - set BM as approver
						result="NO";
						System.out.println("RequestApprover:checkMoveBranch - [Warning] boundary mismatch =" + boundary);
					}
				}
			}
		}else{
			// if change is between different branch - not in same branch
			if (boundary == "Branch") { 
				System.out.println("RequestApprover:checkMoveBranch - Branch limit boundary - trying to move beyond limitation");
			} else {
				String AOCur = "";
				String AOChk = "";
				String NWCur = "";
				String NWChk = "";
				if (boundary.equals("Area")) {
					AOCur = BranchInfo.getAOID(rps.getCurBranch());
					AOChk = BranchInfo.getAOID(chkBranch);
					System.out.println("Area Cur : " + AOCur);
					System.out.println("Area Chk : " + AOChk);
					if(AOCur.equals(AOChk)){
						// same area
						result="AM";
					}
				} else {
					if (boundary.equals("Network")) {
						NWCur = BranchInfo.getNWID(rps.getCurBranch());
						NWChk = BranchInfo.getNWID(chkBranch);
						System.out.println("Network Cur : " + NWCur);
						System.out.println("Network Chk : " + NWChk);
						if(NWCur.equals(NWChk)){
							// same network
							result="NM";
						}else{
							// different network
							result="EVP";
						}
					} else {
						System.out.println("RequestApprover:checkMoveBranch - Boundary mismatch");
						result="NO";
					}
				}
			}
		}
	    return result;
	}
	
	
	public String getOcTypebyEmpID(String empID){
		String rs = null;
		Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
		
		try {
			PreparedStatement preparedStatement = connect
					.prepareStatement("select oc_type from IAM i where employeeID = ? ");		
			preparedStatement.setString(1, empID);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				rs = resultSet.getString("oc_type");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
		
		return rs;
	}
	
	public String getCorpStaffbyEmpID(String empID){
		String corpStaff = null;
		Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
		
		try {
			PreparedStatement preparedStatement = connect
					.prepareStatement("select Corporate_Title_EN from IAM i where employeeID = ? ");		
			preparedStatement.setString(1, empID);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				corpStaff = resultSet.getString("Corporate_Title_EN");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
		
		return corpStaff;
	}
	
	public String getLvCorpStaff(String corp){
		String lv = "";
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			PreparedStatement preparedStatement = connect
					.prepareStatement("select lv from tblmt_lvcorpstaff l where l.CorpStaff like ? ");		
			preparedStatement.setString(1, corp);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				lv = resultSet.getString("lv");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}
		return lv;
	}
	
	public String getLvRole(String role){
		String lvRole = "";
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			PreparedStatement preparedStatement = connect
					.prepareStatement("select lvCorp FROM tblmt_maprolelvcorp c where c.Role = ?");		
			preparedStatement.setString(1, role);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				lvRole = resultSet.getString("lvCorp");
			}else{
				lvRole = "0";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}	
		return lvRole;
	}
	
	public boolean checkCorpStaff(String empID,String role){
		boolean problem = true;
		
		String corpStaff = getCorpStaffbyEmpID(empID);
		String lvEmp	 = getLvCorpStaff(corpStaff);
		String lvRole	 = getLvRole(role);
		System.out.println(lvEmp+":"+lvRole);
		//if(Integer.parseInt(lvEmp) >= Integer.parseInt(lvRole))
		if(Integer.parseInt(lvEmp) <= Integer.parseInt(lvRole))
			problem = true;
		else
			problem = false;
		
		return problem;
	}
	
	public String convertLongToShortByOcType(String ocType){
		String shortOC="";
		if("Branch".equals(ocType))
			shortOC="BM";
		else if("Area".equals(ocType))
			shortOC="AM";
		else if("Region".equals(ocType))
			shortOC="NM";
		else if("Division".equals(ocType))
			shortOC="Division";
		return shortOC; 
	}
	
	public String getEVP(String empID){/// support get column svp up
		String evp = null;
			String m=empID;
			String corpTitle="";
			
				Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
				PreparedStatement preparedStatement;
				
				try {
					
					do{preparedStatement = connect
					          .prepareStatement("select h.manager,h.Corporate_Title_EN from IAM h where h.employeeID = ?");
					preparedStatement.setString(1, m);
					ResultSet resultSet = preparedStatement.executeQuery();
					resultSet.next();
					evp=m;
					m = resultSet.getString("manager");
					corpTitle = resultSet.getString("Corporate_Title_EN");
					}while(!corpTitle.equals("EVP"));
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						connect.close();
					} catch (SQLException e) {
					}			
				}
					
				System.out.println("getEVP result: "+m);			
		return evp;
	}
	
	public String getSupervisor(String empID){
		
		String supervisor="";
		
		Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
			
		try {
			
				PreparedStatement preparedStatement = connect
				          .prepareStatement("select h.manager,h.OC_TYPE from IAM h where h.employeeID = ?");
				preparedStatement.setString(1, empID);
				ResultSet resultSet = preparedStatement.executeQuery();
				
				if(resultSet.next()){
					supervisor = resultSet.getString("manager");
				}			
			
		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}finally{
			try { connect.close();	} catch (SQLException e) {	}			
		}	
			System.out.println("getSupervisor result: "+supervisor);			
		
		return supervisor;
	}
	
	public boolean setVerify(){
		boolean flag = true;
		// Add AO user in Area & Network for being verification for their manager in the workflow 
		List<String> VerifyUserAO;// = new ArrayList<String>();
		List<String> VerifyUserNO = new ArrayList<String>();
		VerifyUserAO = setVerifyUserAO(rq.getEmpID());
		if(VerifyUserAO.size()==0){
			VerifyUserAO.add("NA");
		}
		VerifyUserNO = setVerifyUserNW(rq.getEmpID());
		if(VerifyUserNO.size()==0){
			VerifyUserNO.add("NA");
		}
		rs.setVerifyUserAO(VerifyUserAO);
		
		rs.setVerifyUserNO(VerifyUserNO);
		rs.setErrorMsg("");
		
		return flag;
	}
	
	public List<String> setVerifyUserAO(String empID){
		List<String> temp = new ArrayList<String>();
		temp.add("90045");
		
		String branchID = IAM.getOCByEmp(empID);
		String AO		= IAM.getOcType(branchID,"Area");
		
		Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
		PreparedStatement preparedStatement;
		
		try {
			preparedStatement = connect
			          .prepareStatement("select employeeID from IAM i where OC_CODE like ? and Job_Title_EN like ? ");
			preparedStatement.setString(1, AO);
			preparedStatement.setString(2, Configuration.getParam("verifyAO"));
			
			System.out.println("preparedStatement: setVerifyUserAO  "+preparedStatement);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()){
				temp.add(resultSet.getString("employeeID"));	
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
		
		return temp;
	}
	
	public List<String> setVerifyUserNW(String empID){
		List<String> temp = new ArrayList<String>();
		//temp.add("90045");
		
		String branchID = IAM.getOCByEmp(empID);
		String NW		= IAM.getBranchNetworkByEmp(branchID); //IAM.getOcType(branchID,"Area");
		
		Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
		PreparedStatement preparedStatement;
		
		try {
			preparedStatement = connect
			          .prepareStatement("select employeeID from IAM i where OC_CODE like ? and Job_Title_EN like ? ");
			preparedStatement.setString(1, NW);
			preparedStatement.setString(2, Configuration.getParam("verifyNW"));
			
//			System.out.println("preparedStatement:"+preparedStatement);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()){
				temp.add(resultSet.getString("employeeID"));	
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
			}			
		}
		
		return temp;
	}
	
	public static String GetBoundary(String empID){ // Parameter: requester employee ID 
				
		String branchID = IAM.getOCByEmp(empID);
		String octype	= "";
		String jobtitle = "";
		
		String boundary = "BRANCH"; // other possible boundary values ("BRANCH", "AREA", "NETWORK")
		
		Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
		PreparedStatement preparedStatement;
		
		try {
			preparedStatement = connect
			          .prepareStatement("select OC_TYPE, Job_Title_EN, manager from IAM i where employeeID = ?");
			preparedStatement.setString(1, empID);
			//preparedStatement.setString(2, Configuration.getParam("verifyNW"));
						
			ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println("RequestApprover:GetBoundary - Get OC_TYPE OK");
			// move to first row 
			//if (resultSet.first() && (!resultSet.wasNull())) {
			if (resultSet.next() && (!resultSet.wasNull())) {

				//resultSet.last();
				// resultSet valid & found at least one row 
				octype = resultSet.getString("OC_TYPE");
				jobtitle = resultSet.getString("Job_Title_EN");
				// start check OC TYPE
				switch (octype.toUpperCase()) {
					case "BRANCH": {
						boundary = "Branch";
						break;
					}
					case "SUB BRANCH": {
						boundary = "Branch";
						break;
					}
					case "AREA": {
						boundary = "Area";
						break;
					}
					case "REGION": {
						boundary = "Network";
						break;
					}
					default: {
						boundary = "HQ";
						break;
					}
				}
				// start check Job Title
				if (boundary == "Area" || boundary == "Network") {
					// if requester is Support Officer or not?
					String temp_jobtitle = "";
					if (!jobtitle.trim().toUpperCase().equals("SUPPORT OFFICER")){ // if NOT "Support Officer"?		
						// if Not Support Officer then check if requester is manager or not?
						if (jobtitle.indexOf(",") > 0) {
							// found "XVP, XXXX Manager" pattern
							temp_jobtitle = jobtitle.substring(jobtitle.indexOf(",")+1, jobtitle.length()).trim();
						}
						// case "SVP, Area" handle
						if ((jobtitle.indexOf("Area") > 0) && (jobtitle.indexOf(",") > 0) && (jobtitle.indexOf("Manager") < 0)) { 
							//  found Area & comma but not Manager
							temp_jobtitle = jobtitle.substring(jobtitle.indexOf(",")+1, jobtitle.length()).trim() + " Manager";
						}
						if (temp_jobtitle.trim().toUpperCase().equals("AREA MANAGER") || jobtitle.trim().toUpperCase().equals("REGIONAL MANAGER")) {
							// if job title is Area Manager or Network Manager
							System.out.println("RequestApprover:GetBoundary - Requester is Area or Network manager");
						} else {
							System.out.println("RequestApprover:GetBoundary - Invalid Job Title limit right to within same area or network");
							boundary = "Branch";
						}
					} else {
						// requester is "Support Officer" / AO of Area or Network
						System.out.println("RequestApprover:GetBoundary - Found Support Officer and boundary is " + boundary);
					}
				} else {
					// requester is not in area or network
					if (boundary == "Branch") {
						// 
						if (octype.toUpperCase().trim().equals("BRANCH")) {
							// user OC is branch
							boundary = "Branch";
						} else {
							boundary = "HQ";
						}
					} else {
						// error found in OC_TYPE or requester is in HQ
						System.out.println("RequestApprover:GetBoundary - Requester empdID is not in Branch,Area,Network ");
					}
				}
				
			} else {
				System.out.println("RequestApprover:GetBoundary - OC_TYPE not found empID is invalid - empdID not found in HRMS");
			}
			//while(resultSet.next()){
			//	temp.add(resultSet.getString("employeeID"));	
			//}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("RequestApprover:GetBoundary - Connection Close failed");
			}			
		}
		
		return boundary;
	}
	
	public void setApprover(String Reqester,String lvApprove){
		IAM requester = new IAM(Reqester);
		String typeRequester = requester.getOC_TYPE();
		
		System.out.println("setApprover():lvApprover="+lvApprove+"typeRequester="+Reqester);
		if(lvApprove.equals("Supervisor")){
			rs.setApproverLevel1("Supervisor");
			rs.setApproverEmpID1(getSupervisor(Reqester));
			
		}else if(lvApprove.equals("BM")){
			if("Sub Branch".equals(typeRequester) || "Branch".equals(typeRequester)){
				rs.setApproverLevel1("Branch");
				rs.setApproverEmpID1(getManager(Reqester,"Branch"));
			}else{
				rs.setErrorMsg("Error lvApprover:"+lvApprove+" typeRequester:"+typeRequester);
//				rs.setApproverLevel1("Branch");
//				rs.setApproverEmpID1(getSupervisor(Reqester));
			}
			
		}else if(lvApprove.equals("AM")){
			if("Sub Branch".equals(typeRequester) || "Branch".equals(typeRequester)){
				rs.setApproverLevel1("Branch");
				rs.setApproverEmpID1(getManager(Reqester,"Branch"));
				rs.setApproverLevel2("Area");
				rs.setApproverEmpID2(getManager(Reqester,"Area"));
			}else if("Area".equals(typeRequester)){
				rs.setApproverLevel1("Area");
				rs.setApproverEmpID1(getManager(Reqester,"Area"));
			}else{
				rs.setErrorMsg("Error lvApprover:"+lvApprove+" typeRequester:"+typeRequester);
			}
			
		}else if(lvApprove.equals("NM")){
			if("Sub Branch".equals(typeRequester) || "Branch".equals(typeRequester)){
				rs.setApproverLevel1("Branch");
				rs.setApproverEmpID1(getManager(Reqester,"Branch"));
				rs.setApproverLevel2("Area");
				rs.setApproverEmpID2(getManager(Reqester,"Area"));
				rs.setApproverLevel3("Region");
				rs.setApproverEmpID3(getManager(Reqester,"Region"));
			}else if("Area".equals(typeRequester)){
				rs.setApproverLevel1("Area");
				rs.setApproverEmpID1(getManager(Reqester,"Area"));
				rs.setApproverLevel2("Region");
				rs.setApproverEmpID2(getManager(Reqester,"Region"));
			}else if("Region".equals(typeRequester)){
				rs.setApproverLevel1("Region");
				rs.setApproverEmpID1(getSupervisor(Reqester));
			}else {
				rs.setErrorMsg("Error lvApprover:"+lvApprove+" typeRequester:"+typeRequester);
			}
			
		}else if(lvApprove.equals("EVP")){
			if("Sub Branch".equals(typeRequester) || "Branch".equals(typeRequester)){
				rs.setApproverLevel1("Branch");
				rs.setApproverEmpID1(getManager(Reqester,"Branch"));
				rs.setApproverLevel2("Area");
				rs.setApproverEmpID2(getManager(Reqester,"Area"));
				rs.setApproverLevel3("EVP");
				rs.setApproverEmpID3(getEVP(Reqester));
			}else if("Area".equals(typeRequester)){
				rs.setApproverLevel1("Area");
				rs.setApproverEmpID1(getManager(Reqester,"Area"));
				rs.setApproverLevel2("Region");
				rs.setApproverEmpID2(getManager(Reqester,"Region"));
				rs.setApproverLevel3("EVP");
				rs.setApproverEmpID3(getEVP(Reqester));
				
			}else {
				rs.setApproverLevel1("EVP");
				rs.setApproverEmpID1(getEVP(Reqester));
			}
			
		}
		
		if("Y".equals(this.escalate)){
			escalation(lvApprove,typeRequester,Reqester);
		}
		
	}
	
	public boolean escalation(String lvApprove,String typeRequester,String Reqester){

		if(lvApprove.equals("BM")){
			if("Sub Branch".equals(typeRequester) || "Branch".equals(typeRequester)){
				rs.setApproverLevel1("Area");
				rs.setApproverEmpID1(getManager(Reqester,"Area"));
				
			}
		}else if(lvApprove.equals("AM")){
			if("Sub Branch".equals(typeRequester) || "Branch".equals(typeRequester)){
				rs.setApproverLevel1(rs.getApproverLevel2());
				rs.setApproverEmpID1(rs.getApproverEmpID2());
				
				rs.setApproverLevel2("");
				rs.setApproverEmpID2("");
				
			}else if("Area".equals(typeRequester)){
				rs.setApproverLevel1("Region");
				rs.setApproverEmpID1(getManager(Reqester,"Region"));
				
			}
		}else if(lvApprove.equals("NM")){
			if("Sub Branch".equals(typeRequester) || "Branch".equals(typeRequester)){
				rs.setApproverLevel1(rs.getApproverLevel2());
				rs.setApproverEmpID1(rs.getApproverEmpID2());
				
				rs.setApproverLevel2(rs.getApproverLevel3());
				rs.setApproverEmpID2(rs.getApproverEmpID3());
				
				rs.setApproverLevel3("");
				rs.setApproverEmpID3("");
				
			}else if("Area".equals(typeRequester)){
				rs.setApproverLevel1(rs.getApproverLevel2());
				rs.setApproverEmpID1(rs.getApproverEmpID2());
				
				rs.setApproverLevel2("");
				rs.setApproverEmpID2("");
				
			}
		}
		
		return true;
	}
	
	
	public static void main(String args[]){
		
//		String path = "D:\\work\\rbfront\\reqid.txt";
//		
//		File file = new File(path);
//		
//		try {
//			BufferedReader br = new BufferedReader(new FileReader(file));
//			String line;
//			while ((line = br.readLine()) != null) {
//				ReqRepository rpstr = new ReqRepository();
//				//rpstr.setRepsitoryByRef("20170400477N");
//				rpstr.setRepsitoryByRef(line);
//				String checkMoveBranch =  checkMoveBranch(rpstr);
//				System.out.println("checkMoveBranch " + checkMoveBranch);
//			}
//			br.close();
//		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		// 
		System.out.println("RequestApprover.class - starting");
		
		//System.out.println("checkMoveBranch " + checkMoveBranch);
		
	}
	
}

















