package com.myshop.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import com.myshop.constant.ItemSellStatus;
import com.myshop.entity.Item;
import com.myshop.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
class ItemRepositoryTest {

	@Autowired
	ItemRepository itemRepository;
	
	@PersistenceContext	// 영속성 컨텍스트를 사용하기 위해 선언
	EntityManager em;	// 엔티티 매니저
	
	
//	@Test
//	@DisplayName("상품 저장 테스트")
//	public void creatItemTest() {
//		Item item = new Item();
//		item.setItemNm("테스트 상품");
//		item.setPrice(10000);
//		item.setItemDetail("테스트 상품 상세 설명");
//		item.setItemSellStatus(ItemSellStatus.SELL);
//		item.setStockNumber(100);
//		item.setRegTime(LocalDateTime.now());
//		item.setUpdateTime(LocalDateTime.now());
//		
//		Item savedItem = itemRepository.save(item);	// save : 데이터 insert || update
//		
//		System.out.println(savedItem.toString());
//	}
	
	public void createItemNmTest() {
		
		for (int i = 1; i <= 10; i++) {
				Item item = new Item();
				item.setItemNm("테스트 상품" + i);
				item.setPrice(10000 + i);
				item.setItemDetail("테스트 상품 상세 설명" + i);
				item.setItemSellStatus(ItemSellStatus.SELL);
				item.setStockNumber(100);
				item.setRegTime(LocalDateTime.now());
				item.setUpdateTime(LocalDateTime.now());
				
				Item savedItem = itemRepository.save(item);	// save : 데이터 insert || update
		}
	}
	
	public void createItemNmTest2() {
		
		for (int i = 1; i <= 5; i++) {
			Item item = new Item();
			item.setItemNm("테스트 상품" + i);
			item.setPrice(10000 + i);
			item.setItemDetail("테스트 상품 상세 설명" + i);
			item.setItemSellStatus(ItemSellStatus.SELL);
			item.setStockNumber(100);
			item.setRegTime(LocalDateTime.now());
			item.setUpdateTime(LocalDateTime.now());
			
			Item savedItem = itemRepository.save(item);	// save : 데이터 insert || update
		}
		
		for (int i = 6; i <= 10; i++) {
			Item item = new Item();
			item.setItemNm("테스트 상품" + i);
			item.setPrice(10000 + i);
			item.setItemDetail("테스트 상품 상세 설명" + i);
			item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
			item.setStockNumber(0);
			item.setRegTime(LocalDateTime.now());
			item.setUpdateTime(LocalDateTime.now());
			
			Item savedItem = itemRepository.save(item);	// save : 데이터 insert || update
		}
	}
	
	@Test
	@DisplayName("상품명 조회 테스트")
	public void findByItemNmTest() {
		this.createItemNmTest();		// item 테이블에 insert
		List<Item> itemList = itemRepository.findByItemNm("테스트 상품2");
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("상품명, 상품상세설명 or 테스트")
	public void findByItemNmOrItemDetailTest() {
		this.createItemNmTest();
		List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("가격 LessThan 테스트")
	public void findByPriceLessThanTest() {
		this.createItemNmTest();
		List<Item> itemList = itemRepository.findByPriceLessThan(10005);
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("가격 내림차순 테스트")
	public void findByPriceThanOrderByPriceTest() {
		this.createItemNmTest();
		List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("@Query를 이용한 상품 조회 테스트")
	public void findByItemDetailTest() {
		this.createItemNmTest();
		List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("@native Query를 이용한 상품 조회 테스트")
	public void findByItemDetailByNativeTest() {
		this.createItemNmTest();
		List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("querydsl 조회 테스트")
	public void queryDslTest() {
		this.createItemNmTest();
		JPAQueryFactory qf = new JPAQueryFactory(em);					// 쿼리를 동적으로 생성하기 위한 객체
		QItem qItem = QItem.item;
		JPAQuery<Item> query = qf.selectFrom(qItem)						// select * from item
				.where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))	// where item_sell_status = 'SELL'
				.where(qItem.itemDetail.like("%테스트 상품 상세 설명%"))		// and item_detail like '%테스트 상품 상세 설명%'
				.orderBy(qItem.price.desc());							// order by price desc;
		
		List<Item> itemList = query.fetch();
		
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("querydsl 조회 테스트2")
	public void queryDslTest2() {
		this.createItemNmTest2();
		
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QItem qItem = QItem.item;
		Pageable page = PageRequest.of(0, 3);							// of(조회할 페이지의 번호, 한페이지당 조회할 데이터의 갯수)
		JPAQuery<Item> query = qf.selectFrom(qItem)
				.where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
				.where(qItem.itemDetail.like("%테스트 상품 상세 설명%"))
				.where(qItem.price.gt(10003))							// gt : price의 값이 gt(설정값)보다 큰 = price > 10003
				.offset(page.getOffset())
				.limit(page.getPageSize());
		
		List<Item> itemList = query.fetch();
		
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("퀴즈 3-1")
	public void queryDslItemNmAndSellTest() {
		this.createItemNmTest();
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QItem qItem = QItem.item;
		JPAQuery<Item> query = qf.selectFrom(qItem)
				.where(qItem.itemNm.eq("테스트 상품1"))
				.where(qItem.itemSellStatus.eq(ItemSellStatus.SELL));
		
		List<Item> itemList = query.fetch();
		
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("퀴즈 3-2")
	public void queryDslPriceTest() {
		this.createItemNmTest();
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QItem qItem = QItem.item;
		JPAQuery<Item> query = qf.selectFrom(qItem)
				.where(qItem.price.between(10004, 10008));
		
		List<Item> itemList = query.fetch();
		
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("퀴즈 3-3")
	public void queryDslRegTimeTest() {
		this.createItemNmTest();
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QItem qItem = QItem.item;
		LocalDateTime regTime = LocalDateTime.of(2023, 01, 01, 12, 12, 44);
		JPAQuery<Item> query = qf.selectFrom(qItem)
				.where(qItem.regTime.after(regTime));
		
		List<Item> itemList = query.fetch();
		
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("퀴즈 3-4")
	public void queryDslItemSellStatTest() {
		this.createItemNmTest();
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QItem qItem = QItem.item;
		JPAQuery<Item> query = qf.selectFrom(qItem)
				.where(qItem.itemSellStatus.isNotNull());
		
		List<Item> itemList = query.fetch();
		
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("퀴즈 3-5")
	public void queryDslItemDetailTest() {
		this.createItemNmTest();
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QItem qItem = QItem.item;
		JPAQuery<Item> query = qf.selectFrom(qItem)
				.where(qItem.itemDetail.endsWith("설명1"));
		
		List<Item> itemList = query.fetch();
		
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	
	
	
/*	
	@Test
	@DisplayName("1번 문제 테스트")
	public void findByItemNmAndItemSellStatusTest() {
		this.createItemNmTest();
		List<Item> itemList = itemRepository.findByItemNmAndItemSellStatus("테스트 상품1", ItemSellStatus.SELL);
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("2번 문제 테스트")
	public void findByPriceBetweenTest() {
		this.createItemNmTest();
		List<Item> itemList = itemRepository.findByPriceBetween(10004, 10008);
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("3번 문제 테스트")
	public void findByRegTimeAfterTest() {
		this.createItemNmTest();
		LocalDateTime time = LocalDateTime.of(2023,01,01,12,12,44);
		List<Item> itemList = itemRepository.findByRegTimeAfter(time);
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("4번 문제 테스트")
	public void findByItemSellStatusNotNullTest() {
		this.createItemNmTest();
		List<Item> itemList = itemRepository.findByItemSellStatusNotNull();
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("5번 문제 테스트")
	public void findByItemDetailEndingWithTest() {
		this.createItemNmTest();
		List<Item> itemList = itemRepository.findByItemDetailEndingWith("설명1");
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("문제2-1")
	public void findByPriceTest() {
		this.createItemNmTest();
		List<Item> itemList = itemRepository.findByPrice(10005);
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("문제2-2")
	public void findByItemNmitemSellStatusTest() {
		this.createItemNmTest();
		List<Item> itemList = itemRepository.findByItemNmitemSellStatus("테스트 상품1", ItemSellStatus.SELL);
		for (Item item : itemList) {
			System.out.println(item.toString());
		}
	}
*/

}
