package com.fitness.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageTest {

    @TempDir
    Path tempDir;   // JUnit will create a temp directory

    @BeforeEach
    void setup() {
        // point storage to the temp dir so tests don't affect real files
        FileStorage.setBaseDirForTests(tempDir);
    }

    @Test
    void saveAndReadProfilePic_roundTrip() throws Exception {
        String userId = "user123";
        byte[] bytes = new byte[]{10, 20, 30, 40};

        try (InputStream in = new ByteArrayInputStream(bytes)) {
            FileStorage.saveProfilePic(userId, in);
        }

        assertTrue(FileStorage.existsProfilePic(userId), "Profile pic should exist after save");

        try (InputStream in = FileStorage.readProfilePic(userId)) {
            assertNotNull(in, "InputStream should not be null");
            byte[] loaded = in.readAllBytes();
            assertArrayEquals(bytes, loaded, "Saved and loaded bytes should match");
        }
    }

    @Test
    void existsProfilePic_falseWhenNoFile() throws Exception {
        assertFalse(FileStorage.existsProfilePic("no-such-user"));
    }
}
