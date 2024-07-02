import {Component} from '@angular/core';
import {NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {BookRequest} from "../../../../services/models/book-request";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {faSave} from "@fortawesome/free-solid-svg-icons";
import {Router, RouterLink} from "@angular/router";
import {BookService} from "../../../../services/services/book.service";

@Component({
  selector: 'app-manage-book',
  standalone: true,
  imports: [
    NgForOf,
    NgOptimizedImage,
    NgIf,
    FormsModule,
    FaIconComponent,
    RouterLink
  ],
  templateUrl: './manage-book.component.html',
  styleUrl: './manage-book.component.scss'
})
export class ManageBookComponent {

  bookRequest: BookRequest = {authorName: '', isbn: '', synopsis: '', title: '', shareable: false}
  errorMsg: Array<string> = [];
  selectedBookCover: File | undefined;
  selectedPicture: string | undefined;
  protected readonly faSave = faSave;


  constructor(
    private readonly bookService: BookService,
    private readonly router: Router) {
  }

  onFileSelected(event: Event) {
    this.selectedBookCover = (event.target as any).files[0] as File;
    if (this.selectedBookCover) {
      this.showFile();
    }
  }

  saveBook() {
    this.bookService.saveBook({
      body: this.bookRequest
    }).subscribe({
      next: bookId => {
        if (this.selectedBookCover) {
          this.bookService.uploadBookCoverPicture({
            "book-id": bookId, body: {file: this.selectedBookCover!}
          }).subscribe({
            complete: () => {
              this.router.navigate(['/books/my-books']);
            }
          })
        }

      },
      error: err => {
        if (err.error.validationErrors) {
          this.errorMsg = err.error.validationErrors;
        } else if (err.error) {
          this.errorMsg = err.error.validationErrors;
        }
      }
    })
  }

  private showFile(): void {
    if (this.selectedBookCover) {
      const reader = new FileReader();
      reader.onload = () => {
        this.selectedPicture = reader.result as string;
      }
      reader.readAsDataURL(this.selectedBookCover);
    }
  }
}
