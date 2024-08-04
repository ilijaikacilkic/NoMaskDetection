import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient/*, public dialog: MatDialog*/) { }

  login(credentials: any): Observable<any> {
    //alert(credentials.username + ' ' + credentials.password)
    return this.http.post("http://localhost:8081/auth/login", JSON.stringify(credentials), 
      { headers: new HttpHeaders({ 'Content-Type' : 'application/json'})}
    );
  }

   /*openDialog() {
     this.dialog.open(DialogElementsExampleDialog);
   }*/

}
