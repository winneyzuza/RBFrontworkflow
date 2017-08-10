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
		//	System.out.println("WS.FormValidate.ProcessNumRole.checkNumRole: Rule Fetch Size=" + Integer.toString(resultSet.getFetchSize()));
			while(resultSet.next()){
				DayBranch = resultSet.getString("DayBranch");
				Operation = resultSet.getString("Operation");
				try {
					Value = Integer.parseInt(resultSet.getString("Value"));
				} catch(Exception e){
					Value = 0;
					System.out.println("WS.FromValidate.ProcessNumRole.checkNumRole: Parse INT error - VALUE");
				}
					
				Action = resultSet.getString("Action");
				if(DayBranch.isEmpty()) {
					DayBranch = "All";
				}
				if (Operation.isEmpty()) {
					System.out.println("WS.FormValidate.ProcessNumRole.checkNumRole: Operation value is empty!");
				}
				if (Value <= 0) {
					System.out.println("WS.FormValidate.ProcessNumRole.checkNumRole: Value value is 0!");
				}
				if (Action.isEmpty()) {
					System.out.println("WS.FormValidate.ProcessNumRole.checkNumRole: Action value is empty!");
				}
				System.out.println("WS.FormValidate.ProcessNumRole.checkNumRole: DayBranch-" + DayBranch + " Operator-" + Operation + " Value-" + Value + " Action-" + Action + " <<<<<");
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
			System.out.println("WS.FormValidate.ProcessNumRole.checkNumRole: SQL Exception found - select command");
			e.printStackTrace();
		}finally{
			try {	
				connect.close();
			} catch (SQLException e) {	
				System.out.println("WS.FormValidate.ProcessNumRole.checkNumRole: SQL Exception found - close command");
			}			
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
