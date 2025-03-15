package com.devsoftec.jaap.users.shared.infrastructure;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.devsoftec.jaap.users.Starter;

public abstract class Config {

	protected List<Resource> searchMappingFiles(String segments, String extension) {
		if (isRunningFromJar()) {
			return searchMappingFilesInJar(segments, extension);
		}
		return searchMappingFilesInFileSystem(segments, extension);
	}

	private List<Resource> searchMappingFilesInJar(String segments, String extension) {
		List<Resource> mappingResources = new ArrayList<>();
		String path = "com/devsoftec/jaap/users";
		try {
			URL url = Starter.class.getResource("/" + path);
			if (url != null) {
				String jarPath = url.getFile().substring(5, url.getFile().indexOf("!"));
				JarFile jarFile = new JarFile(jarPath);
				Enumeration<JarEntry> entries = jarFile.entries();
				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					String entryName = entry.getName();

					if (entryName.startsWith(path + "/") && entryName.contains(segments) && entryName.endsWith(extension)) {
						mappingResources.add(new ClassPathResource(entryName));
					}
				}
				jarFile.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mappingResources;
	}

	private List<Resource> searchMappingFilesInFileSystem(String segments, String extension) {
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
		File dir = new File(path);
		String[] files = dir.list((current, name) -> new File(current, name).getName().endsWith(extension));

		return (files == null) ? new String[0] : files;
	}

	private String[] subdirectoriesFor(String path) {
		File dir = new File(path);
		String[] files = dir.list((current, name) -> new File(current, name).isDirectory());

		return (files == null) ? new String[0] : files;
	}

	private boolean isRunningFromJar() {
		return getClass().getResource("/").toString().startsWith("jar:");
	}
}
