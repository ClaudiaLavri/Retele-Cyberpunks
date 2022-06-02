package ro.ase.acs.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {
	private static DataOutputStream dataOutputStream = null;
	private static DataInputStream dataInputStream = null;

	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(5000)) {
			System.out.println("Listening to port: 5000");

			System.out.println("FISIERE: ");
			File file = new File("./src/ro/ase/acs/server");
			for (String fileNames : file.list())
				System.out.println(fileNames);

			Socket clientSocket = serverSocket.accept();
			System.out.println(clientSocket + " connected.");
			dataInputStream = new DataInputStream(clientSocket.getInputStream());
			dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

			String command = dataInputStream.readUTF();
			String[] s = command.split(" ");
			if (s[0].equals("delete")) {
				String name = s[1];
				file = new File("./src/ro/ase/acs/server/" + name);
				file.delete();
			} else {
				receiveFile("./src/ro/ase/acs/server/");
			}

			System.out.println("FISIERE: ");
			file = new File("./src/ro/ase/acs/server");
			for (String fileNames : file.list())
				System.out.println(fileNames);

			dataInputStream.close();
			dataOutputStream.close();
			clientSocket.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private static void receiveFile(String fileName) throws Exception {
		int bytes = 0;

		long size = dataInputStream.readLong();

		String filename = dataInputStream.readUTF();
		String f = filename.replaceAll("\\s+", "");

		FileOutputStream fileOutputStream = new FileOutputStream(fileName + f);

		byte[] buffer = new byte[4 * 1024];
		while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
			fileOutputStream.write(buffer, 0, bytes);
			size -= bytes;

		}
		fileOutputStream.close();
	}
}
