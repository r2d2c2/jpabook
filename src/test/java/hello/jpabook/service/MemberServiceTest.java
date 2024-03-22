package hello.jpabook.service;

import hello.jpabook.domain.Member;
import hello.jpabook.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional//롤백
class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    //@Rollback(value = false)//em.flush()로 대체
    public void 회원가입() throws Exception{
        //given
        Member member=new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);
        em.flush();//강제 커밋 저장은 안된다
        //then
        Assertions.assertEquals(member,memberRepository.findOne(saveId));
    }
    @Test
    public void 중복회원예외() throws Exception{
        //given
        Member member1=new Member();
        member1.setName("kim");
        Member member2=new Member();
        member2.setName("kim");
        //when
        memberService.join(member1);
        try{
            memberService.join(member2);
        }catch (IllegalStateException e){
            return;//예외가 발생하면 나감
        }

        //then
        fail("예외가 발생해야 한다");//강제 테스트 실패
    }

}