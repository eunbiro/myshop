package com.myshop.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.myshop.constant.OrderStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "orders")	// 테이블명 (설정안하면 클래스이름으로 설정됨)
@Getter
@Setter
@ToString
public class Order {

	@Id
	@Column(name = "order_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	private LocalDateTime orderDate;	// 주문일
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;	// 주문상태
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)	// OrderItem에 있는 order에 의해 관리가 된다.
	private List<OrderItem> orderItems = new ArrayList<>();
	
	public void addOrderItem(OrderItem orderItem) {
		
		orderItems.add(orderItem);
		orderItem.setOrder(this);		// ★양방향 참조관계일때는 orderItem객체에도 order객체를 세팅한다.
	}
	
	// order 객체를 생성해준다.
	public static Order createOrder(Member member, List<OrderItem> orderItemList) {
		
		Order order = new Order();
		order.setMember(member);
		
		for (OrderItem orderItem : orderItemList) {
			
			order.addOrderItem(orderItem);
		}
		
		order.setOrderStatus(OrderStatus.ORDER);
		order.setOrderDate(LocalDateTime.now());
		
		return order;
	}
	
	// 총 주문금액
	public int getTotalPrice() {
		
		int totalPrice = 0;
		
		for (OrderItem orderItem : orderItems) {
			
			totalPrice += orderItem.getTotalPrice();
		}
		
		return totalPrice;
	}
	
	// 주문 취소
	public void cancelOrder() {
		
		this.orderStatus = OrderStatus.CANCEL;
		for (OrderItem orderItem : orderItems) {
			
			orderItem.cancel();		// 재고 증가	
		}
	}
}
