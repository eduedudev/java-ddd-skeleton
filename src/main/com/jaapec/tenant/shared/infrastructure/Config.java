package com.jaapec.tenant.shared.infrastructure;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.jaapec.tenant.Starter;

public abstract class Config {

	protected List<Resource> searchMappingFiles(String segments, String extension) {
		if (isRunningFromJar()) {
			return searchMappingFilesInJar(segments, extension);
		}
		return searchMappingFilesInFileSystem(segments, extension);
	}

	private List<Resource> searchMappingFilesInJar(String segments, String extension) {
		List<Resource> mappingResources = new ArrayList<>();
		String path = "com/jaapec/tenant";
		try (
			JarFile jarFile = new JarFile(new File(Objects.requireNonNull(Starter.class.getResource("/" + path)).toURI()))
		) {
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String entryName = entry.getName();

				if (entryName.startsWith(path + "/") && entryName.contains(segments) && entryName.endsWith(extension)) {
					mappingResources.add(new ClassPathResource(entryName));
				}
			}
		} catch (Exception e) {
			throw new IllegalStateException("Error while searching mapping files in JAR", e);
		}

		return mappingResources;
	}

	private List<Resource> searchMappingFilesInFileSystem(String segments, String extension) {
		String path = "src/main/com/jaapec/tenant/";
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
		return Objects.requireNonNull(getClass().getResource("/")).toString().startsWith("jar:");
	}
}
