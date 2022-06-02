package ro.ase.acs.client;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Scanner;

public class Client {
	private static DataOutputStream dataOutputStream = null;
	private static DataInputStream dataInputStream = null;
	
	public static void main(String[] args) {
		try(Socket socket = new Socket("localhost", 5000)) {
			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			
			Scanner scanner = new Scanner(System.in);
			while (true) {
				String command = scanner.nextLine();
				if(command.split(" ")[0].equals("delete")) {
					sendDelete(command.split(" ")[1]);
				} else if (command.split(" ")[0].equals("add")) {
					sendFile("./src/ro/ase/acs/client/" + command.split(" ")[1]);
				} else if (command == null || "exit".equals(command)) {
					break;
				}
			}
			
			dataInputStream.close();
			dataOutputStream.close();
			System.out.println("Se incarca fisierul...");
			Thread.sleep(500);
			System.out.println("Loading: 25%");
			Thread.sleep(500);
			System.out.println("Loading: 50%");
			Thread.sleep(500);
			System.out.println("Loading: 75%");
			Thread.sleep(500);
			System.out.println("Loading: 99%");
			Thread.sleep(500);
			System.out.println("ERROR: 404");
			Thread.sleep(3000);
			System.out.println("Fisierul a fost incarcat.");
		} catch(Exception e) {
			System.out.println("N-avem");
		}
	}
	
	private static void sendFile(String path) throws Exception {
		int bytes = 0;
		File file = new File(path);
		FileInputStream fileInputStream = new FileInputStream(file);
		
		//get file name
		String[] datas = path.split("/");
		String filename = datas[datas.length - 1];
		
		dataOutputStream.writeUTF("add " + filename);
		
		//send file size
		dataOutputStream.writeLong(file.length());
		
		dataOutputStream.writeUTF(filename);
		
		//break file into chunks
		byte[] buffer = new byte[4*1024];
		
		while((bytes = fileInputStream.read(buffer)) != -1) {
			dataOutputStream.write(buffer, 0, bytes);
			dataOutputStream.flush();
		}
		
		fileInputStream.close();
	}
	
	private static void sendDelete(String name) throws IOException {
		int bytes = 0;
		dataOutputStream.writeUTF("delete " + name);
		dataOutputStream.flush();
	}
}
