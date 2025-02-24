package hongik.demo_book.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "member")
@Getter
@Builder @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
//이메일, 가입날짜, 비밀번호 추가하기

//pk
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name="membername", length = 50)
    private String membername;

    @Column(name = "email", unique = true)
    @Email
    private String email;

    @Column(length = 100)
    private String password;
    @Temporal(TemporalType.TIMESTAMP)
    private Date join_time;
    @Embedded
    private Address address;

//활성화 여부
    @Column(name = "activated")
    private boolean activated;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Category> categories = new ArrayList<>();


    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Library> librarys = new ArrayList<>();

    //관계를 1대다 다대1로 새로 매핑(authority랑)
    @ManyToMany
    @JoinTable(
            name = "member_authority",
            joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

}
