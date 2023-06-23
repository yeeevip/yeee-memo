package vip.yeee.memo.demo.elasticsearch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.yeee.memo.demo.elasticsearch.domain.mysql.entity.TProject;
import vip.yeee.memo.demo.elasticsearch.domain.mysql.mapper.TProjectMapper;
import org.springframework.stereotype.Service;
import vip.yeee.memo.demo.elasticsearch.service.ITProjectService;

/**
 * <p>
 * 众筹项目表 服务实现类
 * </p>
 *
 * @author yeeee
 * @since 2022-05-17
 */
@Service
public class TProjectServiceImpl extends ServiceImpl<TProjectMapper, TProject> implements ITProjectService {

}
