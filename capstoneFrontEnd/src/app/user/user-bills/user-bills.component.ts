import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { finalize } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import {CommonModule} from '@angular/common';
import {RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {MatCheckbox} from '@angular/material/checkbox';

export interface BillFeeDTO {
  name?: string;
  amount?: number;
}

export interface UserBillDTO {
  id?: number;
  billDate?: string;
  dueDate?: string;
  paidDate?: string;
  paid?: boolean;


  waterUsage?: number;
  sewerUsage?: number;
  waterCharge?: number;
  sewerCharge?: number;
  fees?: BillFeeDTO[];


}



export interface BillCompareResult {
  municipality: string;
  state: string;
  county: string;
  estimatedWaterCharge: number;
  estimatedSewerCharge: number;
  total: number;
}

@Component({
  selector: 'app-user-bills',
  templateUrl: './user-bills.component.html',
  styleUrls: ['./user-bills.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
  imports: [CommonModule, RouterLink, RouterOutlet]})
export class UserBillsComponent implements OnInit {

  private base = `${environment.apiUrl}/api/userbills`;

  bills: UserBillDTO[] = [];
  loading = false;
  error: string | null = null;

  // selection / actions
  selectedBill: UserBillDTO | null = null;

  compareResults: BillCompareResult[] = [];
  compareLoading = false;
  compareError: string | null = null;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadBills();
  }

  loadBills(): void {
    this.loading = true;
    this.error = null;

    this.http.get<UserBillDTO[]>(this.base)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (rows) => {
          // normalize for UI (usage/amount/unit/municipalityName)
          this.bills = (rows || []).map(b => this.normalizeBill(b));
        },
        error: (err) => {
          console.error('Failed to load bills', err);
          this.error = err?.status === 401 ? 'Please sign in again.' : 'Could not load bills.';
        }
      });
  }

  // saveNewBill(req: CreateBillRequest): void {
  //   this.error = null;
  //   this.loading = true;
  //
  //   this.http.post(`${this.base}`, req)
  //     .pipe(finalize(() => this.loading = false))
  //     .subscribe({
  //       next: () => this.loadBills(),
  //       error: (err) => {
  //         console.error('Save bill failed', err);
  //         this.error = 'Failed to save bill.';
  //       }
  //     });
  // }



  viewBill(bill: UserBillDTO): void {
    this.selectedBill = bill;
    console.log('Viewing bill', bill);
  }

  compare(waterUsage: number, sewerUsage: number): void {
    if (waterUsage == null || sewerUsage == null) return;

    this.compareLoading = true;
    this.compareError = null;
    this.compareResults = [];

    const params = { waterUsage, sewerUsage } as any;

    this.http.get<BillCompareResult[]>(`${this.base}/compare`, { params })
      .pipe(finalize(() => this.compareLoading = false))
      .subscribe({
        next: (rows) => this.compareResults = rows || [],
        error: (err) => {
          console.error('Compare failed', err);
          this.compareError = err?.error || 'Compare failed.';
        }
      });
  }


  hasData(b: UserBillDTO): boolean {
    return !!b.billDate || !!b.dueDate || !!b.paid || !!b.waterUsage || !!b.sewerUsage || !! b.waterCharge
    || !!b.sewerCharge || !! b.fees;

  }

  private normalizeBill(b: UserBillDTO): {
    id?: number;
    billDate?: string;
    dueDate?: string;
    paidDate?: string;
    paid?: boolean;
    waterUsage?: number;
    sewerUsage?: number;
    waterCharge?: number;
    sewerCharge?: number;
    fees?: BillFeeDTO[];

  } {
    const water = this.toNumber(b.waterCharge);
    const sewer = this.toNumber(b.sewerCharge);
    const feesTotal = (b.fees || []).reduce((sum, f) => sum + this.toNumber(f.amount), 0);






    return {
      ...b,


    };
  }

  private toNumber(v: any): number {
    if (v == null) return 0;
    if (typeof v === 'number') return v;
    const n = Number(v);
    return Number.isFinite(n) ? n : 0;
  }
}
