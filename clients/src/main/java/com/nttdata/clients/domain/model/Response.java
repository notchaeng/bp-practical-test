package com.nttdata.clients.domain.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Response implements Serializable {

    private String message;

    private Object data;

    public Response setResponse(Response response, String message, Object data) {
        response.setData(data);
        response.setMessage(message);
        return response;
    }
}
