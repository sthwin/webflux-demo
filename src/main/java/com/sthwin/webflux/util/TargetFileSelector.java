package com.sthwin.webflux.util;

import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by sthwin on 2020/11/01 6:13 오후
 */
public class TargetFileSelector {
    public static File getTargetFile() {
        FileSystemResource resource = new FileSystemResource("input");
        File[] files = resource.getFile().listFiles();
        if (files == null || files.length < 1)
            return null;
        else
            return sort(files)[0];
    }

    private static File[] sort(File[] files) {
        return (File[]) Arrays.stream(files)
                .sorted(Comparator.comparing(File::getName))
                .toArray();
    }
}
