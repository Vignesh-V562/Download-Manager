# Download Manager - Using Java Swings and Network....

 - Download Manager is developed that manages Internet downloads for you and makes simple work of resuming interrupted downloads. 
It also lets you pause and then resume a download, and manage multiple downloads, simultaneously.
 - At the core of the Download Manager’s usefulness is its ability to take advantage of downloading only specific portions of a file. 
In a classic download scenario, a whole file is downloaded from beginning to end. If the transmission of the file is interrupted for 
any reason, the progress made toward completing the downloading of the file is lost. The Download Manager, however, can pick up from 
where an interruption occurs and then download only the file’s remaining fragment.

## How Internet downloads really work:

> Internet downloads in their simplest form are merely client/server transactions. The client requests to download a file from a server 
on the Internet. The server then responds by sending the requested file to your browser. The most common protocols for downloading files 
are File Transfer Protocol (FTP) and Hypertext Transfer Protocol (HTTP). 

> FTP is usually associated generically with exchanging files between computers, whereas HTTP is usually associated specifically with 
transferring web pages and their related files.  

> This Download Manager will only support HTTP downloads. HTTP downloads come in two forms: resumable (HTTP 1.1) and 
nonresumable (HTTP 1.0). The difference between these two forms lies in the way files can be requested from servers. 
With the antiquated HTTP 1.0, a client can only request that a server send it a file, whereas with HTTP 1.1, a client can request that 
a server send it a complete file or only a specific portion of a file. This is the feature the Download Manager is built on.

## An Overview of the Download Manager

 The Download Manager uses a simple yet effective GUI interface built with Java’s Swing libraries.The GUI maintains a list of downloads 
that are currently being managed. Each download in the list reports its URL, size of the file in bytes, progress as a percentage toward 
completion, and current status. 
The downloads can each be in one of the following different states: 
  - Downloading
  - Paused
  - Complete
  - Error
  - Cancelled. 
The GUI also has controls for adding downloads to the list and for changing the state of each download in the list. When a download in the 
list is selected, depending on its current state, it can be **paused, resumed, cancelled, or removed** from the list altogether.

>> The Download Manager is broken into a few classes for natural separation of functional components. These are the 
  - Download
  - DownloadsTableModel
  - ProgressRenderer
  - DownloadManager classes

The **DownloadManager** class is responsible for the **GUI interface** and makes use of the DownloadsTableModel and ProgressRenderer classes 
for displaying the current list of downloads. The **Download class** represents a **“managed” download** and is responsible for performing 
the actual downloading of a file.

**The Download Class**

 The Download class is the workhorse of the Download Manager. Its primary purpose is to download a file and save that file’s contents to disk.
Each time a new download is added to the Download Manager, a new Download object is instantiated to handle the download.
 
 The Download Manager has the ability to download multiple files at once. To achieve this, it’s necessary for each of the simultaneous downloads
to run independently. It’s also necessary for each individual download to manage its own state so that it can be reflected in the GUI. This is 
accomplished with the Download class.

**The stateChanged() Method**

 In order for the Download Manager to display up-to-date information on each of the downloads it’s managing, it has to know each time a download’s 
information changes. To handle this, **the Observer software design pattern** is used. 

  The Observer pattern is analogous to an announcement’s mailing list where several people register to receive announcements. Each time there’s a 
new announcement, each person on the list receives a message with the announcement. In the Observer pattern’s case, there’s an observed class with 
which observer classes can register themselves to receive change notifications.

 The Download class employs the Observer pattern by extending Java’s built-in Observable utility class. Extending the Observable class allows 
classes that implement Java’s Observer interface to register themselves with the Download class to receive change notifications. 
  
  Each time the Download class needs to notify its registered Observers of a change, the stateChanged( ) method is invoked. The stateChanged( )
method first calls the Observable class’ ``setChanged( )`` method to flag the class as having been changed. Next, the stateChanged( ) method calls 
Observable’s notifyObservers( ) method, which broadcasts the change 


**The ProgressRenderer Class**

 The ```ProgressRenderer class``` is a small utility class that is used to render the current progress of a download listed in the GUI’s 
"Downloads" JTable instance. Normally, a JTable instance renders each cell’s data as text. However, often it’s particularly useful to render a
cell’s data as something other than text. In the Download Manager’s case, we want to render each of the table’s Progress column cells as progress 
bars. The ```ProgressRenderer``` class makes that possible.

The ProgressRenderer class takes advantage of the fact that Swing’s JTable class has a rendering system that can accept “plug-ins” for rendering 
table cells. 
- To plug into this rendering system, first, the ProgressRenderer class has to implement Swing’s TableCellRenderer interface. 
- Second, a ProgressRenderer instance has to be registered with a JTable instance; doing so instructs the JTable instance as to which cells should be rendered with the “plug-in.”

**The getTableCellRendererComponent()**

- Method is invoked each time a JTable instance goes to render a cell for which this class has been registered.The **value** variable holds the 
data for the cell being rendered and is passed to JProgressBar’s setValue( ) method

**The DownloadsTableModel Class**

> The DownloadsTableModel class houses the Download Manager’s list of downloads and is the backing data source for the GUI’s "Downloads" JTable 
instance.
> The DownloadsTableModel class essentially is a utility class utilized by the "Downloads" JTable instance for managing data in the table.

**The DownloadManager Class**

  The DownloadManager class is responsible for creating and running the Download Manager’s GUI. This class has a main( ) method declared, so on
execution it will be invoked first.

## Contributing
  ```Vignesh V```
  ```Vishvapriya M```
  ```Priyadarshini V```

## Compiling and Running the Download Manager

*Compile* DownloadManager like this:  ```javac DownloadManager.java```

*Run* DownloadManager like this:   ```java DownloadManager```

 The Download Manager is easy to use. First, enter the URL of a file that you want to download in the text field at the top of the screen. After 
adding a download to the Download Manager, you can manage it by selecting it in the table. Once selected, you can pause, cancel, resume, and clear 
a download. 

## Sample links to test the file

**Audio file mp3**
Link 1 - https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3
Link 2 - https://download.samplelib.com/mp3/sample-3s.mp3

**Image**
Link 1 - https://sample-videos.com/img/Sample-jpg-image-500kb.jpg
Link 2 - https://sample-videos.com/img/Sample-png-image-500kb.png

**Video File**
Link 1 - https://download.samplelib.com/mp4/sample-5s.mp4
Link 2 - https://download.samplelib.com/mp4/sample-10s.mp4

**Video File**
Link 1 - https://www.sample-videos.com/text/Sample-text-file-10kb.txt

***Thank You***
