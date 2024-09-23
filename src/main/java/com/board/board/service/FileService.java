package com.board.board.service;

import com.board.board.entity.Board;
import com.board.board.entity.FileEntity;
import com.board.board.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    @Value("${file.dir}")
    private String fileDir;

    public FileEntity saveFile(MultipartFile files, Board board) throws IOException {

        if (files.isEmpty())
            return null;

        String origName = files.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String extension = origName.substring(origName.lastIndexOf("."));
        String savedName = uuid + extension;
        String savedPath = fileDir + savedName;

        FileEntity file = FileEntity.builder()
                .origNm(origName)
                .saveNm(savedName)
                .savedPath(savedPath)
                .board(board)
                .build();

        files.transferTo(new File(savedPath));

        FileEntity savedFile = fileRepository.save(file);
        return savedFile;
    }

}
