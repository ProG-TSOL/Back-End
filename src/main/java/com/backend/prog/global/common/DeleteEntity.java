package com.backend.prog.global.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class DeleteEntity extends BaseEntity{
    @Column(name = "is_deleted")
    @ColumnDefault("false")
    @Comment("삭제 여부")
    private boolean isDeleted;

    public void deleteData(){
        this.isDeleted = true;
    }
}
