package domind.doc_management.service;

import domind.doc_management.exception.FileStorageException;
import domind.doc_management.exception.MyFileNotFoundException;
import domind.doc_management.property.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
            Files.createDirectories(Paths.get(this.fileStorageLocation.toString(), "myTable"));
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
                    ex);
        }
    }

    public String storeFile(MultipartFile file, String path) {
        Path fileStorageLocationSubfolder;
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        fileStorageLocationSubfolder = Paths.get(this.fileStorageLocation.toString(), path).toAbsolutePath()
                .normalize();
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            // Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Path targetLocation = fileStorageLocationSubfolder.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    public String deleteFileS(String fileName, String filePath) {
        Path targetLocation = Paths.get(this.fileStorageLocation.toString(), filePath);
        targetLocation = targetLocation.resolve(fileName);
        String k = targetLocation.toString();
        if (k.charAt(1) == ':')
            k = k.replaceAll("\\\\", "\\\\\\\\");
        File file = new File(k);
        if (file.delete()) {
            return "deleted: " + k;
        } else {
            return "Delete operation is failed.";
        }
    }

}
