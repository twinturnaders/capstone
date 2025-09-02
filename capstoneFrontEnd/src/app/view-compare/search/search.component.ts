import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Component, OnInit} from '@angular/core';
import { MunicipalityService, RateSummaryDto } from '../../shared/municipality.service';
import {CurrencyPipe, NgForOf, NgIf} from '@angular/common';
import {StarRatingComponent} from '../../shared/rating/rating.component';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  imports: [
    CurrencyPipe,
    StarRatingComponent,
    ReactiveFormsModule,
    NgForOf,
    NgIf
  ],
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  loading = false;
  error = '';
  results: RateSummaryDto[] = [];


  radiusOptions = [30, 60, 100, 250];

  searchForm!: FormGroup;

  constructor(private fb: FormBuilder, private svc: MunicipalityService) {}

ngOnInit() {


  this.searchForm = this.fb.group({
    zip: ['', [Validators.required, Validators.pattern(/^\d{5}$/)]],
    radius: [10, [Validators.required]],
    gallonsUsed: [5000, [Validators.min(0)]]
  });
}

  get zipCtrl() { return this.searchForm.get('zip'); }


  onSearch() {
    if (this.searchForm.invalid) return;


    const { zip, radius, gallonsUsed } = this.searchForm.value as any;
    this.loading = true; this.error = '';


    this.svc.getNearby(String(zip), Number(radius), Number(gallonsUsed))
      .subscribe({
        next: arr => { this.results = arr ?? []; this.loading = false; },
        error: e => { this.error = this.readableError(e); this.loading = false; }
      });
  }


  private readableError(e: any): string {
    if (e?.status === 0) return 'Cannot reach server. Check API URL / CORS.';
    if (e?.status === 403) return 'Forbidden. Check auth/cookies.';
    return (e?.error?.message) || 'Something went wrong.';
  }


  starsOf(conf?: string | null): number {
    return (conf ?? '').trim().length;
  }
}
