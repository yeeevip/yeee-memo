
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* create by yeah.一页 ${generateDate}
*/
@Repository
public class ${tableClass.shortClassName}${mapperSuffix} extends BaseDAO implements ${tableClass.shortClassName}Mapper {

    @Override
    protected String getNameSpace() {
        return "${tableClass.shortClassName}Mapper.";
    }

    @Override
    public List<${tableClass.shortClassName}> getList(${tableClass.shortClassName} ${tableClass.variableName}) {
        return selectList("getList",${tableClass.variableName});
    }

    @Override
    public ${tableClass.shortClassName} getOne(${tableClass.shortClassName} ${tableClass.variableName}) {
        return (${tableClass.shortClassName}) selectOne("getOne",${tableClass.variableName});
    }

    @Override
    public int insert(${tableClass.shortClassName} ${tableClass.variableName}) {
        return insert("insert",${tableClass.variableName});
    }

    @Override
    public int updateByPrimaryKey(${tableClass.shortClassName} ${tableClass.variableName}) {
        if(${tableClass.variableName} == null || ${tableClass.variableName}.getId() == null)return 0;
        return update("updateByPrimaryKey",${tableClass.variableName});
    }

    @Override
    public int batchInsert(List<${tableClass.shortClassName}> ${tableClass.variableName}List) {
        return insert("batchInsert",${tableClass.variableName}List);
    }
}



