import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public class UDP_CLIENT {
    public JTextArea showText;
    public void messageMenu(DatagramSocket socket,int port){
        //跳转到新的页面
        JFrame frame=new JFrame();
        frame.setTitle("Client登陆成功发送信息页面！！");
        frame.setLocation(700,100);
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //退出方式
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);     //居中显示
        frame.setLayout(new FlowLayout(1));  //设置窗体布局为流布局
        frame.setVisible(true);
        //消息接收标签
        JLabel labReceive=new JLabel("发送信息展示及反馈窗口,端口号："+port);
        frame.add(labReceive);
        frame.add(new JLabel("                                                                          " ));
        //消息接收窗口：
        JTextArea textArea=new JTextArea(18,45);
        textArea.setSize(400,380);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(false);
        frame.add(textArea);
        //消息发送标签
        JLabel labSend=new JLabel("信息发送窗口");
        frame.add(labSend);
        frame.add(new JLabel("                                                                                                           " ));
        //消息接收窗口：
        JTextArea textSend=new JTextArea(8,45);
        textSend.setSize(400,180);
        textSend.setLineWrap(true);
        textSend.setWrapStyleWord(true);
        frame.add(textSend);
        //消息发送按钮
        JButton sendMessage=new JButton("向端口号为8888的服务器发送消息");
        SendButtonListener sendButtonListener=new SendButtonListener(socket,frame,textArea,textSend);
        sendMessage.addActionListener(sendButtonListener);
        frame.add(sendMessage);
        //组件可视化
        frame.setVisible(true);
        this.showText=textArea;
    }

    public static void main(String[] args) throws SocketException {
        UDP_CLIENT client=new UDP_CLIENT();
        int port=33333;
        DatagramSocket socket=new DatagramSocket(port);
        socket.setBroadcast(true);
        client.messageMenu(socket,port);
        new Thread(()->{
            try {
                while (true){
                    /*
                     * 接收服务器端响应的数据
                     */
                    byte[] data2 = new byte[1024];//创建字节数组
                    DatagramPacket packet2 = new DatagramPacket(data2, data2.length);// 1.创建数据报，用于接收服务器端响应的数据

                    socket.receive(packet2);// 2.接收服务器响应的数
//                    //4.取出数据
//                    int len = packet2.getLength();
//                    String rs = new String(data2,0,len);
//                    String ip=packet2.getAddress().getHostAddress();
//                    int port=packet2.getPort();
//                    System.out.println(port);
//                    System.out.println(ip);
//                    System.out.println(rs);
//                    System.out.println(packet2.getAddress().getHostAddress());
                    //3.读取数据
                    String reply = new String(data2, 0, packet2.getLength());//创建字符串对象
                    System.out.println("接收:"+reply);
                    client.showText.append(reply+"\r\n");
                    System.out.println(reply);//输出提示信息
                }
            } catch (IOException e) {
            e.printStackTrace();
            }
        }).start();

    }

}
