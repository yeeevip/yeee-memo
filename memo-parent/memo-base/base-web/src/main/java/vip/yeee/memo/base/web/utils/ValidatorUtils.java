package vip.yeee.memo.base.web.utils;

import cn.hutool.core.collection.CollectionUtil;
import vip.yeee.memo.base.model.exception.BizException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

public class ValidatorUtils {

    public static <T> void checkAnnoParams(T obj, Class<?>... group) {
        Set<ConstraintViolation<T>> set = ((Validator)SpringContextUtils.getBean(Validator.class)).validate(obj, group);
        if (CollectionUtil.isNotEmpty(set)) {
            throw new BizException(set.iterator().next().getMessage());
        }
    }

    public static <T> void checkAnnoParams(T obj, List<Class<?>> groups) {
        checkAnnoParams(obj, groups.toArray(new Class[] {}));
    }


}
