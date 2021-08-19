package moveration.bot.data;

import java.nio.file.Path;

public interface DiskResource {

	void createNew();

	boolean exists();

	void delete();

	boolean isValid();

	void load();

	void write();

	Path getPath();

}