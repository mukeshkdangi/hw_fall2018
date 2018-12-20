import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class ResultService {

  public url;

  constructor(private http: HttpClient) { }

  getSearchResultByKeyword() {
     return this.returnResults(this.url)
  }

  private returnResults(url): any {
    console.log(url);
    return this.http.get<any>(url);
  }

}
