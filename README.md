# codefly
LET'S Code Fly! (Java version 9+)

based on [30-seconds](https://github.com/30-seconds/30-seconds-of-code)

## CFString
**capitalize**
>capitalize the first letter of a string.  
>i.e. hello -> Hello

**capitalize**
>capitalize the first letter of a string  
>i.e. hello World -> Hello world (if lowerRest is true)

**capitalizeAllWords**	
>i.e. hello world -> Hello World

**deCamel**
>i.e. deCamel("helloWorld") -> ["hello", "world"]  
>i.e. deCamel("some text") -> ["some", "text"]  
>i.e. deCamel("some-mixed_string With spaces_underscores-and-hyphens") -> ["some", "mixed", "string", "with", "spaces", "underscores", "and", "hyphens"]  
>i.e. deCamel("AllThe-small Things") -> ["all", "the", "small", "things"]  
>i.e. deCamel("IAmListeningToFMWhileLoadingDifferentURLOnMyBrowserAndAlsoEditingSomeXMLAndHTML") ->  ["i", "am", "listening", "to", "fm", "while", "loading", "different", "url", "on", "my", "browser", "and", "also", "editing", "some", "xml", "and", "html"]

**decodeBase64**
>decode base64 string

**encodeBase64**
>encode base64 string

**encodeMD5**
>generate a string md5 code

**encodeToEncode**
>i.e. encodeToEncode("Ã¼zÃ¼m baÄlarÄ±", "ISO-8859-1", "UTF-8") -> üzüm bağları

**escapeHtml**	
>i.e. "\<a href="#">Me & you\</a>" -> "\&lt;a href=\&quot;#\&quot;\&gt;Me \&amp; you\&lt;/a\&gt;"

**findAndReplace**	
>i.e. findAndReplace("abc", "b", String::toUpperCase) -> aBc  
Note: you can use Mather.replaceAll() instead

**isAbsoluteURL**	
>i.e. https://google.com -> true  
>i.e. ftp://www.myserver.net -> true  
>i.e. /foo/bar -> false

**isAnagram**
>return same with anagram  
>i.e. 'hello world' and 'world hello' is true  
>i.e. 'hello world' and 'hlleo wlrod' is true  
>i.e. 'hello world' and 'hlo wlro eld' is true  
>i.e. 'abcd' and 'cdba' is true  

**isNullOrEmpty**
> isNullOrEmpty

**ISO88591ToUTF8**
>i.e. ISO88591ToUTF8("Ã¼zÃ¼m baÄlarÄ±") -> üzüm bağları

**mask**
>i.e. mask(abcdefghijk, [4], \*) -> \*\*\*\*\*\*\*hijk  
>i.e. mask(abcdefghijk, [3], $) -> $$$$$$$$ijk  
>i.e. mask(abcdefghijk, [-4], \*) -> abcd\*\*\*\*\*\*\*  
>i.e. mask(abcdefghijk, [3, -4], \*) -> abcd\*\*\*\*ijk  
>i.e. mask(abcdefghijk, [-4, 3], \*) -> abcd\*\*\*\*ijk  
>  
>i.e. mask(abcdefghijk, 0, 6, \*) -> \*\*\*\*\*\*\*hijk  
>i.e. mask(abcdefghijk, 0, 7, $) -> $$$$$$$$ijk  
>i.e. mask(abcdefghijk, 4, 7, \*) -> abcd\*\*\*\*ijk  

**randomString**
>generate random string of length or char pool given.  
>i.e. randomString(6, false) -> skduxn  
>i.e. randomString(6, true) -> ngt$k#

**removeNonASCII**	
>i.e. removeNonASCII("äÄçÇéÉêlorem-ipsumöÖÐþúÚ") -> lorem-ipsum

**reverse**	
>reverse a string  
>i.e. reverse("abcd") -> dcba

**sort**	
>i.e. sort("abdc") -> abcd

**splitCamelCase**
>i.e. helloWorld -> hello world (if separator is ' ')  
>i.e. helloWorld -> hello-world (if separator is '-')  
>i.e. helloWorld -> hello_world (if separator is '_')

**stripHTMLTags**
>i.e. stripHTMLTags("\<p>\<em>lorem\</em> \<strong>ipsum\</strong>\</p>") -> lorem ipsum

**toCamelCase**
>i.e. toCamelCase("some_database_field_name") -> someDatabaseFieldName  
>i.e. toCamelCase("Some label that needs to be camelized") -> someLabelThatNeedsToBeCamelized  
>i.e. toCamelCase("some-javascript-property") -> someJavascriptProperty  
>i.e. toCamelCase("some-mixed_string with spaces_underscores-and-hyphens") -> someMixedStringWithSpacesUnderscoresAndHyphens

**truncate**
>i.e. truncate("abc", 4) -> abc  
>i.e. truncate("abcd", 4) -> abcd  
>i.e. truncate("abcde", 4) -> a...  
>i.e. truncate("abcdef", 4) -> a...

**ulrJoin**	
>i.e. ulrJoin("http://www.wanaright.com", "a", "b/cd", "?foo=123", "?goo=foo") -> http://www.wanaright.com/a/b/cd?foo=123&goo=foo

**unescapeHTML**
>i.e. unescapeHTML("\&lt;a href=\&quot;#\&quot;\&gt;Me \&amp; you\&lt;/a\&gt;") -> \<a href="#">Me & you\</a>(https://github.com/30-seconds/30-seconds-of-code)

## CSArray
TBC...
