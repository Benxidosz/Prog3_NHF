package ges.menu;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TableRow {
	private File file;

	TableRow(File file) {
		this.file = file;
	}

	public String getName() {
		return file.getName();
	}

	public String getModified() throws IOException {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
		Path path = Paths.get(file.toURI());
		BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);

		LocalDateTime localDateTime = attr.lastModifiedTime()
				.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		return localDateTime.format(dateFormatter);
	}

	public File getFile() {
		return file;
	}
}
