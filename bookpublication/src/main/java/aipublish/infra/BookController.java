package aipublish.infra;

import aipublish.domain.*;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/books")
@Transactional
public class BookController {

@Autowired
    BookRepository bookRepository;

    @PostMapping("/books/savebookcommand")
    public Book saveBookCommand(@RequestBody SaveBookCommand command) {
        System.out.println("##### /books/savebookcommand called #####");
        Book book = new Book();
        book.saveBookCommand(command);
        return bookRepository.save(book);
    }

    @PostMapping("/books/submitbookcommand")
    public Book submitBookCommand(@RequestBody SubmitBookCommand command) throws Exception {
        System.out.println("##### /books/submitbookcommand called #####");

        Optional<Book> optionalBook = bookRepository.findById(command.getBookId());
        if (!optionalBook.isPresent()) {
            throw new Exception("Book not found with id: " + command.getBookId());
        }

        Book book = optionalBook.get();
        book.submitBookCommand(command); // 상태 변경 및 이벤트 발행
        return bookRepository.save(book); // 갱신된 Book 저장
    }

    @PostMapping("/books/updatemetadata")
    public Book updateBookMetadata(@RequestBody UpdateBookMetadataCommand command) {
        System.out.println("##### /books/updatemetadata called #####");

        Optional<Book> optionalBook = bookRepository.findById(command.getBookId());
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.updateBookMetadata(command);
            return bookRepository.save(book);
        } else {
            throw new RuntimeException("Book not found with ID: " + command.getBookId());
        }
    }
}
//>>> Clean Arch / Inbound Adaptor
