package com.myshop.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.myshop.constant.ItemSellStatus;
import com.myshop.entity.Item;

									//  JpaRepository<사용할 엔티티, 기본키 타입> : 기본적인 CRUD 및 페이징 처리를 위한 메소드가 정의되어 있다.
public interface ItemRepository extends JpaRepository<Item, Long> {
	
	List<Item> findByItemNm(String itemNm);
	// "SELECT * FROM ITEM WHERE ITEM_NM = " + itemNm
	
	List<Item> findByItemNmOrItemDetail(String itemNm, String itemdetail);
	// "SELECT * FROM ITEM WHERE ITEM_NM = " + itemNm + "OR ITEM_DETAIL = " + itemdetail
	
	List<Item> findByPriceLessThan(Integer price);
	// "SELECT * FROM ITEM WHERE PRICE < " + price
	
	List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
	// "SELECT * FROM ITEM WHERE PRICE < " + price + "ORDER BY PRICE DESC"
	
	List<Item> findByItemNmAndItemSellStatus(String itemNm, ItemSellStatus itemDetail);
	
	List<Item> findByPriceBetween(Integer price1, Integer price2);
	
	List<Item> findByRegTimeAfter(LocalDateTime RegTime);
	
	List<Item> findByItemSellStatusNotNull();
	
	List<Item> findByItemDetailEndingWith(String itemDetail);
}
