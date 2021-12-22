package mzc.data.portal.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class ApiResponse<T> {
    int status;
    String error;
    T data;

    public ApiResponse(int status, String error, T data) {
        this.status = status;
        this.error = error;
        this.data = data;
    }

    public static final int OK = 200;
    public static final int CREATED = 201;

    //일반적인 오류
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int CONFLICT = 409;
    public static final int INTERNAL_SERVER_ERROR = 500;

    //원인불명
    public static final int UNKNOWN = -9;

}
