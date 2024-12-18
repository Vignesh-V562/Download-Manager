import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

class DownloadTableModel extends AbstractTableModel {

    // These are the names for the table's columns.
    private static final String[] columnNames = {"URL", "Size", "Progress", "Status"};
    // These are the classes for each column's values.
    private static final Class<?>[] columnClasses = {String.class, String.class, JProgressBar.class, String.class};

    // List of downloads
    private final ArrayList<Download> downloadList = new ArrayList<>();

    // Add a new download
    public void addDownload(Download download) {
        // Register to be notified when the download changes.
        download.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("progress".equals(evt.getPropertyName()) || "status".equals(evt.getPropertyName())) {
                    // Find the row index for the download and notify the table to update it
                    int index = downloadList.indexOf(download);
                    if (index >= 0) {
                        fireTableRowsUpdated(index, index);
                    }
                }
            }
        });
        downloadList.add(download);

        // Fire table row insertion notification to table
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    // Get a download for the specified row.
    public Download getDownload(int row) {
        return downloadList.get(row);
    }

    // Remove a download from the list.
    public void clearDownload(int row) {
        downloadList.remove(row);
        fireTableRowsDeleted(row, row); // Notify row deletion
    }

    // Get table's column count
    public int getColumnCount() {
        return columnNames.length;
    }

    // Get a column's name
    public String getColumnName(int col) {
        return columnNames[col];
    }

    // Get a column's class
    public Class<?> getColumnClass(int col) {
        return columnClasses[col];
    }

    // Get table's row count.
    public int getRowCount() {
        return downloadList.size();
    }

    // Get value for a specific cell
    public Object getValueAt(int row, int col) {
        Download download = downloadList.get(row);
        switch (col) {
            case 0:
                return download.getUrl();
            case 1:
                int size = download.getSize();
                return (size == -1) ? "" : Integer.toString(size);
            case 2:
                return download.getProgress(); // Return progress as a float value
            case 3:
                return Download.STATUSES[download.getStatus()]; // Access updated statuses
        }
        return "";
    }
}
/* This method is passed a reference to the Download that has changed, in the form of an 
Observable object. Next, an index to that download is looked up in the list of downloads, 
and that index is then used to fire a table row update event notification, which alerts the 
table that the given row has been updated. The table will then rerender the row with the 
given index, reflecting its new values.*/
