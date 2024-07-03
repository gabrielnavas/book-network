import {Component, OnInit} from '@angular/core';
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {NgForOf, NgIf} from "@angular/common";
import {PageResponseBorrowedBooksResponse} from "../../../../services/models/page-response-borrowed-books-response";
import {BookService} from "../../../../services/services/book.service";
import {
  faAngleLeft,
  faAnglesLeft,
  faAnglesRight,
  faChevronRight,
  faCircleCheck,
  faCogs,
  faPaperPlane,
  faStar
} from "@fortawesome/free-solid-svg-icons";
import {BorrowedBooksResponse} from "../../../../services/models/borrowed-books-response";

@Component({
  selector: 'app-return-books',
  standalone: true,
  imports: [
    FaIconComponent,
    NgForOf,
    NgIf
  ],
  templateUrl: './return-books.component.html',
  styleUrl: './return-books.component.scss'
})
export class ReturnBooksComponent implements OnInit {

  protected returnedBooks: PageResponseBorrowedBooksResponse = {}
  protected selectedBook: BorrowedBooksResponse | undefined = undefined;

  protected page = 0;
  protected size = 5;

  protected message: string = '';
  protected level: string = '';

  protected readonly icons = {
    faAnglesLeft: faAnglesLeft,
    faAngleLeft: faAngleLeft,
    faChevronRight: faChevronRight,
    faAnglesRight: faAnglesRight,
    faCogs: faCogs,
    faStar: faStar,
    faPaperPlane: faPaperPlane,
    faCircleCheck: faCircleCheck,
  }

  constructor(
    private readonly bookService: BookService
  ) {
  }

  ngOnInit(): void {
    this.findAllReturnedBooks();
  }

  approveBookReturn(book: BorrowedBooksResponse) {
    if (!book.returned) {
      this.level = 'danger';
      this.message = 'The book is not yet returned';
      return;
    }

    this.bookService.approveReturnBorrowBook({
      "book-id": book.id as number,
    }).subscribe({
      next: borrowedBookId => {
        this.level = 'success';
        this.message = 'Book return approved';

        this.returnedBooks.content = this.returnedBooks.content?.map(borrowedBook => {
          if (borrowedBook.id === book.id) {
            return {...book, returnApproved: true}
          }
          return borrowedBook
        })
      },
      error: err => {
        this.level = 'danger';
        if (err.error.validationErrors) {
          this.message = err.error.validationErrors;
        } else if (err.error) {
          this.message = err.error.error;
        }
      }
    })
  }

  protected isLastPage(): boolean {
    return this.page === this.returnedBooks.totalPages;
  }

  protected goToPage(page: number) {
    this.page = page;
    this.findAllReturnedBooks();
  }

  protected goToFirstPage() {
    this.page = 0;
    this.findAllReturnedBooks();
  }

  protected goToPreviousPage() {
    this.page--;
    this.findAllReturnedBooks();
  }

  protected goToLastPage() {
    this.page = this.returnedBooks.totalPages as number - 1;
    this.findAllReturnedBooks();
  }

  protected goToNextPage() {
    this.page++;
    this.findAllReturnedBooks();
  }

  private findAllReturnedBooks() {
    this.bookService.findAllReturnedBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: returnedBooks => {
        this.returnedBooks = returnedBooks
      },
      error: err => {

      }
    })
  }
}
