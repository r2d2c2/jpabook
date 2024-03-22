package hello.jpabook.api;

import hello.jpabook.domain.Member;
import hello.jpabook.service.MemberService;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController//api
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> memberV1(){
        return memberService.findMembers();
        //리스트라고 해도 json으로 나가서 유연 성이 떨어 진다
    }
    @GetMapping("/api/v2/members")
    public Result MemberV2(){
        List<Member> members = memberService.findMembers();
        List<MemberDto> collect = members.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect.size(),collect);
        //json 안에 json으로 수정 및 추가가 쉽다
    }
    @Data@AllArgsConstructor
    static class Result<T>{
        private int count;//json에 표시 되는 이름
        private T data;
    }
    @Data@AllArgsConstructor
    static class MemberDto{
        private String name;
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Validated Member member){
        //json 데이터를 Member메핑
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
    @PostMapping("/api/v2/members")//안정성 강화
    public CreateMemberResponse saveMemberV2(@RequestBody @Validated CreateMemberRequest req){
        Member member1=new Member();
        member1.setName(req.getName());
        Long id = memberService.join(member1);
        return new CreateMemberResponse(id);
    }
    @PutMapping("/api/v2/members/{id}")//수정
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id
            ,@RequestBody @Validated UpdateMemberRequest request){
        //주소 값으로 id 받고 post로 객체 받기
        memberService.update(id,request.name);
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(),findMember.getName());
    }

    @Data
    static class UpdateMemberRequest{//웹에서 받는 값
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }
    @Data
    static class CreateMemberRequest{//dto 클레스
        private String name;
    }
    @Data
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
