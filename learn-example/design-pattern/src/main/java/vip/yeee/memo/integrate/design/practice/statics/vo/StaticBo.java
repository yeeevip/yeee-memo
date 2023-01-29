package vip.yeee.memo.integrate.design.practice.statics.vo;

import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/1/3 13:28
 */
@Data
public class StaticBo {

    private OprType oprType;

    private String siteId;

    private List<String> subjectIds;

    public static StaticBo genBo(OprType oprType, String siteId,  List<String> subjectIds) {
        StaticBo staticBo = StaticBo.genBo(siteId, subjectIds);
        staticBo.setOprType(oprType);
        return staticBo;
    }

    public static StaticBo genBo(String siteId,  List<String> subjectIds) {
        StaticBo staticBo = new StaticBo();
        staticBo.setSiteId(siteId);
        staticBo.setSubjectIds(subjectIds);
        return staticBo;
    }

    public static StaticBo genOpenBo(String siteId, List<String> subjectIds) {
        return StaticBo.genBo(OprType.OPEN, siteId, subjectIds);
    }

    public static StaticBo genCloseBo(String siteId, List<String> subjectIds) {
        return StaticBo.genBo(OprType.CLOSE, siteId, subjectIds);
    }

    public static StaticBo genDeleteBo(String siteId, List<String> subjectIds) {
        return StaticBo.genBo(OprType.DELETE, siteId, subjectIds);
    }

    public static StaticBo genUpdateBo(String siteId, List<String> subjectIds) {
        return StaticBo.genBo(OprType.UPDATE, siteId, subjectIds);
    }

    @Getter
    public enum OprType {

        OPEN(10, "开启"),
        CLOSE(20, "关闭"),
        DELETE(30, "删除"),
        UPDATE(40, "修改")
        ;

        private final Integer code;
        private final String desc;

        OprType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

}
