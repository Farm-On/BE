package com.backend.farmon.service.BoardService;

import com.backend.farmon.dto.Board.BoardRequestDto;
import com.backend.farmon.dto.post.PostRequestDTO;
import com.backend.farmon.dto.post.PostResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {

    PostResponseDTO save_FreePost(BoardRequestDto.FreePost postDto, List<MultipartFile> multipartFiles)throws Exception;


   // void save_QnaPost(BoardRequestDto.QnaPost postDto, List<MultipartFile> multipartFiles)throws Exception;

    // 분야 선택 안 할 시 에러가 일어나게 에러 전문가 칼럼 과 qna 게시판에 추가
    PostResponseDTO save_QnaPost( BoardRequestDto.QnaPost postDto, List<MultipartFile> multipartFiles) throws Exception;

    // 분야 필수 에러 전문가 칼럼 과 qna 게시판에 추가
    //void save_ExperCol(BoardRequestDto.ExpertColumn postDto, List<MultipartFile> multipartFiles) throws Exception;

    PostResponseDTO  save_ExperCol( BoardRequestDto.ExpertColumn postDto, List<MultipartFile> multipartFiles) throws Exception;
}
