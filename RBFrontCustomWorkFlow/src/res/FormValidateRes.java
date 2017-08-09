package res;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FormValidateRes {

	String RefNo;
	String ErrorMsg;
	
	public FormValidateRes() {
		// TODO Auto-generated constructor stub
	}

	public String getRefNo() {
		return RefNo;
	}

	public void setRefNo(String refNo) {
		RefNo = refNo;
	}

	public String getErrorMsg() {
		return ErrorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		ErrorMsg = errorMsg;
	}

}
