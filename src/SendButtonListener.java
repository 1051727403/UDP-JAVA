import javax.accessibility.Accessible;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

public class SendButtonListener implements ActionListener {
    private JFrame frame;
    private JTextArea showText;
    private JTextArea sendText;
    private DatagramSocket socket;
    SendButtonListener(DatagramSocket socket, JFrame frame,JTextArea textArea,JTextArea textSend){
        this.sendText=textSend;
        this.socket=socket;
        this.showText=textArea;
        this.frame=frame;

    }
    public void actionPerformed(ActionEvent e) {
        try {
            DatagramSocket socket=this.socket;
            JFrame frame=this.frame;
            JTextArea showText=this.showText;
            JTextArea sendText=this.sendText;
            //发送信息到服务器并显示在上方框中
            String info=sendText.getText();
            String[] check=info.split(":");
            if(check.length!=2){
                showText.append("输入格式错误，请重新输入！\r\n");
            }else {
                byte[] buffer = info.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), 8888);
                socket.send(packet);
                showText.append("发送:" + info + "\r\n");
                //清空发送框
                sendText.setText("");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
