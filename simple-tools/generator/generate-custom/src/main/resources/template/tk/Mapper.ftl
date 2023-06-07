package ${package};

import ${tableClass.packageName}.${tableClass.shortClassName};
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
/**
 * create by yeee.一页 ${generateDate}
 */
public interface ${tableClass.shortClassName}${mapperSuffix} extends Mapper<${tableClass.shortClassName}> {

    List<${tableClass.shortClassName}> getList(${tableClass.shortClassName} ${tableClass.variableName});

    ${tableClass.shortClassName} getOne(${tableClass.shortClassName} ${tableClass.variableName});

    int batchInsert(List<${tableClass.shortClassName}> ${tableClass.variableName}List);

}




