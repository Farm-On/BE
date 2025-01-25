package com.backend.farmon.service.BoardService;

import com.backend.farmon.dto.Board.BoardRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    // 각 게시판 별 저장 로직 ( )
    void save_AllPost(BoardRequestDto.AllPost postDto, List<MultipartFile> multipartFiles)throws Exception;

    void save_FreePost(BoardRequestDto.FreePost postDto, List<MultipartFile> multipartFiles)throws Exception;

    void save_PopularPost(BoardRequestDto.PopularPost postDto, List<MultipartFile> multipartFiles)throws Exception;

    void save_QnaPost(BoardRequestDto.QnaPost postDto, List<MultipartFile> multipartFiles)throws Exception;

    void save_ExperCol(BoardRequestDto.ExpertColumn postDto, List<MultipartFile> multipartFiles)throws Exception;

}
