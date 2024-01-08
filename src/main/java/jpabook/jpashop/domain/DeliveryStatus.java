package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public enum DeliveryStatus {
    READY,COMP
}
