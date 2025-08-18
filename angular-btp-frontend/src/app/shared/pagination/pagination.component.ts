import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Page } from '../../core/models/page.model';

@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pagination.component.html'
})
export class PaginationComponent {
  // Input: The component receives the entire Page object from its parent.
  @Input() page: Page<any> | null = null;

  // Output: The component emits the new page number when the user clicks.
  @Output() pageChange = new EventEmitter<number>();

  constructor() {}

  /**
   * Emits the new page number to the parent component.
   * @param newPage The page number to navigate to.
   */
  goToPage(newPage: number): void {
    if (this.page && newPage >= 0 && newPage < this.page.totalPages) {
      this.pageChange.emit(newPage);
    }
  }

  /**
   * Generates an array of page numbers to display in the template.
   * e.g., [1, 2, 3, 4, 5]
   */
  getPages(): number[] {
    if (!this.page) {
      return [];
    }
    return Array(this.page.totalPages).fill(0).map((x, i) => i);
  }
}
