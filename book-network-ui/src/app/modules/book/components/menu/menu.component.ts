import {Component, OnInit} from '@angular/core';
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {
  faBook,
  faBookBookmark,
  faDoorOpen,
  faHeart,
  faHomeAlt,
  faNavicon,
  faSearch
} from "@fortawesome/free-solid-svg-icons";
import {Router, RouterLink} from "@angular/router";
import {TokenService} from "../../../../services/token/token.service";

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [
    FaIconComponent,
    RouterLink
  ],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit {

  protected readonly icons = {
    faHomeAlt: faHomeAlt,
    faBook: faBook,
    faHeart: faHeart,
    faBookBookmark: faBookBookmark,
    faDoorOpen: faDoorOpen,
    faNavicon: faNavicon,
    faSearch: faSearch,
  }

  constructor(
    private readonly tokenService: TokenService,
    private readonly router: Router,
  ) {
  }

  ngOnInit() {
    const linkColor = document.querySelectorAll(".nav-link");
    linkColor.forEach(link => {
      this.keepLinkActiveOnEnterPage(link);

      link.addEventListener('click', () => {
        this.removeAllActive(linkColor);
        this.addActive(link);
      })
    })
  }

  logout() {
    this.tokenService.logout();
    window.location.reload();
  }

  private keepLinkActiveOnEnterPage(link: Element) {
    // keep the link active, on enter page
    const href = link.getAttribute("href") || '';
    const endWithHref = window.location.href.endsWith(href);
    if (endWithHref) {
      link.classList.add('active')
    }
  }

  private removeAllActive(linkColor: NodeListOf<Element>) {
    linkColor.forEach(l => l.classList.remove('active'));
  }

  private addActive(link: Element) {
    link.classList.add('active');
  }
}
