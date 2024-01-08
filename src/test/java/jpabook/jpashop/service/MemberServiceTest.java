package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) // 스프링을 엮어서 테스트
@SpringBootTest //스프링부트를 띄운 상태로 테스트
@Transactional  //테스트에서만 끝나고 롤백시켜줌
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;
    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("kim");

        //when
        Long savedId = memberService.join(member);

        //then
        em.flush();
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class) // == 여기서 이 오류 클래스가 발생해야 한다. try catch 제거 가
    public void 중복_회원_예외() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("kim");

        Member member2 = new Member();
        member2.setUsername("kim");
        //when

        memberService.join(member);
        memberService.join(member2);
        /*
        try {
            memberService.join(member2);
        } catch (IllegalStateException e) {
            return ;
        }
        */

        //then
        fail("예외가 발생해야 한다."); //여기까지 오면 안된다.

    }

}