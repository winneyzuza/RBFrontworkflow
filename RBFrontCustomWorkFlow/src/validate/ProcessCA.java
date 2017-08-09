package validate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import db.DatbaseConnection;
import req.FormValidateReq;
import validate.Validation;
import mt.BranchInfo;
import mt.NormalFunction;

public class ProcessCA {
	
	private String msgError="";
	private String approver="";
	
	public String getApprover() {
		return approver;
	}

	public String getMsgError() {
		return msgError;
	}



	public ProcessCA() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public boolean checkCA(FormValidateReq rq){
		boolean flag = true;
		BranchInfo BranchF = new BranchInfo(rq.getFwdBranch());
		BranchInfo BranchR = new BranchInfo(rq.getRevBranch());
		
		String empID = rq.getEmpID();
		String sDate = rq.getFwdEffectiveStartDate();
		String eDate = rq.getFwdEffectiveEndDate();
		
		Validation pCA = new Validation();
		pCA.setValidateByValidateDayBranch("NumCA", BranchF.getOperationDay());
		
		int reqCAF = rq.getFwdPosition().indexOf("CA");
		int reqCAR = rq.getRevPosition().indexOf("CA");
		
		if(reqCAF>=0){
//			int numCAF = getNumCA(BranchF);
//			System.out.println("countCA:"+numCAF);
			
			eDate = NormalFunction.AddDate1(eDate);
			int countCA =0;
			while(!sDate.equals(eDate)){
				countCA = getCountCAbyBranch(sDate,BranchF.getOrgCode(),empID)+1;
				System.out.println("countCAF:"+countCA+":"+sDate);
				if( !checkOperationCA(pCA,countCA) ){
					flag = false;
					this.msgError = this.msgError +"สาขาที่โยกย้ายจำนวน  CA เกิน ";
					break;
				}
					
				sDate = NormalFunction.AddDate1(sDate);
			}
				
		}
		
		if(reqCAR>0){
//			int numCAR = getNumCA(BranchF);
			int countCA =0;
			
			for(int i=0;i<3;i++){
				countCA = getCountCAbyBranch(sDate,BranchR.getOrgCode(),empID)+1;
				System.out.println("countCAR:"+countCA+":"+sDate);
				if( !checkOperationCA(pCA,countCA) ){
					flag = false;
					this.msgError = this.msgError +"สาขาrevokeจำนวน   CA เกิน ";
					break;
				}					
				sDate = NormalFunction.AddDate1(sDate);
			}
				
		}		
		
		if(!flag){
			if(NormalFunction.checkApproverReject(pCA.getAction())){
				this.approver = pCA.getAction();
				flag = true;
			}
		}
		
		return flag;
	}
	
	public boolean checkOperationCA(Validation valid,int countCA){
		boolean result = true;
			
		int value = Integer.parseInt(valid.getValue());
			switch(valid.getOperation()){
				case "<":{
					if(countCA < value){ result = false;	}
					break;}
				case "<=":{
					if(countCA <= value){ result = false;	}
					break;}
				case ">":{
					if(countCA > value){ result = false;	}
					break;}
				case ">=":{
					if(countCA >= value){ result = false;	}
					break;}
				case "=":{
					if(countCA == value){ result = false;	}
					break;}
				case "!=":{
					if(countCA != value){ result = false;	}
					break;}
			}
		
		return result;
	}
	
	public int getCountCAbyBranch(String dateT,String branch,String empID){
		int count =0;
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			//// get All			
			PreparedStatement preparedStatement = connect
			          .prepareStatement("select count(*) from tblmt_employeeinfo e "+
			        		  " where e.BusinessRole like '%CA%' "+
			        		  " and e.EmpID <> ? "+
			        		  " and e.BranchID = ?");
			preparedStatement.setString(1, empID);
			preparedStatement.setString(2, branch);		
			ResultSet resultSet = preparedStatement.executeQuery();			
			if(resultSet.next())
				count = (int)resultSet.getInt(1);
//			System.out.println("A:+"+count);
			
			/// + between forward
			preparedStatement = connect
			          .prepareStatement("select count(*) from tbldt_reqrepository r join tblmt_employeeinfo e on e.EmpID=r.EmpID "+
			        		  " where ? BETWEEN r.EffStartDate and r.EffEndDate "+
			        		  " and e.BusinessRole not like '%CA%' "+
			        		  " and r.FwdPosition like '%CA%' "+
			        		  " and e.EmpID <> ? "+
			        		  " and r.Status not in ('R') "+
			        		  " and r.FwdBranch like ?");
			preparedStatement.setString(1, dateT);
			preparedStatement.setString(2, empID);
			preparedStatement.setString(3, branch);			
			resultSet = preparedStatement.executeQuery();			
			if(resultSet.next())
				count = count + (int)resultSet.getInt(1);
			//System.out.println(preparedStatement);
//			System.out.println("B:+"+(int)resultSet.getInt(1));
			
			
			/// + since revert
			preparedStatement = connect
			          .prepareStatement("select count(*) from tbldt_reqrepository r join tblmt_employeeinfo e on e.EmpID=r.EmpID "+
			        		  " where ? > r.EffEndDate "+
			        		  " and e.BusinessRole not like '%CA%' "+
			        		  " and r.RevPosition like '%CA%' "+
			        		  " and e.EmpID <> ? "+
			        		  " and r.Status not in ('R') "+
			        		  " and r.FwdBranch like ?");
			preparedStatement.setString(1, dateT);
			preparedStatement.setString(2, empID);
			preparedStatement.setString(3, branch);			
			resultSet = preparedStatement.executeQuery();			
			if(resultSet.next())
				count = count + (int)resultSet.getInt(1);			
//			System.out.println("C:+"+(int)resultSet.getInt(1));
			
			// - in forward
			preparedStatement = connect
			          .prepareStatement("select count(*) from tbldt_reqrepository r join tblmt_employeeinfo e on e.EmpID=r.EmpID "+
					" where ? BETWEEN r.EffStartDate and r.EffEndDate "+
					" and e.BusinessRole like '%CA%' "+
					" and r.FwdPosition not like '%CA%' "+
					" and e.EmpID <> ? "+
					" and r.Status not in ('R') "+
					" and r.FwdBranch like ? ");			
			preparedStatement.setString(1, dateT);
			preparedStatement.setString(2, empID);
			preparedStatement.setString(3, branch);			
			resultSet = preparedStatement.executeQuery();			
			if(resultSet.next())
				count = count - (int)resultSet.getInt(1);		
//			System.out.println("D:-"+(int)resultSet.getInt(1));
			
			
			// - since rever
			preparedStatement = connect
	          .prepareStatement("select count(*) from tbldt_reqrepository r join tblmt_employeeinfo e on e.EmpID=r.EmpID "+
			" where ? > r.EffStartDate "+
			" and e.BusinessRole like '%CA%' "+
			" and r.RevPosition not like '%CA%' "+
			" and e.EmpID <> ? "+
			" and r.Status not in ('R') "+
			" and r.FwdBranch like ? ");			
			preparedStatement.setString(1, dateT);
			preparedStatement.setString(2, empID);
			preparedStatement.setString(3, branch);	
			resultSet = preparedStatement.executeQuery();			
			if(resultSet.next())
				count = count - (int)resultSet.getInt(1);
//			System.out.println("E:-"+(int)resultSet.getInt(1));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}
		
		
		return count;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		FormValidate a = new FormValidate();
//		FormValidateReq test = new FormValidateReq();
//		test.setEmpID("90051");
//		test.setFwdBranch("0001");
//		test.setFwdEffectiveStartDate("2016-09-25");
//		test.setFwdEffectiveEndDate("2016-09-25");
////		
////		a.checkCA(test);
//		
////		a.restDay(test);
		
		String a = "CA";
		System.out.println(a.indexOf("CA"));
		
			
	}
}
