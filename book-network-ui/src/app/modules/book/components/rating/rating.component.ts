import {Component, Input} from '@angular/core';
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {faStar, faStarHalf} from "@fortawesome/free-solid-svg-icons";
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-rating',
  standalone: true,
  imports: [
    FaIconComponent,
    NgForOf,
    NgIf
  ],
  templateUrl: './rating.component.html',
  styleUrl: './rating.component.scss'
})
export class RatingComponent {
  @Input() rating: number = 0;
  maxRating = 5;
  protected readonly faStar = faStar;
  protected readonly faStarHalf = faStarHalf;

  get fullStars(): number {
    return Math.floor(this.rating);
  }

  get hasHalfStar(): boolean {
    return this.rating % 1 !== 0;
  }

  get emptyStars(): number {
    return this.maxRating - Math.ceil(this.rating);
  }
}
