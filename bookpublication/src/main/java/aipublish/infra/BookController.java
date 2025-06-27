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
    public Book submitBookCommand(@RequestBody SubmitBookCommand command) {
        System.out.println("##### /books/submitbookcommand called #####");
        Book book = new Book();
        book.submitBookCommand(command);
        return bookRepository.save(book);
    }
}
//>>> Clean Arch / Inbound Adaptor
