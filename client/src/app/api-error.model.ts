export interface ApiFieldError {
  field: string;
  message: string;
}

export class ApiError extends Error {
  constructor(
    public status: number,
    public override message: string,
    public original: any,
    public errors?: ApiFieldError[],
    public path?: string,
    public timestamp?: string
  ) {
    super(message);
    Object.setPrototypeOf(this, ApiError.prototype);
  }

  toJSON() {
    return {
      status: this.status,
      message: this.message,
      errors: this.errors,
      path: this.path,
      timestamp: this.timestamp,
    };
  }
}

/**
 * Maps a backend error response to a typed ApiError.
 */
export function mapApiError(response: any): ApiError {
  console.log('API Error:', response);

  const payload = response?.error ?? response;
  const data = payload?.data ?? payload;

  const status: number = data?.status ?? response?.status ?? 500;
  const message: string =
    data?.message ??
    response?.message ??
    'Unknown API error';
  const errors: ApiFieldError[] = data?.errors ?? [];
  const path: string = payload?.path ?? '';
  const timestamp: string = payload?.timestamp ?? new Date().toISOString();

  return new ApiError(status, message, response, errors, path, timestamp);
}
