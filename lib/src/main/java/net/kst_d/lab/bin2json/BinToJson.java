package net.kst_d.lab.bin2json;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import net.kst_d.common.CommonUtils;
import net.kst_d.common.Generator;
import net.kst_d.common.SID;
import net.kst_d.common.log.KstLogger;
import net.kst_d.common.log.KstLoggerFactory;
import net.kst_d.common.log.MethodLogger;

public class BinToJson {
    private static final KstLogger LOG = KstLoggerFactory.logger(BinToJson.class);

    public static String read(byte[] in) throws JsonProcessingException {
	return read(in, Generator.sid());
    }

    public static String read(byte[] in, SID sid) throws JsonProcessingException {
	final MethodLogger logger = LOG.silentEnter(sid, "read");

	logger.trace("{}", CommonUtils.hex(in));

	final Map<String, Object> data = Converters.readStructure(in, 0, in.length, sid);
	logger.trace("after conversion {}", data);

	ObjectMapper mapper = new ObjectMapper()
			.registerModule(new Jdk8Module())
			.registerModule(new JavaTimeModule());
	final ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter()
			.without(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

	return writer.writeValueAsString(data);
    }
}
