package com.hee.muzihee.musical.service;


import com.hee.muzihee.common.paging.SelectCriteria;
import com.hee.muzihee.musical.dao.MusicalMapper;
import com.hee.muzihee.musical.dto.MusicalDto;
import com.hee.muzihee.util.FileUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class MusicalService {

    @Value("${image.image-dir}")
    private String IMAGE_DIR;
    @Value("${image.image-url}")
    private String IMAGE_URL;

    private final MusicalMapper musicalMapper;

    public MusicalService(MusicalMapper musicalMapper) {
        this.musicalMapper = musicalMapper;
    }


    // 전체 리스트 갯수 구하기 (날짜 지난 공연 제외)
    public int selectAllMusicalTotal() {
        return  musicalMapper.selectMusicalTotal();
    }

    // 전체 리스트 가져오기 (일반유저용)
    public List<MusicalDto> selectMusicalList(SelectCriteria selectCriteria){
        List<MusicalDto> musicalList = musicalMapper.selectMusicalList(selectCriteria);

        for (MusicalDto musicalDto : musicalList) {
            musicalDto.setMusicalPoster(IMAGE_URL + musicalDto.getMusicalPoster());
        }

        return musicalList;
    }


    // 모든 작품 갯수 구하기
    public int selectMusicalTotalForAdmin(){
        return musicalMapper.selectMusicalTotalForAdmin();
    }

    // 모든 작품 리스트로 가져오기 ( 날짜 지난 공연 포함)
    public List<MusicalDto> selectMusicalListWithPagingForAdmin(SelectCriteria selectCriteria){
        List<MusicalDto> musicalList = musicalMapper.selectMusicalListWithPagingForAdmin(selectCriteria);

        for (MusicalDto musicalDto : musicalList) {
            musicalDto.setMusicalPoster(IMAGE_URL + musicalDto.getMusicalPoster());
           }

        return musicalList;
    }

    // 한 개의 정보 가져오기 (날짜 지난 공연 포함)
    public MusicalDto selectMusicalDetailForAdmin(String musicalCode){

        MusicalDto musicalDto = musicalMapper.selectMusicalDetailForAdmin(musicalCode);
        musicalDto.setMusicalPoster(IMAGE_URL + musicalDto.getMusicalPoster());

        if(musicalDto.getMusicalDiscount() != null) {
            System.out.println("할인정보 이미지 확인" + musicalDto.getMusicalDiscount());
            musicalDto.setMusicalDiscount(IMAGE_URL + musicalDto.getMusicalDiscount());
        }
        if(musicalDto.getMusicalDetail() != null){
            System.out.println("할인정보 이미지 확인" + musicalDto.getMusicalDetail());
            musicalDto.setMusicalDetail(IMAGE_URL + musicalDto.getMusicalDetail());
        }

        return musicalDto;
    }

    // 오리지널 뮤지컬 갯수 구하기
    public int selectOriginMusicalTotal() {

        int result = musicalMapper.selectOriginMusicalTotal();

        return  result;
    }

    // 오리지널 뮤지컬 리스트로 가져오기
    public List<MusicalDto> selectOriginMusicalListWithPaging(SelectCriteria selectCriteria) {

        List<MusicalDto> musicalList = musicalMapper.selectOriginMusicalListWithPaging(selectCriteria);

        for (MusicalDto musicalDto : musicalList) {
            musicalDto.setMusicalPoster(IMAGE_URL + musicalDto.getMusicalPoster());
        }

        return musicalList;
    }

    // 창작 뮤지컬 갯수 구하기
    public int selectCreativeMusicalTotal() {

        int result = musicalMapper.selectCreativeMusicalTotal();

        return  result;
    }

    // 창작 뮤지컬 리스트로 가져오기
    public List<MusicalDto> selectCreativeMusicalListWithPaging(SelectCriteria selectCriteria) {
        List<MusicalDto> musicalList = musicalMapper.selectCreativeMusicalListWithPaging(selectCriteria);

        for (MusicalDto musicalDto : musicalList) {
            musicalDto.setMusicalPoster(IMAGE_URL + musicalDto.getMusicalPoster());
        }

        return musicalList;
    }

    // 아동 뮤지컬 갯수 구하기
    public int selectFamilyMusicalTotal() {

        int result = musicalMapper.selectFamilyMusicalTotal();

        return  result;
    }

    // 아동 뮤지컬 리스트 가져오기
    public List<MusicalDto> selectFamilyMusicalListWithPaging(SelectCriteria selectCriteria) {

        List<MusicalDto> musicalList = musicalMapper.selectFamilyMusicalListWithPaging(selectCriteria);

        for (MusicalDto musicalDto : musicalList) {
            musicalDto.setMusicalPoster(IMAGE_URL + musicalDto.getMusicalPoster());
        }

        return musicalList;
    }

    // 한 개의 정보 가져오기
    public MusicalDto selectMusicalDetail(String musicalCode) {

        MusicalDto musicalDto = musicalMapper.selectMusicalDetail(musicalCode);
        musicalDto.setMusicalPoster(IMAGE_URL + musicalDto.getMusicalPoster());

        if(musicalDto.getMusicalDiscount() != null) {
            System.out.println("할인정보 이미지 확인" + musicalDto.getMusicalDiscount());
            musicalDto.setMusicalDiscount(IMAGE_URL + musicalDto.getMusicalDiscount());
        }
        if(musicalDto.getMusicalDetail() != null){
            System.out.println("할인정보 이미지 확인" + musicalDto.getMusicalDetail());
            musicalDto.setMusicalDetail(IMAGE_URL + musicalDto.getMusicalDetail());
        }

        return musicalDto;
    }

    @Transactional
    public String insertMusical(MusicalDto musicalDto) {
        log.info("[ProductService] insertProduct Start ===================================");
        log.info("[ProductService] musicalDto : " + musicalDto);

        String imageName = UUID.randomUUID().toString().replace("-", "");
        String replacePosterFileName = null;
//        String replaceDiscountFileName = null;
        int result = 0;

        log.info("[ProductService] IMAGE_DIR : " + IMAGE_DIR);
        log.info("[ProductService] imageName : " + imageName);

        try {
            replacePosterFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, musicalDto.getMusicalPosterImg());
            musicalDto.setMusicalPoster(replacePosterFileName);
            log.info("[ProductService] replaceFileName : " + replacePosterFileName);
            log.info("[ProductService] insert Image Name : "+ replacePosterFileName);

            // 할인정보, 상세정보 => 이미지도 되고 텍스트도 가능하게...? => 불러올 때 태그가 img니까 안될듯!
//            replaceDiscountFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, musicalDto.getMusicalPosterImg());
//            musicalDto.setMusicalPoster(replaceDiscountFileName);

            result = musicalMapper.insertMusical(musicalDto);
        } catch (IOException e) {
            log.info("[ProductService] IOException IMAGE_DIR : "+ IMAGE_DIR);

            log.info("[ProductService] IOException deleteFile : "+ replacePosterFileName);

            FileUploadUtils.deleteFile(IMAGE_DIR, replacePosterFileName);
            throw new RuntimeException(e);
        }

        log.info("[ProductService] result > 0 성공: "+ result);
        return (result > 0) ? "뮤지컬 입력 성공" : "뮤지컬 입력 실패";
    }

    @Transactional
    public Object updateMusical(MusicalDto musicalDto) {
        log.info("[ProductService] updateProduct Start ===================================");
        log.info("[ProductService] productDto : " + musicalDto);
        String replaceFileName = null;
        int result = 0;

        try {
            String oriImage = musicalMapper.selectMusicalDetail(String.valueOf(musicalDto.getMusicalCode())).getMusicalPoster();
            log.info("[updateProduct] oriImage : " + oriImage);

            if(musicalDto.getMusicalPosterImg() != null){
                // 이미지 변경 진행
                String imageName = UUID.randomUUID().toString().replace("-", "");
                replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, musicalDto.getMusicalPosterImg());

                System.out.println("getMusicalPosterImg 확인" + musicalDto.getMusicalPosterImg());
                System.out.println("imageName 확인" + imageName);
                System.out.println("replaceFileName 확인" + replaceFileName);

                log.info("[updateProduct] IMAGE_DIR!!"+ IMAGE_DIR);
                log.info("[updateProduct] imageName!!"+ imageName);

                log.info("[updateProduct] InsertFileName : " + replaceFileName);
                musicalDto.setMusicalPoster(replaceFileName);
                System.out.println("getMusicalPoster 값이 제대로 바뀌었는지 확인 " + musicalDto.getMusicalPoster());

                log.info("[updateProduct] deleteImage : " + oriImage);
                boolean isDelete = FileUploadUtils.deleteFile(IMAGE_DIR, oriImage);
                log.info("[update] isDelete : " + isDelete);
            } else {
                // 이미지 변경 없을 시
                musicalDto.setMusicalPoster(oriImage);
            }

            result = musicalMapper.updateMusical(musicalDto);

        } catch (IOException e) {
            log.info("[updateProduct] Exception!!");
            FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
            throw new RuntimeException(e);
        }
        log.info("[ProductService] updateProduct End ===================================");
        log.info("[ProductService] result > 0 성공: "+ result);

        return (result > 0) ? "뮤지컬 업데이트 성공" : "뮤지컬 업데이트 실패";
    }

    @Transactional
    public Object deleteMusical(String musicalCode) {
        int result = musicalMapper.deleteMusical(musicalCode);

        return (result > 0) ? "뮤지컬 삭제 성공" : "뮤지컬 삭제 실패";

    }

}
