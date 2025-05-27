export type ErrorResponse = {
    message: string;
    timestamp: string;
    code: string;
    path: string;
};

export type ValidationError = {
    timestamp: string;
    status: string;
    error: string;
    messages: string[];
    path: string;
};
