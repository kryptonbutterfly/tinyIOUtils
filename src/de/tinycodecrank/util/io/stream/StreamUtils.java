package de.tinycodecrank.util.io.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.IntConsumer;

import de.tinycodecrank.math.utils.limit.OutOfBoundsException;

public class StreamUtils
{
	/**
	 * Transfers all the available data from the InutStream to the OutputStream
	 * @param	iStream	the stream to read from
	 * @param	oStream	the stream to write to
	 * @throws	IOException
	 * @see		java.io.InputStream#read(byte [], int, int)
	 * @see		java.io.OutputStream#write(byte[], int, int)
	 */
	public static final void pipe(InputStream iStream, OutputStream oStream) throws IOException
	{
		byte[] buffer = new byte[1024];
		int read = -1;
		while((read = iStream.read(buffer)) != -1)
		{
			oStream.write(buffer, 0, read);
		}
	}
	
	/**
	 * Transfers the specified amount of bytes from the InputStream to the OutputStream
	 * @param	iStream the stream to read from
	 * @param	oStream the stream to write to
	 * @param	size the amount of bytes to transfer
	 * @throws	IOException
	 * @see		java.io.InputStream#read(byte [], int, int)
	 * @see		java.io.OutputStream#write(byte[], int, int)
	 */
	public static final void pipe(InputStream iStream, OutputStream oStream, long size) throws IOException
	{
		pipe(iStream, oStream, size, _percent -> {});
	}
	
	/**
	 * Transfers the specified amount of bytes from the InputStream to the OutputStream
	 * @param	iStream the stream to read from
	 * @param	oStream the stream to write to
	 * @param	size the amount of bytes to transfer
	 * @param	progressListener a listener that gets called frequently with the progress in %
	 * @throws	IOException
	 * @see		java.io.InputStream#read(byte [], int, int)
	 * @see		java.io.OutputStream#write(byte[], int, int)
	 */
	public static final void pipe(InputStream iStream, OutputStream oStream, long size, IntConsumer progressListener) throws IOException
	{
		byte[] buffer = new byte[1024];
		int read = -1;
		long count = 0;
		long left = size - count;
		while(count < size && (read = iStream.read(buffer, 0, (int)Math.min(1024, left))) != -1)
		{
			oStream.write(buffer, 0, read);
			count += read;
			left = size - count;
			progressListener.accept((int)(count * 100 / size));
		}
		progressListener.accept(100);
	}

	/**
	 * @param	iStream	The stream to read from
	 * @param	numBytes	The number of bytes to read from the stream
	 * @return	a byte[] containing the read bytes
	 * @throws	IOException
	 * @throws	OutOfBoundsException if the end of the stream was reached before the specified amount of bytes was read
	 * @see		java.io.InputStream#read(byte[], int, int)
	 */
	public static byte[] read(InputStream iStream, int numBytes) throws IOException
	{
		byte[] result = new byte[numBytes];
		int read = 0;
		int position = 0;
		while(position < numBytes)
		{
			read = iStream.read(result, position, numBytes-position);
			if(read == -1)
			{
				final String tmp = "tried to read %d bytes, but the stream only contained %d bytes!";
				throw new OutOfBoundsException(String.format(tmp, numBytes, position));
			}
			else
			{
				position += read;
			}
		}
		return result;
	}
}