package com.myshop.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.myshop.dto.OrderDto;
import com.myshop.dto.OrderHistDto;
import com.myshop.dto.OrderItemDto;
import com.myshop.entity.Item;
import com.myshop.entity.ItemImg;
import com.myshop.entity.Member;
import com.myshop.entity.Order;
import com.myshop.entity.OrderItem;
import com.myshop.repository.ItemRepository;
import com.myshop.repository.MemberRepository;
import com.myshop.repository.OrderRepository;
import com.myshop.repository.itemImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

	private final ItemRepository itemRepository;
	
	private final MemberRepository memberRepository;
	
	private final OrderRepository orderRepository;
	
	private final itemImgRepository itemImgRepository;
	
	public Long order(OrderDto orderDto, String email) {
		
		Item item = itemRepository.findById(orderDto.getItemId())
				.orElseThrow(EntityNotFoundException::new);
		
		Member member = memberRepository.findByEmail(email);
		
		List<OrderItem> orderItemList = new ArrayList<>();
		OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
		orderItemList.add(orderItem);
		
		Order order = Order.createOrder(member, orderItemList);
		orderRepository.save(order);
		
		return order.getId();
	}
	
	@Transactional(readOnly = true)
	public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {
		
		List<Order> orders = orderRepository.findOrders(email, pageable);	// 주문 목록
		Long totalCount = orderRepository.countOrder(email);				// 총 주문 목록 갯수
		
		List<OrderHistDto> orderHistDtos = new ArrayList<>();
		
		for (Order order : orders) {
			
			OrderHistDto orderHistDto = new OrderHistDto(order);
			List<OrderItem> orderItems = order.getOrderItems();
			
			for (OrderItem orderItem : orderItems) {
				
				ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");	// 상품의 대표이미지를 가져옴
				OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
				orderHistDto.addOrderItemDto(orderItemDto);
			}
			
			orderHistDtos.add(orderHistDto);
		}
		
		return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
	}
	
	// 현재 로그인한 사용자와 주문데이터를 생성한 사용자가 같은지 검사
	@Transactional(readOnly = true)
	public boolean validateOrder(Long orderId, String email) {
		
		Member curMember = memberRepository.findByEmail(email);		// 로그인한 사용자 찾기
		Order order = orderRepository.findById(orderId)
									 .orElseThrow(EntityNotFoundException::new);
		Member savedMember = order.getMember();						// 주문한 사용자 찾기
		
		if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
			
			return false;
		}
		
		return true;
	}
	
	// 주문 취소
	public void cancelOrder(Long orderId) {
		
		Order order = orderRepository.findById(orderId)
									 .orElseThrow(EntityNotFoundException::new);
		order.cancelOrder();
	}
	
	// 주문 삭제
	public void deleteOrder(Long orderId) {
		
		Order order = orderRepository.findById(orderId)
				                      .orElseThrow(EntityNotFoundException::new);
		orderRepository.delete(order);
	}
}
