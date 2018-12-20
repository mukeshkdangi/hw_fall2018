import { Component } from '@angular/core';
import { SearchForm } from './SearchForm';
import { ResultService } from './result.service';
import { HttpClient } from '@angular/common/http';
import { CustomeFadeInAnimation } from './CustomeFadeInAnimation';
import { NgxSpinnerService } from 'ngx-spinner';
import { Subject } from 'rxjs';


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
  public keywordList: string[];
  public typedKeyword: string;
  public lastKeyPress: number = 0;
  public eventKey = new Subject();
  public correctSpell: string
  public show: number = 10;
  isSpellMistake: boolean
  matchedSubstring: string

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

  searchAutoCompelete($event) {
    this.keywordList = [];
    if ($event.timeStamp - this.lastKeyPress > 100) {
      this.eventKey.next($event.target.value);
      this.typedKeyword = $event.target.value;
      if (this.typedKeyword.length > 0) {
        this.getTicketMasterAPISuggestion();
      }
    }
    this.lastKeyPress = $event.timeStamp;
  }

  getSpellingSuggestion(value) {
    value  = value.replace(/ +(?= )/g,'');
    this.url = '/api/spellcorrect?keyword=' + value.toLowerCase();
    this.service.url = this.url;
    console.log('hitting url ..', this.service.url)
    this.http.get<any>(this.url).subscribe(data => {
      this.correctSpell = data.word;
      console.log('tempSthis.typedKeywordpell 1', value.toLowerCase());
      if (this.correctSpell) {
        if (this.correctSpell === value.toLowerCase()) {
          this.correctSpell = undefined;
        }
      }
    });
  }

  getTicketMasterAPISuggestion() {
    this.typedKeyword = this.typedKeyword.toLowerCase()
    let intial = this.typedKeyword.substring(0, this.typedKeyword.lastIndexOf(" ") + 1);
    intial = intial.replace(/ +(?= )/g,'');
    let last = this.typedKeyword.substring(this.typedKeyword.lastIndexOf(" ") + 1, this.typedKeyword.length);
    last = last.replace(/ +(?= )/g,'');

    this.url = '/api/suggest?keyword=' + last;
    this.service.url = this.url;
    console.log('hitting url ..', this.service.url)
    this.http.get<any>(this.url).subscribe(data => {
      let suggestResponse = data;
      suggestResponse.suggest.suggest[last].suggestions.forEach(element => {
        let term = element.term
        if (term)
          this.keywordList.push(intial + term);
      });;
    });
  }

  onSubmit() {
    this.isSpellMistake = false;
    this.correctSpell = undefined;
    this.getSpellingSuggestion(this.form.keyword)
    this.searchFinished = false;
    this.form.keyword = this.form.keyword.replace(/ +(?= )/g,'');
    this.url = '/api/getresult?keyword=' + this.form.keyword + '&rows=10';

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

  getSnippet(value) {
    this.url = '/api/snippet?file=' + value;
    this.service.url = this.url;
    console.log('hitting url ..', this.service.url)
    this.http.get<any>(this.url).subscribe(data => {
      let suggestResponse = data.data;
      let matced = suggestResponse.match(this.form.keyword)
      if (matced && matced.length > 0) {
        let min = suggestResponse.length > matced.index - 80 ? matced.index - 80 : 0;
        let max = suggestResponse.length > matced.index + 80 ? matced.index + 80 : suggestResponse.length;
        this.matchedSubstring = '...' + suggestResponse.substring(min, max) + '...';
      } else {
        let allWords = this.form.keyword.split(" ");
        for (let idx = 0; idx < allWords.length; idx++) {
          let len = 160 / allWords.length
          matced = suggestResponse.match(allWords[idx]);
          if (matced && matced.length > 0) {
            let min = suggestResponse.length > matced.index - len ? matced.index - len : 0;
            let max = suggestResponse.length > matced.index + len ? matced.index + len : suggestResponse.length;
            this.matchedSubstring = this.matchedSubstring==undefined ?  '...' + suggestResponse.substring(min, max) + '...' : this.matchedSubstring +'...' + suggestResponse.substring(min, max) + '...';

          }
        }
      }
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
