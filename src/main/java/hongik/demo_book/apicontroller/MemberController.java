package hongik.demo_book.apicontroller;

import hongik.demo_book.dto.AddressDto;
import hongik.demo_book.dto.MemberDto;
import hongik.demo_book.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //서버 동작 확인용 (Token 없어도 동작)

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }

    //토큰 있을시 api/member 로 이동

    @PostMapping("/test-redirect")
    public void testRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/member");
    }

    //회원가입

    @PostMapping("/signup")
    public ResponseEntity<MemberDto> signup(
            @Valid @RequestBody MemberDto memberDto
    ) {
        return ResponseEntity.ok(memberService.signup(memberDto));
    }

    //USER권한이랑 ADMIN 모두 허용
    //권한 정보 이용해서 내용 반환하는듯
    //email도 반환

    @GetMapping("/member")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MemberDto> getMyMemberInfo(HttpServletRequest request) {
        return ResponseEntity.ok(memberService.getMyUserWithAuthorities());
    }

    //ADMIN만 허용

    @GetMapping("/member/{membername}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<MemberDto> getMemberInfo(@PathVariable String membername) {
        return ResponseEntity.ok(memberService.getMemberWithAuthorities(membername));
    }



    //주소 저장(구현 완료)
    //주소 변경

    @PostMapping("/member/addressSave")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<AddressDto> saveMemberAddress(
            @Valid @RequestBody AddressDto addressdto
    ) {
        return ResponseEntity.ok(memberService.AddressSave(addressdto));
    }


    //주소 삭제

    @DeleteMapping("/member/addressDelete")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<AddressDto> deleteMemberAddress(HttpServletRequest request) {
        memberService.deleteAddress();
        return ResponseEntity.noContent().build();
    }

    //저장된 주소 반환

    @GetMapping("/member/addressReturn")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<AddressDto> returnMemberAddress(HttpServletRequest request){
        return ResponseEntity.ok(memberService.AddressReturn());
    }

}
