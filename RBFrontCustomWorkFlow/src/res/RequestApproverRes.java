package res;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import db.DatbaseConnection;

@XmlRootElement
public class RequestApproverRes {
	
	String RefNo;
	String ApproverLevel1;
	String ApproverEmpID1;
	String ApproverLevel2;
	String ApproverEmpID2;
	String ApproverLevel3;
	String ApproverEmpID3;
	List<String> VerifyUserAO = new ArrayList<String>();
	List<String> VerifyUserNO = new ArrayList<String>();
	String ErrorMsg;
	String Escalation;

	public String getEscalation() {
		return Escalation;
	}

	public void setEscalation(String escalation) {
		Escalation = escalation;
	}

	public String getRefNo() {
		return RefNo;
	}

	public void setRefNo(String refNo) {
		RefNo = refNo;
	}

	public String getApproverLevel1() {
		return ApproverLevel1;
	}

	public void setApproverLevel1(String approverLevel1) {
		ApproverLevel1 = approverLevel1;
	}

	public String getApproverEmpID1() {
		return ApproverEmpID1;
	}

	public void setApproverEmpID1(String approverEmpID1) {
		ApproverEmpID1 = approverEmpID1;
	}

	public String getApproverLevel2() {
		return ApproverLevel2;
	}

	public void setApproverLevel2(String approverLevel2) {
		ApproverLevel2 = approverLevel2;
	}

	public String getApproverEmpID2() {
		return ApproverEmpID2;
	}

	public void setApproverEmpID2(String approverEmpID2) {
		ApproverEmpID2 = approverEmpID2;
	}

	public String getApproverLevel3() {
		return ApproverLevel3;
	}

	public void setApproverLevel3(String approverLevel3) {
		ApproverLevel3 = approverLevel3;
	}

	public String getApproverEmpID3() {
		return ApproverEmpID3;
	}

	public void setApproverEmpID3(String approverEmpID3) {
		ApproverEmpID3 = approverEmpID3;
	}

	public String getErrorMsg() {
		return ErrorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		ErrorMsg = errorMsg;
	}

	public RequestApproverRes() {
		RefNo="";
		ApproverLevel1="";
		ApproverEmpID1="";
		ApproverLevel2="";
		ApproverEmpID2="";
		ApproverLevel3="";
		ApproverEmpID3="";
		VerifyUserAO.add("");
		VerifyUserNO.add("");
		ErrorMsg="";
		Escalation="N";
	}
	
	public RequestApproverRes(String refNo){
		
			System.out.println("refNo from ws approve :"+refNo);	
			Connection connect = DatbaseConnection.getConnectionMySQL();
			try{
				  				
				  PreparedStatement preparedStatement = connect
			          .prepareStatement("select * from tbldt_wsrequestapproverrs r where r.RefNo like ? ");

			      preparedStatement.setString(1, refNo);		      
			      ResultSet resultSet = preparedStatement.executeQuery();
					
				  if(resultSet.next()){
						RefNo = resultSet.getString("RefNo"); 
						ApproverLevel1 = resultSet.getString("ApproverLevel1");
						ApproverEmpID1 = resultSet.getString("ApproverUserID1");
						ApproverLevel2 = resultSet.getString("ApproverLevel2");
						ApproverEmpID2 = resultSet.getString("ApproverUserID2");
						ApproverLevel3 = resultSet.getString("ApproverLevel3");
						ApproverEmpID3 = resultSet.getString("ApproverUserID3");
						Escalation = resultSet.getString("Escalation");
						
						String ao = resultSet.getString("VerifyUserAO");
						ao = ao.replace("[", "");
						ao = ao.replace("]", "");
						String ao2[] = ao.split(",");
						for(String temp : ao2){
							VerifyUserAO.add(temp);
						}
						
						String no = resultSet.getString("VerifyUserNO");
						no = no.replace("[", "");
						no = no.replace("]", "");
						String no2[] = no.split(",");
						for(String temp : no2){
							VerifyUserNO.add(temp);
						}
						
				  }else{
					  
				  }
			      
				}catch(Exception ex){
					ErrorMsg = "returnRequestApproverRes:"+ex.getMessage();
					ex.getStackTrace();
				}finally{
					try {	connect.close();} catch (SQLException e) {	}				
				}

	}
	
	public List<String> getVerifyUserAO() {
		return VerifyUserAO;
	}

	public void setVerifyUserAO(List<String> verifyUserAO) {
		VerifyUserAO = verifyUserAO;
	}

	public List<String> getVerifyUserNO() {
		return VerifyUserNO;
	}

	public void setVerifyUserNO(List<String> verifyUserNO) {
		VerifyUserNO = verifyUserNO;
	}

}
