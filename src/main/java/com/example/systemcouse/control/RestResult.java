package com.example.systemcouse.control;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.example.systemcouse.model.*;
import com.example.systemcouse.repository.TaskRepository;
import com.example.systemcouse.service.*;

import java.sql.Date;
import java.util.List;

@Path("/courseSystem")
public class RestResult {

    @EJB
    private AllService allService;

    @EJB
    private TaskRepository taskRepository;

    //Task RESTS//
    @GET
    @Path("/selectTasks")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response selectTasks() {
        if(allService.selectTasks() != null) {
            return Response.ok(allService.selectTasks()).build();
        }
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Path("/selectTask/{id}")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response selectTask(@PathParam("id") int taskId) {
        if(allService.selectTask(taskId) != null) {
            return Response.ok(allService.selectTask(taskId)).build();
        }
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertTask")
    @RolesAllowed("ADMIN")
    public Response insertTask(Task task){
        if(allService.insertTask(task).getTaskId() != null) // проверка
            return Response.ok(task).build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @PUT
    @Path("/updateTask/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN","INSTRUCTOR"})
    public Response updateTask(Task taskName){
        if(allService.updateTask(taskName))
            return Response.ok().entity("updated").build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @DELETE
    @Path("/deleteTask/{taskId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN","INSTRUCTOR"})
    public Response deleteTask(
            @PathParam("taskId") int id) {
        if(allService.deleteTask(id))
            return Response.ok().entity("Deleted").build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }


    //Score RESTS//
    @GET
    @Path("/selectScores")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response selectScores() {
        if(allService.selectScores() != null)
            return Response.ok(allService.selectScores()).build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    //Score RESTS//
    @GET
    @Path("/selectScore/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response selectScore(@PathParam("id") int scoreId) {
        if(allService.selectScore(scoreId) != null) {
            return Response.ok(allService.selectScore(scoreId)).build();
        }
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @PUT
    @Path("/updateScore/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN","INSTRUCTOR"})
    public Response updateScore(Score score){
        if(allService.updateScore(score))
            return Response.ok().entity("updated").build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertScore")
    @RolesAllowed("ADMIN")
    public Response insertScore(Score score) {
        if(allService.insertScore(score).getScoreId() != null)
            return Response.ok(score).build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @DELETE
    @Path("/deleteScore/{scoreId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN","INSTRUCTOR"})
    public Response deleteRank(
            @PathParam("scoreId") int id) {
        if(allService.deleteScore(id)) {
            return Response.ok().entity("Deleted").build();
        }
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }


    //User_ RESTS//
    @GET
    @Path("/selectUsers")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response selectUsers() {
        if(allService.selectUsers() != null)
            return Response.ok(allService.selectUsers()).build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @GET
    @Path("/selectUser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response selectUser(@PathParam("id") int userId) {
        if(allService.selectUser(userId) != null)
            return Response.ok(allService.selectUser(userId)).build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @PUT
    @Path("/updateUser/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("USER")
    public Response updateUser(User_ user) {
        if(allService.updateUser(user))
            return Response.ok().entity("updated").build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertUser")
    @RolesAllowed("ADMIN")
    public Response insertUser(User_ user) {
        if(allService.insertUser(user).getUserId() != null)
            return Response.ok(user).build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @DELETE
    @Path("/deleteUser/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response deleteUser(
            @PathParam("userId") int id) {
        if(allService.deleteUser(id))
            return Response.ok().entity("Deleted").build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    //JMS//
    @POST
    @Path("/jms")
    @PermitAll
    public String sendMessage(String message) {
        return allService.sendJmsMessage(message);
    }

    @GET
    @Path("/jms")
    @RolesAllowed("ADMIN")
    public String getMessage() {
        return allService.getMessage();
    }

    @GET
    @Path("/common")
    @RolesAllowed("ADMIN")
    public Response getCommon() {
        return Response.ok(allService.getCommon()).build();
    }

}
