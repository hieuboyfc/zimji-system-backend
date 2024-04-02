package com.zimji.system.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.zimji.system.utils.enums.ResponseCode;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "success", "title", "message", "description", "result"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseResponse<E> implements Serializable {

    static final long serialVersionUID = -1082791442821228L;

    String code;

    String title;

    String message;

    String description;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ssZ",
            timezone = "Asia/Ho_Chi_Minh"
    )
    Date timestamp;

    E result;

    public BaseResponse() {
        this.code = ResponseCode.SUCCESS.getCode();
        this.message = ResponseCode.SUCCESS.getMessage();
    }

    public BaseResponse(Integer code) {
        this.code = code.toString();
        this.message = ResponseCode.getMessage(this.code);
        this.title = ResponseCode.getTitle(this.code);
    }

    public BaseResponse(String code) {
        this.code = code;
        this.message = ResponseCode.getMessage(code);
        this.title = ResponseCode.getTitle(code);
    }

    public BaseResponse(Integer code, String message) {
        this.code = code.toString();
        this.message = message;
        this.title = ResponseCode.getTitle(this.code);
    }

}