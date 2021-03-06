package com.jaru.media_serveer.core.usecase.directory;

import com.jaru.media_serveer.boundaries.Dto.RequestDirectoryDto;
import com.jaru.media_serveer.core.exception.AlreadyExistException;
import com.jaru.media_serveer.core.exception.MediaServerException;
import com.jaru.media_serveer.infra.UploadFileProperties;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CreateDirUsecaseImpl implements CreateDirUsecase{
    private static final Logger logger = LoggerFactory.getLogger(UploadFileProperties.class);
    private final UploadFileProperties uploadFileProperties;

    @Override
    public boolean execute(RequestDirectoryDto requestDirectoryDto) {
        String fullPath = uploadFileProperties.getUploadDir() + requestDirectoryDto.getDirectoryName();
        File directory = new File(fullPath);
        if(directory.exists()) {
            throw new AlreadyExistException(HttpStatus.BAD_REQUEST, "This directory is already exists");
        }

        try{
            FileUtils.forceMkdir(directory);
            return true;
        }catch (IOException ioex){
            logger.error(ioex.getLocalizedMessage());
            throw new MediaServerException(ioex.getMessage(), ioex);
        }
    }
}
