package hello.jpabook.controller;

import hello.jpabook.domain.Address;
import hello.jpabook.domain.Member;
import hello.jpabook.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm",new MemberForm());
        //컨트롤러에서 뷰로 넘어 갈때 넘기기
        return "members/createMemberForm";
    }
    @PostMapping("/members/new")
    public String create(@Validated MemberForm memberForm, BindingResult result){
        //BindingResult result에서 에러코드 저장
        if(result.hasErrors()){//에러시
            return "members/createMemberForm";
        }
        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());
        Member member=new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);
        memberService.join(member);
        return "redirect:/";
    }
    @GetMapping(value = "/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);
        //간소화
        //model.addAttribute("members", memberService.findMembers());
        return "members/memberList";
    }
}
