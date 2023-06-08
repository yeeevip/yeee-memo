package ${package};

import ${tableClass.packageName}.${tableClass.shortClassName};
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
/**
 * create by yeee.一页 ${generateDate}
 */
public interface ${tableClass.shortClassName}Mapper extends BaseMapper<${tableClass.shortClassName}> {

    List<${tableClass.shortClassName}> getList(${tableClass.shortClassName} ${tableClass.variableName});

    ${tableClass.shortClassName} getOne(${tableClass.shortClassName} ${tableClass.variableName});

    int batchInsert(List<${tableClass.shortClassName}> ${tableClass.variableName}List);

}