package ${package};

import ${tableClass.packageName}.${tableClass.shortClassName};
import ${modelPackage}.domain.mapper.${tableClass.shortClassName}Mapper;
import ${modelPackage}.model.request.IdRequest;
import ${modelPackage}.model.request.${tableClass.shortClassName}AddRequest;
import ${modelPackage}.model.request.${tableClass.shortClassName}ListRequest;
import ${modelPackage}.model.request.${tableClass.shortClassName}UpdRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
* create by yeee.一页 ${generateDate}
*/
@Service
public class ${tableClass.shortClassName}${serviceSuffix} extends ServiceImpl<${tableClass.shortClassName}Mapper, ${tableClass.shortClassName}> {

    public List<${tableClass.shortClassName}> ${tableClass.variableName}PageList(${tableClass.shortClassName}ListRequest request) {
        return null;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public void ${tableClass.variableName}Add(${tableClass.shortClassName}AddRequest request) {

    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public void ${tableClass.variableName}Upd(${tableClass.shortClassName}UpdRequest request) {

    }

    public void ${tableClass.variableName}Del(IdRequest request) {

    }

    public ${tableClass.shortClassName} query${tableClass.shortClassName}ById(Object id) {
        return null;
    }
}



