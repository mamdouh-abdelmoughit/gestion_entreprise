export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  role?: string; // Optional, backend defaults to ROLE_USER
}

