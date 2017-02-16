package net.kst_d.lab.bin2json;

import java.math.BigInteger;
import java.util.Objects;

import org.testng.Assert;
import org.testng.annotations.Test;

import net.kst_d.common.Generator;
import net.kst_d.common.SID;

public class ConvertersTest {


    @Test
    public void testReadUInt16() throws Exception {
        SID sid = Generator.sid();

	Assert.expectThrows(NullPointerException.class, () -> Converters.readUInt16(null, -1, -2, sid));
	Assert.expectThrows(IllegalArgumentException.class, () -> Converters.readUInt16(new byte[] {0x00}, 0, 1, sid));
	Assert.expectThrows(IllegalArgumentException.class, () -> Converters.readUInt16(new byte[] {0x00, 0x01, 0x02, 0x03, 0x04}, 0, 4, sid));
	Assert.expectThrows(IllegalArgumentException.class, () -> Converters.readUInt16(new byte[] {0x00, 0x01, 0x02, 0x03, 0x04}, 4, 2, sid));

	Assert.assertEquals(Converters.readUInt16(new byte[] {0x01, 0, 0, 0}, 0, 2, sid), 1);
	Assert.assertEquals(Converters.readUInt16(new byte[] {0x01, 0x02, 0, 0}, 0, 2, sid), 1 + 2 * 256);
	Assert.assertEquals(Converters.readUInt16(new byte[] {0x01, 0x02, 0x03, 0}, 1, 2, sid), 2 + 3 * 256);
	Assert.assertEquals(Converters.readUInt16(new byte[] {0x01, 0x02, 0x03, 0x04}, 1, 2, sid), 2 + 3 * 256);

    }

    @Test
    public void testReadUInt32() throws Exception {
	SID sid = Generator.sid();

	Assert.expectThrows(NullPointerException.class, () -> Converters.readUInt32(null, -1, -2, sid));
	Assert.expectThrows(IllegalArgumentException.class, () -> Converters.readUInt32(new byte[] {0x00}, 0, 1, sid));
	Assert.expectThrows(IllegalArgumentException.class, () -> Converters.readUInt32(new byte[] {0x00, 0x01, 0x02, 0x03, 0x04}, 0, 5, sid));
	Assert.expectThrows(IllegalArgumentException.class, () -> Converters.readUInt32(new byte[] {0x00, 0x01, 0x02, 0x03, 0x04}, 3, 4, sid));

	Assert.assertEquals(Converters.readUInt32(new byte[] {0x01, 0, 0, 0}, 0, 4, sid), 1);
	Assert.assertEquals(Converters.readUInt32(new byte[] {0x01, 0x02, 0, 0}, 0, 4, sid), 1 + 2 * 256);
	Assert.assertEquals(Converters.readUInt32(new byte[] {0x01, 0x02, 0x03, 0}, 0, 4, sid), 1 + 2 * 256 + 3 * 256 * 256);
	Assert.assertEquals(Converters.readUInt32(new byte[] {0x01, 0x02, 0x03, 0x04}, 0, 4, sid), 1 + 2 * 256 + 3 * 256 * 256 + 4 * 256 * 256 * 256);

    }

    @Test
    public void testReadStr866() throws Exception {
	SID sid = Generator.sid();

	Assert.assertNull(Converters.readStr866(null, -1, -2, sid));
	final byte[] data = {(byte) 0x8E, (byte) 0x8E, (byte) 0x8E, (byte) 0x20, (byte) 0x90, (byte) 0xAE, (byte) 0xAC, (byte) 0xA0, (byte) 0xE8, (byte) 0xAA,
			(byte) 0xA0};
	Assert.assertEquals(Converters.readStr866(data, 0, data.length, sid), "ООО Ромашка");
    }

    @Test
    public void testReadDate() {
	SID sid = Generator.sid();

	Assert.expectThrows(NullPointerException.class, () -> Converters.readDate(null, -1, 2, sid));
	Assert.expectThrows(IllegalArgumentException.class, () -> Converters.readDate(new byte[] {0x00}, 0, 4, sid));
	Assert.expectThrows(IllegalArgumentException.class, () -> Converters.readDate(new byte[] {0x00, 0x01, 0x02, 0x03, 0x04}, 0, 5, sid));

	Assert.assertEquals(Objects.toString(Converters.readDate(new byte[] {(byte) 0xA8, (byte) 0x32, (byte) 0x92, (byte) 0x56}, 0, 4, sid)), "2016-01-10T10:30");
	Assert.assertEquals(Objects.toString(Converters.readDate(new byte[] {(byte) 0xA8, (byte) 0x32, (byte) 0x92, (byte) 0x56, 0}, 0, 4, sid)), "2016-01-10T10:30");
	Assert.assertEquals(Objects.toString(Converters.readDate(new byte[] {0, (byte) 0xA8, (byte) 0x32, (byte) 0x92, (byte) 0x56}, 1, 4, sid)), "2016-01-10T10:30");
    }

    @Test
    public void testReadInteger() {
	SID sid = Generator.sid();

	Assert.expectThrows(IllegalArgumentException.class, () -> Converters.readInteger(new byte[] {0}, 0, 2, sid));
	Assert.expectThrows(IllegalArgumentException.class, () -> Converters.readInteger(new byte[] {0}, 0, 0, sid));
	Assert.expectThrows(IllegalArgumentException.class, () -> Converters.readInteger(new byte[] {}, 0, 1, sid));

	Assert.assertEquals(Converters.readInteger(new byte[] {0, 0, 0x40, (byte) 0x9c}, 2, 2, sid), BigInteger.valueOf(40000));
    }

    @Test
    public void testReadFloat() {
	SID sid = Generator.sid();

	Assert.expectThrows(IllegalArgumentException.class, () -> Converters.readFloat(new byte[] {0}, 0, 2, sid));
	Assert.expectThrows(IllegalArgumentException.class, () -> Converters.readFloat(new byte[] {}, 0, 1, sid));
	Assert.expectThrows(IllegalArgumentException.class, () -> Converters.readFloat(new byte[] {0, 0}, 1, 1, sid));

	Assert.assertEquals(Objects.toString(Converters.readFloat(new byte[] {0, 1, 0x40, (byte) 0x9c}, 1, 3, sid)), "4000.0");
	Assert.assertEquals(Objects.toString(Converters.readFloat(new byte[] {0, 2, 0x40, (byte) 0x9c}, 1, 3, sid)), "400.00");
	Assert.assertEquals(Objects.toString(Converters.readFloat(new byte[] {0, 3, 0x40, (byte) 0x9c}, 1, 3, sid)), "40.000");
	Assert.assertEquals(Objects.toString(Converters.readFloat(new byte[] {0, 4, 0x40, (byte) 0x9c}, 1, 3, sid)), "4.0000");
	Assert.assertEquals(Objects.toString(Converters.readFloat(new byte[] {0, 5, 0x40, (byte) 0x9c}, 1, 3, sid)), "0.40000");
	Assert.assertEquals(Objects.toString(Converters.readFloat(new byte[] {0, 6, 0x40, (byte) 0x9c}, 1, 3, sid)), "0.040000");
	Assert.assertEquals(Objects.toString(Converters.readFloat(new byte[] {0, 0, 0x40, (byte) 0xe2, (byte) 0x01}, 1, 4, sid)), "123456");
	Assert.assertEquals(Objects.toString(Converters.readFloat(new byte[] {0, 1, 0x40, (byte) 0xe2, (byte) 0x01}, 1, 4, sid)), "12345.6");
	Assert.assertEquals(Objects.toString(Converters.readFloat(new byte[] {0, 2, 0x40, (byte) 0xe2, (byte) 0x01}, 1, 4, sid)), "1234.56");
    }


}