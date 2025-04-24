/**
 * @author Tansor
 * @time 2023-08-20
 */

import java.util.HashMap;
import java.util.Objects;

public class Main {
    protected static HashMap<String,String> argsList=new HashMap<>();

    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("--")) {
                String[] parts = arg.split("=");
                String key = parts[0].substring(2);
                String value = parts.length > 1 ? parts[1] : "";
                argsList.put(key, value);
            }
        }

        if(argsList.containsKey("help")){
            System.out.println("\n\t\t\tSS3D动作采集器\n" +
                    "==============================================\n" +
                    "||  参数说明:                             \t||\n" +
                    "||  --agentPort:   agent启动脚本中设置的port\t||\n" +
                    "||  --serverPort:  rcssserver监听的port   \t||\n" +
                    "||  --serverIP:    rcssserver的IP        \t||\n" +
                    "||------------------------------------------||\n" +
                    "=============================================="
            );
            return;
        }

        int agentPort=3101;
        int serverPort=3100;
        String serverIP="localhost";
        if(argsList.containsKey("agentPort")){
            String value=argsList.get("agentPort");
            if(!Objects.equals(value, "")){
                agentPort=Integer.parseInt(value);
            }
        }
        if(argsList.containsKey("serverPort")){
            String value=argsList.get("serverPort");
            if(!Objects.equals(value, "")){
                serverPort=Integer.parseInt(value);
            }
        }
        if(argsList.containsKey("serverIP")){
            String value=argsList.get("serverIP");
            if(!Objects.equals(value, "")){
                serverIP=value;
            }
        }
        start(agentPort,serverPort,serverIP);
    }

    protected static void start(int agentPort,int serverPort,String serverIP){
        System.out.println("as proxy between agent("+ agentPort +") and server(" + serverIP+":"+serverPort+")");
        Thread GUIThread = new Thread(GUI::start);
        Thread proxyThread = new Thread(() -> {
            Proxy.startProxy(agentPort,serverPort,serverIP);
        });
        GUIThread.start();
        proxyThread.start();
    }

}
