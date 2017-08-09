package validate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DatbaseConnection;
import db.DatbaseConnectionMsSQL;
import mt.BranchInfo;
import mt.IAM;
import mt.NormalFunction;
import req.FormValidateReq;

public class ProcessGroup {
	
	private String msgError="";
	private String approver="";
	
	public String getApprover() {
		return approver;
	}

	public String getMsgError() {
		return msgError;
	}
	
	public ProcessGroup() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean checkGroup(FormValidateReq rq){

		boolean flag = true;
		
		int fwGroup = BranchInfo.getGroupBranch(rq.getFwdBranch());
		int revGroup = BranchInfo.getGroupBranch(rq.getRevBranch());

		flag = flag && checkGroupMain(fwGroup,rq.getEmpID(),rq.getFwdPosition()," For. Branch");
		flag = flag && checkGroupMain(revGroup,rq.getEmpID(),rq.getRevPosition()," Rev. Branch");
		
		return flag;
	}
	
	
	public boolean checkGroupMain(int GroupB,String empID,String position,String ForwordOrBack){
		boolean flag = true;
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			
			PreparedStatement preparedStatement = connect
			          .prepareStatement("select DayBranch,Operation,Value,Action from tblmt_validation v where v.Validate = 'Group"+GroupB+"' and v.Active = 'Y' ");

			ResultSet resultSet = preparedStatement.executeQuery();
//			System.out.println(preparedStatement);
			while(resultSet.next()){

				String lvJob = resultSet.getString("DayBranch");
				String Value = resultSet.getString("Value");
				String Action = resultSet.getString("Action");
				
				if("Corp3".equals(lvJob)){ //เงือนไขในใบคำสั่ง group3 corp3 up

					boolean personalBankerOrTeller = IAM.isPersonalBankerOrTeller(empID);
					
					int lvCorp = IAM.getlvCorpByEmp(empID);
					int lvCorpStaff3 = getlvCorp("Staff 3");
					boolean overStaff3 = (lvCorp==lvCorpStaff3);	// employee must lvCorp == staff3	
					if(personalBankerOrTeller && overStaff3){
						if( position.equals(Value) ){							
							if(NormalFunction.checkApproverReject(Action)){
								this.approver = NormalFunction.chooseApprover(this.approver,Action);
								flag = flag || true;
							}else{
								flag = false;
								this.msgError = this.msgError+ForwordOrBack+" เงือนไข group"+GroupB+" ผู้ร้องขอมีตำแหน่ง "+lvCorp+" ไม่สามารถขอสิทธ์ "+position+" ได้";
							}
						}						
					}
					continue;
				}//end if corp3
				
				if("Corp2".equals(lvJob)){ //เงือนไขในใบคำสั่ง group3 corp2 lower
					
					int lvCorp = IAM.getlvCorpByEmp(empID);
					int lvCorpStaff2 = getlvCorp("Staff 2");
					boolean lowStaff2 = (lvCorp<=lvCorpStaff2);	// employee must lvCorp <= staff2	
					if(lowStaff2){
						if( position.equals(Value) ){							
							if(NormalFunction.checkApproverReject(Action)){
								this.approver = NormalFunction.chooseApprover(this.approver,Action);
								flag = flag || true;
							}else{
								flag = false;
								this.msgError = this.msgError+ForwordOrBack+" เงือนไข group"+GroupB+" ผู้ร้องขอมีตำแหน่ง "+lvCorp+" ไม่สามารถขอสิทธ์ "+position+" ได้";
							}
						}						
					}
					continue;
				}//end if corp2
				
//				System.out.println("B"+getLvjobEmp(empID)+lvJob);
				if("All".equals(lvJob) || Integer.parseInt(getLvjobEmp(empID)) > Integer.parseInt(lvJob) ){
					
					if(position.equals(Value)){
						if(NormalFunction.checkApproverReject(Action)){
							this.approver = NormalFunction.chooseApprover(this.approver,Action);
							flag = flag || true;
						}else{
							flag = false;
							this.msgError = this.msgError+ForwordOrBack+" เงือนไข group"+GroupB+" ไม่สามารถขอสิทธ์ "+position+" ได้";
						}
					}
				}//end if All
				
			}		
			
			preparedStatement.close();
			connect.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}
		return flag;
	}
	
	public String getLvjobEmp(String empID){
			String jobTitleName ="";
			String lvJob = "";
			
			Connection connect = DatbaseConnectionMsSQL.getConnectionMsSQL();
			try {
				
				PreparedStatement preparedStatement = connect
				          .prepareStatement("select h.Job_Title_EN JOBNAME from IAM h where h.employeeID = ?");
				preparedStatement.setString(1, empID);
				ResultSet resultSet = preparedStatement.executeQuery();
				resultSet.next();
				
				jobTitleName = resultSet.getString("JOBNAME");
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					connect.close();
				} catch (SQLException e) {
				}			
			}
			
			connect = DatbaseConnection.getConnectionMySQL();
			try {
				
				PreparedStatement preparedStatement = connect
				          .prepareStatement("select lv lvJob from tblmt_lvjob  where JobName like ? ");
				preparedStatement.setString(1, jobTitleName);
				ResultSet resultSet = preparedStatement.executeQuery();
				
				//System.out.println("checkBranch result: "+resultSet.getInt(1));
				if( resultSet.next())
					lvJob = resultSet.getString("lvJob");
				
				preparedStatement.close();
				connect.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					connect.close();
				} catch (SQLException e) {
				}			
			} 
		
		return lvJob;
	}
	
	////////////////////////////////////// condition 2 //////////////////////
	
	public int getlvCorp(String corpN){
		int lv = 999;		
  
		Connection connect = DatbaseConnection.getConnectionMySQL();
		try {
			String sql = "select lv from tblmt_lvcorpstaff l where l.CorpStaff like ? ";
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, corpN);
			
			ResultSet rs = preparedStatement.executeQuery();
//			System.out.println(preparedStatement);
			if(rs.next()){
				lv = Integer.parseInt(rs.getString("lv"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {	connect.close();} catch (SQLException e) {	}			
		}	
		return lv;
	}
	
}
