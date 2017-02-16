package net.kst_d.lab.bin2json;

enum DataType {
    time(Converters::readDate),
    uint(Converters::readUInt32),
    str(Converters::readStr866),
    num(Converters::readInteger),
    flt(Converters::readFloat),
    composite(Converters::readStructure)
    ;
    final Converter converter;

    DataType(Converter converter) {
	this.converter = converter;
    }
}
