package jpabook.jpashop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER);
        Join<Order, OrderItem> oi = o.join("orderItems",JoinType.RIGHT);//회원과 조인
        List<Predicate> criteria = new ArrayList<>();
//주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
//회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("username"), "%" + orderSearch.getMemberName()
                            + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000 건
        return query.getResultList();
    }

    public List<Order> findAllbyQuerydsl(OrderSearch orderSearch) {
        QOrder order = new QOrder("o");
        QMember member = new QMember("m");
        QOrderItem orderItem = new QOrderItem("oi");
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
        BooleanBuilder builder = new BooleanBuilder();

        if(orderSearch.getOrderStatus()!=null){
            builder.and(order.status.eq(orderSearch.getOrderStatus()));
        }
        if(orderSearch.getMemberName()!=null) {
            builder.and(member.username.eq(orderSearch.getMemberName()));
        }
        return jpaQueryFactory
                .select(order)
                .from(order)
                .leftJoin(order.member, member)
                .rightJoin(order.orderItems,orderItem)
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

    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i", Order.class)
                .getResultList();
    }
}
