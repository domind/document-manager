package domind.doc_management.controller;

import domind.doc_management.payload.UploadFileResponse;
import domind.doc_management.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import domind.doc_management.model.UsersList;
import domind.doc_management.model.UsersRepository;
import domind.doc_management.tool.TokenExpiryCheck;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    private UsersRepository usersRepository;

    public FileController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Autowired
    private FileStorageService fileStorageService;

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<String> handle() {
        return new ResponseEntity<String>(HttpStatus.OK);
    }


    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping("/uploadFile/{myfilePath}")
    public UploadFileResponse uploadFile(@PathVariable String myfilePath, @RequestParam("file") MultipartFile file,
            @RequestHeader String token, @RequestHeader String user) {
        UsersList usersList = usersRepository.findByUserName(user);
        if (usersList.getToken().equals(token) && TokenExpiryCheck.tokenExpiryCheck(token)) {
            String fileName = fileStorageService.storeFile(file, myfilePath); // file + directory String

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
                    .path(fileName).toUriString();
                    logger.info("uploaded file: "+ fileName);
            return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
        } else{
            logger.info("upload failed: wrong user, or token");
            return new UploadFileResponse("", "", "", 0);
        }
    }
    /*
     * @PostMapping("/uploadMultipleFiles") public List<UploadFileResponse>
     * uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) { return
     * Arrays.asList(files) .stream() .map(file -> uploadFile(file))
     * .collect(Collectors.toList()); }
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @DeleteMapping("/delete/{filePath}/{fileName:.+}")
    public ResponseEntity<?> deleteFile(@PathVariable String filePath, @PathVariable String fileName,@RequestHeader String token, @RequestHeader String user) {
        UsersList usersList = usersRepository.findByUserName(user);
        if (usersList.getToken().equals(token) && TokenExpiryCheck.tokenExpiryCheck(token)) {
        String response = fileStorageService.deleteFileS(fileName, filePath);
        logger.info(response);
        return new ResponseEntity<Resource>(HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<String>("Operations failed, wrong creditentials", HttpStatus.BAD_REQUEST);  
        }
    }

}
