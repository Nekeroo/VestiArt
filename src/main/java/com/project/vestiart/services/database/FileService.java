package com.project.vestiart.services.database;

import com.project.vestiart.models.File;
import com.project.vestiart.repositories.IFileRepository;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    private final IFileRepository fileRepository;

    public FileService(IFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void saveFile(File file) {
        fileRepository.save(file);
    }

}
