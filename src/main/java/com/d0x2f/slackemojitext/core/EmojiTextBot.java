package com.d0x2f.slackemojitext.core;

import me.ramswaroop.botkit.slackbot.core.Bot;
import me.ramswaroop.botkit.slackbot.core.Controller;
import me.ramswaroop.botkit.slackbot.core.EventType;
import me.ramswaroop.botkit.slackbot.core.models.Event;
import me.ramswaroop.botkit.slackbot.core.models.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

@Component
public class EmojiTextBot extends Bot
{
    @Value("${slackBotToken}")
    private String slackToken;
    
    HashMap<Character, String> letters = new HashMap<Character, String>();
    
    public EmojiTextBot ()
    {
        super();
        
        letters.put(' ', "0\n0\n0\n0\n0");
        letters.put('A', "111\n101\n111\n101\n101");
        letters.put('B', "111\n101\n110\n101\n111");
        letters.put('C', "111\n100\n100\n100\n111");
        letters.put('D', "110\n101\n101\n101\n110");
        letters.put('E', "111\n100\n111\n100\n111");
        letters.put('F', "111\n100\n111\n100\n100");
        letters.put('G', "111\n100\n100\n101\n111");
        letters.put('H', "101\n101\n111\n101\n101");
        letters.put('I', "111\n010\n010\n010\n111");
        letters.put('J', "01\n01\n01\n01\n11");
        letters.put('K', "101\n101\n110\n101\n101");
        letters.put('L', "100\n100\n100\n100\n111");
        letters.put('M', "10001\n11011\n10101\n10001\n10001");
        letters.put('N', "1001\n1101\n1111\n1011\n1001");
        letters.put('O', "111\n101\n101\n101\n111");
        letters.put('P', "111\n101\n111\n100\n100");
        letters.put('Q', "111\n101\n101\n111\n111");
        letters.put('R', "111\n101\n110\n101\n101");
        letters.put('S', "111\n100\n111\n001\n111");
        letters.put('T', "111\n010\n010\n010\n010");
        letters.put('U', "101\n101\n101\n101\n111");
        letters.put('V', "101\n101\n101\n111\n010");
        letters.put('W', "10001\n10001\n10101\n11011\n10001");
        letters.put('X', "101\n101\n010\n101\n101");
        letters.put('Y', "101\n101\n010\n010\n010");
        letters.put('Z', "111\n001\n010\n100\n111");
    }

    @Override
    public String getSlackToken()
    {
        return slackToken;
    }

    @Override
    public Bot getSlackBot()
    {
        return this;
    }

    @Controller(events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE}, pattern = "(.{1,}) (:[a-zA-Z_]{1,}:) (:[a-zA-Z_]{1,}:)")
    public void onReceiveDM(WebSocketSession session, Event event, Matcher matcher)
    {
    	//Remove the initial @emojibot: or <@SOMEID>: from the DM string
    	String message = matcher.group(1).replaceFirst("<?@[a-zA-Z0-9-<_>:]+", "");
        reply(session, event, new Message(generateEmojiText(message, matcher.group(2), matcher.group(3))));
    }
    
    /**
     * Create a multi-line string that reads 'text' using the given emojis.
     * 
     * @param text
     * @param emoji1
     * @param emoji2
     * @return String
     */
    private String generateEmojiText(String text, String emoji1, String emoji2)
    {
        text = text.trim();
        emoji1 = emoji1.trim();
        emoji2 = emoji2.trim();
        
        //Organise into lines
        String lines[] = new String[6];
        lines[0] = emoji2;
        lines[1] = emoji2;
        lines[2] = emoji2;
        lines[3] = emoji2;
        lines[4] = emoji2;
        lines[5] = emoji2;
        
        //For each character, find it's template, fill it with emojis and concatenate the parts onto each line.
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (!this.letters.containsKey(Character.toUpperCase(c))) {
                continue;
            }
            
            //Fill the emojis into the character template
            String character = this.letters.get(Character.toUpperCase(c));
            String[] char_lines = character.split("\n");
            int length = char_lines[0].length();

            //add parts to lines
            lines[0] += new String(new char[length+1]).replace("\0", emoji2);
            
            for (int j=0; j<5; j++) {
                lines[j+1] += char_lines[j].replaceAll("1", emoji1).replaceAll("0", emoji2) + emoji2;
            }
        }
        
        return String.join("\n", lines) + "\n" + lines[0];
    }
}