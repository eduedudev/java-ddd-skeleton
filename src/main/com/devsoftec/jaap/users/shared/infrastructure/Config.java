package com.devsoftec.jaap.users.shared.infrastructure;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Config {
    protected List<Resource> searchMappingFiles(String segments, String extension) {
        String path = "src/main/com/devsoftec/jaap/users/";
        String[] modules = subdirectoriesFor(path);
        List<String> goodPaths = new ArrayList<>();

        for (String module : modules) {
            String[] files = mappingFilesIn(path + module + segments, extension);

            for (String file : files) {
                goodPaths.add(path + module + segments + file);
            }
        }

        return goodPaths.stream().map(FileSystemResource::new).collect(Collectors.toList());
    }

    private String[] mappingFilesIn(String path, String extension) {
        String[] files = new File(path).list((current, name) -> new File(current, name).getName().contains(extension));

        if (null == files) {
            return new String[0];
        }

        return files;
    }

    private String[] subdirectoriesFor(String path) {
        String[] files = new File(path).list((current, name) -> new File(current, name).isDirectory());

        if (null == files) {
            return new String[0];
        }

        return files;
    }

}
