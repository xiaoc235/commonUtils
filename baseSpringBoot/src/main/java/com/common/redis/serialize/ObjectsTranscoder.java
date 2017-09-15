package com.common.redis.serialize;


import java.io.*;

public class ObjectsTranscoder<M> extends SerializeTranscoder {
	

	  @Override
	  public byte[] serialize(Object value) {
	    if (value == null) {  
	      throw new NullPointerException("Can't serialize null");  
	    }  
	    byte[] result = null;  
	    ByteArrayOutputStream bos = null;  
	    ObjectOutputStream os = null;
	    try {  
	      bos = new ByteArrayOutputStream();  
	      os = new ObjectOutputStream(bos);
	      M m = (M) value;
	      os.writeObject(m);  
	      os.close();  
	      bos.close();  
	      result = bos.toByteArray();  
	    } catch (Exception e) {  
	    	e.printStackTrace();
	    } finally {
	      close(os);  
	      close(bos);  
	    }  
	    return result;  
	  }

	  @SuppressWarnings("unchecked")
	  @Override
	  public M deserialize(byte[] in) {
	    M result = null;  
	    ByteArrayInputStream bis = null;  
	    ObjectInputStream is = null;  
	    try {  
	      if (in != null) {  
	        bis = new ByteArrayInputStream(in);  
	        is = new ObjectInputStream(bis);  
	        result = (M) is.readObject();  
	        is.close();  
	        bis.close();  
	      }  
	    } catch (IOException e) {  
	    } catch (ClassNotFoundException e) {
	    } finally {
	      close(is);  
	      close(bis);  
	    }  
	    return result;  
	  }
	}
