package vip.yeee.memo.demo.stools.kit.xmlmapper;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/3/9 13:34
 */
@XStreamAlias("root")
@Data
class TestXmlObject {
    @XStreamAlias("class")
    @XStreamAsAttribute
    private String clazz = "vip.yeee.memo.demo.stools.kit.xmlmapper.A";
    @XStreamAlias("subject")
    private Subject subject;
    @XStreamAlias("extraInfo")
    private ExtraInfo extraInfo;

    @Data
    public static class Subject {
        @XStreamAlias("class")
        @XStreamAsAttribute
        private String clazz = "vip.yeee.memo.demo.stools.kit.xmlmapper.B";
        @XStreamConverter(TestXmlFieldConverter.class)
        private String title;
        @XStreamConverter(TestXmlFieldConverter.class)
        private String desc;
        @XStreamConverter(TestXmlFieldConverter.class)
        private Date createTime;
    }

    @Data
    public static class ExtraInfo {
        @XStreamAlias("class")
        @XStreamAsAttribute
        private String clazz = "vip.yeee.memo.demo.stools.kit.xmlmapper.C";
        @XStreamImplicit(itemFieldName = "item")
        private List<ExtraItem> itemList;
    }

    @Data
    public static class ExtraItem {
        @XStreamConverter(TestXmlFieldConverter.class)
        private String key;
        @XStreamConverter(TestXmlFieldConverter.class)
        private String value;
    }

    public String toXML() {
        XStream xstream = new XStream();
        xstream.processAnnotations(this.getClass());
        String temp = "" +
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<root defDateFormat=\"yyyy-MM-dd HH:mm:ss\" driver=\"vip.yeee.memo.demo.stools.kit.xmlmapper.CCCC\" version=\"1.0\">\n" +
                "   {}\n" +
                "</root>";
        return StrUtil.format(temp, xstream.toXML(this));
    }

    public static void main(String[] args) {
        TestXmlObject xmlObject = new TestXmlObject();
        Subject subject = new Subject();
        ExtraInfo extraInfo = new ExtraInfo();
        extraInfo.setItemList(Lists.newArrayList());
        xmlObject.setSubject(subject);
        xmlObject.setExtraInfo(extraInfo);

        subject.setTitle("测试标题");
        subject.setDesc("测试描述测试描述测试描述测试描述");
        subject.setCreateTime(new Date());

        ExtraItem extraItem = new ExtraItem();
        extraItem.setKey("key1");
        extraItem.setValue("value1");
        extraInfo.getItemList().add(extraItem);

        ExtraItem extraItem2 = new ExtraItem();
        extraItem2.setKey("key2");
        extraItem2.setValue("value2");
        extraInfo.getItemList().add(extraItem2);

        ExtraItem extraItem3 = new ExtraItem();
        extraItem3.setKey("key3");
        extraItem3.setValue("value3");
        extraInfo.getItemList().add(extraItem3);


        System.out.println(xmlObject.toXML());
    }
}
