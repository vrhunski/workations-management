import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';


export type RiskLevel = 'HIGH_RISK' | 'LOW_RISK' | 'NO_RISK';

export interface WorkationRequest {
  employee: string;
  country: string;
  countryDest: string;
  startDate: Date | string;
  endDate: Date | string;
  days: number;
  risk: RiskLevel;
}

export interface WorkationResponse {
  id: number;
  employee: string;
  country: string;
  countryDest: string;
  startDate: Date | string;
  endDate: Date | string;
  days: number;
  risk: RiskLevel;
}

export interface PageResponse<T> {
  content: T[];
  pageable: {
    pageNumber: number;
    pageSize: number;
    sort: {
      sorted: boolean;
      unsorted: boolean;
      empty: boolean;
    };
    offset: number;
    paged: boolean;
    unpaged: boolean;
  };
  totalPages: number;
  totalElements: number;
  last: boolean;
  size: number;
  number: number;
  sort: {
    sorted: boolean;
    unsorted: boolean;
    empty: boolean;
  };
  numberOfElements: number;
  first: boolean;
  empty: boolean;
}

export interface WorkationQueryParams {
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: 'ASC' | 'DESC';
  employee?: string;
  country?: string;
  countryDest?: string;
  risk?: RiskLevel;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/api/v1/workations';

  constructor(private http: HttpClient) { }

  getAllWorkations(params?: WorkationQueryParams): Observable<PageResponse<WorkationResponse>> {
    let httpParams = new HttpParams();

    if (params) {
      if (params.page !== undefined) httpParams = httpParams.set('page', params.page.toString());
      if (params.size !== undefined) httpParams = httpParams.set('size', params.size.toString());
      if (params.sortBy) httpParams = httpParams.set('sortBy', params.sortBy);
      if (params.sortDirection) httpParams = httpParams.set('sortDirection', params.sortDirection);
      if (params.employee) httpParams = httpParams.set('employee', params.employee);
      if (params.country) httpParams = httpParams.set('country', params.country);
      if (params.countryDest) httpParams = httpParams.set('countryDest', params.countryDest);
      if (params.risk) httpParams = httpParams.set('risk', params.risk);
    }

    return this.http.get<PageResponse<WorkationResponse>>(this.apiUrl, { params: httpParams });
  }

  getWorkationById(id: number): Observable<WorkationResponse> {
    return this.http.get<WorkationResponse>(`${this.apiUrl}/${id}`);
  }

  createWorkation(workation: WorkationRequest): Observable<WorkationResponse> {
    return this.http.post<WorkationResponse>(this.apiUrl, workation);
  }

  updateWorkation(id: number, workation: WorkationRequest): Observable<WorkationResponse> {
    return this.http.put<WorkationResponse>(`${this.apiUrl}/${id}`, workation);
  }

  deleteWorkation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
