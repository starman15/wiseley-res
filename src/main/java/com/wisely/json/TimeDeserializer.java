package com.wisely.json;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Time;

import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.stream.JsonParser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;




/**
 * Pays attention to date, hour, min fields.
 * 
 * This class must be included in {@code JacksonConfig}
 * @author JThomas1
 *
 */
public class TimeDeserializer extends StdDeserializer<Time> implements JsonbDeserializer<Time> {
	 
	private static final long serialVersionUID = 1L;

	public TimeDeserializer() { 
        this(null); 
    } 
 
    public TimeDeserializer(Class<Time> t) {
        super(t); 
    }


	@Override
	public Time deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
		String tm = parser.getString();
		Time time = null;
		if (tm!=null) {
			long count = tm.chars().filter(ch -> ch == ':').count();
			if (count==1) {
				tm = tm + ":00";
			}
			time = Time.valueOf(tm);
		}

		return time;
	}

	@Override
	public Time deserialize(com.fasterxml.jackson.core.JsonParser p,
			com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String tm = p.getText();
		Time time = null;
		if (tm!=null) {
			long count = tm.chars().filter(ch -> ch == ':').count();
			if (count==1) {
				tm = tm + ":00";
			}
			time = Time.valueOf(tm);
		}

		return time;
	}


}

