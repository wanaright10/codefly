package code.fly.string;


import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class CFStringTest {
    @Test
    public void capitalize() {
        String source = "dong,WANG";
        String result = CFString.capitalize(source);
        Assert.assertEquals(result, "Dong,WANG");
    }

    @Test
    public void capitalizeRestLower() {
        String source = "dong,WANG";
        String result = CFString.capitalize(source, true);
        Assert.assertEquals(result, "Dong,wang");
    }

    @Test
    public void findAndReplace() {
        String source = "dong wang";
        String result = CFString.findAndReplace(source, "\\b[a-z]", String::toUpperCase);
        Assert.assertEquals(result, "Dong Wang");
    }

    @Test
    public void escapeHtml() {
        String source = "<a href=\"#\">Me & you</a>";
        String result = CFString.escapeHtml(source);
        Assert.assertEquals(result, "&lt;a href=&quot;#&quot;&gt;Me &amp; you&lt;/a&gt;");
    }

    @Test
    public void splitCamelCase() {
        String source = "dongWang";
        String result = CFString.splitCamelCase(source, "_");
        Assert.assertEquals(result, "dong_wang");
    }

    @Test
    public void isAbsoluteURL() {
        String url1 = "https://google.com";
        String url2 = "ftp://www.myserver.net";
        String url3 = "/foo/bar";

        Assert.assertTrue(CFString.isAbsoluteURL(url1));
        Assert.assertTrue(CFString.isAbsoluteURL(url2));
        Assert.assertFalse(CFString.isAbsoluteURL(url3));
    }

    @Test
    public void isAnagram() {
        String word1 = "world hello";
        String word2 = "hlleo wlrod";
        String word3 = "hlo wlro eld";
        String word4 = "cdba";

        Assert.assertTrue(CFString.isAnagram("hello world", word1));
        Assert.assertTrue(CFString.isAnagram("hello world", word2));
        Assert.assertTrue(CFString.isAnagram("hello world", word3));
        Assert.assertTrue(CFString.isAnagram("abcd", word4));
    }

    @Test
    public void mask() {
        String string = "abcdefghijk";

        Assert.assertEquals(CFString.mask(string, new int[]{4}, "*"), "*******hijk");
        Assert.assertEquals(CFString.mask(string, new int[]{3}, "$"), "$$$$$$$$ijk");
        Assert.assertEquals(CFString.mask(string, new int[]{-4}, "*"), "abcd*******");
        Assert.assertEquals(CFString.mask(string, new int[]{3, -4}, "*"), "abcd****ijk");
        Assert.assertEquals(CFString.mask(string, new int[]{-4, 3}, "*"), "abcd****ijk");
    }

    @Test
    public void maskPlace() {
        String string = "abcdefghijk";

        Assert.assertEquals(CFString.mask(string, 0, 6, "*"), "*******hijk");
        Assert.assertEquals(CFString.mask(string, 0, 7, "$"), "$$$$$$$$ijk");
        Assert.assertEquals(CFString.mask(string, 4, 7, "*"), "abcd****ijk");
        Assert.assertEquals(CFString.mask(string, 0, 0, "*"), "*bcdefghijk");
    }

    @Test
    public void randomString() {
        System.out.println(CFString.randomString(6, true));
        Assert.assertEquals(CFString.randomString(6, false).length(), 6);
    }

    @Test
    public void removeNonASCII() {
        String string = "äÄçÇéÉêlorem-ipsumöÖÐþúÚ";

        Assert.assertEquals(CFString.removeNonASCII(string), "lorem-ipsum");
    }

    @Test
    public void reverse() {
        String string = "abcd";

        Assert.assertEquals(CFString.reverse(string), "dcba");
    }

    @Test
    public void sort() {
        String string = "dcba";

        Assert.assertEquals(CFString.sort(string), "abcd");
    }

    @Test
    public void stripHTMLTags() {
        String string = "<p><em>lorem</em> <strong>ipsum</strong></p>";

        Assert.assertEquals(CFString.stripHTMLTags(string), "lorem ipsum");
    }

    @Test
    public void toCamelCase() {
        String string1 = "some_database_field_name";
        String string2 = "Some label that needs to be camelized";
        String string3 = "some-javascript-property";
        String string4 = "some-mixed_string with spaces_underscores-and-hyphens";

        Assert.assertEquals(CFString.toCamelCase(string1), "someDatabaseFieldName");
        Assert.assertEquals(CFString.toCamelCase(string2), "someLabelThatNeedsToBeCamelized");
        Assert.assertEquals(CFString.toCamelCase(string3), "someJavascriptProperty");
        Assert.assertEquals(CFString.toCamelCase(string4), "someMixedStringWithSpacesUnderscoresAndHyphens");
    }

    @Test
    public void deCamel() {
        String string1 = "helloWorld";
        String string2 = "some text";
        String string3 = "some-mixed_string With spaces_underscores-and-hyphens";
        String string4 = "AllThe-small Things";
        String string5 = "IAmListeningToFMWhileLoadingDifferentURLOnMyBrowserAndAlsoEditingSomeXMLAndHTML";

        Assert.assertArrayEquals(CFString.deCamel(string1).toArray(), Arrays.asList("hello", "world").toArray());
        Assert.assertArrayEquals(CFString.deCamel(string2).toArray(), Arrays.asList("some", "text").toArray());
        Assert.assertArrayEquals(CFString.deCamel(string3).toArray(), Arrays.asList("some", "mixed", "string", "with", "spaces", "underscores", "and", "hyphens").toArray());
        Assert.assertArrayEquals(CFString.deCamel(string4).toArray(), Arrays.asList("all", "the", "small", "things").toArray());
        Assert.assertArrayEquals(CFString.deCamel(string5).toArray(), Arrays.asList("i", "am", "listening", "to", "fm", "while", "loading", "different", "url", "on", "my", "browser", "and", "also", "editing", "some", "xml", "and", "html").toArray());
    }

    @Test
    public void truncate() {
        String string1 = "abc";
        String string2 = "abcd";
        String string3 = "abcde";

        Assert.assertEquals(CFString.truncate(string1, 4), "abc");
        Assert.assertEquals(CFString.truncate(string2, 4), "abcd");
        Assert.assertEquals(CFString.truncate(string3, 4), "a...");
    }

    @Test
    public void unescapeHTML() {
        String string = "&lt;a href=&quot;#&quot;&gt;Me &amp; you&lt;/a&gt;";

        Assert.assertEquals(CFString.unescapeHTML(string), "<a href=\"#\">Me & you</a>");
    }

    @Test
    public void ulrJoin() {
        String[] strings = {"http://www.wanaright.com", "a", "b/cd", "?foo=123", "?goo=foo"};

        Assert.assertEquals(CFString.ulrJoin(strings), "http://www.wanaright.com/a/b/cd?foo=123&goo=foo");
    }

    @Test
    public void md5() {
        Assert.assertEquals(CFString.encodeMD5("ILoveJava"), "35454b055cc325ea1af2126e27707052");
    }

    @Test
    public void base64() {
        String string = "hello world";

        System.out.println(CFString.encodeBase64(string));
        Assert.assertEquals(CFString.decodeBase64(CFString.encodeBase64(string)), string);
    }
}