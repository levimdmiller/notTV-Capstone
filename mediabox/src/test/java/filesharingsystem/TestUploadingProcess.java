package filesharingsystem;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import filesharingsystem.process.TtorrentUploadProcess;
import filesharingsystem.process.UploadException;
import filesharingsystem.process.UploadProcess;

import util.storage.StorageService;

public class TestUploadingProcess {
    private File torrFile;
    private UploadProcess up;
    @Qualifier("TorrentStorage")
    private StorageService torrentStorage;
    @Autowired
    @Qualifier("VideoStorage")
    private StorageService videoStorage;

    @Before
    public void setup() {
        torrFile = new File(System.getProperty("user.home"), "cat.txt");
        try (PrintWriter out = new PrintWriter(torrFile)) {
            out.println("  )\\._.,--....,'``.");
            out.println(" /,   _.. \\   _\\  (`._ ,.");
            out.println("`._.-(,_..'--(,_..'`-.;.'");
            out.flush();

            // Test if a .torrent file can be created during the upload process
            up = new TtorrentUploadProcess("cat", torrFile);
            up.run();
        } catch (FileNotFoundException | UploadException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        torrFile.delete();
    }

    @Test
    public void testSingleFileUpload() throws URISyntaxException, UploadException {
        assertTrue(new File(System.getProperty("user.home"), "cat.torrent").exists());
    }

    @Test
    public void testServerHasTorrent() throws ClientProtocolException, IOException {
        // Test the upload process to check if a new file got uploaded properly to the file sharing system
        // Also tests this: Test if the download process can properly access .torrent files
        // Since it checks the server has the file with a GET request the same as the
        // download process would.
        String json = Request.Get(String.format("http://nottv.levimiller.ca/list-torrents")).execute().returnContent().asString();
        // Kind of hacky, but I don't want to import a JSON library just for a test.
        assertTrue(json.contains("cat.torrent")); // if created torrent is returned.
    }
}
