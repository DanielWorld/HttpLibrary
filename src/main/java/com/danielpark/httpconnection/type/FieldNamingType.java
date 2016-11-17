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
     * <p>Here's a few examples of the form "Java Field Name" ---> "JSON Field Name":</p>
     * <ul>
     *   <li>someFieldName ---> someFieldName</li>
     *   <li>_someFieldName ---> _someFieldName</li>
     * </ul>
     */
    IDENTITY,

    /**
     * Using this naming policy with response JSON will ensure that the "letters" of
     * the Java field name is lowercase.
     *
     * <p>Here's a few examples of the form "Java Field Name" ---> "JSON Field Name":</p>
     * <ul>
     *   <li>someFieldName ---> somefieldname</li>
     *   <li>_someFieldName ---> _somefieldname</li>
     * </ul>
     */
    LOWERCASE,

    /**
     * Using this naming policy with response JSON will ensure that the "letters" of
     * the Java field name is capitalized.
     *
     * <p>Here's a few examples of the form "Java Field Name" ---> "JSON Field Name":</p>
     * <ul>
     *   <li>someFieldName ---> SOMEFIELDNAME</li>
     *   <li>_someFieldName ---> _SOMEFIELDNAME</li>
     * </ul>
     */
    UPPERCASE,

    /**
     * Using this naming policy with response JSON will ensure that the first "letter" of
     * the Java field name is capitalized.
     *
     * <p>Here's a few examples of the form "Java Field Name" ---> "JSON Field Name":</p>
     * <ul>
     *   <li>someFieldName ---> SomeFieldName</li>
     *   <li>_someFieldName ---> _SomeFieldName</li>
     * </ul>
     */
    UPPER_CAMEL_CASE,

    /**
     * Using this naming policy with response JSON will ensure that the first "letter" of
     * the Java field name is capitalized and the words will be separated by a space.
     *
     * <p>Here's a few examples of the form "Java Field Name" ---> "JSON Field Name":</p>
     * <ul>
     *   <li>someFieldName ---> Some Field Name</li>
     *   <li>_someFieldName ---> _Some Field Name</li>
     * </ul>
     */
    UPPER_CAMEL_CASE_WITH_SPACES,

    /**
     * Using this naming policy with response JSON will ensure that the first "letter" of
     * the Java field name is lowercase.
     *
     * <p>Here's a few examples of the form "Java Field Name" ---> "JSON Field Name":</p>
     * <ul>
     *   <li>SomeFieldName ---> someFieldName</li>
     *   <li>_SomeFieldName ---> _someFieldName</li>
     * </ul>
     */
    LOWER_CAMER_CASE,

    /**
     * Using this naming policy with response JSON will modify the Java Field name from its camel cased
     * form to a lower case field name where each word is separated by an underscore (_).
     *
     * <p>Here's a few examples of the form "Java Field Name" ---> "JSON Field Name":</p>
     * <ul>
     *   <li>someFieldName ---> some_field_name</li>
     *   <li>_someFieldName ---> _some_field_name</li>
     *   <li>aStringField ---> a_string_field</li>
     *   <li>aURL ---> a_u_r_l</li>
     * </ul>
     */
    LOWER_CASE_WITH_UNDERSCORES,

    /**
     * Using this naming policy with response JSON will modify the Java Field name from its camel cased
     * form to a lower case field name where each word is separated by a dash (-).
     *
     * <p>Here's a few examples of the form "Java Field Name" ---> "JSON Field Name":</p>
     * <ul>
     *   <li>someFieldName ---> some-field-name</li>
     *   <li>_someFieldName ---> _some-field-name</li>
     *   <li>aStringField ---> a-string-field</li>
     *   <li>aURL ---> a-u-r-l</li>
     * </ul>
     */
    LOWER_CASE_WITH_DASHES,
}
