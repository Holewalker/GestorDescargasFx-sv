package com.svalero.gestordescargasfx.task;

import com.svalero.gestordescargasfx.controller.DownloadController;
import javafx.concurrent.Task;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class DownloadTask extends Task<Integer> {

    private URL url;
    private File file;

    private static final Logger logger = LogManager.getLogger(DownloadController.class);

    public DownloadTask(String urlText, File file) throws MalformedURLException {
        this.url = new URL(urlText);
        this.file = file;
    }

    @Override
    protected Integer call() throws Exception {
        logger.trace("Descarga " + url.toString() + " iniciada");
        updateMessage("Conectando con el servidor . . .");

        URLConnection urlConnection = url.openConnection();
        double fileSize = urlConnection.getContentLength();

        double megaSize = fileSize / 1048576;
        if (megaSize > 10) {
            logger.trace("Máximo tamaño de fichero alcanzado");
            throw new Exception("Max. size");
        }
        BufferedInputStream in = new BufferedInputStream(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte dataBuffer[] = new byte[1024];
        int bytesRead;
        int totalRead = 0;
        double downloadProgress = 0;

        StopWatch watch = new StopWatch();
        watch.start();

        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            downloadProgress = ((double) totalRead / fileSize);

            updateProgress(downloadProgress, 1);
            updateMessage(downloadProgress * 100 + " %");


            updateMessage(Math.round(downloadProgress * 100) + " %\t\t\t\t" + Math.round(watch.getTime() / 1000) + " sec.");


            Thread.sleep(1);

            fileOutputStream.write(dataBuffer, 0, bytesRead);
            totalRead += bytesRead;

            if (isCancelled()) {
                logger.trace("Descarga " + url.toString() + " cancelada");
                return null;
            }
        }

        updateProgress(1, 1);
        updateMessage("100 %");

        logger.trace("Descarga " + url.toString() + " finalizada");

        return null;
    }
}
