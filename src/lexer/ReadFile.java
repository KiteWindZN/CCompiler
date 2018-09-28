package lexer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ReadFile {
	int batchSize=0;
	String fileName="";
	RandomAccessFile raf=null;
	ReadFile(String file,int size) throws IOException{
		batchSize=size;
		fileName=file;
		raf=new RandomAccessFile(fileName,"r");
		long fileLength=raf.length();
		int beginIndex=0;
		raf.seek(beginIndex);
	}
	
	public byte[] getNextBatch() throws IOException{
		byte[] batch=new byte[batchSize];
		if(raf.read(batch)!=-1){
			//for(int i=0;i<batchSize;i++)
			//System.out.print((char)batch[i]);
			return batch;
		}
		return null;
	}
	
	public static void main(String[] args) throws IOException{
		ReadFile myObj=new ReadFile("program.c",100);
		byte[] batch=myObj.getNextBatch();
		batch=myObj.getNextBatch();
		System.out.println("success");
	}
}
