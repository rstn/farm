package com.simbirsoft.farm.resource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import com.simbirsoft.farm.databind.LocalDateTimeParameter;
import com.simbirsoft.farm.model.Rabbit;
import com.simbirsoft.farm.model.RabbitBase;
import com.simbirsoft.farm.request.EatForm;
import com.simbirsoft.farm.request.FileUploadForm;
import com.simbirsoft.farm.request.MoveForm;
import com.simbirsoft.farm.request.PeriodForm;
import com.simbirsoft.farm.response.ErrorInfo;
import com.simbirsoft.farm.response.GetEnergyResponse;
import com.simbirsoft.farm.response.ResponseCreator;
import com.simbirsoft.farm.service.RabbitService;

@Path(RabbitResource.PATH)
@Consumes({ MediaType.APPLICATION_JSON, CustomMediaType.TEXT_YAML })
@Produces({ MediaType.APPLICATION_JSON, CustomMediaType.TEXT_YAML })
public class RabbitResource {
    
    public static final String PATH = "/rabbits";
    
    private static final Logger LOG = Logger.getLogger(RabbitResource.class.getName());

    @Inject
    private RabbitService rabbitService;
    
    @GET
    @Path("/{id}")
    public Response getRabbit(@PathParam("id") final Integer id) {
        RabbitBase rabbit = rabbitService.find(id).baseClone();
        LOG.info(String.format("GET rabbit: ID = %d, result = '%s'", id, rabbit));
        if (rabbit == null) {
            return ResponseCreator.error(Response.Status.NOT_FOUND);
        } else {
            return ResponseCreator.success(rabbit);
        }
    }

    @GET
    public Response getRabbits() {
        List<RabbitBase> result = rabbitService.findAll();
        LOG.info(String.format("GET all rabbits: result = '%s'", result));
        return ResponseCreator.success(result);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRabbit(@PathParam("id") final Integer id) {
        rabbitService.delete(id);
        LOG.info(String.format("DELETE rabbit: ID = %d", id));
        return ResponseCreator.deleted();
    }

    @POST
    public Response createRabbit(Rabbit rabbit) {
        ErrorInfo.Errors errors = rabbitService.validate(rabbit);
        if (errors.hasErrors()) {
            LOG.info(String.format("INVALID POST rabbit: '%s'", rabbit));
            return ResponseCreator.error(Response.Status.BAD_REQUEST, errors);
        } else {
            Rabbit created = rabbitService.create(rabbit);
            if (created == null) {
                LOG.info(String.format("IVALID POST rabbit: '%s'", created));
                return ResponseCreator.error(Response.Status.INTERNAL_SERVER_ERROR);
            } else {
                LOG.info(String.format("POST rabbit: '%s'", created));
                return ResponseCreator.created(created.baseClone(), PATH);
            }
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateRabbit(@PathParam("id") final Integer id, Rabbit rabbit) {
        ErrorInfo.Errors errors = rabbitService.validate(rabbit);
        if (errors.hasErrors()) {
            LOG.info(String.format("INVALID PUT rabbit: '%s'", rabbit));
            return ResponseCreator.error(Response.Status.BAD_REQUEST, errors);
        } else {
            rabbit.setId(id);
            Rabbit updated = rabbitService.update(rabbit);
            if (updated == null) {
                LOG.info(String.format("INVALID PUT rabbit: ID = %d, '%s'", id, rabbit));
                return ResponseCreator.error(Response.Status.NOT_FOUND);
            } else {
                LOG.info(String.format("PUT rabbit: '%s'", updated));
                return ResponseCreator.success(updated.baseClone());
            }
        }
    }

    @POST
    @Path("/{id}/walk")
    public Response moveRabbit(@PathParam("id") final Integer id, MoveForm param) {
        if ((param == null) || !param.isValid()) {
            LOG.info(String.format("INVALID POST move rabbit: ID = %d, '%s'", id, param));
            return ResponseCreator.error(Response.Status.BAD_REQUEST);
        } else {
            Rabbit rabbit = rabbitService.move(id, param);
            if (rabbit == null) {
                LOG.info(String.format("INVALID POST move rabbit: ID = %d, '%s'", id, param));
                return ResponseCreator.error(Response.Status.NOT_FOUND);
            } else {
                LOG.info(String.format("POST move rabbit: ID = %d, '%s'", id, param));
                return ResponseCreator.success(rabbit.baseClone());
            }
        }
    }

    @POST
    @Path("/{id}/eat")
    public Response eatRabbit(@PathParam("id") final Integer id, EatForm param) {
        if ((param == null) || !param.isValid()) {
            LOG.info(String.format("INVALID POST eat rabbit: ID = %d, '%s'", id, param));
            return ResponseCreator.error(Response.Status.BAD_REQUEST);
        } else {
            Rabbit rabbit = rabbitService.eat(id, param);
            if (rabbit == null) {
                LOG.info(String.format("INVALID POST move rabbit: ID = %d, '%s'", id, param));
                return ResponseCreator.error(Response.Status.NOT_FOUND);
            } else {
                LOG.info(String.format("POST move rabbit: ID = %d, '%s'", id, param));
                return ResponseCreator.success(rabbit.baseClone());
            }
        }
    }
    
    @GET
    @Path("/{id}/energy")
    public Response getEnergyConsumption(
            @PathParam("id") final Integer id, 
            @QueryParam("start") LocalDateTimeParameter start,
            @QueryParam("stop") LocalDateTimeParameter stop) {
        LocalDateTime startTime = (start == null) ? null : start.getValue();
        LocalDateTime stopTime = (stop == null) ? null : stop.getValue();
        PeriodForm period = new PeriodForm(startTime, stopTime);
        if (!period.isValid()) {
            return ResponseCreator.error(Response.Status.BAD_REQUEST);
        }
        GetEnergyResponse energy = rabbitService.getEnergyConsumption(id, period);
        if (energy.getEnergyValue() < 0L) {
            return ResponseCreator.error(Response.Status.NOT_FOUND);
        } else {
            LOG.info(String.format("GET energy: ID = %d, result = '%s'", id, energy));
            return ResponseCreator.success(energy);
        }
    }

    @POST
    @Path("/{id}/photo")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadPhoto(
            @PathParam("id") final Integer id, 
            @MultipartForm FileUploadForm photo) {
        if ((id == null) || (photo == null)) {
            LOG.info(String.format("POST photo bad request: ID = %d", id));
            return ResponseCreator.error(Response.Status.BAD_REQUEST);
        } else {
            Rabbit rabbit = rabbitService.updatePhoto(id, photo.getData());
            if (rabbit == null) {
                LOG.info(String.format("POST photo not found: ID = %d", id));
                return ResponseCreator.error(Response.Status.NOT_FOUND);
            } else {
                LOG.info(String.format("POST photo: ID = %d", id));
                return ResponseCreator.success();
            }
        }
    }

    @GET
    @Path("/{id}/photo")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadPhoto(@PathParam("id") final Integer id) {
        if (id == null) {
            LOG.info(String.format("GET photo bad request: ID = %d", id));
            return ResponseCreator.error(Response.Status.BAD_REQUEST);
        } else {
            Rabbit rabbit = rabbitService.find(id);
            if ((rabbit == null) || (rabbit.getPhoto() == null)) {
                LOG.info(String.format("GET photo not found: ID = %d", id));
                return ResponseCreator.error(Response.Status.NOT_FOUND);
            } else {
                LOG.info(String.format("GET photo: ID = %d", id));
                InputStream photoStream = new ByteArrayInputStream(rabbit.getPhoto());
                return ResponseCreator.file(photoStream, rabbit.getPhotoFileName());
            }
        }
    }

    @DELETE
    @Path("/{id}/photo")
    public Response deletePhoto(@PathParam("id") final Integer id) {
        rabbitService.deletePhoto(id);
        LOG.info(String.format("DELETE photo: ID = %d", id));
        return ResponseCreator.deleted();
    }
}
