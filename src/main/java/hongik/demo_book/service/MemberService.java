package hongik.demo_book.service;


import hongik.demo_book.Repository.MemberRepository;
import hongik.demo_book.domain.Address;
import hongik.demo_book.domain.Authority;
import hongik.demo_book.domain.Member;
import hongik.demo_book.dto.AddressDto;
import hongik.demo_book.dto.MemberDto;
import hongik.demo_book.exception.DuplicateMemberException;
import hongik.demo_book.exception.NotFoundMemberException;
import hongik.demo_book.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    @Transactional
    public MemberDto signup(MemberDto memberDto) {
        if (memberRepository.findOneWithAuthoritiesByEmail(memberDto.getEmail()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Member member = Member.builder()
                .membername(memberDto.getMembername())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .email(memberDto.getEmail())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return MemberDto.from(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public MemberDto getMemberWithAuthorities(String email) {
        return MemberDto.from(memberRepository.findOneWithAuthoritiesByEmail(email).orElse(null));
    }

    //읽기전용
    @Transactional(readOnly = true)
    public MemberDto getMyUserWithAuthorities() {
        return MemberDto.from(
                SecurityUtil.getCurrentEmail()
                        .flatMap(memberRepository::findOneWithAuthoritiesByEmail)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }

    //쓰기전용
    @Transactional
    public AddressDto AddressSave(AddressDto addressdto) {


        Member member = userService.GetCurrentMmember();

        Address address = new Address();

        address.setZipcode(addressdto.getZipcode());
        address.setCity(addressdto.getCity());
        address.setStreet(addressdto.getStreet());

        member.setAddress(address);

        memberRepository.save(member);

        Address savedAddress = member.getAddress();
        AddressDto savedAddressDto = new AddressDto();
        savedAddressDto.setZipcode(savedAddress.getZipcode());
        savedAddressDto.setCity(savedAddress.getCity());
        savedAddressDto.setStreet(savedAddress.getStreet());

        return savedAddressDto;
    }


    //주소 삭제
    @Transactional
    public void AddressDelete() {

        Member member = userService.GetCurrentMmember();

        Address currentAdddress = member.getAddress();

        if(currentAdddress == null){
            throw new RuntimeException("주소가 설정되어있지 않습니다");
        }

        member.setAddress(null);
        memberRepository.save(member);


    }

    //주소 목록 반환
    @Transactional(readOnly = true)
    public AddressDto AddressReturn(){

        Member member = userService.GetCurrentMmember();


        return AddressDto.builder()
                .zipcode(member.getAddress().getZipcode())
                .street(member.getAddress().getStreet())
                .city(member.getAddress().getCity())
                .build();
    }
}
