package de.tinycodecrank.util.io.stream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

import de.tinycodecrank.monads.OptInt;
import de.tinycodecrank.monads.opt.Opt;

public class DataStreamUtils
{
	/**
//	 * @deprecated use {@link DataStreamUtils#writeOptUTF(DataOutputStream, Opt)} instead!
	 * @param oStream
	 * @param string
	 * @throws IOException
	 */
	public static void writeNullableUTF(DataOutputStream oStream, String string) throws IOException
	{
		oStream.writeBoolean(Objects.isNull(string));
		Opt.of(string)
			.if_Throws(s -> oStream.writeUTF(s));
	}
	
	/**
//	 * @deprecated use {@link DataStreamUtils#readOptUTF(DataInputStream)} instead!
	 * @param iStream
	 * @return
	 * @throws IOException
	 */
	public static String readNullableUTF(DataInputStream iStream) throws IOException
	{
		if(!iStream.readBoolean())
		{
			return iStream.readUTF();
		}
		else
		{
			return null;
		}
	}
	
	public static void writeOptUTF(DataOutputStream oStream, Opt<String> string) throws IOException
	{
		oStream.writeBoolean(string.isPresent());
		string.if_Throws(oStream::writeUTF);
	}
	
	public static Opt<String> readOptUTF(DataInputStream iStream) throws IOException
	{
		if(iStream.readBoolean())
		{
			return Opt.of(iStream.readUTF());
		}
		else
		{
			return Opt.empty();
		}
	}
	
	public static void writeOptInt(DataOutputStream oStream, OptInt value) throws IOException
	{
		oStream.writeBoolean(value.isPresent());
		value.if_Throws(oStream::writeInt);
	}
	
	public static OptInt readOptInt(DataInputStream iStream) throws IOException
	{
		if(iStream.readBoolean())
		{
			return OptInt.of(iStream.readInt());
		}
		else
		{
			return OptInt.empty();
		}
	}
	
	public static void writeBytes(DataOutputStream oStream, byte[] value) throws IOException
	{
		oStream.writeInt(value.length);
		oStream.write(value);
	}
	
	public static byte[] readBytes(DataInputStream iStream) throws IOException
	{
		int length = iStream.readInt();
		byte[] result = new byte[length];
		int read = 0;
		while(read < length)
		{
			int tmp = iStream.read(result, read, length - read);
			if(tmp != -1)
			{
				read += tmp;
			}
			else
			{
				throw new ArrayIndexOutOfBoundsException(tmp);
			}
		}
		return result;
	}
}