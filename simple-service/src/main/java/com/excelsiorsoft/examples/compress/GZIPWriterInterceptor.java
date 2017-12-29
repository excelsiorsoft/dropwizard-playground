package com.excelsiorsoft.examples.compress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

@Compress
@Provider
public class GZIPWriterInterceptor implements WriterInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(GZIPWriterInterceptor.class);

    @Override
    public void aroundWriteTo(WriterInterceptorContext context)
            throws IOException, WebApplicationException {

        logger.debug("about to compress in the interceptor");

        final OutputStream outputStream = context.getOutputStream();
        context.setOutputStream(new GZIPOutputStream(outputStream));

        logger.info("done with compressing, proceeding...");
        context.proceed();
    }
}
