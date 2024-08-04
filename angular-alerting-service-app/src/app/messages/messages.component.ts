import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { DomSanitizer } from '@angular/platform-browser';
import { MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition } from '@angular/material/snack-bar';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit {

  constructor(private http: HttpClient, private sanitizer: DomSanitizer, private snackBar: MatSnackBar) { }
  horizontalPosition: MatSnackBarHorizontalPosition = 'start';
  verticalPosition: MatSnackBarVerticalPosition = 'bottom';

  public messages: Array<any> = [];
  public message: any;
  public messageId: number | any;

  ngOnInit(): void {
    this.getMessages().subscribe(data => {
      this.messages = data;
    })
  }

  getMessages(): Observable<any> {
    return this.http.get<any>("http://localhost:8081/message",
      { headers: new HttpHeaders({ "Authorization": "Bearer " + localStorage.getItem("jwt") }) }
    );
  }

  getMessageById() {
    if (this.messageId) {
      this.http.get<any>("http://localhost:8081/message/" + this.messageId,
        { headers: new HttpHeaders({ "Authorization": "Bearer " + localStorage.getItem("jwt") }) }
      ).subscribe(data => {
        this.messages = [];  // this.message 
        this.messages.push(data);
        (<HTMLInputElement>document.getElementById('clearSearch')).hidden = false;
      }, err => {
        this.snackBar.open(err.error, 'Cancel', {
          horizontalPosition: this.horizontalPosition,
          verticalPosition: this.verticalPosition,
          duration: 10000,
        });   
      })
    }
  }

  getAllMessages() {
    this.messageId = ''; 
    this.ngOnInit();
    (<HTMLInputElement>document.getElementById('clearSearch')).hidden = true;
  }

  deleteMessage(messageId: any) {
    if(confirm('Are you sure you want to remove an image that you think is false positive?')) {
      this.http.delete<any>("http://localhost:8081/message/" + messageId,
      { headers: new HttpHeaders({ "Authorization": "Bearer " + localStorage.getItem("jwt") }) })
        .subscribe(data => { })
      window.location.reload(); 
      alert('Message is deleted successfuly.') 
    } 
  }


  hideClearSearchButton() {
    if(this.messageId == '' || !this.messageId) {
      (<HTMLInputElement>document.getElementById('clearSearch')).hidden = true;
    } else {
      (<HTMLInputElement>document.getElementById('clearSearch')).hidden = false;
    }
  }
}
