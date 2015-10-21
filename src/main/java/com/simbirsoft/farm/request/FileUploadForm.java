package com.simbirsoft.farm.request;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class FileUploadForm {

    private byte[] data;

    public byte[] getData() {
        return data;
    }

    @FormParam("uploadedFile")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public void setData(byte[] data) {
        this.data = data;
    }
}
