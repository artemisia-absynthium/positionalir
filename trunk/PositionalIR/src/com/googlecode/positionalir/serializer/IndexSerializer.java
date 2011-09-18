package com.googlecode.positionalir.serializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import com.googlecode.positionalir.model.Index;
import com.googlecode.positionalir.util.IOUtil;
import com.googlecode.positionalir.util.LogUtil;


public class IndexSerializer {
	
	private final Logger log = LogUtil.getLogger(IndexSerializer.class);
	
	private final String filePath;
	
	public IndexSerializer(String filePath) {
		this.filePath = filePath;
	}

	public void saveIndex(Index index) {
		log.info(LogUtil.logTaskStart("Save Index"));
		ObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new ObjectOutputStream(getWritingStream());
			objectOutputStream.writeObject(index);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtil.close(objectOutputStream);
			log.info(LogUtil.logTaskEnd("Save Index"));
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
			IOUtil.close(objectInputStream);
		}
	}

	protected OutputStream getWritingStream() {
		try {
			return new BufferedOutputStream(new FileOutputStream(filePath));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected InputStream getReadingStream() {
		try {
			return new BufferedInputStream(new FileInputStream(filePath));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
}
