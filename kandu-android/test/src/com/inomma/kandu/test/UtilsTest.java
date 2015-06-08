/**
 * 
 */
package com.inomma.kandu.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.junit.Test;

import com.inomma.kandu.Utils;

/**
 * @author marlinf
 *
 */
public class UtilsTest {

	/**
	 * Test method for {@link com.inomma.kandu.Utils#mapFromJsonArray(org.json.JSONArray)}.
	 * @throws JSONException 
	 */
	@Test
	public void testMapFromJsonArray() throws JSONException {
		String[] values = { "a", "a b", "a_b", "a__b" };		
		JSONArray array = new JSONArray(values);
		Map<String, String> map = Utils.mapFromJsonArray(array);			
	}

	/**
	 * Test method for {@link com.inomma.kandu.Utils#mapFromJsonObject(org.json.JSONObject)}.
	 */
	@Test
	public void testMapFromJsonObject() {
		Map<String, String> values = new HashMap<String, String>();
		
		values.put("a", "A");
		values.put("a b", "A B");
		values.put("a_b", "A_B");
		values.put("a__b", "A__B");
		
		JSONObject object = new JSONObject(values);
		Map<String, String> map = Utils.mapFromJsonObject(object);
		
		assertEquals("The output should equals the input", values, map);
		assertNotSame("The output should not be the same as the input", values, map);
	}

}
