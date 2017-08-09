package validate;

public class CheckFile {

	public CheckFile() {
		// TODO Auto-generated constructor stub
	}
	
	public static boolean checkBranch(String branchID){
		boolean flag = true;
//			if(!(branchID.trim().length()<6))
//				flag = false;
		
			if(branchID.trim().equals(""))
				flag = false;
			
//			try {
//			    int op1 = Integer.parseInt(branchID);
//			} catch (NumberFormatException e) {
//			    System.out.println("validate.checkFile.checkBranch() error branchID not int");
//			    flag = false;
//			}
			
		return flag;
	}
	
	public static boolean checkEmpID(String empID){
		boolean flag = true;
//			if(!(empID.trim().length()<6))
//				flag = false;
		
			if(empID.trim().equals(""))
				flag = false;

		
//			try {
//			    int op1 = Integer.parseInt(branchID);
//			} catch (NumberFormatException e) {
//			    System.out.println("validate.checkFile.checkBranch() error branchID not int");
//			    flag = false;
//			}
			
		return flag;
	}
	
	public static boolean checkTerminal(String terminalID){
		boolean flag = true;
			if(!(terminalID.trim().length()<6))
				flag = false;
			
		return flag;
	}
	
	public static boolean checkYN(String reserved){
		boolean flag = true;
			if(!(reserved.trim().length()<2))
				flag = false;
			if(!(reserved.trim().equals("Y")||reserved.trim().equals("N")))
				flag = false;
//		System.out.println("reserved"+reserved);
			
		return flag;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
