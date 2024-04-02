package com.zimji.system.payload.response;

import com.zimji.system.utils.MapperUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> {

    Integer pageNumber;

    Integer pageSize;

    Long totalElements;

    List<T> content;

    public PageResponse(Integer pageNumber, Integer pageSize, Long totalElements, List<T> content) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.content = content;
    }

    public PageResponse(Page<T> page, Class<T> clazz) {
        this.pageNumber = page.getPageable().getPageNumber();
        this.pageSize = page.getPageable().getPageSize();
        this.totalElements = page.getTotalElements();
        this.content = MapperUtils.map(page.getContent(), clazz);
    }

}