import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalyzeMessage {
    public static HashMap<String,Float> getDataList(String msg) {
        // 提取关节名字和关节角度的键值对
        Pattern jointPattern = Pattern.compile("HJ \\(n (\\w+)\\) \\(ax ([-+]?\\d+\\.\\d+)\\)");
        Matcher jointMatcher = jointPattern.matcher(msg);
        HashMap<String, Float> jointMap = new HashMap<>();
        while (jointMatcher.find()) {
            String jointName = jointMatcher.group(1);
            float jointAngle = Float.parseFloat(jointMatcher.group(2));
            jointMap.put(jointName, jointAngle);
        }

        // 提取时间
        Pattern timePattern = Pattern.compile("\\(t (\\d+\\.\\d+)\\)");
        Matcher timeMatcher = timePattern.matcher(msg);
        float time = 0;
        if (timeMatcher.find()) {
            time = Float.parseFloat(timeMatcher.group(1));
        }
        jointMap.put("time",time);

        return jointMap;
    }
}
