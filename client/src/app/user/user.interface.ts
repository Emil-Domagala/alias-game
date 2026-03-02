import { UserRole } from './user-role.enum';
import { UserScore } from './user-score.interface';

export interface UserResponse {
  id: string;
  nick: string;
  email: string;
  roles: UserRole[];
  createdAt: string;
  updatedAt: string;
  score: UserScore;
}

export interface User {
  id: string;
  nick: string;
  email: string;
  roles: UserRole[];
  createdAt: Date;
  updatedAt: Date;
  score: UserScore;
}

export function mapUserResponseToUser(
  response: UserResponse
): User {
  return {
    ...response,
    createdAt: new Date(response.createdAt),
    updatedAt: new Date(response.updatedAt),
    score: { ...response.score }
  };
}
