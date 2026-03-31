package com.orion.vendorvault.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Component
public class FileStorageUtil {

    public String store(MultipartFile file, String subDirectory) {
        // Mocked for compilation
        return "/uploads/" + subDirectory + "/" + file.getOriginalFilename();
    }
    
    public void delete(String filePath) {
        // mock
    }
}
