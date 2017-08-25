package servlet;

public class TerminalList {
	private String orgCode;
	private String empID;
	private String terminalID;
	
	@Override
	public String toString() {
		return "TerminalList [orgCode=" + orgCode + ", empID=" + empID + ", terminalID=" + terminalID + "]";
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getEmpID() {
		return empID;
	}
	public void setEmpID(String empID) {
		this.empID = empID;
	}
	public String getTerminalID() {
		return terminalID;
	}
	public void setTerminalID(String terminalID) {
		this.terminalID = terminalID;
	}
	
	public void setTerminalList(String pOrgCode, String pEmpID, String pTterminalID) {
		
		orgCode = pOrgCode;
		empID = pEmpID;
		terminalID = pTterminalID;
	}
	
	public void setTerminalList2(String pOrgCode, String pTterminalID) {
		
		orgCode = pOrgCode;
		terminalID = pTterminalID;
	}
}
