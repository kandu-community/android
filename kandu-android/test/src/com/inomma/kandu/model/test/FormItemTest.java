/**
 * 
 */
package com.inomma.kandu.model.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.inomma.kandu.model.FormItem;


/**
 * @author marlinf
 *
 */
public class FormItemTest {

	@Test
	public void testChoicesArray() {
		JSONObject config = new JSONObject();
		
		String[] items = { "a", "a b", "a_b", "a__b" };
		JSONArray choices = new JSONArray(items);
		
		config.put("name", "field");
		config.put("type", "choice");
		config.put("choices", choices);
		
		FormItem item = new FormItem(config);
		System.out.println(item.getChoices());
	}

	@Test
	public void testChoicesObject() {
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("a", "A");
		map.put("a b", "A B");
		map.put("a_b", "A_B");
		map.put("a__b", "A__B");
		
		JSONObject choices = new JSONObject(map);
		JSONObject config = new JSONObject();
		
		config.put("name", "field");
		config.put("type", "choice");
		config.put("choices", choices);
		
		FormItem item = new FormItem(config);
		Map<String, String> output = item.getChoices();

		assertEquals("The output should equals the input", output, map);
		assertNotSame("The output should not be the same as the input", output, map);
	}

}
