import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoParser {
    private final String EU4BasePath = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Europa Universalis IV\\";

    public InfoParser() {

    }

    public HashMap<String, Achievement> parseAchievements() throws IOException {
        HashMap<String, Achievement> achievements = new HashMap<>();

        File gameFile = new File(EU4BasePath + "common/achievements.txt");
        File localisationFile = new File(EU4BasePath + "localisation/achievements_l_english.yml");
        File iconFolder = new File(EU4BasePath + "gfx/interface/achievements");

        BufferedReader reader = null;
        BufferedReader reader2 = null;

        try {
            reader = new BufferedReader(new FileReader(gameFile));
            reader2 = new BufferedReader(new FileReader(localisationFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Pattern achievement = Pattern.compile("^achievement_.*(?= =)");
        Pattern localisation = Pattern.compile("NEW_ACHIEVEMENT[\\d_]*\\d");
        Pattern name = Pattern.compile("(?<=NAME:0 \")(.*)(?=\")");
        //System.out.println(name.pattern());
        Pattern desc = Pattern.compile("(?<=DESC:0 \")(.*)(?=\")");

        String readLine = null;
        ArrayList<String> achievementLines = new ArrayList<>();
        ArrayList<String> localisationLines = new ArrayList<>();

        while((readLine = reader.readLine()) != null) {
            Matcher ma = achievement.matcher(readLine);
            Matcher ml = localisation.matcher(readLine);

            if(ma.find()) {
                achievementLines.add(ma.group().toLowerCase());
            }
            if(ml.find()) {
                localisationLines.add(ml.group());
            }
        }

        for(String achievementLine : achievementLines) {
            achievements.put(achievementLine, new Achievement(achievementLine));
        }

        for(File iconFile : iconFolder.listFiles()) {
            String fileName = iconFile.getName();
            //System.out.println(fileName + " / " + fileName.substring(0, fileName.length() - 4));
            achievements.get(fileName.substring(0, fileName.length() - 4).toLowerCase()).setIcon(iconFile.getPath());
        }

        while((readLine = reader2.readLine()) != null) {
            Matcher ml = localisation.matcher(readLine);
            Matcher mln = name.matcher(readLine);
            Matcher mld = desc.matcher(readLine);

            if(ml.find()) {
                //System.out.println(ml.group() + " - " + localisationLines.indexOf(ml.group()));
                Achievement a = achievements.get(achievementLines.get(localisationLines.indexOf(ml.group())));
                //System.out.println(a.getId());

                if(mln.find()) {
                    //System.out.println("mlnfind: " + mln.group());
                    a.setName(mln.group());
                }
                if(mld.find()) {
                    //System.out.println("mldfind: " +  mld.group());
                    a.setDesc(mld.group());
                }
            }
        }

        return achievements;
    }
}
