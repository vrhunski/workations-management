
import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService, WorkationResponse, WorkationRequest, WorkationQueryParams, RiskLevel } from '../shared/services/api.service';

@Component({
  selector: 'app-workations',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './workations.component.html',
  styleUrls: ['./workations.component.scss']
})
export class WorkationsComponent implements OnInit {
  workations = signal<WorkationResponse[]>([]);
  loading = signal<boolean>(false);
  error = signal<string | null>(null);

  currentPage = signal<number>(0);
  pageSize = signal<number>(10);
  pageSizeValue = 10;
  totalPages = signal<number>(0);
  totalElements = signal<number>(0);

  currentSortBy = signal<string>('id');
  currentSortDirection = signal<'ASC' | 'DESC'>('ASC');

  // Country flag mapping
  private countryFlags: { [key: string]: string } = {
    'United States': '🇺🇸',
    'Portugal': '🇵🇹',
    'Singapore': '🇸🇬',
    'Japan': '🇯🇵',
    'Spain': '🇪🇸',
    'Mexico': '🇲🇽',
    'United Kingdom': '🇬🇧',
    'Thailand': '🇹🇭',
    'Canada': '🇨🇦',
    'Iceland': '🇮🇸',
    'Germany': '🇩🇪',
    'Croatia': '🇭🇷',
    'Australia': '🇦🇺',
    'Indonesia': '🇮🇩',
    'France': '🇫🇷',
    'Morocco': '🇲🇦',
    'Netherlands': '🇳🇱',
    'Greece': '🇬🇷',
    'Sweden': '🇸🇪',
    'Italy': '🇮🇹',
    'South Korea': '🇰🇷',
    'Vietnam': '🇻🇳',
    'Costa Rica': '🇨🇷',
    'India': '🇮🇳',
    'United Arab Emirates': '🇦🇪',
    'Switzerland': '🇨🇭',
    'Austria': '🇦🇹',
    'Ireland': '🇮🇪',
    'Israel': '🇮🇱',
    'Turkey': '🇹🇷',
    'Malaysia': '🇲🇾',
    'Czech Republic': '🇨🇿',
    'Brazil': '🇧🇷',
    'Argentina': '🇦🇷',
    'China': '🇨🇳',
    'Norway': '🇳🇴',
    'Denmark': '🇩🇰',
    'Russia': '🇷🇺',
    'Georgia': '🇬🇪',
    'New Zealand': '🇳🇿',
    'Poland': '🇵🇱',
    'Hungary': '🇭🇺',
    'Belgium': '🇧🇪',
    'Luxembourg': '🇱🇺',
    'Egypt': '🇪🇬',
    'Jordan': '🇯🇴',
    'Malta': '🇲🇹',
    'Philippines': '🇵🇭',
    'Sri Lanka': '🇱🇰'
  };

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadWorkations({
      size: this.pageSize(),
      sortBy: this.currentSortBy(),
      sortDirection: this.currentSortDirection()
    });
  }

  loadWorkations(params?: WorkationQueryParams): void {
    this.loading.set(true);
    this.error.set(null);

    this.apiService.getAllWorkations(params).subscribe({
      next: (response) => {
        this.workations.set(response.content);
        this.currentPage.set(response.number);
        this.totalPages.set(response.totalPages);
        this.totalElements.set(response.totalElements);
        this.pageSize.set(response.size);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Failed to load workations');
        console.error('Error loading workations:', err);
        this.loading.set(false);
      }
    });
  }

  sortBy(column: string): void {
    if (this.currentSortBy() === column) {
      // Toggle direction
      this.currentSortDirection.set(
        this.currentSortDirection() === 'ASC' ? 'DESC' : 'ASC'
      );
    } else {
      // New column, default to ASC
      this.currentSortBy.set(column);
      this.currentSortDirection.set('ASC');
    }

    this.loadWorkations({
      page: 0, // Reset to first page when sorting
      size: this.pageSize(),
      sortBy: this.currentSortBy(),
      sortDirection: this.currentSortDirection()
    });
  }

  onPageSizeChange(): void {
    this.pageSize.set(this.pageSizeValue);
    this.loadWorkations({
      page: 0,
      size: this.pageSize(),
      sortBy: this.currentSortBy(),
      sortDirection: this.currentSortDirection()
    });
  }

  nextPage(): void {
    if (this.currentPage() < this.totalPages() - 1) {
      this.loadWorkations({
        page: this.currentPage() + 1,
        size: this.pageSize(),
        sortBy: this.currentSortBy(),
        sortDirection: this.currentSortDirection()
      });
    }
  }

  previousPage(): void {
    if (this.currentPage() > 0) {
      this.loadWorkations({
        page: this.currentPage() - 1,
        size: this.pageSize(),
        sortBy: this.currentSortBy(),
        sortDirection: this.currentSortDirection()
      });
    }
  }

  goToPage(page: number): void {
    this.loadWorkations({
      page,
      size: this.pageSize(),
      sortBy: this.currentSortBy(),
      sortDirection: this.currentSortDirection()
    });
  }

  getPageNumbers(): number[] {
    const total = this.totalPages();
    const current = this.currentPage();
    const pages: number[] = [];

    // Show max 5 page numbers
    let start = Math.max(0, current - 2);
    let end = Math.min(total, start + 5);

    // Adjust start if we're near the end
    if (end - start < 5) {
      start = Math.max(0, end - 5);
    }

    for (let i = start; i < end; i++) {
      pages.push(i);
    }

    return pages;
  }

  deleteWorkation(id: number): void {
    if (confirm('Are you sure you want to delete this workation?')) {
      this.apiService.deleteWorkation(id).subscribe({
        next: () => {
          this.loadWorkations({
            page: this.currentPage(),
            size: this.pageSize(),
            sortBy: this.currentSortBy(),
            sortDirection: this.currentSortDirection()
          });
        },
        error: (err) => {
          this.error.set('Failed to delete workation');
          console.error('Error deleting workation:', err);
        }
      });
    }
  }

  editWorkation(workation: WorkationResponse): void {
    // TODO: Implement edit functionality
    console.log('Edit workation:', workation);
  }

  // Format date to dd/MM/yyyy
  formatDate(date: Date | string): string {
    const d = new Date(date);
    const day = String(d.getDate()).padStart(2, '0');
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const year = d.getFullYear();
    return `${day}/${month}/${year}`;
  }

  // Get country flag emoji
  getCountryFlag(country: string): string {
    return this.countryFlags[country] || '🏳️';
  }

  // Calculate risk level based on days
  // 0-50 days = NO_RISK
  // 50-100 days = LOW_RISK
  // More than 100 days = HIGH_RISK
  calculateRiskLevel(days: number): RiskLevel {
    if (days <= 50) {
      return 'NO_RISK';
    } else if (days <= 100) {
      return 'LOW_RISK';
    } else {
      return 'HIGH_RISK';
    }
  }

  // Get the risk level for a workation (calculated from days)
  getWorkationRisk(workation: WorkationResponse): RiskLevel {
    return this.calculateRiskLevel(workation.days);
  }

  // Format risk level for display
  formatRisk(risk: RiskLevel): string {
    return risk.replace('_', ' ');
  }

  // Get CSS class for risk
  getRiskClass(risk: RiskLevel): string {
    switch (risk) {
      case 'HIGH_RISK':
        return 'risk-high';
      case 'LOW_RISK':
        return 'risk-low';
      case 'NO_RISK':
        return 'risk-none';
      default:
        return '';
    }
  }

  // Get risk icon
  getRiskIcon(risk: RiskLevel): string {
    switch (risk) {
      case 'HIGH_RISK':
        return '🔴';
      case 'LOW_RISK':
        return '🟡';
      case 'NO_RISK':
        return '🟢';
      default:
        return '⚪';
    }
  }
}
