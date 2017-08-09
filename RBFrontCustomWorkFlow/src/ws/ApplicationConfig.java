package ws;

import java.util.Set;

import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("rest")
public class ApplicationConfig extends Application  {

	
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resources = new java.util.HashSet<>();
		addRestResourceClasses(resources);
		return resources;
	}
	
	private void addRestResourceClasses(Set<Class<?>> resources) {
		resources.add(ws.DemoRest.class);
		resources.add(ws.FormValidate.class);
		resources.add(ws.RequestApprover.class);
		resources.add(ws.ReqStatusQuery.class);
		resources.add(ws.ReqStatusUpdate.class);
		resources.add(ws.Joiner.class);
		resources.add(ws.Leaver.class);
	}

}
