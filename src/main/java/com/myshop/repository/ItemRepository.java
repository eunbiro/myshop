package com.myshop.repository;

//import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.myshop.constant.ItemSellStatus;
import com.myshop.entity.Item;

									//  JpaRepository<사용할 엔티티, 기본키 타입> : 기본적인 CRUD 및 페이징 처리를 위한 메소드가 정의되어 있다.
public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
	
	List<Item> findByItemNm(String itemNm);
	// "SELECT * FROM ITEM WHERE ITEM_NM = " + itemNm
	
	List<Item> findByItemNmOrItemDetail(String itemNm, String itemdetail);
	// "SELECT * FROM ITEM WHERE ITEM_NM = " + itemNm + "OR ITEM_DETAIL = " + itemdetail
	
	List<Item> findByPriceLessThan(Integer price);
	// "SELECT * FROM ITEM WHERE PRICE < " + price
	
	List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
	// "SELECT * FROM ITEM WHERE PRICE < " + price + "ORDER BY PRICE DESC"
	
	
														// 가져온 파라미터 값을 넣어주고 싶은 경우 : 를 붙이고 사용한다.
//	@Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
//	List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
								// 받은 값을 쿼리에 넣어줘야 할 경우 @Param을 사용한다.
								// 받은 파라메터를 @Param(이름)으로 쿼리문에 넣어둔다.
	
	@Query("select i from Item i where i.itemDetail like %?1% order by i.price desc")
	List<Item> findByItemDetail(String itemDetail);		//?1 : 첫번째 파라메터
	
	@Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
	List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
	
	


	
	/*
	// 퀴즈1
	List<Item> findByItemNmAndItemSellStatus(String itemNm, ItemSellStatus itemDetail);
	List<Item> findByPriceBetween(Integer price1, Integer price2);
	List<Item> findByRegTimeAfter(LocalDateTime RegTime);
	List<Item> findByItemSellStatusNotNull();
	List<Item> findByItemDetailEndingWith(String itemDetail);
	
	// 퀴즈2
	@Query("select i from Item i where i.price >= :price")
	List<Item> findByPrice(@Param("price") Integer price);
	
	@Query("select i from Item i where i.itemNm in :itemNm and i.itemSellStatus in :itemSellStatus")
	List<Item> findByItemNmitemSellStatus(@Param("itemNm") String itemNm, @Param("itemSellStatus") ItemSellStatus itemSellStatus);
	
																							// 열거형(enum) 타입의 Param 데이터는 name()으로 지정해서 가져와야한다. 
	@Query(value = "select * from item i where i.item_nm in :itemNm and i.item_sell_status in :#{itemSellStatus.name()}", nativeQuery = true)
	List<Item> findByItemNmitemSellStatus(@Param("itemNm") String itemNm, @Param("itemSellStatus") ItemSellStatus itemSellStatus);
	*/
}
