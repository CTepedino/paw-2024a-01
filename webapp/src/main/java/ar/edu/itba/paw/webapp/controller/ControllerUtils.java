package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.files.File;

import javax.ws.rs.core.*;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

public class ControllerUtils {
    private ControllerUtils(){}

    private static final int MAX_AGE = (int) TimeUnit.DAYS.toSeconds(7);

    public static Response.ResponseBuilder paginatedResponse(Response.ResponseBuilder response, PaginatedContent<?> paginatedContent, UriInfo uriInfo){
        response.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", 1).build(), "first");
        response.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", paginatedContent.getPageCount()).build(), "last");
        if (paginatedContent.hasMorePages()){
            response.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", paginatedContent.getPageNumber()+1).build(), "next");
        }
        if (paginatedContent.getPageNumber() > 1){
            response.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", paginatedContent.getPageNumber()-1).build(), "prev");
        }
        response.header("X-Total-Count", paginatedContent.getTotalSize());
        return response;
    }


    public static <T extends File> Response.ResponseBuilder fileResponse(T file, String mediaType, Request request) {
        if (file == null){
            return Response.status(Response.Status.NOT_FOUND);
        }

        try {
            EntityTag entityTag = new EntityTag(generateETag(file.getFile()));
            Response.ResponseBuilder response = request.evaluatePreconditions(entityTag);
            if (response == null){
                return Response.ok(file.getFile(), mediaType).tag(entityTag);
            }
            return response;

        } catch (IOException e){
            return Response.ok(file.getFile(), mediaType);
        }

    }

    private static String generateETag(byte[] data) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(data);
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IOException();
        }
    }


    public static Response.ResponseBuilder setUnconditionalCache(Response.ResponseBuilder response){
        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(MAX_AGE);
        return response.cacheControl(cacheControl);
    }

}
