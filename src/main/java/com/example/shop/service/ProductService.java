package com.example.shop.service;

import com.example.shop.controller.product.dto.ProductCreateRequest;
import com.example.shop.controller.product.dto.ProductMainDto;
import com.example.shop.controller.product.dto.ProductResponse;
import com.example.shop.controller.product.dto.ProductUpdateRequest;
import com.example.shop.domain.Product;
import com.example.shop.domain.ProductImage;
import com.example.shop.repository.ProductImageRepository;
import com.example.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final FileService fileService;
    private final WishlistService wishlistService;

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    @Value("${itemImgUrl}")
    private String itemImgUrl;

    /**
     * 상품 및 다중 이미지 저장 (최초 등록)
     */
    @Transactional
    public Long saveProduct(ProductCreateRequest request) throws Exception {
        Product product = request.toEntity();
        productRepository.save(product);

        // 변수명 통일: getProductImgFiles()
        List<MultipartFile> imgFiles = request.getProductImgFiles();
        if (imgFiles != null && !imgFiles.isEmpty()) {
            for (int i = 0; i < imgFiles.size(); i++) {
                saveProductImages(product, imgFiles.get(i), i, (i == 0));
            }
        }
        return product.getId();
    }

    /**
     * 이미지 저장 공통 로직
     */
    private void saveProductImages(Product product, MultipartFile file, int sortOrder, boolean isMain) throws Exception {
        if (file.isEmpty() || file.isEmpty()) return;

        String oriImgName = file.getOriginalFilename();
        String imgName = fileService.uploadFile(itemImgLocation, oriImgName, file.getBytes());
        String imgUrl = itemImgUrl + imgName;

        ProductImage productImage = ProductImage.builder()
                .imgName(imgName)
                .oriImgName(oriImgName)
                .imgUrl(imgUrl)
                .isMain(isMain ? "Y" : "N")
                .sortOrder(sortOrder)
                .product(product)
                .build();

        productImageRepository.save(productImage);
    } // 중괄호 오타 수정됨

    /**
     * 상품 정보 및 이미지 수정 (핵심 로직)
     */
    @Transactional
    public Long updateProduct(ProductUpdateRequest request) throws Exception {
        Product product = productRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id=" + request.getId()));

        // 1. 기본 정보 수정 (더티 체킹)
        product.update(request);

        // 2. 이미지 통합 처리
        List<ProductUpdateRequest.ProductImageUpdateDto> imageInfos = request.getImageInfos();
        // 수정 시에도 getProductImgFiles()로 통일하여 사용
        List<MultipartFile> newFiles = request.getProductImgFiles();

        if (imageInfos != null) {
            for (ProductUpdateRequest.ProductImageUpdateDto info : imageInfos) {
                boolean isMainFlag = (info.getSortOrder() == 0);

                // (1) 삭제 처리
                if (info.isDeleted() && info.getId() != null) {
                    ProductImage targetImg = productImageRepository.findById(info.getId()).orElse(null);
                    if (targetImg != null) {
                        fileService.deleteFile(itemImgLocation + "/" + targetImg.getImgName());
                        productImageRepository.delete(targetImg);
                    }
                }

                // (2) 기존 이미지 정보 수정 (순서, 대표여부)
                else if (info.getId() != null) {
                    ProductImage targetImg = productImageRepository.findById(info.getId()).orElse(null);
                    if (targetImg != null) {
                        targetImg.updateImageMetadata(info.getSortOrder(), isMainFlag ? "Y" : "N");
                    }
                }

                // (3) 신규 이미지 추가
                else if (info.getTempFileIndex() != null && newFiles != null) {
                    if (info.getTempFileIndex() < newFiles.size()) {
                        MultipartFile file = newFiles.get(info.getTempFileIndex());
                        saveProductImages(product, file, info.getSortOrder(), info.isMain());
                    }
                }
            }
        }
        return product.getId();
    }

    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id=" + id));
        return ProductResponse.of(product);
    }

    public List<ProductMainDto> getProductsForMain(String category, Long memberId) {
        // 1. 카테고리별로 상품 목록 조회
        List<Product> products = (category == null || category.equals("전체"))
                ? productRepository.findAll()
                : productRepository.findByCategory(category);  //memberId 제거 (레포지토리 맞춰서 수정)

        // 2. Product 엔티티를 ProductMainDto로 변환하면서 찜 여부(isLiked) 체크
        return products.stream().map(product -> {
            boolean isLiked = wishlistService.isLiked(memberId, product.getId());
            return ProductMainDto.fromEntity(product, isLiked);
        }).collect(Collectors.toList());
    }


    public List<Product> getProductsByCategory(String category){
        if (category == null || category.equals("전체")){
            return productRepository.findAll();
        }
        return productRepository.findByCategory(category);
    }
}