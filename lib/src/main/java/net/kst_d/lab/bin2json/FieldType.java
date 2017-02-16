package net.kst_d.lab.bin2json;

enum FieldType {
    datetime(1, false, "dateTime", DataType.time),
    ordernum(2, false, "orderNumber", DataType.num),
    customer(3, false, "customerName", DataType.str),
    items(4, true, "items", DataType.composite),
    name(11, false, "name", DataType.str),
    price(12, false, "price", DataType.num),
    quantity(13, false, "quantity", DataType.flt),
    sum(14, false, "sum", DataType.num);


    private static final FieldType[] byCode;
    static {
        int m=0;
	final FieldType[] v = values();
	for (FieldType i : v) {
	    m = m < i.tag ? i.tag : m;
	}
	byCode = new FieldType[m + 1];
	for (FieldType i : v) {
	    byCode[i.tag] = i;
	}
    }

    static FieldType find(int tag) {
        FieldType ret;
	if (tag < 0 || tag > byCode.length + 1 || (ret = byCode[tag]) == null) {
	    throw new IllegalArgumentException("invalid tag " + tag);
	}
	return ret;
    }

    final int tag;
    final boolean multiple;
    final String fieldName;
    final DataType type;

    FieldType(int tag, boolean multiple, String fieldName, DataType type) {
	this.tag = tag;
	this.multiple = multiple;
	this.fieldName = fieldName;
	this.type = type;
    }


    @Override
    public String toString() {
	return "FieldType{" +
			"name=" + name() +
			", tag=" + tag +
			", multiple=" + multiple +
			", fieldName='" + fieldName + '\'' +
			", type=" + type +
			'}';
    }
}
