package com.mindlinksoft.recruitment.mychat;

import com.google.gson.*;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.time.Instant;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link ConversationExporter}.
 */
public class ConversationExporterTests {



    @Test(expected = IllegalArgumentException.class)
    public void testBadInputFileArgument() throws Exception{
        ConversationExporter exporter = new ConversationExporter();
        String[] args = new String[]{"Nonexistent.txt","output.json"};
        ConversationExporterConfiguration configuration = new CommandLineArgumentParser().parseCommandLineArguments(args);
        exporter.exportConversation(configuration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyConversation() throws Exception{
        ConversationExporter exporter = new ConversationExporter();
        String[] args = new String[]{"EmptyConversation.txt","Empty.json"};
        ConversationExporterConfiguration configuration = new CommandLineArgumentParser().parseCommandLineArguments(args);
        exporter.exportConversation(configuration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadTimestamp() throws Exception{
        ConversationExporter exporter = new ConversationExporter();
        String[] args = new String[]{"BadTimeStamp.txt","BadTimestamp.json"};
        ConversationExporterConfiguration configuration = new CommandLineArgumentParser().parseCommandLineArguments(args);
        exporter.exportConversation(configuration);
    }


    /**
     * Tests that exporting a conversation will export the conversation correctly.
     * @throws Exception When something bad happens.
     */
    @Test
    public void testExportingConversationExportsConversation() throws Exception {
        ConversationExporter exporter = new ConversationExporter();
        String[] args = new String[]{"chat.txt", "chat.json"};
        ConversationExporterConfiguration configuration = new CommandLineArgumentParser().parseCommandLineArguments(args);
        exporter.exportConversation(configuration);

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Instant.class, new InstantDeserializer());

        Gson g = builder.create();

        Conversation c = g.fromJson(new InputStreamReader(new FileInputStream("chat.json")), Conversation.class);

        assertEquals("My Conversation", c.name);

        assertEquals(7, c.messages.size());

        Message[] ms = new Message[c.messages.size()];
        c.messages.toArray(ms);

        assertEquals(ms[0].timestamp, Instant.ofEpochSecond(1448470901));
        assertEquals(ms[0].senderId, "bob");
        assertEquals(ms[0].content, "Hello there!");

        assertEquals(ms[1].timestamp, Instant.ofEpochSecond(1448470905));
        assertEquals(ms[1].senderId, "mike");
        assertEquals(ms[1].content, "how are you?");

        assertEquals(ms[2].timestamp, Instant.ofEpochSecond(1448470906));
        assertEquals(ms[2].senderId, "bob");
        assertEquals(ms[2].content, "I'm good thanks, do you like pie?");

        assertEquals(ms[3].timestamp, Instant.ofEpochSecond(1448470910));
        assertEquals(ms[3].senderId, "mike");
        assertEquals(ms[3].content, "no, let me ask Angus...");

        assertEquals(ms[4].timestamp, Instant.ofEpochSecond(1448470912));
        assertEquals(ms[4].senderId, "angus");
        assertEquals(ms[4].content, "Hell yes! Are we buying some pie?");

        assertEquals(ms[5].timestamp, Instant.ofEpochSecond(1448470914));
        assertEquals(ms[5].senderId, "bob");
        assertEquals(ms[5].content, "No, just want to know if there's anybody else in the pie society...");

        assertEquals(ms[6].timestamp, Instant.ofEpochSecond(1448470915));
        assertEquals(ms[6].senderId, "angus");
        assertEquals(ms[6].content, "YES! I'm the head pie eater there...");
    }


}
