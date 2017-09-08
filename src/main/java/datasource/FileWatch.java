package datasource;

import com.google.common.base.Preconditions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class FileWatch {

    private final String watchedDir;

    public FileWatch(String watchedDir) {
        this.watchedDir = watchedDir;
    }

    public void isFilesChanged(List<String> watchedFiles, Callable action) throws IOException, URISyntaxException, InterruptedException {
        Preconditions.checkArgument(!watchedFiles.isEmpty());

        URL watchedDirUrl = getClass().getClassLoader().getResource(watchedDir);
        if (watchedDirUrl == null) {
            throw new FileNotFoundException("Resource folder testdata not found");
        }

        Path path = Paths.get(watchedDirUrl.toURI());
        WatchService watchService =  path.getFileSystem().newWatchService();
        path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        WatchKey watchKey = null;
        while (true) {
            watchKey = watchService.poll(1, TimeUnit.SECONDS);
            if(watchKey != null) {
                if (eventAffectsWatchedFiles(watchedFiles, watchKey)) {
                    try {
                        action.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                watchKey.reset();
            }
        }
    }

    private boolean eventAffectsWatchedFiles(List<String> watchedFiles, WatchKey watchKey) {
        return watchKey
            .pollEvents()
            .stream()
            .anyMatch(event -> watchedFiles.contains(event.context().toString()));
    }
}
