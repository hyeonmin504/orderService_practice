package jpabook.jpashop.repository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.QDelivery;
import jpabook.jpashop.domain.QMember;
import jpabook.jpashop.domain.QOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderRepository2CustomImpl implements OrderRepository2Custom{
    private final EntityManager em;
    JPAQueryFactory queryFactory;


    @Override
    public List<Order> findAllbyQuerydsl(OrderSearch orderSearch) {
        QOrder order = new QOrder("o");
        QMember member = new QMember("m");
        QDelivery delivery = new QDelivery("d");
        queryFactory = new JPAQueryFactory(em);
        BooleanBuilder builder = new BooleanBuilder();

        if(orderSearch.getOrderStatus()!=null){
            builder.and(order.status.eq(orderSearch.getOrderStatus()));
        }
        if(orderSearch.getMemberName()!=null) {
            builder.and(member.username.eq(orderSearch.getMemberName()));
        }
        return queryFactory
                .select(order)
                .from(order)
                .innerJoin(order.member, member).fetchJoin()
                .innerJoin(order.delivery, delivery).fetchJoin()
                .where(builder)
                .fetch();

    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class
        ).getResultList();
    }
}
