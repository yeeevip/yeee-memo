package vip.yeee.memo.demo.scloud.tac.seatapgsql.typehandler;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTWriter;
import java.io.IOException;

/**
 * description......
 *
 * @author yeeee
 * @since 2024/5/10 10:19
 */
public class GeometryModule extends SimpleModule {
    public GeometryModule() {
        addDeserializer(Geometry.class, new WKTGeometryDeserializer());
        addSerializer(Geometry.class, new WKTGeometrySerializer());
    }
    public static class WKTGeometryDeserializer extends JsonDeserializer<Geometry> {

        private final org.locationtech.jts.io.WKTReader wktReader = new org.locationtech.jts.io.WKTReader();

        @Override
        public Geometry deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String wkt = p.getText();
            try {
                return  wktReader.read(wkt);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class WKTGeometrySerializer extends JsonSerializer<Geometry> {
        @Override
        public void serialize(Geometry value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            try {
                WKTWriter writer = new WKTWriter();
                gen.writeString(writer.write(value));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
