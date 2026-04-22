package com.example.shop.controller.product.dto;

//id는 보통 URL 경로로 받으므로 필드에서는 제외하거나 포함할 수 있습니다.

import com.example.shop.domain.SellStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductUpdateRequest {
    private Long id;
    private String pName;
    private int price;
    private String pDesc;
    private String category;
    private int stock;
    private List<ProductImageUpdateDto> imageInfos; // 1. 이미지의 최종 순서 및 상태를 담은 리스트 (기존+신규 통합)
    private List<MultipartFile> productImgFiles; // 2. 새로 추가된 이미지 파일들
    private SellStatus sellStatus;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ProductImageUpdateDto {
        private Long id;          // 기존 이미지의 경우 ID (신규면 null)
        private String imageUrl;  // 이미지 경로
        private int sortOrder;    // 드래그 앤 드랍으로 정해진 순서 (0번이 대표사진)
        private boolean isMain;   // 대표 사진 여부
        private boolean isDeleted; // 삭제 여부 플래그

        // ★ 핵심: 신규 파일 매핑용
        // newImageFiles 리스트의 몇 번째 인덱스 파일이 이 순서(sortOrder)에 들어갈지 지정
        private Integer tempFileIndex;
    }
}


