import {Component, OnInit} from '@angular/core';
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {
  faAngleLeft,
  faAnglesLeft,
  faAnglesRight,
  faChevronRight,
  faCircleCheck,
  faCogs,
  faPaperPlane,
  faSave,
  faStar
} from "@fortawesome/free-solid-svg-icons";
import {PageResponseBorrowedBooksResponse} from "../../../../services/models/page-response-borrowed-books-response";
import {BorrowedBooksResponse} from "../../../../services/models/borrowed-books-response";
import {BookService} from "../../../../services/services/book.service";
import {NgForOf} from "@angular/common";
import {FeedbackRequest} from "../../../../services/models/feedback-request";
import {FormsModule} from "@angular/forms";
import {RatingComponent} from "../../components/rating/rating.component";
import {RouterLink} from "@angular/router";
import {FeedbackService} from "../../../../services/services/feedback.service";

@Component({
  selector: 'app-borrowed-book-list',
  standalone: true,
  imports: [
    FaIconComponent,
    NgForOf,
    FormsModule,
    RatingComponent,
    RouterLink
  ],
  templateUrl: './borrowed-book-list.component.html',
  styleUrl: './borrowed-book-list.component.scss'
})
export class BorrowedBookListComponent implements OnInit {
  protected page = 0;
  protected size = 5;

  protected selectedBook: BorrowedBooksResponse | undefined = undefined;
  protected borrowedBook: PageResponseBorrowedBooksResponse = {};
  protected feedbackRequest: FeedbackRequest = {bookId: 0, comment: '', note: 0};

  protected readonly icons = {
    faCogs: faCogs,
    faStar: faStar,
    faPaperPlane: faPaperPlane,
    faCircleCheck: faCircleCheck,
    faAnglesLeft: faAnglesLeft,
    faAngleLeft: faAngleLeft,
    faChevronRight: faChevronRight,
    faAnglesRight: faAnglesRight,
    faSave: faSave,
  }

  constructor(
    private readonly bookService: BookService,
    private readonly feedbackService: FeedbackService
  ) {

  }

  ngOnInit(): void {
    this.findAllBorrowedBooks();
  }

  returnBorrowedBook(book: BorrowedBooksResponse) {
    this.selectedBook = book;
    this.feedbackRequest.bookId = this.selectedBook.id as number;
  }

  returnBook(withFeedback: boolean) {
    if (!this.selectedBook) {
      throw new Error('missing borrow book');
    }
    this.bookService.returnBorrowBook({
      "book-id": this.selectedBook.id as number,
    }).subscribe({
      next: () => {
        if (withFeedback) {
          this.giveFeedback();
        }
        this.selectedBook = undefined;
        this.findAllBorrowedBooks();
      },
      error: err => {

      }
    })
  }

  cancelSelectedBook() {
    this.selectedBook = undefined;
  }

  protected isLastPage(): boolean {
    return this.page === this.borrowedBook.totalPages;
  }

  protected goToPage(page: number) {
    this.page = page;
    this.findAllBorrowedBooks();
  }

  protected goToFirstPage() {
    this.page = 0;
    this.findAllBorrowedBooks();
  }

  protected goToPreviousPage() {
    this.page--;
    this.findAllBorrowedBooks();
  }

  protected goToLastPage() {
    this.page = this.borrowedBook.totalPages as number - 1;
    this.findAllBorrowedBooks();
  }

  protected goToNextPage() {
    this.page++;
    this.findAllBorrowedBooks();
  }

  private findAllBorrowedBooks() {
    this.bookService.findAllBorrowedBooks({
      page: this.page,
      size: this.size,
    }).subscribe({
      next: (books) => {
        this.borrowedBook = books;
      }
    })
  }

  private giveFeedback() {
    if (!this.selectedBook) {
      throw new Error('missing borrow book');
    }
    if (this.feedbackRequest.bookId <= 0) {
      throw new Error('missing book id');
    }
    this.feedbackService.saveFeedback({
      body: this.feedbackRequest
    }).subscribe({
      next: (feedback) => {

      },
      error: err => {

      }
    })
  }
}
