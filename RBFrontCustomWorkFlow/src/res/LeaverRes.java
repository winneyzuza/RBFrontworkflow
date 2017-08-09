package res;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LeaverRes {
	
	String Stauts;
	String ErrorMsg;
	String RefNo;
	
	public String getStauts() {
		return Stauts;
	}

	public void setStauts(String stauts) {
		Stauts = stauts;
	}

	public String getErrorMsg() {
		return ErrorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		ErrorMsg = errorMsg;
	}

	public String getRefNo() {
		return RefNo;
	}

	public void setRefNo(String refNo) {
		RefNo = refNo;
	}

	public LeaverRes() {
		// TODO Auto-generated constructor stub
	}

}
