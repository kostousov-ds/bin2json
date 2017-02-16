package net.kst_d.lab.bin2json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.kst_d.common.SID;
import net.kst_d.common.log.KstLogger;
import net.kst_d.common.log.KstLoggerFactory;
import net.kst_d.common.log.MethodLogger;

class Converters {
    private static final KstLogger LOG = KstLoggerFactory.logger(Converters.class);

    private static Charset CHARSET = Charset.forName("CP866");

    private Converters() {

    }

    static int readUInt16(byte[] in, int offset, int len, SID sid) {
	final MethodLogger logger = LOG.silentEnter(sid, "readUInt16");

	Objects.requireNonNull(in);

	if (len != 2) {
	    logger.debug("len must be 2");
	    throw new IllegalArgumentException("data length must be 2");
	}

	if (in.length < offset + len) {
	    logger.debug("offset with len are greater than data length: {} < {}", in.length, (offset + len));
	    throw new IllegalArgumentException("length is too big");
	}

	return (in[offset] & 0xff) | (in[offset + 1] & 0xff) << 8;
    }

    static long readUInt32(byte[] in, int offset, int len, SID sid) {
	final MethodLogger logger = LOG.silentEnter(sid, "readUInt32");

	Objects.requireNonNull(in);

	if (len != 4) {
	    logger.debug("len must be 4");
	    throw new IllegalArgumentException("data length must be 4");
	}

	if (in.length < offset + len) {
	    logger.debug("offset with len are greater than data length: {} < {}", in.length, (offset + len));
	    throw new IllegalArgumentException("length is too big");
	}

	return (in[offset] & 0xff) | (in[offset + 1] & 0xff) << 8 | (in[offset + 2] & 0xff) << 16 | (in[offset + 3] & 0xff) << 24;
    }

    static String readStr866(byte[] in, int offset, int len, SID sid) {
	if (in == null) {
	    return null;
	}
	return new String(in, offset, len, CHARSET);
    }

    static LocalDateTime readDate(byte[] in, int offset, int len, SID sid) {
	final long date = readUInt32(in, offset, len, sid);
	return LocalDateTime.ofEpochSecond(date, 0, ZoneOffset.UTC);
    }

    static BigDecimal readFloat(byte[] in, int offset, int len, SID sid) {
	final MethodLogger logger = LOG.silentEnter(sid, "readFloat");

	Objects.requireNonNull(in);
	if (len < 2) {
	    logger.debug("len must be greater 4");
	    throw new IllegalArgumentException("data length must be greater than 1");
	}

	if (in.length < offset + len) {
	    logger.debug("offset with len are greater than data length: {} < {}", in.length, (offset + len));
	    throw new IllegalArgumentException("length is too big");
	}

	int pos = in[offset];

	final BigInteger bigInteger = readInteger(in, offset + 1, len - 1, sid);

	return new BigDecimal(bigInteger, pos);
    }

    static BigInteger readInteger(byte[] in, int offset, int len, SID sid) {
	final MethodLogger logger = LOG.silentEnter(sid, "readInteger");
	Objects.requireNonNull(in);

	if (len < 1) {
	    logger.debug("len must be greater 0");
	    throw new IllegalArgumentException("data length must be greater 0");
	}

	if (in.length < offset + len) {
	    logger.debug("offset with len are greater than data length: {} < {}", in.length, (offset + len));
	    throw new IllegalArgumentException("length is too big");
	}

	byte[] num = new byte[len+1];
	num[0] = 0;
	for (int i = 0; i < len; i++) {
	    num[i + 1] = in[offset + len - 1 - i];
	}
	return new BigInteger(num);
    }

    static Map<String, Object> readStructure(byte[] in, int offset, int len, SID sid) {
	final MethodLogger logger = LOG.silentEnter(sid, "readStructure");

	len = len < 0 ? in.length - offset : len;

	Map<String,Object> ret = new HashMap<>();

	for (int position = offset; position < offset + len; ) {
	    int tag = readUInt16(in, position, 2, sid);
	    position += 2;
	    int l = readUInt16(in, position, 2, sid);
	    position += 2;
	    logger.trace("tag {}, data len {}", tag, len);

	    final FieldType field = FieldType.find(tag);
	    logger.trace("found field {}", field);
	    final Object data = field.type.converter.read(in, position, l, sid);
	    logger.trace("data {}", data);
	    position += l;

	    if (field.multiple) {
		List<Object> list = (List<Object>) ret.computeIfAbsent(field.fieldName, k -> new ArrayList<>());
		list.add(data);
	    }else{
		ret.put(field.fieldName, data);
	    }

	}
	return ret;
    }

}
