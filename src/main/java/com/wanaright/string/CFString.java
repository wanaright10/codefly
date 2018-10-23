package com.wanaright.string;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CFString {
    private static final Map<String, String> ESCAPE_HTML_POOL = Map.of("&", "&amp;",
            "<", "&lt;",
            ">", "&gt;",
            "'", "&#39;",
            "\"", "&quot;",
            "&amp;", "&",
            "&lt;", "<",
            "&gt;", ">",
            "&#39;", "'",
            "&quot;", "\"");
    private static final char[] CHAR_POOL = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final char[] CHAR_POOL_SPECIAL = {'!', '@', '#', '$', '%', '^', '&', '*', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private CFString() {
    }

    /**
     * capitalize the first letter of a string
     * i.e. hello -> Hello
     */
    public static String capitalize(String string) {
        return capitalize(string, false);
    }

    /**
     * capitalize the first letter of a string
     *
     * @param string    source string want to capitalize first letter
     * @param lowerRest whether keep rest letters as lower case
     *                  i.e. hello World -> Hello world (if lowerRest is true)
     */
    public static String capitalize(String string, boolean lowerRest) {
        notNull(string);
        if (string.isEmpty()) {
            return string;
        }

        char[] chars = string.toCharArray();
        char firstChar = chars[0];

        if (chars.length == 1) {
            return Character.toString(Character.toUpperCase(firstChar));
        }

        StringBuilder result = new StringBuilder();
        result.append(Character.toUpperCase(firstChar));
        for (int i = 1; i < chars.length; i++) {
            result.append(lowerRest ? Character.toLowerCase(chars[i]) : chars[i]);
        }

        return result.toString();
    }

    /**
     * i.e. hello world -> Hello World
     */
    public static String capitalizeAllWords(String string) {
        return findAndReplace(string, "\\b[a-z]", String::toUpperCase);
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }

    /**
     * i.e. "<a href="#">Me & you</a>" -> "&lt;a href=&quot;#&quot;&gt;Me &amp; you&lt;/a&gt;"
     */
    public static String escapeHtml(String string) {
        return findAndReplace(string, "[&<>'\\\"]", ESCAPE_HTML_POOL::get);
    }

    /**
     * findAndReplace("abc", "b", String::toUpperCase) -> aBc
     * Note: you can use Mather.replaceAll() instead
     *
     * @param string source string want to capitalize first letter
     * @param regex  the regex pattern for finding
     * @param action the rule for apply when find each string
     */
    public static String findAndReplace(String string, String regex, Function<String, String> action) {
        notNull(string);
        notEmpty(regex);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);

        StringBuilder stringBuilder = new StringBuilder();
        while (matcher.find()) {
            String matchString = matcher.group();
            matcher.appendReplacement(stringBuilder, action.apply(matchString));
        }
        matcher.appendTail(stringBuilder);
        return stringBuilder.toString();
    }


    /**
     * i.e. helloWorld -> hello world (if separator is ' ')
     * i.e. helloWorld -> hello-world (if separator is '-')
     * i.e. helloWorld -> hello_world (if separator is '_')
     */
    public static String splitCamelCase(String string, String separator) {
        notNull(string);
        notEmpty(separator);
        return string.replaceAll("([a-z\\d])([A-Z])", "$1" + separator + "$2")
                .replaceAll("([A-Z]+)([A-Z][a-z\\d]+)", "$1" + separator + "$2")
                .toLowerCase();
    }

    /**
     * i.e. https://google.com -> true
     * i.e. ftp://www.myserver.net -> true
     * i.e. /foo/bar -> false
     */
    public static boolean isAbsoluteURL(String url) {
        notNull(url);
        return Pattern.compile("^[a-z][a-z0-9+.-]*:").matcher(url).find();
    }

    /**
     * return same with anagram
     * i.e. 'hello world' and 'world hello' is true
     * i.e. 'hello world' and 'hlleo wlrod' is true
     * i.e. 'hello world' and 'hlo wlro eld' is true
     * i.e. 'abcd' and 'cdba' is true
     */
    public static boolean isAnagram(String string1, String string2) {
        notNull(string1);
        notNull(string2);

        String source = string1.trim().toLowerCase().replaceAll(" ", "");
        String target = string2.trim().toLowerCase().replaceAll(" ", "");

        if (source.length() != target.length()) {
            return false;
        }

        if (source.equals(target)) {
            return true;
        }

        char[] sourceChars = source.toCharArray();
        char[] targetChars = target.toCharArray();
        Arrays.sort(sourceChars);
        Arrays.sort(targetChars);

        return Arrays.equals(sourceChars, targetChars);
    }

    /**
     * mask(abcdefghijk, [4], *) -> *******hijk
     * mask(abcdefghijk, [3], $) -> $$$$$$$$ijk
     * mask(abcdefghijk, [-4], *) -> abcd*******
     * mask(abcdefghijk, [3, -4], *) -> abcd****ijk
     * mask(abcdefghijk, [-4, 3], *) -> abcd****ijk
     *
     * @param position +number means keep last ${number} letters,
     *                 -number means keep first ${number} letters,
     *                 [+,-] or [-,+] means keep first and last ${number} letters,
     *                 can not be [+,+] and [+,-] and out of index length
     * @see #mask(String, int, int, String)
     */
    public static String mask(String string, int[] position, String mask) {
        notNull(string);
        notEmpty(mask);

        if (position.length != 1 && position.length != 2) {
            throw new IllegalArgumentException("position can be only 1 or 2 numbers");
        }

        int length = string.length();
        if (position.length == 1) {
            if (position[0] > 0) {
                return mask(string, 0, length - position[0] - 1, mask);
            } else {
                return mask(string, Math.abs(position[0]), length - 1, mask);
            }
        }

        int position1 = position[0];
        int position2 = position[1];

        if (position1 * position2 >= 0) {
            throw new IllegalArgumentException("position 2 numbers must one positive and one negative");
        }

        // switch value
        if (position1 < 0) {
            position1 = position1 ^ position2;
            position2 = position1 ^ position2;
            position1 = position1 ^ position2;
        }

        return mask(string, Math.abs(position2), length - position1 - 1, mask);
    }

    /**
     * mask(abcdefghijk, 0, 6, *) -> *******hijk
     * mask(abcdefghijk, 0, 7, $) -> $$$$$$$$ijk
     * mask(abcdefghijk, 4, 7, *) -> abcd****ijk
     */
    public static String mask(String string, int start, int end, String mask) {
        notNull(string);
        notEmpty(mask);

        int length = string.length();
        if (start < 0 || end < start || end > length - 1) {
            throw new IllegalArgumentException("please enter correct mask position range");
        }

        StringBuilder stringBuilder = new StringBuilder(string.substring(0, start));
        for (int i = 0; i <= (end - start); i++) {
            stringBuilder.append(mask);
        }
        stringBuilder.append(string.substring(end + 1));
        return stringBuilder.toString();
    }

    /**
     * generate random string of length given.
     * i.e. randomString(6, false) -> skduxn
     * i.e. randomString(6, true) -> ngt$k#
     *
     * @param specialSymbol whether contains special symbol like !@#$%^&*
     * @see #randomString(int, char[])
     */
    public static String randomString(int length, boolean specialSymbol) {
        return randomString(length, specialSymbol ? CHAR_POOL_SPECIAL : CHAR_POOL);
    }

    /**
     * generate random string of length & char pool given.
     *
     * @param charPool give custom char pool for generate string
     */
    public static String randomString(int length, char[] charPool) {
        if (length <= 0 || charPool.length == 0) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(charPool[random.nextInt(charPool.length)]);
        }
        return stringBuilder.toString();
    }

    /**
     * i.e. removeNonASCII("äÄçÇéÉêlorem-ipsumöÖÐþúÚ") -> lorem-ipsum
     */
    public static String removeNonASCII(String string) {
        return string.replaceAll("[^\\x20-\\x7E]", "");
    }

    /**
     * i.e. ISO88591ToUTF8("Ã¼zÃ¼m baÄlarÄ±") -> üzüm bağları
     *
     * @see #encodeToEncode(String, String, String)
     */
    public static String ISO88591ToUTF8(String string) {
        return encodeToEncode(string, "ISO-8859-1", "UTF-8");
    }

    /**
     * i.e. encodeToEncode("Ã¼zÃ¼m baÄlarÄ±", "ISO-8859-1", "UTF-8") -> üzüm bağları
     *
     * @see #ISO88591ToUTF8(String)
     */
    public static String encodeToEncode(String string, String sourceEncode, String targetEncode) {
        try {
            return new String(string.getBytes(sourceEncode), targetEncode);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("encoding name error");
        }
    }

    /**
     * reverse a string
     * i.e. reverse("abcd") -> dcba
     */
    public static String reverse(String string) {
        notNull(string);
        return new StringBuilder(string).reverse().toString();
    }

    /**
     * i.e. sort("abdc") -> abcd
     */
    public static String sort(String string) {
        notNull(string);

        char[] chars = string.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    /**
     * i.e. stripHTMLTags("<p><em>lorem</em> <strong>ipsum</strong></p>") -> lorem ipsum
     */
    public static String stripHTMLTags(String string) {
        return string.replaceAll("<[^>]*>", "");
    }

    /**
     * i.e. toCamelCase("some_database_field_name") -> someDatabaseFieldName
     * i.e. toCamelCase("Some label that needs to be camelized") -> someLabelThatNeedsToBeCamelized
     * i.e. toCamelCase("some-javascript-property") -> someJavascriptProperty
     * i.e. toCamelCase("some-mixed_string with spaces_underscores-and-hyphens") -> someMixedStringWithSpacesUnderscoresAndHyphens
     */
    public static String toCamelCase(String string) {
        notNull(string);

        String[] words = string.split("[_|\\-| ]");
        for (int i = 1; i < words.length; i++) {
            words[i] = capitalize(words[i]);
        }

        String result = String.join("", words);
        return result.substring(0, 1).toLowerCase() + result.substring(1);
    }

    /**
     * i.e. deCamel("helloWorld") -> ["hello", "world"]
     * i.e. deCamel("some text") -> ["some", "text"]
     * i.e. deCamel("some-mixed_string With spaces_underscores-and-hyphens") -> ["some", "mixed", "string", "with", "spaces", "underscores", "and", "hyphens"]
     * i.e. deCamel("AllThe-small Things") -> ["all", "the", "small", "things"]
     * i.e. deCamel("IAmListeningToFMWhileLoadingDifferentURLOnMyBrowserAndAlsoEditingSomeXMLAndHTML") -> ["i", "am", "listening", "to", "fm", "while", "loading", "different", "url", "on", "my", "browser", "and", "also", "editing", "some", "xml", "and", "html"]
     */
    public static List<String> deCamel(String string) {
        notNull(string);

        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("[A-Z]{2,}(?=[A-Z][a-z]+[0-9]*|\\b)|[A-Z]?[a-z]+[0-9]*|[A-Z]|[0-9]+");
        Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            result.add(matcher.group().toLowerCase());
        }
        return result;
    }

    /**
     * i.e. truncate("abc", 4) -> abc
     * i.e. truncate("abcd", 4) -> abcd
     * i.e. truncate("abcde", 4) -> a...
     * i.e. truncate("abcdef", 4) -> a...
     */
    public static String truncate(String string, int keepLetters) {
        notNull(string);
        if (string.length() <= keepLetters) {
            return string;
        }

        return string.substring(0, keepLetters > 3 ? keepLetters - 3 : keepLetters) + "...";
    }

    /**
     * i.e. unescapeHTML("&lt;a href=&quot;#&quot;&gt;Me &amp; you&lt;/a&gt;") -> <a href="#">Me & you</a>
     */
    public static String unescapeHTML(String string) {
        notNull(string);
        return findAndReplace(string, "&amp;|&lt;|&gt;|&#39;|&quot;", ESCAPE_HTML_POOL::get);
    }

    /**
     * i.e. ulrJoin("http://www.wanaright.com", "a", "b/cd", "?foo=123", "?goo=foo") -> http://www.wanaright.com/a/b/cd?foo=123&goo=foo
     */
    public static String ulrJoin(String... strings) {
        return String.join("/", strings)
                .replaceAll("[\\/]+", "/")
                .replaceAll("^(.+):\\/", "$1://")
                .replaceAll("^file:", "file:/")
                .replaceAll("\\/(\\?|&|#[^!])", "$1")
                .replaceAll("\\?", "&")
                .replaceFirst("&", "?");
    }

    /**
     * generate a string md5 code
     */
    public static String encodeMD5(String string) {
        notNull(string);
        if (string.isEmpty()) {
            return string;
        }

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(string.getBytes());
            return String.format("%032x", new BigInteger(1, digest));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * encode base64 string
     */
    public static String encodeBase64(String string) {
        return new String(Base64.getEncoder().encode(string.getBytes()));
    }

    /**
     * decode base64 string
     */
    public static String decodeBase64(String string) {
        return new String(Base64.getDecoder().decode(string.getBytes()));
    }

    private static void notNull(String string) {
        if (string == null) {
            throw new NullPointerException();
        }
    }

    private static void notEmpty(String string) {
        if (isNullOrEmpty(string)) {
            throw new NullPointerException();
        }
    }
}
