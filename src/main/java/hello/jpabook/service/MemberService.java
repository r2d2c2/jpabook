package hello.jpabook.service;

import hello.jpabook.domain.Member;
import hello.jpabook.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)//읽기전용 성능 이점도 있다
@RequiredArgsConstructor//final 만 생성자 추가
public class MemberService {

    //@Autowired//생략
    private final MemberRepository memberRepository;


    //회원 가입
    @Transactional
    public Long join(Member member){
        ValidateDuplicateMember(member);//중복 회원 찾기
        memberRepository.save(member);
        return member.getId();
    }
    //@Transactional//데이터 변경시 필수
    private void ValidateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){//null 아니면(이미 있으면)
            throw new IllegalStateException("이미 존제하는 회원 입니다.");
        }
    }
    //회원 전체 조회

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);//연속 상태 변경 되면 업데이트 된다
    }
}
