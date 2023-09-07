import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class UDP_SEVER {
    private JTextArea textArea;
    private ArrayList<Person> acceptPort= new ArrayList<>();
    public void showMenu(){
        JFrame frame = new JFrame("UDP_SEVER");
        frame.setLocation(100,100);
        frame.setSize(750,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //退出方式
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);     //居中显示
        frame.setLayout(null);  //设置窗体布局为绝对布局
        //创建一个服务器基础信息展示框
        JLabel basicInfo=new JLabel("                     服务器IP地址：127.0.0.1    服务器端口号：8888");
        basicInfo.setBounds(20,10,420,40);
        basicInfo.setOpaque(true);
        basicInfo.setBackground(Color.white);
        frame.add(basicInfo);
        //创建一个服务器信息显示标签
        JLabel message=new JLabel("服务器信息显示框：\n");
        message.setBounds(20,50,200,30);
        frame.add(message);
        //创建一个文本框存放消息
        JTextArea textArea=new JTextArea(20,30);
        textArea.setBounds(20,80,420,450);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(false);
        frame.add(textArea);
        this.textArea=textArea;
        //组件可视化
        frame.setVisible(true);
    }
    //获取合法的端口信息
    public void getAcceptPort() throws IOException {
        String path=UDP_SEVER.class.getClassLoader().getResource("").getPath()+"text.txt";
        File myFile = new File(path);
        if (myFile.isFile() && myFile.exists()) {
            InputStreamReader Reader = new InputStreamReader(new FileInputStream(myFile), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(Reader);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                String []str=lineTxt.split(" ");
                int port=Integer.parseInt(str[1]);
                Person person=new Person(str[0],port);
                this.acceptPort.add(person);
                System.out.println(str[0]+"  "+port);
            }

            Reader.close();
        }
    }
    public static void main(String[] args) throws IOException {
        UDP_SEVER sever=new UDP_SEVER();
        sever.showMenu();
        DatagramSocket socket = new DatagramSocket(8888);
        //2.创建一个数据包对象接收数据
        byte[] buf = new byte[1024 * 64];
        DatagramPacket packet = new DatagramPacket(buf,buf.length);
        //获取正确端口信息
        sever.getAcceptPort();
        while (true) {
            //3.等待接收数据
            socket.receive(packet);
            //4.取出数据
            int len = packet.getLength();
            String rs = new String(buf,0,len);
            String ip=packet.getAddress().getHostAddress();
            int port=packet.getPort();
            System.out.println(packet.getAddress().getHostAddress());
            boolean loginSuccess=false;
            //与库中用户信息进行对比
            for(Person person:sever.acceptPort){
                if(ip.equals(person.ip)&&port==person.port){
                    loginSuccess=true;
                    break;
                }
            }
            System.out.println(packet.getAddress());
            System.out.println(packet.getPort());
            if(loginSuccess) {
                String[] info=rs.split(":");
                String goalport=info[0];
                String trueinfo=info[1];
                System.out.println("发送端口："+goalport+"发送数据:"+trueinfo);
                sever.textArea.append("【服务器】：收到了ip为：" + ip + " 端口号为：" + port + "  的消息：" + rs + "\r\n");
                System.out.println("【服务器】：收到了ip为：" + ip + " 端口号为：" + port + "  的消息：" + rs);
                //发送给指定端口用户
                //发送信息到指定端口并显示在上方框中
                System.out.println(InetAddress.getLocalHost()+"\n");
                byte[] buffer = (port+":"+trueinfo).getBytes();
                DatagramPacket pack=new DatagramPacket(buffer,buffer.length, InetAddress.getLocalHost(),Integer.parseInt(goalport));
                socket.send(pack);
                System.out.println("【服务器】："+port+" 发送 "+goalport+" 消息:"+rs+"\r\n");
                sever.textArea.append("【服务器】："+port+" 发送 "+goalport+" 消息:"+rs+"\r\n");
            }else{
                sever.textArea.append("【服务器】：拒绝接收ip为：" + ip + " 端口号为：" + port + "  的消息\r\n");
                System.out.println("【服务器】：拒绝接收ip为：" + ip + " 端口号为：" +port + "  的消息" );
            }
        }

    }
}
