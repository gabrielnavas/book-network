import {Component, EventEmitter, Input, Output} from '@angular/core';
import {BookResponse} from "../../../../services/models/book-response";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {
  faArchive,
  faBook,
  faCircleInfo,
  faCode,
  faEdit,
  faHeart,
  faListCheck,
  faShareNodes,
  faUser,
  faUserCheck
} from "@fortawesome/free-solid-svg-icons";
import {RatingComponent} from "../rating/rating.component";

@Component({
  selector: 'app-book-card',
  standalone: true,
  imports: [
    FaIconComponent,
    RatingComponent
  ],
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.scss'
})
export class BookCardComponent {
  icons = {
    faBook: faBook,
    faUserCheck: faUserCheck,
    faCode: faCode,
    faUser: faUser,
    faCircleInfo: faCircleInfo,
    faListCheck: faListCheck,
    faHeart: faHeart,
    faEdit: faEdit,
    faShareNodes: faShareNodes,
    faArchive: faArchive,
  }

  @Output() private share: EventEmitter<BookResponse> = new EventEmitter();
  @Output() private archive: EventEmitter<BookResponse> = new EventEmitter();
  @Output() private addToWaitingList: EventEmitter<BookResponse> = new EventEmitter();
  @Output() private borrow: EventEmitter<BookResponse> = new EventEmitter();
  @Output() private edit: EventEmitter<BookResponse> = new EventEmitter();
  @Output() private details: EventEmitter<BookResponse> = new EventEmitter();

  private _manage = false;

  get manage(): boolean {
    return this._manage;
  }

  @Input()
  set manage(value: boolean) {
    this._manage = value;
  }

  private _bookCover: string | undefined;

  get bookCover(): string | undefined {
    if (this._book.cover) {
      return this.convertToBase64(this._book.cover)
    }
    return 'https://as2.ftcdn.net/v2/jpg/05/17/53/57/1000_F_517535712_q7f9QC9X6TQxWi6xYZZbMmw5cnLMr279.jpg';
  }

  set bookCover(value: string | undefined) {
    this._bookCover = value;
  }

  private _book: BookResponse = {}

  get book(): BookResponse {
    return this._book;
  }

  @Input()
  set book(value: BookResponse) {
    this._book = value;
  }

  onEdit() {
    this.edit.emit(this.book);
  }

  onShare() {
    this.share.emit(this.book);
  }

  onArchive() {
    this.archive.emit(this.book);
  }

  onShowDetails() {
    this.details.emit(this.book);
  }

  onBorrow() {
    this.borrow.emit(this.book);
  }

  onAddToWaitingList() {
    this.addToWaitingList.emit(this.book);
  }

  private convertToBase64(cover: string[]): string {
    const prefixBase64 = 'data:image/jpg;base64,';
    return `${prefixBase64}${this._book.cover}`;
  }
}
