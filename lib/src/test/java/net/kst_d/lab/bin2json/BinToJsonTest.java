package net.kst_d.lab.bin2json;

import org.testng.Assert;
import org.testng.annotations.Test;

import net.kst_d.common.Generator;
import net.kst_d.common.SID;
import net.kst_d.common.log.KstLogger;
import net.kst_d.common.log.KstLoggerFactory;
import net.kst_d.common.log.MethodLogger;

public class BinToJsonTest {
    private static final KstLogger LOG = KstLoggerFactory.logger(BinToJsonTest.class);

    public static final byte[] DATA = new byte[] {
		    (byte) 0x01, (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0xA8, (byte) 0x32, (byte) 0x92, (byte) 0x56, (byte) 0x02, (byte) 0x00, (byte) 0x03, (byte) 0x00,
		    (byte) 0x04, (byte) 0x71, (byte) 0x02, (byte) 0x03, (byte) 0x00, (byte) 0x0B, (byte) 0x00, (byte) 0x8E, (byte) 0x8E, (byte) 0x8E, (byte) 0x20, (byte) 0x90,
		    (byte) 0xAE, (byte) 0xAC, (byte) 0xA0, (byte) 0xE8, (byte) 0xAA, (byte) 0xA0, (byte) 0x04, (byte) 0x00, (byte) 0x1D, (byte) 0x00, (byte) 0x0B, (byte) 0x00,
		    (byte) 0x07, (byte) 0x00, (byte) 0x84, (byte) 0xEB, (byte) 0xE0, (byte) 0xAE, (byte) 0xAA, (byte) 0xAE, (byte) 0xAB, (byte) 0x0C, (byte) 0x00, (byte) 0x02,
		    (byte) 0x00, (byte) 0x20, (byte) 0x4E, (byte) 0x0D, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x0E, (byte) 0x00, (byte) 0x02,
		    (byte) 0x00, (byte) 0x40, (byte) 0x9C
    };

    public static final String JSON = "" +
		    "{\n" +
		    "  \"dateTime\" : \"2016-01-10T10:30:00\",\n" +
		    "  \"orderNumber\" : 160004,\n" +
		    "  \"items\" : [ {\n" +
		    "    \"quantity\" : 2,\n" +
		    "    \"price\" : 20000,\n" +
		    "    \"name\" : \"Дырокол\",\n" +
		    "    \"sum\" : 40000\n" +
		    "  } ],\n" +
		    "  \"customerName\" : \"ООО Ромашка\"\n" +
		    "}";

    @Test
    public void testRead() throws Exception {
	SID sid = Generator.sid();
	final MethodLogger logger = LOG.silentEnter(sid,  "testRead");

	final String str = BinToJson.read(DATA, sid);
	logger.trace("json\n{}", str);
	Assert.assertNotNull(str);
	Assert.assertEquals(str, JSON);
    }

}