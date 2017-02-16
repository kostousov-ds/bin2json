package net.kst_d.lab.bin2json;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.kst_d.common.Generator;
import net.kst_d.common.SID;
import net.kst_d.common.log.KstLogger;
import net.kst_d.common.log.KstLoggerFactory;
import net.kst_d.common.log.MethodLogger;

public class Main {
    private static final KstLogger LOG = KstLoggerFactory.logger(Main.class);

    public static void main(String[] args) {
	SID sid = Generator.sid();
	final MethodLogger logger = LOG.silentEnter(sid, "main");

	logger.trace("args len {}", args.length);
	if (args.length < 1) {
	    logger.error("Too few arguments. You must specify input file name");
	    System.exit(1);
	    return;
	}

	Path input = Paths.get(args[0]);
	OutputStream out;
	boolean outMustBeClosed = false;
	if (args.length == 1) {
	    out = System.out;
	} else {
	    Path output = Paths.get(args[1]);
	    if (Files.exists(output)) {
		logger.error("out file {} already exist", output);
		System.exit(2);
		return;
	    }
	    try {
		out = Files.newOutputStream(output);
		outMustBeClosed = true;
	    } catch (IOException e) {
		logger.error("can't open file {} for write", e, output);
		System.exit(3);
		return;
	    }
	}
	try {
	    final byte[] bytes = Files.readAllBytes(input);
	    final String json = BinToJson.read(bytes, sid);
	    out.write(json.getBytes());
	} catch (Exception e) {
	    logger.error("can't process file {}", e, input);
	} finally {
	    if (outMustBeClosed) {
		try {
		    out.close();
		} catch (IOException e) {
		    logger.error("", e);
		}
	    }
	}
    }
}
