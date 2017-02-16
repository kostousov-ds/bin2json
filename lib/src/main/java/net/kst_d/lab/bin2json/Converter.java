package net.kst_d.lab.bin2json;

import net.kst_d.common.SID;

interface Converter {

    Object read(byte[] in, int offset, int len, SID sid);
}
