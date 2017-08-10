package mt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;
import java.util.ArrayList;
//import java.util.Date;
import java.util.List;

import db.DatbaseConnection;

public class ReqRepository {
	String 	RequestID;
	String	ReqSubmitDate;
	String	RSAReferenceID;
	String	Requestor;
	String	EmpID;
	String	EmpName;
	String	CorpTitleID;
	String	CorpTitleName;
	String	JobTitleID;
	String	JobTitleName;
	String	EffStartDate;
	String	EffEndDate;
	String	CurPosition;
	String	FwdPosition;
	String	RevPosition;
	String	CurLimit;
	String	FwdLimit;
	String	CurBranch;
	String	FwdBrandch;
	String	RevBranch;
	String	Approver;
	String	Status;
	String	LastChange;
	String 	Remark;
	String	CompleteF;
	String	CompleteR;
	
	String  Code;	
	String FwdDiff;
	String RevDiff;
	
	public String getFwdDiff() {
		return FwdDiff;
	}


	public void setFwdDiff(String fwdDiff) {
		FwdDiff = fwdDiff;
	}


	public String getRevDiff() {
		return RevDiff;
	}


	public void setRevDiff(String revDiff) {
		RevDiff = revDiff;
	}


	public String getCompleteF() {
		return CompleteF;
	}


	public void setCompleteF(String completeF) {
		CompleteF = completeF;
	}


	public String getCompleteR() {
		return CompleteR;
	}


	public void setCompleteR(String completeR) {
		CompleteR = completeR;
	}


	
	public ReqRepository() {
		// TODO Auto-generated constructor stub
	}


	public String getRequestID() {
		return RequestID;
	}


	public void setRequestID(String requestID) {
		RequestID = requestID;
	}


	public String getReqSubmitDate() {
		return ReqSubmitDate;
	}


	public void setReqSubmitDate(String reqSubmitDate) {
		ReqSubmitDate = reqSubmitDate;
	}


	public String getRSAReferenceID() {
		return RSAReferenceID;
	}


	public void setRSAReferenceID(String rSAReferenceID) {
		RSAReferenceID = rSAReferenceID;
	}


	public String getRequestor() {
		return Requestor;
	}


	public void setRequestor(String requestor) {
		Requestor = requestor;
	}


	public String getEmpID() {
		return EmpID;
	}


	public void setEmpID(String empID) {
		EmpID = empID;
	}


	public String getEmpName() {
		return EmpName;
	}


	public void setEmpName(String empName) {
		EmpName = empName;
	}


	public String getCorpTitleID() {
		return CorpTitleID;
	}


	public void setCorpTitleID(String corpTitleID) {
		CorpTitleID = corpTitleID;
	}


	public String getCorpTitleName() {
		return CorpTitleName;
	}


	public void setCorpTitleName(String corpTitleName) {
		CorpTitleName = corpTitleName;
	}


	public String getJobTitleID() {
		return JobTitleID;
	}


	public void setJobTitleID(String jobTitleID) {
		JobTitleID = jobTitleID;
	}


	public String getJobTitleName() {
		return JobTitleName;
	}


	public void setJobTitleName(String jobTitleName) {
		JobTitleName = jobTitleName;
	}


	public String getEffStartDate() {
		return EffStartDate;
	}


	public void setEffStartDate(String effStartDate) {
		EffStartDate = effStartDate;
	}


	public String getEffEndDate() {
		return EffEndDate;
	}


	public void setEffEndDate(String effEndDate) {
		EffEndDate = effEndDate;
	}


	public String getCurPosition() {
		return CurPosition;
	}


	public void setCurPosition(String curPosition) {
		CurPosition = curPosition;
	}


	public String getFwdPosition() {
		return FwdPosition;
	}


	public void setFwdPosition(String fwdPosition) {
		FwdPosition = fwdPosition;
	}


	public String getRevPosition() {
		return RevPosition;
	}


	public void setRevPosition(String revPosition) {
		RevPosition = revPosition;
	}


	public String getCurLimit() {
		return CurLimit;
	}


	public void setCurLimit(String curLimit) {
		CurLimit = curLimit;
	}


	public String getFwdLimit() {
		return FwdLimit;
	}


	public void setFwdLimit(String fwdLimit) {
		FwdLimit = fwdLimit;
	}


	public String getCurBranch() {
		return CurBranch;
	}


	public void setCurBranch(String curBranch) {
		CurBranch = curBranch;
	}


	public String getFwdBrandch() {
		return FwdBrandch;
	}


	public void setFwdBrandch(String fwdBrandch) {
		FwdBrandch = fwdBrandch;
	}


	public String getRevBranch() {
		return RevBranch;
	}


	public void setRevBranch(String revBranch) {
		RevBranch = revBranch;
	}


	public String getApprover() {
		return Approver;
	}


	public void setApprover(String approver) {
		Approver = approver;
	}


	public String getStatus() {
		return Status;
	}


	public void setStatus(String status) {
		Status = status;
	}


	public String getLastChange() {
		return LastChange;
	}


	public void setLastChange(String lastChange) {
		LastChange = lastChange;
	}


	public String getRemark() {
		return Remark;
	}


	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getCode() {
		return Code;
	}


	public void setCode(String code) {
		Code = code;
	}


	public void setRepsitoryByRef(String ref){
		
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		  
		connect = DatbaseConnection.getConnectionMySQL();
		try {	
			preparedStatement = connect
			          .prepareStatement("select *,TIMESTAMPDIFF(DAY,r.EffEndDate,CURDATE()) AS FwdDiff,TIMESTAMPDIFF(DAY,r.EffStartDate,CURDATE()) AS RevDiff from tbldt_reqrepository r where r.RequestID like ?");
			preparedStatement.setString(1, ref);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				this.setRequestID(resultSet.getString("RequestID"));
				this.setFwdPosition(resultSet.getString("FwdPosition"));
				this.setRevPosition(resultSet.getString("RevPosition"));
				this.setEmpID(resultSet.getString("EmpID"));
				this.setFwdBrandch(resultSet.getString("FwdBranch"));
				this.setRevBranch(resultSet.getString("RevBranch"));		
				this.setRequestor(resultSet.getString("Requestor"));
				this.setStatus(resultSet.getString("Status"));
				this.setReqSubmitDate(resultSet.getString("ReqSubmitDate"));
				
				this.setCurBranch(resultSet.getString("CurBranch"));
				this.setCurPosition(resultSet.getString("CurPosition"));		
				this.setCorpTitleID(resultSet.getString("CorpTitleID"));
				this.setCorpTitleName(resultSet.getString("CorpTitleName"));
				
				this.setApprover(resultSet.getString("Approver"));
				this.setCompleteF(resultSet.getString("CompleteF"));
				this.setCompleteR(resultSet.getString("CompleteR"));
				
				this.setEffEndDate(resultSet.getString("EffEndDate"));
				this.setEffStartDate(resultSet.getString("EffStartDate"));
				
				this.setFwdDiff(resultSet.getString("FwdDiff"));
				this.setRevDiff(resultSet.getString("RevDiff"));
				
				this.setCode("000");
			}else{
				this.setCode("999");
				this.setRemark("ref not found");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}

	}
	
public List<ReqRepository> getRepsitoryByEmpID(String empID){
		
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		
		List<ReqRepository> listRespository = new ArrayList<ReqRepository>();  
		
		
		connect = DatbaseConnection.getConnectionMySQL();
		try {
			preparedStatement = connect
			          .prepareStatement("select * from tbldt_reqrepository r where r.EmpID like ? and r.EffStartDate > now() ");
			preparedStatement.setString(1, empID);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()){
				ReqRepository temp = new ReqRepository();
				
	//			temp.setRequestID(resultSet.getString("RequestID"));
	//			temp.setFwdPosition(resultSet.getString("FwdPosition"));
	//			temp.setRevPosition(resultSet.getString("RevPosition"));
	//			temp.setEmpID(resultSet.getString("EmpID"));
	//			temp.setFwdBrandch(resultSet.getString("FwdBranch"));
	//			temp.setRevBranch(resultSet.getString("RevBranch"));		
	//			temp.setRequestor(resultSet.getString("Requestor"));
	//			temp.setStatus(resultSet.getString("Status"));
	//			temp.setReqSubmitDate(resultSet.getString("ReqSubmitDate"));
	//			
	//			temp.setCurBranch(resultSet.getString("CurBranch"));
	//			temp.setCurPosition(resultSet.getString("CurPosition"));		
	//			temp.setCorpTitleID(resultSet.getString("CorpTitleID"));
	//			temp.setCorpTitleName(resultSet.getString("CorpTitleName"));
				
				temp.setEffStartDate(resultSet.getString("EffStartDate"));
				temp.setEffEndDate(resultSet.getString("EffEndDate"));
				
				temp.setCode("000");
				
				listRespository.add(temp);
				
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}
		
		return listRespository;
	}
	
   public static boolean setComplete(String reqID,String modeOutput,String complete){	    
	    boolean flag = true;
	    String sql = "";
	    
	    System.out.println(" complete " + complete);
	    
	    if("Forward".equals(modeOutput)){
	    	sql = "update tbldt_reqrepository r set r.CompleteF = ? where r.RequestID = ? ";
	    }else if("Backward".equals(modeOutput)){
	    	sql = "update tbldt_reqrepository r set r.CompleteR = ? where r.RequestID = ? ";
	    }
	    	
	    
	    Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			if(reqID.indexOf("-") > 0) {
				reqID = reqID.substring(0, reqID.indexOf("-"));
			}
			PreparedStatement	preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, complete);
			preparedStatement.setString(2, reqID);
			
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			flag = false;
			System.out.println("ReqRepository.setComplete Error ");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}
		
		return flag;
		
		
   }
   
   public static boolean setStatus(String reqID,String status){	    
	    boolean flag = true;
	    
	    String sql = "update tbldt_reqrepository r set r.Status = ? where r.RequestID = ? ";
    
	    Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			
			PreparedStatement	preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setString(2, reqID);
			
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			flag = false;
			System.out.println("ReqRepository.setStatus Error ");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}
		
		return flag;
		
		
  }
   
}
