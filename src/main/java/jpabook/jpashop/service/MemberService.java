package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 읽기 전용이 많아서 전체적으로 설정 / 최적화 코드
@RequiredArgsConstructor //파이널있는 필드로 생성자를 만들어줌 + autowired / 롬복
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional //전체보다 우선 실행 / 디폴트가 false
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member); //->persist를 통해 키, 벨류가 id 값으로 매핑되면서 저장시킴
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getUsername());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }



}
