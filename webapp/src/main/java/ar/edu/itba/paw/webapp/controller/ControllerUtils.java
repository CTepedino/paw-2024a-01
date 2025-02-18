package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.files.File;

import javax.imageio.ImageIO;
import javax.ws.rs.core.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
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
                return Response.ok(file.getFile(), mediaType)
                        .tag(entityTag)
                        .cacheControl(CacheControl.valueOf("private, no-cache"));
            }
            return response;

        } catch (IOException e){
            return Response.ok(file.getFile(), mediaType);
        }

    }

    public static <T extends File> Response.ResponseBuilder imageResponse(T file, String mediaType, Request request, Integer width, Integer height){
        if (file == null){
            return Response.status(Response.Status.NOT_FOUND);
        }
        byte[] downsized = downsizeImage(file.getFile(), width, height);
        try {
            EntityTag entityTag = new EntityTag(generateETag(downsized));
            Response.ResponseBuilder response = request.evaluatePreconditions(entityTag);
            if (response == null){
                return Response.ok(downsized, mediaType)
                        .tag(entityTag)
                        .cacheControl(CacheControl.valueOf("private, no-cache"));
            }
            return response;

        } catch (IOException e){
            return Response.serverError();
        }
    }

    private static String generateETag(byte[] data) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(data);
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IOException();
        }
    }

    private static byte[] downsizeImage(byte[] file, Integer width, Integer height){
        ByteArrayInputStream in = new ByteArrayInputStream(file);
        try {
            BufferedImage img = ImageIO.read(in);
            if (img == null){
                return file;
            }

            if (height == null || height > img.getHeight()){
                height = img.getHeight();
            }
            if (width == null || width > img.getWidth()){
                width = img.getWidth();
            }
            Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0,0,0), null);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            ImageIO.write(imageBuff, "jpg", buffer);

            return buffer.toByteArray();

        } catch (IOException e){
            return file;
        }
    }

    public static Response.ResponseBuilder setUnconditionalCache(Response.ResponseBuilder response){
        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(MAX_AGE);
        return response.cacheControl(cacheControl);
    }



}
