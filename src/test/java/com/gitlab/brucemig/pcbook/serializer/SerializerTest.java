package com.gitlab.brucemig.pcbook.serializer;

import com.gitlab.brucemig.pcbook.pb.Laptop;
import com.gitlab.brucemig.pcbook.sample.Generator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SerializerTest {
    @Test
    public void writeAndReadBinaryFile() throws IOException {
        String binaryFile = "laptop.bin";
        Laptop laptop1 = new Generator().NewLaptop();

        Serializer serializer = new Serializer();
        serializer.WriteBinaryFile(laptop1, binaryFile);

        Laptop laptop2 = serializer.ReadBinaryFile(binaryFile);
        assertEquals(laptop1, laptop2);
    }
}