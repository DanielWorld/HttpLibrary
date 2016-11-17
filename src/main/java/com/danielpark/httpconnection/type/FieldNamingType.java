package com.danielpark.httpconnection.type;

/**
 * An enumeration that defines a few standard naming convention for JSON field names.
 * <br><br>
 * Copyright (C) 2014-2016 daniel@bapul.net
 * Created by Daniel on 2016-11-17.
 */

public enum FieldNamingType {

    /**
     * Using this naming policy with response JSON will ensure that the field name is
     * unchanged.
     *
     * <p>Here's a few examples of the form "Java Field Name" ---&gt; "JSON Field Name":</p>
     * <ul>
     *   <li>someFieldName ---&gt; someFieldName</li>
     *   <li>_someFieldName ---&gt; _someFieldName</li>
     * </ul>
     */
    IDENTITY,

    /**
     * Using this naming policy with response JSON will ensure that the "letters" of
     * the Java field name is lowercase.
     *
     * <p>Here's a few examples of the form "Java Field Name" ---&gt; "JSON Field Name":</p>
     * <ul>
     *   <li>someFieldName ---&gt; somefieldname</li>
     *   <li>_someFieldName ---&gt; _somefieldname</li>
     * </ul>
     */
    LOWERCASE,

    /**
     * Using this naming policy with response JSON will ensure that the "letters" of
     * the Java field name is capitalized.
     *
     * <p>Here's a few examples of the form "Java Field Name" ---&gt; "JSON Field Name":</p>
     * <ul>
     *   <li>someFieldName ---&gt; SOMEFIELDNAME</li>
     *   <li>_someFieldName ---&gt; _SOMEFIELDNAME</li>
     * </ul>
     */
    UPPERCASE,

    /**
     * Using this naming policy with response JSON will ensure that the first "letter" of
     * the Java field name is capitalized.
     *
     * <p>Here's a few examples of the form "Java Field Name" ---&gt; "JSON Field Name":</p>
     * <ul>
     *   <li>someFieldName ---&gt; SomeFieldName</li>
     *   <li>_someFieldName ---&gt; _SomeFieldName</li>
     * </ul>
     */
    UPPER_CAMEL_CASE,

    /**
     * Using this naming policy with response JSON will ensure that the first "letter" of
     * the Java field name is capitalized and the words will be separated by a space.
     *
     * <p>Here's a few examples of the form "Java Field Name" ---&gt; "JSON Field Name":</p>
     * <ul>
     *   <li>someFieldName ---&gt; Some Field Name</li>
     *   <li>_someFieldName ---&gt; _Some Field Name</li>
     * </ul>
     */
    UPPER_CAMEL_CASE_WITH_SPACES,

    /**
     * Using this naming policy with response JSON will ensure that the first "letter" of
     * the Java field name is lowercase.
     *
     * <p>Here's a few examples of the form "Java Field Name" ---&gt; "JSON Field Name":</p>
     * <ul>
     *   <li>SomeFieldName ---&gt; someFieldName</li>
     *   <li>_SomeFieldName ---&gt; _someFieldName</li>
     * </ul>
     */
    LOWER_CAMER_CASE,

    /**
     * Using this naming policy with response JSON will modify the Java Field name from its camel cased
     * form to a lower case field name where each word is separated by an underscore (_).
     *
     * <p>Here's a few examples of the form "Java Field Name" ---&gt; "JSON Field Name":</p>
     * <ul>
     *   <li>someFieldName ---&gt; some_field_name</li>
     *   <li>_someFieldName ---&gt; _some_field_name</li>
     *   <li>aStringField ---&gt; a_string_field</li>
     *   <li>aURL ---&gt; a_u_r_l</li>
     * </ul>
     */
    LOWER_CASE_WITH_UNDERSCORES,

    /**
     * Using this naming policy with response JSON will modify the Java Field name from its camel cased
     * form to a lower case field name where each word is separated by a dash (-).
     *
     * <p>Here's a few examples of the form "Java Field Name" ---&gt; "JSON Field Name":</p>
     * <ul>
     *   <li>someFieldName ---&gt; some-field-name</li>
     *   <li>_someFieldName ---&gt; _some-field-name</li>
     *   <li>aStringField ---&gt; a-string-field</li>
     *   <li>aURL ---&gt; a-u-r-l</li>
     * </ul>
     */
    LOWER_CASE_WITH_DASHES,
}
