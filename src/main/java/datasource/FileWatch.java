package datasource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FileWatch {

    private final String watchedDir;

    public FileWatch(String watchedDir) {
        this.watchedDir = watchedDir;
    }

    public boolean isFilesChanged(List<String> watchedFiles) throws IOException, URISyntaxException, InterruptedException {
        URL watchedDirUrl = getClass().getClassLoader().getResource(watchedDir);

        if (watchedDirUrl == null) {
            throw new FileNotFoundException("Resource folder testdata not found");
        }

        Path path = Paths.get(watchedDirUrl.toURI());
        WatchService watchService =  path.getFileSystem().newWatchService();
        path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        WatchKey watchKey = null;
        boolean fileChanged;
        while (true) {
            watchKey = watchService.poll(1, TimeUnit.SECONDS);
            if(watchKey != null) {
                watchKey.pollEvents().forEach(event -> {
                    if (watchedFiles.contains(event.context())) {
                    }
                });
                watchKey.reset();
            }
        }
    }
}
