import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../service/login.service';
import { Router } from '@angular/router';
import { MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition }
   from '@angular/material/snack-bar';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  constructor(private router: Router, private loginService: LoginService, private snackBar: MatSnackBar) { }
  horizontalPosition: MatSnackBarHorizontalPosition = 'start';
  verticalPosition: MatSnackBarVerticalPosition = 'bottom';

  ngOnInit(): void {
  }

  onSubmit() { 
      let username = (<HTMLInputElement>document.getElementById('username')).value;
      let password = (<HTMLInputElement>document.getElementById('password')).value;
      let credentials = { username: username, password: password };

      if(username && password) {
        this.loginService.login(credentials).subscribe(data => 
          { 
            localStorage.setItem("jwt", data.accessToken); 
            this.router.navigateByUrl('/imageUpload');
          },
          error => 
          {
            this.snackBar.open('Invalid username and/or password ', 'Cancel', {
              horizontalPosition: this.horizontalPosition,
              verticalPosition: this.verticalPosition,
              duration: 10000,
            });
          }
        ); 
      } else {
          this.snackBar.open('Enter your username and password', 'Cancel', { 
              horizontalPosition: this.horizontalPosition,
              verticalPosition: this.verticalPosition,
              duration: 10000,
            });
      }
  } 

}
