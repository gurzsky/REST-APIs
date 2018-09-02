package org.gurski.javabrains.rest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Calendar;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

@Provider
public class MyDateConverterProvider implements ParamConverterProvider {

	@Override
	public <T> ParamConverter<T> getConverter(final Class<T> rawType, Type genericType, Annotation[] annotations) {
		if (rawType.getName().equals(MyDate.class.getName())) {
			return new ParamConverter<T>() {
				
				// The Param annotations maps incoming request parameters, so we are just interested in
				// String-to-DataType conversions here
				
				@Override
				public T fromString(String value) {
					
					Calendar requestDate = Calendar.getInstance();
					if ("tomorrow".equalsIgnoreCase(value)) {
						requestDate.add(Calendar.DATE, 1);
					} else if ("yesterday".equalsIgnoreCase(value)) {
						requestDate.add(Calendar.DATE, -1);
					}
					MyDate myDate = new MyDate();
					myDate.setDate(requestDate.get(Calendar.DATE));
					myDate.setMonth(requestDate.get(Calendar.MONTH));
					myDate.setYear(requestDate.get(Calendar.YEAR));	
					
					return rawType.cast(myDate);
				}

				@Override
				public String toString(T value) {
					if (value == null)
						return null;
					
					return value.toString();
				}
				
			};
		}
		return null;
	}

}
