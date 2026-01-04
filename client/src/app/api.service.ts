import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../environment';

export const API = {
  V1: {
    PUBLIC: '/api/v1/public',
    PRIVATE: '/api/v1/private',
    ADMIN: '/api/v1/admin'
  }
} as const;

export interface ResponseWrapper<T> {
  data: T;
  path: string;
  status: number;
  timestamp: string;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private readonly baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  get<T>(url: string, params?: HttpParams) {
    return this.http.get<ResponseWrapper<T>>(`${this.baseUrl}${url}`, { params, withCredentials: true });
  }

  post<T>(url: string, body: unknown) {
    return this.http.post<ResponseWrapper<T>>(`${this.baseUrl}${url}`, body,{ withCredentials: true });
  }

  put<T>(url: string, body: unknown) {
    return this.http.put<ResponseWrapper<T>>(`${this.baseUrl}${url}`, body,{ withCredentials: true });
  }

  delete<T>(url: string) {
    return this.http.delete<ResponseWrapper<T>>(`${this.baseUrl}${url}`,{ withCredentials: true });
  }
}
