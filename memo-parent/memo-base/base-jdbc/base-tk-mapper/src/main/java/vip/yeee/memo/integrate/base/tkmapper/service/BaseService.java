package vip.yeee.memo.integrate.base.tkmapper.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class BaseService<T> {

    @Autowired
    private Mapper<T> mapper;

    // 缓存子类泛型类型
    private Class<T> cache = null;

    public T queryByPrimaryKey(Object key){
        return this.mapper.selectByPrimaryKey(key);
    }

    public List<T> queryAll(){
        return this.mapper.select(null);
    }


    public List<T> queryListByWhere(T pojo){
        return this.mapper.select(pojo);
    }

    public int queryCount(T pojo){
        return this.mapper.selectCount(pojo);
    }

    public int queryCountByExample(Example example){
        return this.mapper.selectCountByExample(example);
    }

    public T queryOne(T pojo){
        return this.mapper.selectOne(pojo);
    }

    public PageInfo<T> queryPageListByWhere(T pojo, int pageNo, int pageSize){
        PageHelper.startPage(pageNo, pageSize);
        List<T> list =  this.mapper.select(pojo);
        return new PageInfo<T>(list);

    }

    public PageInfo<T>  queryPageListByField(Integer pageNo, Integer pageSize, Sqls whereSqls, String orderByField, String ascOrDesc, String ...fields){
        return new PageInfo<T>(queryByFiledBase(pageNo,pageSize,null, whereSqls,orderByField, ascOrDesc, fields));
    }

    public PageInfo<T>  queryPageListByField(Integer pageNo, Integer pageSize, Example example, String ...fields){
        return new PageInfo<T>(queryByFiledBase(pageNo,pageSize,example,null,null, null, fields));
    }

    private List<T> queryByFiledBase(Integer pageNo,Integer pageSize,Example example, Sqls whereSqls,String orderByField, String ascOrDesc, String ...fields){
        if (example == null) {
            Example.Builder builder=null;
            if(null==fields||fields.length==0){
                //查询所有
                builder = Example.builder(getTypeArguement());

            }else{
                //查询指定字段,where的内容拿出来进行动态sql拼接
                builder= Example.builder(getTypeArguement()).select(fields);
            }
            if(whereSqls!=null){
                builder=builder.where(whereSqls);
            }

            if(orderByField!=null){
                if (ascOrDesc.toUpperCase().equals("DESC")){
                    builder= builder
                            .orderByDesc(orderByField);
                }else{
                    builder= builder
                            .orderByAsc(orderByField);
                }
            }
            example=builder.build();
        }
        if(pageNo!=null&&pageSize!=null) {
            // 分页插件
            PageHelper.startPage(pageNo, pageSize);
        }
        return this.mapper.selectByExample(example);
    }

    public List<T> queryByExample(Example example){
        return this.mapper.selectByExample(example);
    }

    public T queryOneByExample(Example example) {
        List<T> list = this.mapper.selectByExample(example);
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

    public Class<T> getTypeArguement() {
        if(cache==null)
            cache= (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return  cache;
    }

    public int save(T pojo){
        return this.mapper.insert(pojo);
    }

    public int saveSelect(T pojo){
        return this.mapper.insertSelective(pojo);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public int batchSave(List<T> list) {
        int result = 0;
        for (T record : list) {
            int count = mapper.insertSelective(record);
            result += count;
        }
        return result;
    }

    public int update(T pojo){
        return this.mapper.updateByPrimaryKey(pojo);
    }

    public int updateSelective(T pojo){
        return this.mapper.updateByPrimaryKeySelective(pojo);
    }

    public int deleteByPrimaryKey(T key){
        return this.mapper.deleteByPrimaryKey(key);
    }

    public int deleteByIds(Class<T> clazz,List<Object> ids){
        // where条件
        Example example = new Example(clazz);
        example.createCriteria().andIn("id", ids);
        return this.mapper.deleteByExample(example);
    /*
       等效于where id in (#{ids})
     */
    }

    public int  deleteByWhere(T pojo){
        return this.mapper.delete(pojo);
    }

    public int batchDelete(List<T> list) {
        int result = 0;
        for (T record : list) {
            int count = mapper.delete(record);
            if (count < 1) {
                throw new RuntimeException("删除数据失败!");
            }
            result += count;
        }
        return result;
    }

    public int deleteByExample(Object example){
        return this.mapper.deleteByPrimaryKey(example);
    }

}
