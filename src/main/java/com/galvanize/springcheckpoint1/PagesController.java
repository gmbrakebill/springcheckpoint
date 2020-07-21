package com.galvanize.springcheckpoint1;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class PagesController {
@GetMapping("/camelize")
public String camelize(
        @RequestParam(value = "original", required = true) String original,
        @RequestParam(value = "badWord", required = false) boolean initialCap
)
{
    StringBuilder sb = new StringBuilder(original);
    String firstLetterCap = original.substring(0,1).toUpperCase() + original.substring(1);
        for(int i = 0; i < sb.length(); i++)
        {
            if(sb.charAt(i)== '_')
            {
                sb.deleteCharAt(i);
                sb.replace(i,i+1,String.valueOf(Character.toUpperCase(sb.charAt(i))));
            }
        }
        if(initialCap == true) {
            return "" + firstLetterCap.charAt(0) + sb.deleteCharAt(0);
        }
        else
        {
            return "" + sb;
        }
}
    @GetMapping("/redact")
    public String redact(@RequestParam(name="original", required=true) String original,
                         @RequestParam(name="badWord", required=true) String[] badWords)
    {
        String returnValue = original;
        for (String word : badWords) {
            int length = word.length();
            String replace = "";
            for (int i=0;i<length;i++)
                replace += "*";
            returnValue = returnValue.replaceAll(word, replace) ;
        }
        return returnValue;
    }
    @PostMapping("/encode")
    public String encode(@RequestParam(name="message", required=true) String message,
                         @RequestParam(name="key", required=true) String key)
    {
        Map<String, String> ring = new HashMap<>();
        String realAlpha = "abcdefghijklmnopqrstuvwxyz";
        String[] codes = key.split("");
        for (int i=0;i<codes.length;i++) {
            ring.put(String.valueOf(realAlpha.toCharArray()[i]), String.valueOf(codes[i]));
        }
        StringBuilder ret = new StringBuilder();
        char[] letters = message.toCharArray();
        for (int i=0;i<letters.length;i++) {
            if (letters[i] == ' ') {
                ret.append(' ');
                continue;
            }

            ret.append(ring.get(String.valueOf(letters[i])));
        }

        return ret.toString();
    }
    @PostMapping("/s/{find}/{replacement}")
    public String sed(@RequestBody(required=false) String body,
                      @PathVariable(name="find", required=true) String find,
                      @PathVariable(name="replacement", required=true) String replace)
    {
        if (body == null)
            return "";
        return body.replaceAll(find, replace);
    }

}


