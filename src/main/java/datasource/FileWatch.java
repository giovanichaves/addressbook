package datasource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FileWatch {

    private final URL watchedDirUrl;

    public FileWatch(String watchedDir) throws FileNotFoundException {
        URL watchedDirUrl = getClass().getClassLoader().getResource(watchedDir);
        if (watchedDirUrl == null) {
            throw new FileNotFoundException("Resource folder " + watchedDir + " not found");
        }
        this.watchedDirUrl = watchedDirUrl;
    }

    public void watchFilesChanged(List<String> constrainedWatchedFiles, Runnable action) {
        try {
            WatchService watchService = setupDirectoryWatchService(StandardWatchEventKinds.ENTRY_MODIFY);

            WatchKey watchKey = null;
            while (true) {
                watchKey = watchService.poll(1, TimeUnit.SECONDS);

                if (watchKey != null) {

                    if (eventAffectsWatchedFiles(constrainedWatchedFiles, watchKey)) {
                        action.run();
                    }

                    watchKey.reset();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("FileWatcher service was interrupted");
            e.printStackTrace();
        }

    }

    private WatchService setupDirectoryWatchService(WatchEvent.Kind<Path> eventKind) {
        WatchService watchService = null;
        try {
            Path path = Paths.get(watchedDirUrl.toURI());
            watchService = path.getFileSystem().newWatchService();
            path.register(watchService, eventKind);

        } catch (IOException e) {
            System.out.println("Failure setting up the Watch Service");
            e.printStackTrace();
        } catch (URISyntaxException e) {
            System.out.println("Error parsing the watched directory path");
            e.printStackTrace();
        }

        return watchService;
    }

    private boolean eventAffectsWatchedFiles(List<String> constrainedWatchedFiles, WatchKey watchKey) {
        return constrainedWatchedFiles.isEmpty() ||
                watchKey
                    .pollEvents()
                    .stream()
                    .anyMatch(event -> constrainedWatchedFiles.contains(event.context().toString()));
    }
}
