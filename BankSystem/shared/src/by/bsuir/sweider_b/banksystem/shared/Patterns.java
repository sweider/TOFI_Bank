package by.bsuir.sweider_b.banksystem.shared;

import java.util.regex.Pattern;

/**
 * Created by sweid on 18.01.2016.
 */
public class Patterns {
    public static Pattern passportNumberPattern = Pattern.compile("[1-6]((0[1-9])|([12][0-9])|(3[01]))((0[1-9])|(1[0-2]))\\d{2}[ABCKEMH]\\d{3}(GB|PB|BA|BI)\\d");
    public static Pattern phoneRegexp = Pattern.compile("\\+375\\d{9}");
}
