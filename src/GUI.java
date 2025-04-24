import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class GUI {
    public static void start() {
        JFrame frame = new JFrame("SS3D动作帧采样工具");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel stateLabel = new JLabel("当前状态：未在采集");
        stateLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        stateLabel.setBounds(50, 50, 250, 30);

        JPanel panel = new JPanel();
        panel.setBounds(50, 125, 500, 150);
        ButtonGroup group = new ButtonGroup();
        JRadioButton radioButton1 = new JRadioButton("生成AIUT动作编辑器格式");
        radioButton1.setFont(new Font("微软雅黑", Font.BOLD, 24));
        group.add(radioButton1);
        JRadioButton radioButton2 = new JRadioButton("生成DreamWing动作编辑器格式");
        radioButton2.setFont(new Font("微软雅黑", Font.BOLD, 24));
        group.add(radioButton2);
        panel.add(radioButton2);
        radioButton2.setSelected(true);
        panel.add(radioButton1);
        panel.add(radioButton2);
        JButton button = new JButton("开始采集");
        button.setFont(new Font("微软雅黑", Font.BOLD, 24));
        button.setBounds(350, 25, 200, 80);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                recordControl(button,radioButton1,radioButton2,stateLabel);
            }
        });

        frame.add(panel);
        frame.add(stateLabel);
        frame.add(button);
        frame.setLayout(null);
        frame.setVisible(true);

    }

    protected static void recordControl(JButton button, JRadioButton radioButton1,JRadioButton radioButton2, JLabel stateLabel){
        //TODO:动作采集控制按钮
        if(Objects.equals(button.getText(), "开始采集")){
            Robot.setRecordSwitch(true);
            button.setText("停止采集");
            stateLabel.setText("当前状态：正在采集");
        }else{
            Robot.setRecordSwitch(false);
            String mvt = "";
            if(radioButton1.isSelected()){
                mvt=Robot.createMovementAsAIUTMotionEditor();
            }else{
                mvt = Robot.createMovementAsDreamWingMotionEditor();
            }
            writeToFile(mvt);
            button.setText("开始采集");
            stateLabel.setText("当前状态：未在采集");
        }
    }

    protected static void writeToFile(String content){
        Date currentTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(currentTime);
        String fileName = timestamp + ".txt";
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(content);
            writer.close();
            System.out.println("已成功写入文件。");
        } catch (IOException e) {
            System.out.println("写入文件时出错：" + e.getMessage());
        }
    }
}
