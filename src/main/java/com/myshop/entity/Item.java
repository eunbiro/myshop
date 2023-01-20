package com.myshop.entity;

import javax.persistence.*;

import com.myshop.constant.ItemSellStatus;
import com.myshop.dto.ItemFormDto;

import lombok.*;

@Entity
@Table(name = "item")	// 테이블명 (설정안하면 클래스이름으로 설정됨)
@Getter
@Setter
@ToString
public class Item extends BaseEntity {
	// not null이 아닐때는 필드 타입을 객체(ex) int > Integer)로 지정해야 한다.
	
	@Id		// PK
	@Column(name = "item_id")
	@GeneratedValue(strategy = GenerationType.AUTO)		// 기본키를 자동으로 증가시켜주는 전략?
	private Long id;						// 상품코드
	
	@Column(nullable = false, length = 50)
	private String itemNm;					// 상품명
	
	@Column(nullable = false, name = "price")
	private int price;						// 상품가격
	
	@Column(nullable = false)
	private int stockNumber;				// 상품수량
	
	@Lob	// 데이터가 큰 타입
	@Column(nullable = false)
	private String itemDetail;				// 상품상세설명
	
	@Enumerated(EnumType.STRING)			// 언어로 저장 (번호로 저장하고싶으면 ORDINAL)
	private ItemSellStatus itemSellStatus;	// 상품 판매상태
	
	public void updateItem(ItemFormDto itemFormDto) {
		this.itemNm = itemFormDto.getItemNm();
		this.price = itemFormDto.getPrice();
		this.stockNumber = itemFormDto.getStockNumber();
		this.itemDetail = itemFormDto.getItemDetail();
		this.itemSellStatus = itemFormDto.getItemSellStatus();
	}
}
