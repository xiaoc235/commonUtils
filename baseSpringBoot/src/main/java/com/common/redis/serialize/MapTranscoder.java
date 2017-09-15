package com.common.redis.serialize;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class MapTranscoder<M> extends SerializeTranscoder {

	@Override
	  public Map<String,M> deserialize(byte[] in) {
	    Map<String,M> map = new HashMap<String, M>();
	    ByteArrayInputStream bis = null;
	    ObjectInputStream is = null;
	    try {
	      if (in != null) {
	        bis = new ByteArrayInputStream(in);
	        is = new ObjectInputStream(bis);
	        while (true) {
	          M m = (M)is.readObject();
	          if (m == null) {
	            break;
	          }
	          map=(HashMap<String, M>)m;
	        }
	        is.close();
	        bis.close();
	      }
	    } catch (IOException e) {  
	  } catch (ClassNotFoundException e) {
	  }  finally {
	      close(is);
	      close(bis);
	    }
	    return  map;
	  }
	  

	  @Override
	  public byte[] serialize(Object value) {
	    if (value == null)
	      throw new NullPointerException("Can't serialize null");
	    
	    Map<String,M> values = (HashMap<String, M>) value;
	    
	    byte[] results = null;
	    ByteArrayOutputStream bos = null;
	    ObjectOutputStream os = null;
	    
	    try {
	      bos = new ByteArrayOutputStream();
	      os = new ObjectOutputStream(bos);
	      os.writeObject(values);
	      os.close();
	      bos.close();
	      results = bos.toByteArray();
	    } catch (IOException e) {
	      throw new IllegalArgumentException("Non-serializable object", e);
	    } finally {
	      close(os);
	      close(bos);
	    }
	    return results;
	  }


	  
	}