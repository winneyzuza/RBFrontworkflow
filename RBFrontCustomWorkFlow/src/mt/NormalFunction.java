package mt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NormalFunction {

	public NormalFunction() {
		// TODO Auto-generated constructor stub
	}
	
	public static String AddDate1(String dd){

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar b = Calendar.getInstance();
		try {
		    b.setTime(sdf.parse(dd));
		} catch (ParseException e) {
		    e.printStackTrace();
		} 
		
		b.add(Calendar.DATE, 1);

		return sdf.format(b.getTime());
	}
	
	public static String chooseApprover(String a1,String a2){
		String result = "";
		
		if("EVP".equals(a1) || "EVP".equals(a2))
			result = "EVP";
		else if("NM".equals(a1) || "NM".equals(a2))
			result = "NM";
		else if("BM".equals(a1) || "BM".equals(a2))
			result = "BM";
		else if("Supervisor".equals(a1) || "Supervisor".equals(a2))
			result = "Supervisor";
			
		return result;
	}
	
	public static boolean checkApproverReject(String Action){
		boolean approverReject = true;
		if(Action.equals("Reject") || Action.equals("Cond.Reject")){
			approverReject = false;
		}
		
		return approverReject;
	}
		
	public static String convertPositionTecnicalToUser(String tecnical){
		String PositionUser = tecnical;
		String temp="";
		if(tecnical.equals("BM")||tecnical.equals("SBM")||tecnical.equals("ABM")||tecnical.equals("TEMP+SS")){
			PositionUser="SSC";
		}else if(tecnical.equals("ABM+CA")){
			PositionUser="SSC+CA";
		}else if(tecnical.equals("ABM+BAO1")){
			PositionUser="SSC+AT";
			
		}else if(tecnical.equals("SBAO")){
			PositionUser="SC";
		}else if(tecnical.equals("SBAO+CA")){
			PositionUser="SC+CA";
		}else if(tecnical.equals("BAO1+SBAO")){
			PositionUser="SC+AT";
		
		}else if(tecnical.equals("BAO1")){
			PositionUser="AT";
		}else if(tecnical.equals("TEMP+AT")){
			PositionUser="AT";
		}else if(tecnical.equals("TEMP+CA")){
			PositionUser="CA";
		
		}else if(tecnical.equals("ABM+AT+CA")){
			PositionUser="SSC+AT+CA";
		}else{
			String posAll[] = PositionUser.split("\\+");
			for(int i=0;i<posAll.length;i++){
				if(i>0){
					temp=temp+"+";
				}
				temp = temp+converPositionSingle(posAll[i]);
			}

			PositionUser = temp;
		}
				
		return PositionUser;
	}
	
	public static String converPositionSingle(String p){
		String rs = p;
		if("BAO1".equals(p)){
			rs= "AT";
		}else if("SBAO".equals(p)){
			rs= "SC";
		}else if(p.equals("BM")||p.equals("SBM")||p.equals("ABM")){
			rs= "SSC";
		}
		
		return rs;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String x = "EVP, Branch Network XXXXX";
		System.out.println(x.indexOf("nch Network"));

	}

}
