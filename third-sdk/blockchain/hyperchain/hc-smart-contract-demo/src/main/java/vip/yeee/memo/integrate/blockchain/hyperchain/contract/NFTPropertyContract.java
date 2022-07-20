package vip.yeee.memo.integrate.blockchain.hyperchain.contract;

import cn.hyperchain.common.utils.ByteUtil;
import cn.hyperchain.common.utils.StringUtil;
import cn.hyperchain.contract.BaseContract;
import cn.hyperchain.contract.property.HPC721Metadata;
import cn.hyperchain.contract.property.PropertyV1;
import com.google.gson.Gson;


/**
 * 数字藏品智能合约接口实现类
 * @author yeeee
 * @since 2022/5/13 15:09
 */
public class NFTPropertyContract extends BaseContract implements HPC721Metadata, NFTPropertyService {

    @Override
    public boolean issue(String ownerAddr, String id, String metaData) {
        if (!this.getOrigin().equals(this.getDeployer())) {
            throw new RuntimeException("the account has no permission");
        }
        if (StringUtil.checkEmpty(ownerAddr) || StringUtil.checkEmpty(metaData)) {
            throw new RuntimeException("the ownerAddr or metaData is not allow empty");
        }
        MetaInfo metaInfo = new Gson().fromJson(metaData, MetaInfo.class);
        if (StringUtil.checkEmpty(metaInfo.getId()) || StringUtil.checkEmpty(metaInfo.getName()) || StringUtil.checkEmpty(metaInfo.getIssuer()) || StringUtil.checkEmpty(metaInfo.getUri())) {
            throw new RuntimeException("the content of metaData is incomplete");
        }
        if (!String.valueOf(id).equals(metaInfo.getId())) {
            throw new RuntimeException("the id and metaData's id not match");
        }
        PropertyV1 property = getProperty0(id.getBytes());
        if (property != null && ownerAddr.equals(property.getOwner())) {
            return true;
        }
        emitProperty(id, ownerAddr, metaData);
        return true;
    }

    @Override
    public boolean issue(String ownerAddr, String id) {
        if (!this.getOrigin().equals(this.getDeployer())) {
            throw new RuntimeException("the account has no permission");
        }
        PropertyV1 property = getProperty0(id.getBytes());
        if (property != null && ownerAddr.equals(property.getOwner())) {
            return true;
        }
        emitProperty(id, ownerAddr, "{}");
        return true;
    }

    @Override
    public boolean transfer(String id, String fromAddr, String toAddr) {
        if (StringUtil.checkEmpty(fromAddr) || StringUtil.checkEmpty(toAddr)) {
            throw new RuntimeException("the fromAddr or toAddr is not allow empty");
        }
        if (!this.getOrigin().equals(this.getDeployer())) {
            throw new RuntimeException("the account has no permission");
        }
        PropertyV1 property = getProperty0(id.getBytes());
        if (property == null) {
            throw new RuntimeException("the property is not exist");
        }
        if (toAddr.equals(property.getOwner())) {
            return true;
        }
        if (!fromAddr.equals(property.getOwner())) {
            throw new RuntimeException("the property owner is not belong to from");
        }
        property.setOwner(toAddr);
        return true;
    }

    @Override
    public boolean transfer(String id, String fromAddr, String toAddr, String metaData) {
        if (StringUtil.checkEmpty(fromAddr) || StringUtil.checkEmpty(toAddr) || StringUtil.checkEmpty(metaData)) {
            throw new RuntimeException("the fromAddr or toAddr or metaData is not allow empty");
        }
        if (!this.getOrigin().equals(this.getDeployer())) {
            throw new RuntimeException("the account has no permission");
        }
        PropertyV1 property = getProperty0(id.getBytes());
        if (property == null) {
            throw new RuntimeException("the property is not exist");
        }
        if (toAddr.equals(property.getOwner())) {
            return true;
        }
        if (!fromAddr.equals(property.getOwner())) {
            throw new RuntimeException("the property owner is not belong to from");
        }
        property.setMetaData(metaData);
        property.setOwner(toAddr);
        return true;
    }

    /**
     * 获取某个资产的拥有者地址
     * 公开查询
     *
     * @param id 资产id
     * @return 资产拥有者
     */
    @Override
    public String ownerOf(long id) {
        PropertyV1 property = getProperty0(ByteUtil.longToBytes(id));
        if (property == null) {
            throw new RuntimeException("the property is not exist");
        }
        return property.getOwner();
    }

    /**
     * 将`id`的资产进行转移，从拥有者手中转移到购买账户上，资产需要处于锁定状态
     * 由应用操作权限账户发起
     *
     * 1. 判断交易发起账户是否为应用操作权限账户
     * 2. 获取资产，若资产为空，则报错
     * 3. 判断资产的所有者是否为参数中指定的资产拥有者地址，若不是则报错
     * 4. 修改资产的所有者为购买者地址
     *
     * @param from 资产拥有者账户地址
     * @param to 购买者账户地址
     * @param id 资产ID
     */
    @Override
    public void transferFrom(String from, String to, long id) {

    }

    /**
     * 铸造资产，指定资产的唯一标识，资产的所有者，资产的自定义meta信息
     * 铸造资产权限：需要由铸造权账户才能进行铸造
     *
     * 1. 检查账户是否有铸造权，检查参数合法性
     * 2. 将id唯一标识转移为byte[]格式的统一表示方式，调用emit0进行发布资产账户
     * 3. 资产账户所有者和meta信息由接口参数指定
     * 4. 初始创建的资产资产为未发布状态(默认状态)，所有者为参数owner
     * 5. 若id对应的已经有资产账户已经被创建出来，则会报错
     *
     * @param id 资产的唯一标识，重复将报错
     * @param owner 资产的所有者地址，不可为null或空字符串
     * @param meta 资产的自定义meta信息，不可为null
     */
    public void emitProperty(String id, String owner, String meta) {
        emit0(id.getBytes(), owner, meta.getBytes());
    }

    /**
     * 将某个资产状态修改为对应的状态`status`
     *
     * 1. 检查是否由应用操作权限账户发起
     * 2. 检查修改的账户状态是否为可修改的状态范围
     * 3. 获取资产，若资产不存在，则报错
     * 4. 修改资产的状态
     *
     * @param id 资产的唯一标识
     * @param status 修改的资产状态
     */
    public void setPropertyStatus(long id, int status) {
        if (!this.getOrigin().equals(this.getDeployer())) {
            throw new RuntimeException("the account has no permission");
        }
        PropertyV1 property = getProperty0(ByteUtil.longToBytes(id));
        if (property == null) {
            throw new RuntimeException("the property is not exist");
        }
        property.setStatus(status);
    }

    /**
     * 查询资产对应的状态信息
     * 公开查询
     *
     * @param id 资产的唯一标识
     * @return 资产对应的状态
     */
    public int queryStatus(String id) {
        PropertyV1 property = getProperty0(id.getBytes());
        if (property == null) {
            throw new RuntimeException("the property is not exist");
        }
        return property.getStatus();
    }

    @Override
    public void eventTransfer(String from, String to, long id) {

    }

    @Override
    public void eventApproval(String owner, String approved, long id) {

    }

    @Override
    public void eventApprovalForAll(String owner, String operator, boolean approved) {

    }

    @Override
    public long balanceOf(String owner) {
        return -1;
    }

    @Override
    public void transferFrom(String from, String to, long id, byte[] calldata) {
        this.transferFrom(from, to, id);
    }

    @Override
    public void approve(String to, long id) {

    }

    @Override
    public String getApproved(long id) {
        return null;
    }

    @Override
    public void setApprovalForAll(String operator, boolean approved) {

    }

    @Override
    public boolean isApprovedForAll(String owner, String operator) {
        return false;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public String symbol() {
        return null;
    }

    @Override
    public String uri(long id) {
        return null;
    }

}
