package com.danielpark.httpconnection.util;

import android.support.annotation.NonNull;

import com.danielpark.httpconnection.type.FieldNamingType;

import java.util.Locale;

/**
 * This class is for translate Field key name (String) to different format
 * <br><br>
 * Copyright (C) 2014-2016 daniel@bapul.net
 * Created by Daniel on 2016-11-17.
 */

public class FieldNamingPolicy {

    /**
     * Current field naming policy
     */
    @NonNull
    private static FieldNamingType mCurrentNamingPolicy = FieldNamingType.IDENTITY;

    /**
     * Set field naming policy
     * @param namingType
     */
    public static void setFieldNamingType(FieldNamingType namingType) {
        if (namingType == null) return;

        mCurrentNamingPolicy = namingType;
    }

    /**
     * Get current field naming policy
     * @return
     */
    public static FieldNamingType getFieldNamingType() {
        return mCurrentNamingPolicy;
    }

    /**
     * Translate the field name into its JSON field name representation.
     * @param original the field object that we are translating
     * @return the translated field name
     */
    public static String translateName(String original) {
        if (mCurrentNamingPolicy == FieldNamingType.IDENTITY || original == null || original.isEmpty()) return original;

        switch (mCurrentNamingPolicy) {
            case LOWERCASE: {
                return original.toLowerCase(Locale.ENGLISH);
            }
            case UPPERCASE: {
                return original.toUpperCase(Locale.ENGLISH);
            }
            case UPPER_CAMEL_CASE: {
                return upperCaseFirstLetter(original);
            }
            case UPPER_CAMEL_CASE_WITH_SPACES: {
                return upperCaseFirstLetter(separateCamelCase(original, " "));
            }
            case LOWER_CAMER_CASE: {
                return lowerCaseFirstLetter(original);
            }
            case LOWER_CASE_WITH_UNDERSCORES: {
                return separateCamelCase(original, "_").toLowerCase(Locale.ENGLISH);
            }
            case LOWER_CASE_WITH_DASHES: {
                return separateCamelCase(original, "-").toLowerCase(Locale.ENGLISH);
            }
        }

        return original;
    }


    /**
     * Converts the field name that uses camel-case define word separation into
     * separate words that are separated by the provided {@code separatorString}.
     */
    private static String separateCamelCase(String name, String separator) {
        StringBuilder translation = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char character = name.charAt(i);
            if (Character.isUpperCase(character) && translation.length() != 0) {
                translation.append(separator);
            }
            translation.append(character);
        }
        return translation.toString();
    }

    /**
     * Ensures the JSON field names begins with an upper case letter.
     */
    private static String upperCaseFirstLetter(String name) {
        StringBuilder fieldNameBuilder = new StringBuilder();
        int index = 0;
        char firstCharacter = name.charAt(index);

        while (index < name.length() - 1) {
            if (Character.isLetter(firstCharacter)) {
                break;
            }

            fieldNameBuilder.append(firstCharacter);
            firstCharacter = name.charAt(++index);
        }

        if (index == name.length()) {
            return fieldNameBuilder.toString();
        }

        if (!Character.isUpperCase(firstCharacter)) {
            String modifiedTarget = modifyString(Character.toUpperCase(firstCharacter), name, ++index);
            return fieldNameBuilder.append(modifiedTarget).toString();
        } else {
            return name;
        }
    }

    /**
     * Ensure the JSON field names begins with an lower case letter.
     * @param name
     * @return
     */
    private static String lowerCaseFirstLetter(String name) {
        StringBuilder fieldNameBuilder = new StringBuilder();
        int index = 0;
        // Daniel (2016-11-17 14:31:01): Get first character of the name String
        char firstCharacter = name.charAt(index);

        while (index < name.length() - 1) {
            if (Character.isLetter(firstCharacter)) {
                break;
            }

            fieldNameBuilder.append(firstCharacter);
            firstCharacter = name.charAt(++index);
        }

        if (index == name.length()) {
            return fieldNameBuilder.toString();
        }

        if (!Character.isLowerCase(firstCharacter)) {
            String modifiedTarget = modifyString(Character.toLowerCase(firstCharacter), name, ++index);
            return fieldNameBuilder.append(modifiedTarget).toString();
        } else {
            return name;
        }
    }

    private static String modifyString(char firstCharacter, String srcString, int indexOfSubstring) {
        return (indexOfSubstring < srcString.length())
                ? firstCharacter + srcString.substring(indexOfSubstring)
                : String.valueOf(firstCharacter);
    }
}
