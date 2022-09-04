
import java.util.List;
/**
 * create by yeah.一页 ${generateDate}
 */
public interface ${tableClass.shortClassName}${mapperSuffix} {
    List<${tableClass.shortClassName}> getList(${tableClass.shortClassName} ${tableClass.variableName});
    ${tableClass.shortClassName} getOne(${tableClass.shortClassName} ${tableClass.variableName});
    int insert(${tableClass.shortClassName} ${tableClass.variableName});
    int updateByPrimaryKey(${tableClass.shortClassName} ${tableClass.variableName});
    int batchInsert(List<${tableClass.shortClassName}> ${tableClass.variableName}List);
}




