

import java.util.*;

public class Robot {
    protected static String factory;//机器人型号

    protected static HashMap<String,Float>jointAngle=new HashMap<>();//关节角度列表

    protected static ArrayList<HashMap<String,Float>> phaseList=new ArrayList<>();//动作帧顺序列表

    protected static String[] jointNameList;//关节名称列表

    protected static Vector2D ballPos;

    protected static Vector2D robotPos;

    protected static boolean recordSwitch = false;

    protected static HashMap<String,String> Perceptor_Effector=new HashMap<>();

//    protected static String LToePitch = "lle7";
//
//    protected static String LFootRoll = "lle6";
//
//    protected static String LFootPitch = "lle5";
//
//    protected static String LKneePitch = "lle4";
//
//    protected static String LHipPitch = "lle3";
//
//    protected static String LHipRoll = "lle2";
//
//    protected static String LHipYawPitch = "lle1";
//
//    protected static String RToePitch = "rle7";
//
//    protected static String RFootRoll = "rle6";
//
//    protected static String RFootPitch = "rle5";
//
//    protected static String RKneePitch = "rle4";
//
//    protected static String RHipPitch = "rle3";
//
//    protected static String RHipRoll = "rle2";
//
//    protected static String RHipYawPitch = "rle1";
//
//    protected static String LShoulderPitch = "lae1";
//
//    protected static String LShoulderYaw = "lae2";
//
//    protected static String LArmRoll = "lae3";
//
//    protected static String LArmYaw = "lae4";
//
//    protected static String RShoulderPitch = "rae1";
//
//    protected static String RShoulderYaw = "rae2";
//
//    protected static String RArmRoll = "rae3";
//
//    protected static String RArmYaw = "rae4";
//
//    protected static String NeckYaw = "he1";
//
//    protected static String NeckPitch = "he2";

    public Robot(String _factory){
        this(_factory,new HashMap<>());
    }

    public Robot(String _factory,Map<String,Float>_jointAngle){
        factory=_factory;
        jointNameList= new String[]{"he1", "he2",
                "lae1","lae2","lae3","lae4","rae1","rae2","rae3","rae4",
                "lle1","lle2","lle3","lle4","lle5","lle6","lle7",
                "rle1","rle2","rle3","rle4","rle5","rle6","rle7",};


    }

    public static void updateJointAngle(HashMap<String,Float> _jointAngle){
        //TODO:时间刷新之后机器人角度更新
        jointAngle.putAll(_jointAngle);
        if(recordSwitch)phaseList.add(_jointAngle);
    }

    public static void updateBallPos(Vector2D _ballPos){
        ballPos=_ballPos;
    }

    public static void updataRobotPos(Vector2D _robotPos){
        robotPos=_robotPos;
    }

    public static void setRecordSwitch(boolean state){
        recordSwitch=state;
    }

    protected static String createMovementAsAIUTMotionEditor(){
        if(phaseList.isEmpty())return "";
        String[] AIUTJointList={"laj1","laj2","laj3","laj4","llj1","llj2","llj3","llj4","llj5",
                "llj6","rlj1","rlj2","rlj3","rlj4","rlj5","rlj6","raj1","raj2","raj3","raj4","llj7","rlj7"};
        String mvt = null;
        for(HashMap<String,Float> phase:phaseList){
            mvt += "1 0 0";
            for(String joint:AIUTJointList){
                Float value = phase.get(joint);
                if(value==null)value= Float.valueOf(0);
                mvt += " " + value;
            }
            mvt = mvt + "\n";
        }
        phaseList.clear();
        return mvt;
    }
//    MAP_PERCEPTOR_TO_INDEX = {"hj1":0,  "hj2":1,  "llj1":2, "rlj1":3,
//            "llj2":4, "rlj2":5, "llj3":6, "rlj3":7,
//            "llj4":8, "rlj4":9, "llj5":10,"rlj5":11,
//            "llj6":12,"rlj6":13,"laj1":14,"raj1":15,
//            "laj2":16,"raj2":17,"laj3":18,"raj3":19,
//            "laj4":20,"raj4":21,"llj7":22,"rlj7":23 }
//
//    # Fix symmetry issues 1a/4 (identification)
//    FIX_PERCEPTOR_SET = {'rlj2','rlj6','raj2','laj3','laj4'}
    public static boolean containsStr(String s,String[] s_l){
        for (int i=0;i<s_l.length;i++){
            if(s.equals(s_l[i]))return true;
        }
        return false;
    }
    public static String createMovementAsDreamWingMotionEditor() {
        if(phaseList.isEmpty())return "";
        String[] FcpJointList={"llj1","rlj1","llj2","rlj2","llj3","rlj3","llj4","rlj4","llj5","rlj5","llj6",
                "rlj6","laj1","raj1","laj2","raj2","laj3","raj3","laj4","raj4","llj7","rlj7"};
        String[] FIX_PERCEPTOR_SET = {"rlj2","rlj6","raj2","laj3","laj4"};
        String mvt = null;
        for(HashMap<String,Float> phase:phaseList){
            mvt += "0.02";
            for(String joint:FcpJointList){
                Float value = phase.get(joint);
                if(value==null)value= Float.valueOf(0);
                if(containsStr(joint,FIX_PERCEPTOR_SET)){
                    value=-value;
                }
                mvt += " " + value;
            }
            mvt = mvt + "\n";
        }
        phaseList.clear();
        return mvt;
    }

    protected static String getEffectorName(String perceptor){
        return Perceptor_Effector.getOrDefault(perceptor, "none");
    }

    protected static void setPerceptor_Effector(){
        Perceptor_Effector.put("laj1","EFF_LA1");
        Perceptor_Effector.put("laj2","EFF_LA2");
        Perceptor_Effector.put("laj3","EFF_LA3");
        Perceptor_Effector.put("laj4","EFF_LA4");
        Perceptor_Effector.put("llj1","EFF_LL1");
        Perceptor_Effector.put("llj2","EFF_LL2");
        Perceptor_Effector.put("llj3","EFF_LL3");
        Perceptor_Effector.put("llj4","EFF_LL4");
        Perceptor_Effector.put("llj5","EFF_LL5");
        Perceptor_Effector.put("llj6","EFF_LL6");
        Perceptor_Effector.put("llj7","EFF_LL7");
        Perceptor_Effector.put("raj1","EFF_RA1");
        Perceptor_Effector.put("raj2","EFF_RA2");
        Perceptor_Effector.put("raj3","EFF_RA3");
        Perceptor_Effector.put("raj4","EFF_RA4");
        Perceptor_Effector.put("rlj1","EFF_RL1");
        Perceptor_Effector.put("rlj2","EFF_RL2");
        Perceptor_Effector.put("rlj3","EFF_RL3");
        Perceptor_Effector.put("rlj4","EFF_RL4");
        Perceptor_Effector.put("rlj5","EFF_RL5");
        Perceptor_Effector.put("rlj6","EFF_RL6");
        Perceptor_Effector.put("rlj7","EFF_RL7");
    }

}
