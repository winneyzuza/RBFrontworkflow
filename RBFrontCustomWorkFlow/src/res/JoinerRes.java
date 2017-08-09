package res;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JoinerRes {
	
	String Stauts;
	String ErrorMsg;
	String RefNo;

	public JoinerRes() {
		// TODO Auto-generated constructor stub
	}

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

}
