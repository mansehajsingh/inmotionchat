package com.inmotionchat.core.util.misc;

import java.util.regex.Pattern;

public class RegExpPatterns {

    public static final Pattern EMAIL
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

}
