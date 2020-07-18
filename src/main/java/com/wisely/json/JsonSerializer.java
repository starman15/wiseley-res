package com.wisely.json;

/**
 * As well as running with the REST serialization, allow manual serialization as well.
 * @author JThomas1
 *
 * @param <T>
 */
public interface JsonSerializer<T> {
	
	public String serialize(T val) throws JsonParseException;
	
	public T deserialize(String val) throws JsonParseException;

}
