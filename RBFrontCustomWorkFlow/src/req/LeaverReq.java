package req;

public class LeaverReq {

	private String EmpID;
	private String EffectiveStartDate;
	private String Branch;
	
	public LeaverReq() {
		// TODO Auto-generated constructor stub
	}

	public String getEmpID() {
		return EmpID;
	}

	public void setEmpID(String empID) {
		EmpID = empID;
	}

	public String getEffectiveStartDate() {
		return EffectiveStartDate;
	}

	public void setEffectiveStartDate(String effectiveStartDate) {
		EffectiveStartDate = effectiveStartDate;
	}

	public String getBranch() {
		return Branch;
	}

	public void setBranch(String branch) {
		Branch = branch;
	}

}
