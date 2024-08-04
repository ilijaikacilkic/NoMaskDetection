import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition }
   from '@angular/material/snack-bar';

@Component({
  selector: 'app-image-upload',
  templateUrl: './image-upload.component.html',
  styleUrls: ['./image-upload.component.css']
})

export class ImageUploadComponent implements OnInit {

  constructor(private http: HttpClient, private _snackBar: MatSnackBar) { }
  fileToUpload: File;
  errorMessage = '';
  horizontalPosition: MatSnackBarHorizontalPosition = 'start';
  verticalPosition: MatSnackBarVerticalPosition = 'bottom';

  ngOnInit(): void {
   
  }

  onFileSelected(event: any) {
      this.fileToUpload = event.target.files[0]
  }

  onClick(event: any) {
    const fd = new FormData();
    fd.append('file', this.fileToUpload);
    this.http.post("http://localhost:8080/image/", fd,
      { headers: new HttpHeaders({ "Authorization": "Bearer " + localStorage.getItem("jwt")}), responseType: 'text'}
    )
    .subscribe(
      res => { 
          this._snackBar.open('File uploaded!', 'Cancel', {
            horizontalPosition: this.horizontalPosition,
            verticalPosition: this.verticalPosition,
            duration: 10000,
        })
      }, 
      error => {
        if(!((<HTMLInputElement>document.getElementById('file')).value)) {
          this._snackBar.open('Please select a file!', 'Cancel', {
            horizontalPosition: this.horizontalPosition,
            verticalPosition: this.verticalPosition,
            duration: 10000,
          });
        } else {
          this._snackBar.open(error.error, 'Cancel', { 
              horizontalPosition: this.horizontalPosition,
              verticalPosition: this.verticalPosition,
              duration: 10000,
          });
        }
      }
    );
  }
}
