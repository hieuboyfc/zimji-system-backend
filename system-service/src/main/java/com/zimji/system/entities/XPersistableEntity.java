package com.zimji.system.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public class XPersistableEntity {

    Long companyId;

    @Version
    Long version;

    Integer status;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ssZ",
            timezone = "Asia/Ho_Chi_Minh"
    )
    Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ssZ",
            timezone = "Asia/Ho_Chi_Minh"
    )
    Date modifiedDate;

    @CreatedBy
    String createBy;

    @LastModifiedBy
    String updateBy;

    @PrePersist
    void setInitialDate() {
        this.createDate = this.modifiedDate = new Date();

        if (ObjectUtils.isEmpty(this.createBy)) {
            this.createBy = "system";
        }

        if (ObjectUtils.isEmpty(this.updateBy)) {
            this.updateBy = "system";
        }

        if (ObjectUtils.isEmpty(this.status)) {
            this.status = 1;
        }
    }

    @PreUpdate
    void updateDate() {
        this.modifiedDate = new Date();
    }

}