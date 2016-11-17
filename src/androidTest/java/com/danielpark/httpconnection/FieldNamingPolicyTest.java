package com.danielpark.httpconnection;

import com.danielpark.httpconnection.type.FieldNamingType;
import com.danielpark.httpconnection.util.FieldNamingPolicy;
import com.danielpark.httpconnection.util.JsonObject;


import junit.framework.Assert;
import junit.framework.TestCase;

import org.json.JSONException;

/**
 * Copyright (c) 2014-2016 op7773hons@gmail.com
 * Created by Daniel Park on 2016-11-17.
 */

public class FieldNamingPolicyTest extends TestCase{

    public void test_fieldNaming_identity() throws JSONException {
        FieldNamingPolicy.setFieldNamingType(FieldNamingType.IDENTITY);

        Assert.assertEquals("StringName", FieldNamingPolicy.translateName("StringName"));
        Assert.assertEquals("_stringName", FieldNamingPolicy.translateName("_stringName"));
        Assert.assertNotSame("stringName", FieldNamingPolicy.translateName("StringName"));
    }

    public void test_fieldNaming_lowercase() throws JSONException {
        FieldNamingPolicy.setFieldNamingType(FieldNamingType.LOWERCASE);

        Assert.assertEquals("stringname", FieldNamingPolicy.translateName("StringName"));
        Assert.assertEquals("_stringname", FieldNamingPolicy.translateName("_stringName"));
        Assert.assertNotSame("stringName", FieldNamingPolicy.translateName("StringName"));
    }

    public void test_fieldNaming_uppercase() throws JSONException {
        FieldNamingPolicy.setFieldNamingType(FieldNamingType.UPPERCASE);

        Assert.assertEquals("STRINGNAME", FieldNamingPolicy.translateName("StringName"));
        Assert.assertEquals("_STRINGNAME", FieldNamingPolicy.translateName("_stringName"));
        Assert.assertNotSame("stringName", FieldNamingPolicy.translateName("StringName"));
    }

    public void test_fieldnaming_upper_camel_case() throws JSONException {
        FieldNamingPolicy.setFieldNamingType(FieldNamingType.UPPER_CAMEL_CASE);

        JsonObject jsonObject = new JsonObject();
        jsonObject.put("realNamePower", 140);
        jsonObject.put("LottoManner", "Sweet heart");

        Assert.assertEquals(140, jsonObject.optInt("realNamePower"));
        Assert.assertNotSame("Sweet heart", jsonObject.optString("LottOManner"));

        Assert.assertEquals("StringName", FieldNamingPolicy.translateName("StringName"));
        Assert.assertEquals("_StringName", FieldNamingPolicy.translateName("_stringName"));
        Assert.assertNotSame("stringName", FieldNamingPolicy.translateName("StringName"));
    }

    public void test_fieldnaming_upper_camel_case_with_spaces() throws JSONException {
        FieldNamingPolicy.setFieldNamingType(FieldNamingType.UPPER_CAMEL_CASE_WITH_SPACES);

        Assert.assertEquals("String Name", FieldNamingPolicy.translateName("StringName"));
        Assert.assertEquals("_String Name", FieldNamingPolicy.translateName("_stringName"));
        Assert.assertNotSame("stringName", FieldNamingPolicy.translateName("StringName"));
    }

    public void test_fieldnaming_lower_camer_case() throws JSONException {
        FieldNamingPolicy.setFieldNamingType(FieldNamingType.LOWER_CAMER_CASE);

        Assert.assertEquals("stringName", FieldNamingPolicy.translateName("StringName"));
        Assert.assertEquals("_stringName", FieldNamingPolicy.translateName("_stringName"));
        Assert.assertNotSame("StringName", FieldNamingPolicy.translateName("StringName"));
    }
}
