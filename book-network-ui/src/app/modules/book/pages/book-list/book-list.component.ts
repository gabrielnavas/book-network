import {Component, OnInit} from '@angular/core';
import {BookService} from "../../../../services/services/book.service";
import {Router} from "@angular/router";
import {PageResponseBookResponse} from "../../../../services/models/page-response-book-response";
import {BookCardComponent} from "../../components/book-card/book-card.component";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {faAngleLeft, faAnglesLeft, faAnglesRight, faChevronRight} from "@fortawesome/free-solid-svg-icons";
import {NgForOf, NgIf} from "@angular/common";
import {BookResponse} from "../../../../services/models/book-response";

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [
    BookCardComponent,
    FaIconComponent,
    NgForOf,
    NgIf
  ],
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss',
})
export class BookListComponent implements OnInit {
  bookResponse: PageResponseBookResponse = {};
  page = 0;
  size = 5;

  icons = {
    faAngleLeft: faAngleLeft,
    faAnglesLeft: faAnglesLeft,
    faChevronRight: faChevronRight,
    faAnglesRight: faAnglesRight,
  }

  protected message: string = '';
  protected level: string = 'success';

  constructor(
    private readonly router: Router,
    private readonly bookService: BookService
  ) {
  }

  get isLastPage() {
    return this.page === this.bookResponse.totalPages as number - 1;
  }

  ngOnInit(): void {
    this.findAllBooks();
  }

  goToPage(page: number) {
    this.page = page;
    this.findAllBooks();
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBooks();
  }

  goToPreviousPage() {
    this.page--;
    this.findAllBooks();
  }

  goToLastPage() {
    this.page = this.bookResponse.totalPages as number - 1;
    this.findAllBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllBooks();
  }

  borrowBook(book: BookResponse) {
    this.message = ''
    this.bookService.borrowBook({
      "book-id": book.id as number,
    }).subscribe({
      next: value => {
        this.message = 'Book successfully added to your list';
        this.level = 'success'
      },
      error: err => {
        this.message = err.error.error;
        this.level = 'danger'
      }
    })
  }

  private findAllBooks() {
    this.bookService.findAllBooks({
      page: this.page,
      size: this.size
    })
      .subscribe({
        next: (books) => this.bookResponse = books
      });
  }
}
