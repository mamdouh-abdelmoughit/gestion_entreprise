export interface User {
  id: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  enabled: boolean;
  roles: string[];
  createdByAdminId?: number;
  createdByAdminUsername?: string;
  // Multi-tenancy: organization info
  organizationId?: number;
  organizationName?: string;
}
