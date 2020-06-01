package me.ryan.netty.example;

import io.netty.buffer.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ByteProcessor;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

public class ByteBufExample {

    private final static Random random = new Random();
    private static final ByteBuf BYTE_BUF_FROM_SOMEWHERE = Unpooled.buffer(1024);
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();
    private static final ChannelHandlerContext CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE = null;

    public static void heapBuffer() {
        ByteBuf heapBuf = BYTE_BUF_FROM_SOMEWHERE;
        if (heapBuf.hasArray()) {
            byte[] array = heapBuf.array();
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            int length = heapBuf.readableBytes();
            handleArray(array, offset, length);
        }
    }

    public static void directBuffer() {
        ByteBuf directBuf = BYTE_BUF_FROM_SOMEWHERE;
        if (!directBuf.hasArray()) {
            int length = directBuf.readableBytes();
            byte[] array = new byte[length];
            directBuf.getBytes(directBuf.readerIndex(), array);
            handleArray(array, 0, length);
        }
    }

    public static void byteBufferComposite(ByteBuffer header, ByteBuffer body) {
        ByteBuffer[] message = new ByteBuffer[] {header, body};

        ByteBuffer message2 = ByteBuffer.allocate(header.remaining() + body.remaining());
        message2.put(header);
        message2.put(body);
        message2.flip();
    }

    public static void byteBufComposite() {
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf = BYTE_BUF_FROM_SOMEWHERE;
        ByteBuf bodyBuf = BYTE_BUF_FROM_SOMEWHERE;
        messageBuf.addComponents(headerBuf, bodyBuf);

        messageBuf.removeComponent(0);
        for (ByteBuf buf : messageBuf) {
            System.out.println(buf.toString());
        }
    }

    public static void byteBufRelativeAccess() {
        ByteBuf buf = BYTE_BUF_FROM_SOMEWHERE;
        for (int i = 0; i < buf.capacity(); i++) {
            byte b = buf.getByte(i);
            System.out.println((char)b);
        }
    }

    public static void readAllData() {
        ByteBuf buf = BYTE_BUF_FROM_SOMEWHERE;
        while (buf.isReadable()) {
            System.out.println(buf.readByte());
        }
    }

    public static void write() {
        ByteBuf buf = BYTE_BUF_FROM_SOMEWHERE;
        while (buf.writableBytes() > 4) {
            buf.writeInt(random.nextInt());
        }
    }

    public static void byteProcessor() {
        ByteBuf buf = BYTE_BUF_FROM_SOMEWHERE;
        int index = buf.forEachByte(ByteProcessor.FIND_CR);
    }

    public static void byteBufProcess() {
        ByteBuf buf = BYTE_BUF_FROM_SOMEWHERE;
        int index = buf.forEachByte(ByteBufProcessor.FIND_CR);
    }

    public static void byteBufSlice() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        ByteBuf sliced = buf.slice(0, 15);
        System.out.println(sliced.toString(utf8));
        buf.setByte(0, (byte)'J');
        System.out.println(buf.getByte(0));
        System.out.println(sliced.getByte(0));
        assert buf.getByte(0) == sliced.getByte(0);
    }

    public static void byteBufCopy() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        ByteBuf copied = buf.copy(0, 15);
        System.out.println(copied.toString(utf8));
        buf.setByte(0, (byte)'J');
        System.out.println(buf.getByte(0));
        System.out.println(copied.getByte(0));
        assert buf.getByte(0) != copied.getByte(0);
    }

    public static void byteBufSetGet() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        System.out.println((char)buf.getByte(0));
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        buf.setByte(0, (byte)'B');
        System.out.println((char)buf.getByte(0));
        assert readerIndex == buf.readerIndex();
        assert writerIndex == buf.writerIndex();
    }

    public static void byteBufWriteRead() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        System.out.println((char)buf.readByte());
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        buf.writeByte((byte)'?');
        assert readerIndex == buf.readerIndex();
        assert writerIndex != buf.writerIndex();
    }

    private static void handleArray(byte[] array, int offset, int length) { }

    public static void obtainingByteBufAllocatorReference() {
        Channel channel = CHANNEL_FROM_SOMEWHERE;
        ByteBufAllocator allocator = channel.alloc();

        ChannelHandlerContext ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE;
        ByteBufAllocator allocator1 = ctx.alloc();
    }

    public static void referenceCounting() {
        Channel channel = CHANNEL_FROM_SOMEWHERE;
        ByteBufAllocator allocator = channel.alloc();

        ByteBuf buffer = allocator.directBuffer();
        assert buffer.refCnt() == 1;
    }

    public static void releaseReferenceCountedObject() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        boolean released = buffer.release();
    }

    public static void main(String[] args) {
        ByteBufExample.byteBufSlice();
        ByteBufExample.byteBufCopy();
    }
}
