package validate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DatbaseConnection;
import mt.BranchInfo;
import mt.NormalFunction;
import req.FormValidateReq;

public class ProcessNumRole {
	
	private String msgError="";
	private String approver="";
	
	public String getApprover() {
		return approver;
	}

	public String getMsgError() {
		return msgError;
	}
	
	public ProcessNumRole() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean checkNumRole(FormValidateReq rq){
		boolean flag = true;
		String DayBranch = "";
		String Operation = "";
		int Value = 0;
		String Action = "";
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			
			PreparedStatement preparedStatement = connect
			          .prepareStatement("select DayBranch,Operation,Value,Action from tblmt_validation v where v.Validate = 'NumRole' and v.Active = 'Y'");
			//preparedStatement.setString(1, branch);
			ResultSet resultSet = preparedStatement.executeQuery();
			//System.out.println(preparedStatement);
			while(resultSet.next()){
				DayBranch = resultSet.getString("DayBranch");
				Operation = resultSet.getString("Operation");
				Value = Integer.parseInt(resultSet.getString("Value"));
				Action = resultSet.getString("Action");
				
				if("All".equals(DayBranch)){
					flag = flag && checkOperationNumRole(rq.getFwdPosition(),Operation,Value);
					flag = flag && checkOperationNumRole(rq.getRevPosition(),Operation,Value);
				}else{
					BranchInfo bf = new BranchInfo(rq.getFwdBranch());
					if(DayBranch.equals(bf.getOperationDay())){
						flag = flag && checkOperationNumRole(rq.getFwdPosition(),Operation,Value);
						flag = flag && checkOperationNumRole(rq.getRevPosition(),Operation,Value);
					}
				}

				if(!flag){
					if(NormalFunction.checkApproverReject(Action)){
						this.approver = NormalFunction.chooseApprover(this.approver,Action);
						flag = true;
					}else{
						this.msgError = this.msgError +"จำนวนสิทธิ์(role)มีเกินกว่าเงือนไขกำหนด";
					}
				}
				
			}						
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}
		
		return flag;
	}
	
	public boolean checkOperationNumRole(String position,String oper,int value){
		boolean flag = true;
		int size = position.split("\\+").length;
		switch(oper){
			case "<":{
				if(size < value){ flag = false;	}
				break;}
			case "<=":{
				if(size <= value){ flag = false;	}
				break;}
			case ">":{
				if(size > value){ flag = false;	}
				break;}
			case ">=":{
				if(size >= value){ flag = false;	}
				break;}
			case "=":{
				if(size == value){ flag = false;	}
				break;}
			case "!=":{
				if(size != value){ flag = false;	}
				break;}
		}
		
		return flag;
	}
	
}
