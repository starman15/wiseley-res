package com.wisely.json;

import java.io.IOException;
import java.sql.Time;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;



/**
 * Pays attention to date, hour, min fields.
 * 
 * @author JThomas1
 *
 */
public class TimeSerializer extends StdSerializer<Time> implements JsonSerializer<Time> {
	 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimeSerializer() { 
        this(null); 
    } 
 
    public TimeSerializer(Class<Time> t) {
        super(t); 
    }
 
    @Override
    public void serialize(
    		Time value, JsonGenerator gen, SerializerProvider arg2) 
      throws IOException, JsonProcessingException {
    	String val = value!=null ? value.toString() : "null";
         gen.writeString(val);
    }

	@Override
	public String serialize(Time val) {
		String ret = null;
		if (val != null) {
			ret = val.toString();
		}
		return ret;
	}

	@Override
	public Time deserialize(String val) throws JsonParseException {
		Time time = Time.valueOf(val);
		return time;
	}
}

