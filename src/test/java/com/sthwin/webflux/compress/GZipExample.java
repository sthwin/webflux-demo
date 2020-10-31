package com.sthwin.webflux.compress;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

/**
 * Created by sthwin on 2020/10/31 1:36 오후
 */
public class GZipExample {
    public static void main(String[] args) {
        Path source = Paths.get("input/hamburger.png.gz");
        Path target = Paths.get("input/hamburger.png");

        if(Files.notExists(source)) {
            System.err.printf("The path %s doesn't exist!", source);
            return;
        }

        try {
            GZipExample.decompressGzip(source, target);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public static void decompressGzip(Path source, Path target) throws IOException {

        try (GZIPInputStream gis = new GZIPInputStream(
                new FileInputStream(source.toFile()));
             FileOutputStream fos = new FileOutputStream(target.toFile())) {

            // copy GZIPInputStream to FileOutputStream
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        }
    }
}
