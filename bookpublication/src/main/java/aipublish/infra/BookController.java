package aipublish.infra;

import aipublish.domain.*;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/books")
@Transactional
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @RequestMapping(
        value = "/books/submitbookcommand",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Book submitBookCommand(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody SubmitBookCommandCommand submitBookCommandCommand
    ) throws Exception {
        System.out.println("##### /book/submitBookCommand  called #####");
        Book book = new Book();
        book.submitBookCommand(submitBookCommandCommand);
        bookRepository.save(book);
        return book;
    }

    @RequestMapping(
        value = "/books/savebookcommand",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Book saveBookCommand(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody SaveBookCommandCommand saveBookCommandCommand
    ) throws Exception {
        System.out.println("##### /book/saveBookCommand  called #####");
        Book book = new Book();
        book.saveBookCommand(saveBookCommandCommand);
        bookRepository.save(book);
        return book;
    }
}
//>>> Clean Arch / Inbound Adaptor
