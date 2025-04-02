package hongik.demo_book.apicontroller;


//아직 구현 X
//로그인 비밀번호 변경 + 주소까지 반환 만들고 그거하기

import hongik.demo_book.domain.CategoryName;
import hongik.demo_book.dto.BookDto;
import hongik.demo_book.dto.BookListDto;
import hongik.demo_book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    //도서 저장
    @PostMapping("/member/books")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<BookDto> saveMemberBook(
            @Valid @RequestBody BookDto bookdto
    ){
        return ResponseEntity.ok(bookService.BookSave(bookdto));
    }

    @GetMapping("/member/books")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<List<BookDto>> MemberBookList(
            @Valid @RequestParam String categoryName
    ){

        CategoryName categoryEnum;
        try {
            categoryEnum = CategoryName.valueOf(categoryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        BookListDto bookListDto = BookListDto.builder()
                .categoryName(categoryEnum)  // Enum 타입으로 설정
                .build();
        return ResponseEntity.ok(bookService.BookList(bookListDto));
    }


    //도서 삭제
    @DeleteMapping("/member/books")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<List<BookDto>> MemberBookDelete(
            @Valid @RequestBody BookDto bookDto
    ){
        return ResponseEntity.ok(bookService.BookDelete(bookDto));
    }
}
