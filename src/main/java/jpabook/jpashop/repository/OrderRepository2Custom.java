package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;

import java.util.List;

public interface OrderRepository2Custom{
    public List<Order> findAllbyQuerydsl(OrderSearch orderSearch);
}
