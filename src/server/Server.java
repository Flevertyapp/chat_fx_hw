package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static int PORT = 8189;

    public static void main(String[] args) {
        ServerSocket server = null;
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);

        try {
            server = new ServerSocket(PORT);
            System.out.println("Сервер запущен");
            socket = server.accept();  //сохраняется клиентский сокет. Здесь поток останавливается и ждет подключение!
            System.out.println("Подключился клиент: " + socket.getRemoteSocketAddress()); //адрес подключенного клиента

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());  //поток для чтения
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream()); //поток для отправки

            //поток на чтение

            while (true) {  //цикл, чтобы сервак не отключался
                String str = inputStream.readUTF();
                if (str.equals("/close")) { //отслеживаем выход клиента
                    System.out.println("Клиент отключился");
                    break;
                } else {        //печатаем сообщения
                    System.out.println("Сервер: " + str);
                    outputStream.writeUTF(str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}


