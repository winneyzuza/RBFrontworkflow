package req;

public class ReqStatusUpdateReq {

	private String ReqRefID;
	private String CurrStatus;
	private String NewStatus;
	private String ApproverID;
	
	public ReqStatusUpdateReq() {
		// TODO Auto-generated constructor stub
	}

	public String getReqRefID() {
		return ReqRefID;
	}

	public void setReqRefID(String reqRefID) {
		ReqRefID = reqRefID;
	}

	public String getCurrStatus() {
		return CurrStatus;
	}

	public void setCurrStatus(String currStatus) {
		CurrStatus = currStatus;
	}

	public String getNewStatus() {
		return NewStatus;
	}

	public void setNewStatus(String newStatus) {
		NewStatus = newStatus;
	}

	public String getApproverID() {
		return ApproverID;
	}

	public void setApproverID(String approverID) {
		ApproverID = approverID;
	}


}
