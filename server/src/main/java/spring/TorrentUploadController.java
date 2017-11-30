package spring;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class TorrentUploadController {
    private final StorageService storageService;

    @Autowired
    public TorrentUploadController(StorageService storageService) {
	this.storageService = storageService;
    }

    @GetMapping("/list-files")
    @ResponseBody
    public List<String> listUploadedFiles(Model model) throws IOException {
	// Return json/xml/whatever list.
	return storageService.loadAll().map(
	    path -> MvcUriComponentsBuilder.fromMethodName(TorrentUploadController.class,
	    "serveFile", path.getFileName().toString()).build().toString())
	    .collect(Collectors.toList());
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

	Resource file = storageService.loadAsResource(filename);
	return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
	"attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/upload")
    @ResponseStatus(value = HttpStatus.OK)
    public void handleFileUpload(@RequestParam("file") MultipartFile file,
    RedirectAttributes redirectAttributes) {
	storageService.store(file);
    }
}
