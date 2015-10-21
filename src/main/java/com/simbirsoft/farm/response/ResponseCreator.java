package com.simbirsoft.farm.response;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import com.simbirsoft.farm.model.Entity;

public final class ResponseCreator {
    
    private static final Logger LOG = Logger.getLogger(ResponseCreator.class.getName());
    
    private ResponseCreator() {
    }

    public static Response error(Response.Status status) {
        return error(status, null);
    }
    
    public static Response error(Response.Status status, Object errors) {
        Response.ResponseBuilder response = Response.status(status);
        if (errors != null) {
            response.entity(errors);
        }
        return response.build();
    }

    public static Response success() {
        return success(null);
    }

    public static Response success(Object object) {
        Response.ResponseBuilder response = Response.ok();
        if (object != null) {
            response.entity(object);
        }
        return response.build();
    }

    public static Response created(Entity object, String location) {
        Response.ResponseBuilder response = Response.status(Response.Status.CREATED);
        if (object != null) {
            response.entity(object);
            if ((location != null) && (object.getId() != null)) {
                try {
                    response.location(new URI(location + "/" + object.getId()));
                } catch (URISyntaxException ignore) {
                    LOG.severe(ignore.getMessage());
                }
            }
        }
        return response.build();
    }

    public static Response deleted() {
        Response.ResponseBuilder response = Response.status(Response.Status.NO_CONTENT);
        return response.build();
    }

    public static Response file(InputStream is, String fileName) {
        Response.ResponseBuilder response = Response.ok(is);
        Response result =  response.build();
        result.getHeaders().add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        return result;
    }

}
