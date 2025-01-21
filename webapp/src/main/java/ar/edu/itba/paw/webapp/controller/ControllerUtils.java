package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.files.File;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;

public class ControllerUtils {
    private ControllerUtils(){}

    public static Response.ResponseBuilder paginatedResponse(Response.ResponseBuilder responseBuilder, PaginatedContent<?> paginatedContent, UriInfo uriInfo){
        responseBuilder.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", 1).build(), "first");
        responseBuilder.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", paginatedContent.getPageCount()).build(), "last");
        if (paginatedContent.hasMorePages()){
            responseBuilder.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", paginatedContent.getPageNumber()+1).build(), "next");
        }
        if (paginatedContent.getPageNumber() > 1){
            responseBuilder.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", paginatedContent.getPageNumber()-1).build(), "prev");
        }

        return responseBuilder;
    }

    public static <T extends File> Response.ResponseBuilder fileResponse(T file){
        if (file == null){
            return Response.noContent();//TODO: noContent, o 404?
        }
        return Response.ok(file.getFile());
    }


}
