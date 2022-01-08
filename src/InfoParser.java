import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoParser {
    private final String EU4BasePath = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Europa Universalis IV\\";
    private final String[] nationTags = {
        "AKS", "ALG", "ADU", "AOT", "ARM", "HAB", "BAV", "BHA", "BUK", "BUL", "BYZ", "CRO", "DAI", "DAL", "DLH", "ENG",
        "ETH", "FRA", "FKN", "GEO", "GLH", "HAU", "UHW", "HIN", "HLR", "ICE", "ILK", "INC", "ISR", "JAP", "KOJ", "KIT",
        "KON", "LXA", "LFA", "LOT", "ZAF", "MSA", "MAL", "MCH", "MAY", "MGE", "MOR", "MSI", "MUG", "NPL", "NBI", "ORI",
        "PER", "POL", "POM", "QNG", "ROM", "RMN", "RZI", "RUM", "SAX", "SCO", "SST", "SIA", "SIL", "SOK", "SOM", "SWI",
        "MAM", "TIB", "TIM", "TRP", "TUN", "VIT", "YEM", "YUA", "GZI"
    };

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

    public Map<String, Nation> parseNations() throws IOException {
        Map<String, Nation> nations = new HashMap<>();

        File tagFile = new File(EU4BasePath + "common/country_tags/00_countries.txt");
        File countryFolder = new File(EU4BasePath + "common/countries");

        for(String tag : nationTags) {
            nations.put(tag, new Nation(tag));
        }

        BufferedReader reader = null;


        try {
            reader = new BufferedReader(new FileReader(tagFile));
            //reader2 = new BufferedReader(new FileReader(localisationFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Pattern tag = Pattern.compile("[A-Z]{3}");
        //System.out.println(tag.pattern());
        Pattern name = Pattern.compile("(?<=countries/)(.*)(?=\\.txt)");
        Pattern countryFile = Pattern.compile("(?<=countries/)(.*)(?=\")");

        Pattern color = Pattern.compile("(?<=color = \\{ )(.*)(?= \\})");

        Map<String, String> countryFiles = new HashMap<>();

        String readLine = null;

        while((readLine = reader.readLine()) != null) {
            Matcher mtag = tag.matcher(readLine);
            Matcher mname = name.matcher(readLine);
            Matcher mfile = countryFile.matcher(readLine);
            //System.out.println(readLine);
            /*
            if(mtag.find()) {
                System.out.println("mtag: " + mtag.group());
            }
            if(mname.find()) {
                //System.out.println("mname: " + mname.group());
            }
            if(mfile.find()) {
                //System.out.println("mfile: " + mfile.group());
            }

             */
            //System.out.println("mtag: " + mtag.find() + ", mname: " + mname.find() + ", mfile: " + mfile.find());

            if(mtag.find() && mname.find() && mfile.find()) {
                //System.out.println("mtag: " + mtag.group() + ", mname: " + mname.group() + ", mfile: " + mfile.group());
                if(nations.containsKey(mtag.group())) {
                    nations.get(mtag.group()).setName(mname.group());
                    countryFiles.put(mfile.group(), mtag.group());
                }
            }
        }

        for(File file : countryFolder.listFiles()) {
            String fileName = file.getName();
            if(nations.containsKey(countryFiles.get(fileName))) {
                BufferedReader reader2 = null;

                try {
                    reader2 = new BufferedReader(new FileReader(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                while((readLine = reader2.readLine()) != null) {
                    Matcher mcolor = color.matcher(readLine);

                    if(mcolor.find()) {
                        String[] rgbWhite = mcolor.group().split(" ");
                        String[] rgb = new String[3];
                        int i = 0;
                        for(String c : rgbWhite) {
                            //System.out.println(c);
                            if(!c.isEmpty()) {
                                rgb[i] = c;
                                i++;
                            }
                        }
                        //System.out.println(fileName + " - " + countryFiles.get(fileName));

                        nations.get(countryFiles.get(fileName)).setDataColor(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
                        break;
                    }
                }
            }
        }

        return nations;
    }

    public void parseNation(String tag) {

    }
}
