package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class IOUtil {

	private static final Logger log = LogUtil.getLogger(IOUtil.class);
	
	public static void close(Reader reader) {
		if(reader == null)
			return;
		try {
			reader.close();
		} catch (IOException e) {
			log.log(Level.WARNING, LogUtil.log("Errore nella chiusura del reader."), e);
		}
	}
	
	public static void close(InputStream inputStream) {
		if(inputStream == null)
			return;
		try {
			inputStream.close();
		} catch (IOException e) {
			log.log(Level.WARNING, LogUtil.log("Errore nella chiusura dello stream."), e);
		}
	}
	
	public static void close(OutputStream outputStream) {
		if(outputStream == null)
			return;
		try {
			outputStream.close();
		} catch (IOException e) {
			log.log(Level.WARNING, LogUtil.log("Errore nella chiusura dello stream."), e);
		}
	}
	
}
