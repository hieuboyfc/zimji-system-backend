package com.zimji.system.utils.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ResponseCode {

    SUCCESS("0", "success", "success"),
    ERROR("1", "error", "error"),
    BUSINESS_EXCEPTION("5000", "business_error", "business_exception"),
    BAD_REQUEST("4000", "bad_request", "bad_request"),
    INVALID("4003", "invalid", "invalid"),
    NO_CONTENT("4001", "request_error", "no_content"),
    NOT_FOUND("4004", "not_found_entity", "not_found_entity"),
    INTERNAL_SERVER_ERROR("4005", "server_error", "internal_server_error"),
    PERMISSION_DENIED("4006", "permission_denied", "permission_denied"),
    FILE_NOT_DETECT("4007", "file_not_detect", "file_not_detect"),
    UN_CAPTURE_EXCEPTION("4008", "un_capture_exception", "un_capture_exception"),
    FIELD_REQUIRE("4009", "field_require", "field_require"),
    FIELD_INVALID_TYPE("4010", "field_invalid_type", "field_invalid_type"),
    FIELD_INVALID_VALUE("4011", "field_invalid_value", "field_invalid_value"),
    DUPLICATE_DATA("4012", "data_duplicate", "data_duplicate"),
    VALUE_MUST_MORE("4013", "value_must_more", "value_must_more"),
    VALUE_MUST_MORE_AND_EQUAL("4014", "value_must_more_and_equal", "value_must_more_and_equal"),
    VALUE_MUST_LESS("4015", "value_must_less", "value_must_less"),
    VALUE_MUST_LESS_AND_EQUAL("4016", "value_must_less_and_equal", "value_must_less_and_equal"),
    VALUE_LENGTH("4017", "value_length", "value_length"),
    VALUE_RANGE("4018", "value_range", "value_range"),
    VALUE_IN_LIST("4019", "value_in_list", "value_in_list");

    String code;
    String message;
    String title;

    ResponseCode(String code, String title, String message) {
        this.code = code;
        this.message = message;
        this.title = title;
    }

    public static String getMessage(String code) {
        return Arrays.stream(ResponseCode.values())
                .filter(responseCode -> responseCode.getCode().equals(code))
                .findFirst()
                .map(ResponseCode::getMessage)
                .orElse(null);
    }

    public static String getTitle(String code) {
        return Arrays.stream(ResponseCode.values())
                .filter(responseCode -> responseCode.getCode().equals(code))
                .findFirst()
                .map(ResponseCode::getTitle)
                .orElse(null);
    }

}