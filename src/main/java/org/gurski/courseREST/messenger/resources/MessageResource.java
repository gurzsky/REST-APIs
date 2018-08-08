package org.gurski.courseREST.messenger.resources;

import java.net.URI;
import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.gurski.courseREST.messenger.model.Message;
import org.gurski.courseREST.messenger.resources.beans.MessageFilterBean;
import org.gurski.courseREST.messenger.service.MessageService;

@Path("/messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public class MessageResource {
	
	MessageService messageService = new MessageService();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	// code using @QueryParam was substituted for @BeanParam
	// public List<Message> getMessages(@QueryParam("year") int year, @QueryParam("start") int start, @QueryParam("size") int size) {
	public List<Message> getJsonMessages(@BeanParam MessageFilterBean filterBean) {	
		if (filterBean.getYear() > 0)
			return messageService.getAllMessagesForYear(filterBean.getYear());
		if (filterBean.getStart() >=0 && filterBean.getSize() >0)
			return messageService.getAllMessagesPaginated(filterBean.getStart(), filterBean.getSize());
		
		return messageService.getAllMessages();		
	}
	
	@GET
	@Produces(MediaType.TEXT_XML)
	// code using @QueryParam was substituted for @BeanParam
	// public List<Message> getMessages(@QueryParam("year") int year, @QueryParam("start") int start, @QueryParam("size") int size) {
	public List<Message> getXmlMessages(@BeanParam MessageFilterBean filterBean) {	
		if (filterBean.getYear() > 0)
			return messageService.getAllMessagesForYear(filterBean.getYear());
		if (filterBean.getStart() >=0 && filterBean.getSize() >0)
			return messageService.getAllMessagesPaginated(filterBean.getStart(), filterBean.getSize());
		
		return messageService.getAllMessages();		
	}
	
	@POST
	public Response addMessage(Message message, @Context UriInfo uriInfo) {
		Message newMessage = messageService.addMessage(message);
		String newId = String.valueOf(newMessage.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build(); 
		return Response.created(uri)
				.entity(newMessage)
				.build();
	}
	
	/*
	@POST
	public Message addMessage(Message message) {
		return messageService.addMessage(message);		
	}
	*/
	
	@PUT
	@Path("/{messageId}")
	public Message updateMessage(@PathParam("messageId") long id, Message message) {
		message.setId(id);
		return messageService.updateMessage(message);
		
	}
	
	@DELETE
	@Path("/{messageId}")	
	public Message updateMessage(@PathParam("messageId") long id) {
		return messageService.removeMessage(id);
		
	}
	
	@GET	
	@Path("/{messageId}")
	public Message getMessage(@PathParam("messageId") long id, @Context UriInfo uriInfo) {
		Message message = messageService.getMessage(id);		
		message.addLink(getUriForSelf(uriInfo, message), "self");
		message.addLink(getUriForProfile(uriInfo, message), "profile");
		message.addLink(getUriForComments(uriInfo, message), "comments");
		return messageService.getMessage(id);
	}

	private String getUriForComments(UriInfo uriInfo, Message message) {
		String uri = uriInfo
				.getBaseUriBuilder()                                  // http://localhost:8080/messeger/webapi
				.path(MessageResource.class)                          // /messages - resource class level
				.path(MessageResource.class, "getCommentResource")    // /messages - resource method level				
				.path(CommentResource.class)                          // /comments - sub resource
				.resolveTemplate("messageId", message.getId())
				.build()
				.toString();
			return uri;
	}

	private String getUriForProfile(UriInfo uriInfo, Message message) {
		String uri = uriInfo
				.getBaseUriBuilder()                      // http://localhost:8080/messeger/webapi
				.path(ProfileResource.class)              // /profiles
				.path(message.getAuthor())                // /{authorName}
				.build()
				.toString();
			return uri;
	}

	private String getUriForSelf(UriInfo uriInfo, Message message) {
		String uri = uriInfo
			.getBaseUriBuilder()                     // http://localhost:8080/messeger/webapi
			.path(MessageResource.class)             // /messages
			.path(Long.toString(message.getId()))    // /{messagesId}
			.build()
			.toString();
		return uri;
	}
	
	@Path("/{messageId}/comments")
	public CommentResource getCommentResource() {
		return new CommentResource();
	}
}
