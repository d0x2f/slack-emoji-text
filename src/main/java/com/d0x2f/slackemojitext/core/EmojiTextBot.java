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
public class EmojiTextBot extends Bot {

    /**
     * Slack token from application.properties file. You can get your slack token
     * next <a href="https://my.slack.com/services/new/bot">creating a new bot</a>.
     */
    @Value("${slackBotToken}")
    private String slackToken;

    @Override
    public String getSlackToken() {
        return slackToken;
    }

    @Override
    public Bot getSlackBot() {
        return this;
    }

    @Controller(events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE}, pattern = "([a-zA-Z ]{1,}) (:[a-zA-Z_]{1,}:) (:[a-zA-Z_]{1,}:)")
    public void onReceiveDM(WebSocketSession session, Event event, Matcher matcher) {
        reply(session, event, new Message(generateEmojiText(matcher.group(1), matcher.group(2), matcher.group(3))));
    }
    
    private String generateEmojiText(String text, String emote1, String emote2) {
        HashMap<Character, String> letters = new HashMap<Character, String>();

        text = text.trim();
        emote1 = emote1.trim();
        emote2 = emote2.trim();

        letters.put(' ', "00\n00\n00\n00\n00");
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
        letters.put('N', "1001\n1101\n1101\n1011\n101");
        letters.put('O', "111\n101\n101\n101\n111");
        letters.put('P', "111\n101\n111\n100\n100");
        letters.put('Q', "111\n101\n101\n111\n111");
        letters.put('R', "111\n101\n110\n101\n101");
        letters.put('S', "111\n100\n111\n001\n111");
        letters.put('T', "111\n010\n010\n010\n010");
        letters.put('U', "101\n101\n101\n101\n111");
        letters.put('V', "101\n101\n101\n111\n010");
        letters.put('M', "10001\n10001\n10101\n11011\n10001");
        letters.put('X', "101\n101\n010\n101\n101");
        letters.put('Y', "101\n101\n010\n010\n010");
        letters.put('Z', "111\n001\n010\n100\n111");
        
        List<String> characters = new ArrayList<String>();
        
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i);
            if (!letters.containsKey(Character.toUpperCase(c))) {
                continue;
            }
            
            String template = letters.get(Character.toUpperCase(c));
            template = template.replaceAll("1", emote1).replaceAll("0", emote2);
            
            characters.add(template);
        }
        
        String lines[] = new String[7];
        lines[0] = emote2;
        lines[1] = emote2;
        lines[2] = emote2;
        lines[3] = emote2;
        lines[4] = emote2;
        lines[5] = emote2;
        lines[6] = emote2;
        
        for (String character : characters) {
            String[] char_lines = character.split("\n");

            lines[0] += emote2 + emote2 + emote2 + emote2;
            lines[6] += emote2 + emote2 + emote2 + emote2;
            for (int i=0; i<5; i++) {
                lines[i+1] += char_lines[i] + emote2;
            }
        }
        
        String output = "";
        
        for (int i=0; i<7; i++) {
            output += lines[i] + "\n";
        }
        
        return output;
    }
}