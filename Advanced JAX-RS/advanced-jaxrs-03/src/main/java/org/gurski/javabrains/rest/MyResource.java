package org.gurski.javabrains.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

// The {Path} Param annotation lets you map a resource to a variable path pattern
@Path("{pathParam}/test")
public class MyResource {
	
	@PathParam("pathParam") private String pathParamExample;
	@QueryParam("query") private String queryParamExample;
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {	
		return "It Works! Path para used " + pathParamExample + " and Query param used " + queryParamExample;
	}
}
