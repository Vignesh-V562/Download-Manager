import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;

// This class downloads a file from a URL.
public class Download implements Runnable {

    // Max size of a download buffer.
    private static final int MAX_BUFFER_SIZE = 1024;

    public static final String STATUSES[] = {
        "Downloading",
        "Paused",
        "Complete",
        "Cancelled",
        "Error"
    };

    // These are the status codes.
    public static final int DOWNLOADING = 0;
    public static final int PAUSED = 1;
    public static final int COMPLETE = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR = 4;

    private final URL url; // download URL
    private int size; // size of download in bytes
    private int downloaded; // number of bytes downloaded
    private int status; // current status of download
    private final PropertyChangeSupport support; // For notifying observers
    private final ExecutorService executor; // Thread executor

    // Constructor
    public Download(URL url) {
        this.url = url;
        this.size = -1;
        this.downloaded = 0;
        this.status = DOWNLOADING;
        this.support = new PropertyChangeSupport(this);
        this.executor = Executors.newSingleThreadExecutor(); // Single thread for downloading
        download();
    }

    public String getUrl() {
        return this.url.toString(); // Getter for URL
    }

    public int getSize() {
        return size; // Getter for download size
    }

    // Get this download's progress as a percentage.
    public float getProgress() {
        return size > 0 ? ((float) downloaded / size) * 100 : 0;
    }

    public int getStatus() {
        return status; // Getter for status
    }

    public void pause() {
        setStatus(PAUSED); // Pause the download
    }

    // Resume this download.
    public void resume() {
        setStatus(DOWNLOADING);
        download();
    }

    // Cancel this download.
    public void cancel() {
        setStatus(CANCELLED);
        executor.shutdownNow(); // Stop the executor
    }

    // Mark this download as having an error.
    private void error() {
        setStatus(ERROR);
    }

    // Get file name portion of URL.
    private String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    // Start or resume downloading.
    private void download() {
        executor.submit(this); // Submit the download task to the executor
    }

    // Actually download the file.
    @Override
    public void run() {
        // file - Output file - it is used to write the content that readed from input stream.
        // stream - Input stream - used to read the content of the file in the url and wite it to output file.

        try (RandomAccessFile file = new RandomAccessFile(getFileName(url), "rw");
             InputStream stream = createInputStream()) {

            file.seek(downloaded); // Move to the correct position in the file.

            byte[] buffer = new byte[MAX_BUFFER_SIZE];
            int bytesRead;

/* Read from server into buffer.  stream.read( ) call. This call reads bytes from the server and places them into the buffer,
returning the count of how many bytes were actually read. If the number of bytes read equals –1, then downloading has completed and the loop is exited */
            while (status == DOWNLOADING && (bytesRead = stream.read(buffer)) != -1) {
                file.write(buffer, 0, bytesRead);
                downloaded += bytesRead;
                stateChanged();
            }

            if (status == DOWNLOADING) {
                setStatus(COMPLETE); // Mark as complete if finished.
            }

        } catch (IOException e) {
            e.printStackTrace();
            error();
        }
    }

    // Create the input stream with the correct byte range.
    private InputStream createInputStream() throws IOException {

        //Open connection : Since  Download Manager supports only HTTP downloads, the connection is cast to the HttpURLConnection type
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Range", "bytes=" + downloaded + "-");

        /*  "Range" property specifies the range of bytes that is being requested for download from the server. Normally, all of a file’s bytes are downloaded at once. 
However, if a download has been interrupted or paused, only the download’s remaining bytes should 
be retrieved. 
The "Range" property is specified in this form:
   *start-byte – end-byte*
For example, "0 – 12345". However, the end byte of the range is optional. If the end byte is absent, the range ends at the end of the file.*/

       //Connect to the Server - This line actually makes the connection
        connection.connect();

        // Check the response code.
        if (connection.getResponseCode() / 100 != 2) {
            throw new IOException("Server responded with: " + connection.getResponseCode());
        }

        // Check content length.
        int contentLength = connection.getContentLength();
        if (contentLength < 1) {
            throw new IOException("Invalid content length");
        }

        // Set the size for this download if not already set.
        if (size == -1) {
            size = contentLength;
            stateChanged();
        }

        return connection.getInputStream();
    }

    // Notify observers of the state change.
    private void stateChanged() {
        SwingUtilities.invokeLater(() -> support.firePropertyChange("progress", null, getProgress()));
    }

    // Set the status and notify observers of the change.
    private void setStatus(int newStatus) {
        int oldStatus = this.status;
        this.status = newStatus;
        support.firePropertyChange("status", oldStatus, newStatus);
    }

    // Add a property change listener.
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    // Remove a property change listener.
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
