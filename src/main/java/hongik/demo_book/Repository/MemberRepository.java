package hongik.demo_book.Repository;

import hongik.demo_book.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;




//db단계 말고 객체 다누이로 담기
//extends 다음에 객체 명으로 넣기
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = "memberAuthorities")
    Optional<Member> findOneWithAuthoritiesByEmail(String email);
}
