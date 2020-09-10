package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private final String IP_ADDRESS = "localhost";
    private final int PORT= 8189;
    @FXML
    public TextArea textArea;
    @FXML
    public TextField textField;

    private Socket socket;
    DataInputStream inputStream;
    DataOutputStream outputStream;

    @Override
    public void initialize(URL location, ResourceBundle resources) { //метод запустится когда все компоненты прогрузятся на клиентской стороне
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());  //поток для чтения
            DataOutputStream  outputStream = new DataOutputStream(socket.getOutputStream()); //поток для отправки

            new Thread(new Runnable() {     //запускаем потоком, чтоб не заблочился граф. интерфейс (кнопки)
                @Override
                public void run() {
                    try {
                        while (true) {  //цикл, чтобы клиент не отключался
                            String str = inputStream.readUTF();

                            if (str.equals("/close")) { //отслеживаем выход клиента
                                System.out.println("Клиент отключился");
                                break;
                            } else {        //печатаем сообщения
                                System.out.println("Сервер : " + str);
                                textArea.appendText(str);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(ActionEvent actionEvent) {
        try {
            outputStream.writeUTF(textField.getText());
            textField.clear();
            textField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
