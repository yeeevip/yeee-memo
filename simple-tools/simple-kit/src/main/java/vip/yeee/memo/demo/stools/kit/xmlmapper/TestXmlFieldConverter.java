package vip.yeee.memo.demo.stools.kit.xmlmapper;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.util.Date;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/3/9 13:16
 */
public class TestXmlFieldConverter implements Converter {

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        writer.addAttribute("class", source.getClass().getName());
        if (source instanceof Date) {
            writer.setValue(DateUtil.format((Date) source, DatePattern.NORM_DATETIME_PATTERN));
        } else {
            writer.setValue(source.toString());
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return null;
    }

    @Override
    public boolean canConvert(Class type) {
        return true;
    }
}
