package com.example.demo.src.controller;

import com.example.demo.src.dto.response.UploadImageResponseDto;
import com.example.demo.src.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/uploadImage")
//    public ApiResponse<String> uploadFile(@RequestParam("images") MultipartFile multipartFile,
//                                          @RequestParam String fileSize) throws IOException {
//        return ApiResponse.success(
//                HttpStatus.CREATED, imageService.UploadImage(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), fileSize)
//        );
//    }
    public UploadImageResponseDto uploadImage(@RequestPart("image") List<MultipartFile> multipartFiles) {
//        return ApiResponse.success(imageService.UploadImage(multipartFiles));
        return imageService.uploadImage(multipartFiles);
    }

//    @ApiOperation("올린 처방전 삭제하기 다중 삭제 가능")
//    @PostMapping("/v1/deleteImage")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteImage(@Authenticated AuthInfo authInfo,@Valid @RequestBody DeleteImageRequestDto deleteImageRequestDto) {
//        imageService.deleteImage(authInfo.getEmail(), deleteImageRequestDto.getImageUrls());
//    }
}
