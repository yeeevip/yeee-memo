package vip.yeee.memo.demo.blockchain.hyperchain.contract;

import java.util.Objects;

/**
 * 藏品实体类
 *
 * @author https://www.yeee.vip
 * @since 2022/5/13 15:09
 */
public class MetaInfo {

    public MetaInfo() {
    }

    /**
     * ID
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 发行方
     */
    private String issuer;

    /**
     * oss地址
     */
    private String uri;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetaInfo dcInfo = (MetaInfo) o;
        return Objects.equals(id, dcInfo.id) && Objects.equals(name, dcInfo.name) && Objects.equals(issuer, dcInfo.issuer) && Objects.equals(uri, dcInfo.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, issuer, uri);
    }
}
