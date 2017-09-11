package datasource.csv;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FileWatcher {

    private final URL watchedDirUrl;

    public FileWatcher(String watchedDir) throws FileNotFoundException {
        URL watchedDirUrl = getClass().getClassLoader().getResource(watchedDir);
        if (watchedDirUrl == null) {
            throw new FileNotFoundException("Resource folder " + watchedDir + " not found");
        }
        this.watchedDirUrl = watchedDirUrl;
    }

    /**
     * @param watchedFiles if empty, watches all files in directory
     * @param action triggered when watched files are affected
     */
    public void watchChangedFiles(List<String> watchedFiles, Runnable action) {
        try {
            WatchService watchService = setupDirectoryWatchService(StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                WatchKey watchKey = watchService.poll(1, TimeUnit.SECONDS);

                if (watchKey != null) {

                    if (hasEventAffectedWatchedFiles(watchedFiles, watchKey)) {
                        action.run();
                    }

                    watchKey.reset();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("FileWatcher service was interrupted");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private WatchService setupDirectoryWatchService(WatchEvent.Kind<Path> eventKind) {
        try {
            Path path = Paths.get(watchedDirUrl.toURI());
            WatchService watchService = path.getFileSystem().newWatchService();
            path.register(watchService, eventKind);
            return watchService;
        } catch (IOException e) {
            System.out.println("Failure setting up the Watch Service");
            e.printStackTrace();
            System.exit(2);
        } catch (URISyntaxException e) {
            System.out.println("Error parsing the watched directory path");
            e.printStackTrace();
            System.exit(3);
        }
        return null;
    }

    private boolean hasEventAffectedWatchedFiles(List<String> constrainedWatchedFiles, WatchKey watchKey) {
        return constrainedWatchedFiles.isEmpty() ||
                watchKey
                    .pollEvents()
                    .stream()
                    .anyMatch(event -> constrainedWatchedFiles.contains(event.context().toString()));
    }
}
