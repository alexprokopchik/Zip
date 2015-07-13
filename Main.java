package compressedLiterature;

import java.io.*;
import java.util.*;

public class Main {

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		long duration = 0;
		
		FileReader inputStream = null;
		String fileName = "WarAndPeace.txt";
		//String fileName = "Adventuresof HuckleberryFinn.txt";
		FileOutputStream outputStream = new FileOutputStream(new File("compressed.txt"));
		FileOutputStream codeStream = new FileOutputStream(new File("codes.txt"));
		
		try {
				inputStream = new FileReader(fileName);
				int c;
				StringBuilder message = new StringBuilder();

				// read characters from the file into a string
				while ((c = inputStream.read()) != -1) {
					message.append((char)c);
				}
				inputStream.close();

				// pass string to constructor
				// constructor carries out all operations 
				// necessary for creating the encoding
				CodingTree ct = new CodingTree(message.toString());
				// write the code file
				// ct.codes is a Map<Character, String> member of CodingTree
				// consisting of one (char, binary-codeword) pair
				
				codeStream.write(ct.codes.toString().getBytes());
				codeStream.close();
				
							
				StringBuffer buffer = new StringBuffer();
				long asciiCost = message.length()*8;
				long compressedCost = 0;
				
				// add bits to a String until the buffer is larger than 256
				// at which point we output one byte at a time
				for(int i = 0; i < message.length(); i++){
					buffer.append(ct.codes.get(message.charAt(i)));
					if(buffer.length() > 256){
						while(buffer.length() > 8){
							int chr = Integer.parseInt(buffer.substring(0, 8),2);
							outputStream.write(chr);
							buffer.delete(0, 8);
							compressedCost += 8;
						}
					}					
				}
				compressedCost += buffer.length();
				
				// output the last block of bits remaining of size less than 256
				while(buffer.length() > 8){
					int chr = Integer.parseInt(buffer.substring(0, 8),2);
					outputStream.write(chr);
					buffer.delete(0, 8);
				}
				// output last bits
				outputStream.write(Integer.parseInt(buffer.toString(),2));
				outputStream.close();
				
				// calculate time of computation
				duration = System.currentTimeMillis() - start;

				// output stats
				System.out.println("Uncompressed file size: " + asciiCost/8 + " bytes");
				System.out.println("Compressed file size: " + compressedCost/8 + " bytes");
				System.out.println("Compression ratio: " + compressedCost*100/asciiCost + "%");
				System.out.println("Running Time: " + duration + " milliseconds");
		} finally {}
	
	}

}
