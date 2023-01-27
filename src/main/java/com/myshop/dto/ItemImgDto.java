package com.myshop.dto;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;

import com.myshop.entity.ItemImg;

import lombok.*;

@Getter
@Setter
public class ItemImgDto {

	private Long id;
	
	private String imgName;		// 이미지 파일명
	
	private String oriImgName;	// 원본 이미지 파일명
	
	private String imgUrl;		// 이미지 조회 경로
	
	private String repimgYn;	// 대표 이미지 여부
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static ItemImgDto of(ItemImg itemImg) {
		
		return modelMapper.map(itemImg, ItemImgDto.class);
	}
	
	private LocalDateTime regTime;			// 등록시간
	private LocalDateTime updateTime;		// 수정시간

}
