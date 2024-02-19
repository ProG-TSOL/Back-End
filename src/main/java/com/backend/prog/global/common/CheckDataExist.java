package com.backend.prog.global.common;

import com.backend.prog.global.error.CommonException;
import com.backend.prog.global.error.ExceptionEnum;

import java.util.Collection;

public class CheckDataExist {

    /**
     * 데이터 존재 유무 체크
     */
    public static <T> void checkData(T entity) {
        if (entity == null) {
            throw new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST);
        }
        if (entity instanceof Collection) {
            if (((Collection<?>) entity).isEmpty()) {
                throw new CommonException(ExceptionEnum.DATA_DOES_NOT_EXIST);
            }
        }
    }
}
