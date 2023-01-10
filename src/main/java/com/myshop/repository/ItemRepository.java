package com.myshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myshop.entity.Item;

									//  JpaRepository<사용할 엔티티, 기본키 타입> : 기본적인 CRUD 및 페이징 처리를 위한 메소드가 정의되어 있다.
public interface ItemRepository extends JpaRepository<Item, Long> {
	
}
