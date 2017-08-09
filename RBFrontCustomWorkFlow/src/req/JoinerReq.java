package req;

public class JoinerReq {
	
	private String EmpID;
	private String EmpJobTitle;
	private String EffectiveStartDate;
	private String Position;
	private String Branch;

	public JoinerReq() {
		// TODO Auto-generated constructor stub
	}

	public String getEmpID() {
		return EmpID;
	}

	public void setEmpID(String empID) {
		EmpID = empID;
	}

	public String getEmpJobTitle() {
		return EmpJobTitle;
	}

	public void setEmpJobTitle(String empJobTitle) {
		EmpJobTitle = empJobTitle;
	}

	public String getEffectiveStartDate() {
		return EffectiveStartDate;
	}

	public void setEffectiveStartDate(String effectiveStartDate) {
		EffectiveStartDate = effectiveStartDate;
	}

	public String getPosition() {
		return Position;
	}

	public void setPosition(String position) {
		Position = position;
	}

	public String getBranch() {
		return Branch;
	}

	public void setBranch(String branch) {
		Branch = branch;
	}

}
