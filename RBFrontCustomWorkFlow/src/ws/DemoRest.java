package ws;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import res.FormValidateRes;

@Path("demo1")
public class DemoRest {


	public DemoRest() {
		// TODO Auto-generated constructor stub
	}
	
	@GET
	@Path("sum/{a}/{b}")
	@Produces(MediaType.TEXT_PLAIN)
	public String sum(@PathParam("a") int a,@PathParam("b") int b){
		
		return String.valueOf(a+b);
	}
	
	@GET
	@Path("formValidate/{a}/{b}")
	@Produces(MediaType.APPLICATION_JSON)
	public FormValidateRes formValidate(@PathParam("a") String a,@PathParam("b") String b){
		FormValidateRes rs = new FormValidateRes();
			rs.setRefNo("1234");
			rs.setErrorMsg("test");
		return rs;
	}

}
