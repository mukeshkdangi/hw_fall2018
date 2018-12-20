import { Component } from '@angular/core';
import { SearchForm } from './SearchForm';
import { ResultService } from './result.service';
import { HttpClient } from '@angular/common/http';
import { CustomeFadeInAnimation } from './CustomeFadeInAnimation';
import { NgxSpinnerService } from 'ngx-spinner';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [CustomeFadeInAnimation]
})


export class AppComponent {

  public title = 'Event Search ';
  public topics = ['Angular', 'Java', 'Javascript'];

  public unitName = [
    { "name": "Miles", "id": "miles" },
    { "name": "Kilometers", "id": "km" }
  ]

  public lat: Number
  public lon: number;
  public isUserInput = false

  public keyword: string;

  public type: any;
  public form = SearchForm
  public url: string
  searchResponse: any
  stats: any
  searchFinished: any

  public show: number = 10;

  urlToHtmlMap: Map<string, string> = new Map<string, string>();

  ngOnInit() {
    this.spinner.show();
    this.getURl2HTML();

  }
  getURl2HTML(): any {
    this.service.url = '/api/csvjson/'
    console.log('hitting url ..', this.service.url)
    this.http.get<any>(this.service.url).subscribe(data => {
      console.log(data)
      data.forEach(element => {
        this.urlToHtmlMap.set(element.docid, element.url);
      });
      setTimeout(() => {
        this.spinner.hide();
      }, 5000);
    });
  }

  getURL(id): any {
    return this.urlToHtmlMap.get(id);
  }

  constructor(
    private service: ResultService,
    private http: HttpClient,
    private spinner: NgxSpinnerService) {
  }

  onSubmit() {
    this.searchFinished = false;
    this.url = '/api/getresult?keyword=' + this.form.keyword + '&rows=50';

    console.log('form is ', this.form)
    if (this.form.isUserInput) {
      this.url = this.url + '&sort=pageRankFile%20desc'
    }

    this.service.url = this.url
    console.log('hitting url ..', this.service.url)
    this.http.get<any>(this.url).subscribe(data => {
      this.searchResponse = data.response;
      this.searchResponse.docs.forEach(element => {
        if (element.og_url && element.og_url.length > 0) {
          element.generator[0] = element.og_url[0];
        } else {
          let id2 = element.id.replace("/Users/mukesh/Office/Tools/Solr/solr-7.5.0/nypost/nypost/", "");
          element.generator[0] = this.urlToHtmlMap.get(id2)
        }
      });
      this.stats = data.responseHeader;
      console.log(this.searchResponse)
      this.searchFinished = true;
    });
  }

  updateAttractionId($event) {
  }

  updateCategoryId($event) {
    if ($event.options[$event.selectedIndex]) {
    }
  }


  updateUnitName($event) {
    if ($event.options[$event.selectedIndex]) {
    }
  }


}
