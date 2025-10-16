import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // Your auth methods here
  isAuthenticated(): boolean {    
    // Implement your authentication logic here
    return !!localStorage.getItem('token'); // Example: check if a token exists in localStorage
  }
}
