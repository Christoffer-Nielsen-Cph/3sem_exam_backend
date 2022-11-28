package rest;

import businessfacades.TrainingSessionDTOFacade;
import businessfacades.UserDTOFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.exceptions.UnirestException;
import dtos.TrainingSessionDTO;
import dtos.UserDTO;

import java.nio.charset.StandardCharsets;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import errorhandling.API_Exception;
import utils.EMF_Creator;

/**
 * @author lam@cphbusiness.dk
 */
@Path("users")
public class UserResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private UserDTOFacade userFacade = UserDTOFacade.getInstance(EMF);
    private TrainingSessionDTOFacade trainingFacade = TrainingSessionDTOFacade.getInstance(EMF);

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    @GET
    @Path("/{userName}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("userName") String userName) throws API_Exception {
        return Response.ok().entity(GSON.toJson(userFacade.getUserByUserName(userName))).type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build();
    }

    @GET
    @Path("/all")
    public Response getAllUsers() throws API_Exception {
        return Response.ok().entity(GSON.toJson(userFacade.getAllUsers())).type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(String content) throws API_Exception {
        UserDTO userDTO = GSON.fromJson(content, UserDTO.class);
        UserDTO newUserDTO = userFacade.createUser(userDTO);
        return Response.ok().entity(GSON.toJson(newUserDTO)).type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build();
    }

    @PUT
    @Path("/{userName}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("userName")String userName, String content) throws EntityNotFoundException, API_Exception {
        UserDTO udto = GSON.fromJson(content, UserDTO.class);
        udto.setUserName(userName);
        UserDTO updatedUser = userFacade.updateUser(udto);
        return Response.ok().entity(GSON.toJson(updatedUser)).type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build();
    }

    @DELETE
    @Path("/{userName}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response deleteUser(@PathParam("userName") String userName) throws API_Exception {
        UserDTO deletedUser = userFacade.deleteUser(userName);
        return Response.ok().entity(GSON.toJson(deletedUser)).type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build();
    }

    @POST
    @Path("/createTrainingSession")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createTrainingSession(String content) throws API_Exception {
        TrainingSessionDTO trainingSessionDTO = GSON.fromJson(content, TrainingSessionDTO.class);
        TrainingSessionDTO newTrainingSessionDTO = trainingFacade.createTrainingSession(trainingSessionDTO);
        return Response.ok().entity(GSON.toJson(newTrainingSessionDTO)).type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build();
    }

    @GET
    @Path("/email/{trainingSessionId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response showUsers(@PathParam("trainingSessionId") int trainingSessionId) throws API_Exception, UnirestException {
        return Response.ok().entity(GSON.toJson(trainingFacade.sendEmailToAllUsers(trainingSessionId))).type(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())).build();
    }

}