package serializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import model.Index;
import util.LogUtil;

public class IndexSerializer {
	
	private final Logger log = LogUtil.getLogger(IndexSerializer.class);
	
	private final String filePath;
	
	public IndexSerializer(String filePath) {
		this.filePath = filePath;
	}

	public void saveIndex(Index index) {
		log.info(LogUtil.logTaskStart("Save Index"));
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(getWritingStream());
			objectOutputStream.writeObject(index);
			objectOutputStream.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			log.info(LogUtil.logTaskEnd("Save Index"));
		}
	}

	protected OutputStream getWritingStream() {
		try {
			return new FileOutputStream(filePath);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected InputStream getReadingStream() {
		try {
			return new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Index loadIndex() {
		log.info(LogUtil.logTaskStart("Load Index"));
		ObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new ObjectInputStream(this.getReadingStream());
			Index index = (Index) objectInputStream.readObject();
			return index;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} finally {
			log.info(LogUtil.logTaskEnd("Load Index"));
			try {
				if(objectInputStream != null)
					objectInputStream.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
}
