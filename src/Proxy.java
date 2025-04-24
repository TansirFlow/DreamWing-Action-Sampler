import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class Proxy {
    public static void startProxy(int agentPort,int serverPort,String serverIP){

        try {
            // 创建agent的ServerSocket
            ServerSocket agentSocket = new ServerSocket(agentPort);
            System.out.println("wait for agent...");

            // 接受agent的连接请求
            Socket agentClient = agentSocket.accept();
            System.out.println("agent connect success");

            // 创建server的Socket并连接到server
            Socket serverClient = new Socket(serverIP, serverPort);
            System.out.println("connect to server success");

            // 启动agent到server的数据转发线程
            Thread agentToServerThread = new Thread(() -> {
                try {
                    InputStream agentInput = agentClient.getInputStream();
                    OutputStream serverOutput = serverClient.getOutputStream();

                    byte[] buffer = new byte[16384];
                    int bytesRead;
                    while ((bytesRead = agentInput.read(buffer)) != -1) {
                        serverOutput.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            agentToServerThread.start();

            // 启动server到agent的数据转发线程
            Thread serverToAgentThread = new Thread(() -> {
                try {
                    InputStream serverInput = serverClient.getInputStream();
                    OutputStream agentOutput = agentClient.getOutputStream();

                    byte[] buffer = new byte[16384];
                    int bytesRead;
                    while ((bytesRead = serverInput.read(buffer)) != -1) {
                        WorldModel.updateWorldModel(decode(buffer));//更新世界模型
                        // System.out.println(decode(buffer));
                        agentOutput.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            serverToAgentThread.start();

            // 等待两个线程执行结束
            agentToServerThread.join();
            serverToAgentThread.join();

            // 关闭连接
            agentClient.close();
            serverClient.close();
            agentSocket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static String decode(byte[] buffer){
        int length = ((buffer[0] & 0xFF) << 24) |
                ((buffer[1] & 0xFF) << 16) |
                ((buffer[2] & 0xFF) << 8) |
                (buffer[3] & 0xFF);

        byte[] data = new byte[length];
        System.arraycopy(buffer, 4, data, 0, length);
        return new String(data, StandardCharsets.UTF_8);
    }

}
