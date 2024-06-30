import {Component, OnInit} from '@angular/core';
import {BookService} from "../../../../services/services/book.service";
import {Router} from "@angular/router";
import {PageResponseBookResponse} from "../../../../services/models/page-response-book-response";
import {BookCardComponent} from "../../components/book-card/book-card.component";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {faAngleLeft, faAnglesLeft, faAnglesRight, faChevronRight} from "@fortawesome/free-solid-svg-icons";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [
    BookCardComponent,
    FaIconComponent,
    NgForOf
  ],
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss',
})
export class BookListComponent implements OnInit {
  bookResponse: PageResponseBookResponse = {};
  page = 0;
  size = 1;

  protected readonly faAngleLeft = faAngleLeft;
  protected readonly faAnglesLeft = faAnglesLeft;
  protected readonly faChevronRight = faChevronRight;
  protected readonly faAnglesRight = faAnglesRight;

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
