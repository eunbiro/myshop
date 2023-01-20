package com.myshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myshop.entity.ItemImg;

public interface itemImgRepository extends JpaRepository<ItemImg, Long>  {

	List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
}
