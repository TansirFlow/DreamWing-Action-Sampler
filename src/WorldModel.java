import java.util.HashMap;

public class WorldModel {
    protected static float gameTime;

    protected static Vector2D ballPos;

    protected static Vector2D robotPos;

    public static void updateGameTime(float _gameTime){
        gameTime=_gameTime;
    }

    public static void updateRobot(HashMap<String,Float> jointAngleList){
        Robot.updateJointAngle(jointAngleList);
    }


    protected static void updateWorldModel(String msg){
        HashMap<String,Float> dataList = AnalyzeMessage.getDataList(msg);
        float time = dataList.get("time");
        if(time>gameTime){
            gameTime=time;
            updateGameTime(gameTime);
            updateRobot(dataList);
        }
    }
}
