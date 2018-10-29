package code.fly.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class CFFileReader {
    private static final long FILE_MILESTONE_SIZE = 512 * 1024 * 1024; // 512 MB
    private static final int FILE_BUFFER_SIZE = 1024 * 64; // 64bit system

    private CFFileReader() {
    }

    public static String readFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return readFile(file);
        } else {
            throw new FileNotFoundException("file [" + filePath + "] is not exist or not a file!");
        }
    }

    public static String readFile(File file) throws IOException {
        long fileSize = file.length();

        try {
            if (fileSize < FILE_MILESTONE_SIZE) { //  below 512M use FileStream
                return readByBufferedFileInputStream(file);
            } else { // above 512M use FileStreamChannel
                return readByNIOFileChannel(file);
            }
        } catch (OutOfMemoryError error) { // if out of memory, try use mapped FileStreamChannel
            System.out.println("meet OutOfMemoryError, try use mapping memory read file");
            return readByMappedFileChannel(file);
        }
    }

    private static String readByMappedFileChannel(File file) throws IOException {
        System.out.println("read file with FileInputStream mapped byte buffer channel");

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            FileChannel fileChannel = fileInputStream.getChannel();
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, fileChannel.size());

            int limit;
            int position;

            StringBuilder content = new StringBuilder();
            while (true) {
                limit = mappedByteBuffer.limit();
                position = mappedByteBuffer.position();
                if (limit == position) {
                    break;
                } else if (limit - position > FILE_BUFFER_SIZE) {
                    byte[] destination = new byte[FILE_BUFFER_SIZE];
                    mappedByteBuffer.get(destination);
                    content.append(new String(destination, 0, FILE_BUFFER_SIZE));
                } else {
                    byte[] destination = new byte[limit - position];
                    mappedByteBuffer.get(destination);
                    content.append(new String(destination, 0, limit - position));
                }
            }

            return content.toString();
        }
    }

    private static String readByNIOFileChannel(File file) throws IOException {
        System.out.println("read file with FileInputStream channel");

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            ByteBuffer buffer = ByteBuffer.allocate(FILE_BUFFER_SIZE);
            FileChannel fileChannel = fileInputStream.getChannel();
            int count;

            StringBuilder content = new StringBuilder();
            while ((count = fileChannel.read(buffer)) != -1) {
                content.append(new String(buffer.array(), 0, count));
            }

            return content.toString();
        }
    }

    private static String readByBufferedFileInputStream(File file) throws IOException {
        System.out.println("read file with bufferedFileInputStream");

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buffer = new byte[FILE_BUFFER_SIZE];
            int count;

            StringBuilder content = new StringBuilder();
            while ((count = bufferedInputStream.read(buffer)) != -1) {
                content.append(new String(buffer, 0, count));
            }

            return content.toString();
        }
    }
}
