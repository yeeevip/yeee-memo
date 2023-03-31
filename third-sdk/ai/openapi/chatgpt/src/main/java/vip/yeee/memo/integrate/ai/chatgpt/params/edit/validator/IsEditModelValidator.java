package vip.yeee.memo.integrate.ai.chatgpt.params.edit.validator;

import cn.hutool.core.lang.Validator;
import vip.yeee.memo.integrate.ai.chatgpt.params.edit.constant.EditModelEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsEditModelValidator implements ConstraintValidator<IsEditModel, Object> {
    @Override
    public void initialize(IsEditModel constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (Validator.isEmpty(o)){
            return false;
        }
        String model = o.toString();
        if (model.equals(EditModelEnum.TEXT.getModel()) || model.equals(EditModelEnum.CODE.getModel())){
            return true;
        }
        return false;
    }
}
