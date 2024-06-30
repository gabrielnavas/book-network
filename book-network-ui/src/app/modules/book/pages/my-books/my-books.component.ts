import {Component, OnInit} from '@angular/core';
import {BookCardComponent} from "../../components/book-card/book-card.component";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {NgForOf, NgIf} from "@angular/common";
import {PageResponseBookResponse} from "../../../../services/models/page-response-book-response";
import {faAngleLeft, faAnglesLeft, faAnglesRight, faChevronRight, faPlus} from "@fortawesome/free-solid-svg-icons";
import {Router} from "@angular/router";
import {BookService} from "../../../../services/services/book.service";
import {BookResponse} from "../../../../services/models/book-response";

@Component({
  selector: 'app-my-books',
  standalone: true,
  imports: [
    BookCardComponent,
    FaIconComponent,
    NgForOf,
    NgIf
  ],
  templateUrl: './my-books.component.html',
  styleUrl: './my-books.component.scss'
})
export class MyBooksComponent implements OnInit {
  bookResponse: PageResponseBookResponse = {};
  page = 0;
  size = 5;

  icons = {
    faAngleLeft: faAngleLeft,
    faAnglesLeft: faAnglesLeft,
    faChevronRight: faChevronRight,
    faAnglesRight: faAnglesRight,
  }
  protected readonly faPlus = faPlus;

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

  archiveBook(book: BookResponse) {
  }

  shareBook(book: BookResponse) {
  }

  editBook(book: BookResponse) {
  }

  private findAllBooks() {
    this.bookService.findAllBooksByOwner({
      page: this.page,
      size: this.size
    })
      .subscribe({
        next: (books) => this.bookResponse = books
      });
  }
}
