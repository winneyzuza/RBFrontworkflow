package res;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReqStatusQueryRes {
	String RefNo;
	String StatusCode;
	String ReqSumbitDate;
	String EmpID;
	String Requestor;
	String LastChangeDate;
	String ErrorMsg;

	public ReqStatusQueryRes() {
		// TODO Auto-generated constructor stub
	}
	
	public String getRefNo() {
		return RefNo;
	}

	public void setRefNo(String refNo) {
		RefNo = refNo;
	}

	public String getStatusCode() {
		return StatusCode;
	}

	public void setStatusCode(String statusCode) {
		StatusCode = statusCode;
	}

	public String getReqSumbitDate() {
		return ReqSumbitDate;
	}

	public void setReqSumbitDate(String reqSumbitDate) {
		ReqSumbitDate = reqSumbitDate;
	}

	public String getEmpID() {
		return EmpID;
	}

	public void setEmpID(String empID) {
		EmpID = empID;
	}

	public String getRequestor() {
		return Requestor;
	}

	public void setRequestor(String requestor) {
		Requestor = requestor;
	}

	public String getLastChangeDate() {
		return LastChangeDate;
	}

	public void setLastChangeDate(String lastChangeDate) {
		LastChangeDate = lastChangeDate;
	}

	public String getErrorMsg() {
		return ErrorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		ErrorMsg = errorMsg;
	}


}
