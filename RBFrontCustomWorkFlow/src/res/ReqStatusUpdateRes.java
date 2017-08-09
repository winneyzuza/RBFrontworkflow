package res;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReqStatusUpdateRes {

	String ResultCode;
	
	public String getResultCode() {
		return ResultCode;
	}

	public void setResultCode(String resultCode) {
		ResultCode = resultCode;
	}

	public ReqStatusUpdateRes() {
		// TODO Auto-generated constructor stub
	}

}
