import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Run implements Comparable {
    enum TimingMethod {
        RTA_NS5,
        RTA_WS5,
        IGT,
        IGT_WSS
    }

    private Database database;

    private String id;
    private Category category;
    private Game game;

    private String comment;

    private User runner;
    private boolean verified = false;
    private User verifier;
    private double pureTime;
    private int[] time = new int[4];



    private TimingMethod timingMethod;
    private Map<String, String> variableValues = new HashMap<>();

    public Run(String id, Database database) {
        this.id = id;
        this.database = database;
    }

    //public void storeData(ArrayList<String> cleanedDataArray) {}

    public void storeData(ArrayList<String> cleanedDataArray) {
        int variableStartIndex = 0;

        for (int i = 0; i < cleanedDataArray.size() - 2; i++) {
            if (cleanedDataArray.get(i).compareTo("game") == 0) {
                this.game = database.getGame(cleanedDataArray.get(i+1));
            }
            else if (cleanedDataArray.get(i).compareTo("level") == 0) {
                if (cleanedDataArray.get(i+1).compareTo("null") != 0) {
                    this.category = database.getCategory(cleanedDataArray.get(i+1));
                }
            }
            else if (cleanedDataArray.get(i).compareTo("category") == 0) {
                if(cleanedDataArray.get(i+1).compareTo("jdrogxld") == 0) {
                    this.timingMethod = TimingMethod.IGT;
                }
                else if(cleanedDataArray.get(i+1).compareTo("q25e0qg2") == 0) {
                    this.timingMethod = TimingMethod.RTA_NS5;
                }
                else if(cleanedDataArray.get(i+1).compareTo("rkl3oqwk") == 0) {
                    this.timingMethod = TimingMethod.IGT_WSS;
                }
                else {
                    this.category = database.getCategory(cleanedDataArray.get(i+1));
                }
            }
            else if (cleanedDataArray.get(i).compareTo("comment") == 0) {
                this.comment = cleanedDataArray.get(i+1);
            }
            else if (cleanedDataArray.get(i).compareTo("verified") == 0) {
                this.verified = true;
            }
            else if (cleanedDataArray.get(i).compareTo("examiner") == 0 && cleanedDataArray.get(i-1).compareTo("rel") != 0) {
                database.addUser(cleanedDataArray.get(i+1));
                this.verifier = database.getUser(cleanedDataArray.get(i+1));
            }
            else if (cleanedDataArray.get(i).compareTo("players") == 0) {
                database.addUser(cleanedDataArray.get(i+4));
                this.runner = database.getUser(cleanedDataArray.get(i+4));
            }
            else if (cleanedDataArray.get(i).compareTo("primary_t") == 0) {
                String cleaned = cleanedDataArray.get(i+1).replaceAll(":", "");
                this.pureTime = Double.parseDouble(cleaned) / 3600000;
                storeTime(Double.parseDouble(cleaned));
            }
            else if (cleanedDataArray.get(i).compareTo("values") == 0) {
                variableStartIndex = i;
                //System.out.println("StartSet: " + variableStartIndex);
            }
            else if (cleanedDataArray.get(i).compareTo("links") == 0
                        && cleanedDataArray.get(i+2).compareTo("self") == 0) {
                //System.out.println("Run: " + runner + "/" + time[0] + "h / " + time[1] + "m / " + time[2] + "s / " + time[3] + "ms");
                //System.out.println("Start: " + variableStartIndex);
                for (int j = i - 2; j > variableStartIndex; j -= 2) {
                    /*
                    System.out.println("j: " + j);
                    if (cleanedDataArray.get(j).compareTo("size") == 0) {
                        for (String str : cleanedDataArray) {
                            System.out.println(str);
                        }
                    }

                     */
                    database.addVariable(cleanedDataArray.get(j));
                    variableValues.put(cleanedDataArray.get(j), cleanedDataArray.get(j+1));

                    checkTimingMethod(cleanedDataArray.get(j), cleanedDataArray.get(j+1));
                }
            }
        }
    }

    private void checkTimingMethod(String varId, String valueId) {
        String valueLabel = database.getVariable(varId).getValue(valueId).getLabel();
        if (valueLabel.contains("NS5")) {
            this.timingMethod = TimingMethod.RTA_NS5;
        }
        else if (valueLabel.contains("RTA")) {
            this.timingMethod = TimingMethod.RTA_WS5;
        }
        else if (valueLabel.contains("No")) {
            this.timingMethod = TimingMethod.IGT;
        }
        else if (valueLabel.contains("IGT")){
            this.timingMethod = TimingMethod.IGT_WSS;
        }
        /*
        else if (valueLabel.contains("null")) {
            this.timingMethod = TimingMethod.RTA_WS5;
        }

         */
    }

    public void printAll(int numPrefixTabs) {
        String prefixTabs = "";
        for (int i = 0; i < numPrefixTabs; i++) {
            prefixTabs += "\t";
        }

        System.out.println(prefixTabs + "id: " + id);
        System.out.println(prefixTabs + "runner: " + runner.getName());
        printTime(prefixTabs);
        System.out.print(prefixTabs + "verified: ");
        if (verified) {
            System.out.println("true");
            System.out.println(prefixTabs + "verifier: " + verifier.getName());
        }
        else {
            System.out.println("false");
        }
        for (String varId : variableValues.keySet()) {
            Variable var = database.getVariable(varId);
            System.out.println(prefixTabs + var.getName() + ": " + var.getValue(variableValues.get(varId)).getLabel());
        }
    }

    public void printTime(String prefixTabs) {
        if (timingMethod == TimingMethod.RTA_NS5) {
            System.out.println(prefixTabs + "timingMethod: RTA NS5");
            System.out.println(prefixTabs + "time: " + time[0] + "h / " + time[1] + "m / " + time[2] + "s / " + time[3] + "ms");
        }
        else if (timingMethod == TimingMethod.RTA_WS5) {
            System.out.println(prefixTabs + "timingMethod: RTA W/S5");
            System.out.println(prefixTabs + "time: " + time[0] + "h / " + time[1] + "m / " + time[2] + "s / " + time[3] + "ms");
        }
        else if (timingMethod == TimingMethod.IGT) {
            System.out.println(prefixTabs + "timingMethod: IGT No Save Scum");
            System.out.println(prefixTabs + "time: " + time[0] + "y / " + time[1] + "m / " + time[2] + "d / ");
        }
        else if (timingMethod == TimingMethod.IGT_WSS) {
            System.out.println(prefixTabs + "timingMethod: IGT W/ Save Scum");
            System.out.println(prefixTabs + "time: " + time[0] + "y / " + time[1] + "m / " + time[2] + "d / ");
        }
    }

    public void printRunner() {

    }

    private void storeTime(double seconds) {
        int intSeconds = (int)seconds;
        seconds -= (double)intSeconds;

        time[0] = intSeconds / 3600;
        intSeconds %= 3600;
        time[1] = intSeconds / 60;
        intSeconds %= 60;
        time[2] = intSeconds;

        seconds *= 1000;
        intSeconds = (int)seconds;
        time[3] = intSeconds;
    }

    @Override
    public int compareTo(Object compRun) {
        //System.out.println("runner: " + runner.getName());
        //printTime("\t");
        //System.out.println("time: " + time[0] + "h / " + time[1] + "m / " + time[2] + "s / " + time[3] + "ms");
        int methodThis = 0;
        int methodComp = 0;
        double valueThis;
        double valueComp;

        if(this.verified) {
            if(this.timingMethod != null) {
                methodThis = this.timingMethod.ordinal() + 1;
            }
            valueThis = (methodThis + this.pureTime);
        }
        else {
            valueThis = Double.MAX_VALUE;
        }

        if(((Run)compRun).verified) {
            if(((Run)compRun).timingMethod != null) {
                methodComp = ((Run)compRun).timingMethod.ordinal() + 1;
            }
            valueComp = (methodComp + ((Run)compRun).pureTime);
        }
        else {
            valueComp = Double.MAX_VALUE;
        }

        //System.out.println("\t" + (methodThis + this.pureTime));

        double timeComp = valueThis - valueComp;
        return timeComp < 0 ? -1 : 1;
    }

    public TimingMethod getTimingMethod() {
        return timingMethod;
    }

    public User getRunner() {
        return runner;
    }

    public int[] getTime() {
        return time;
    }

    public String[] getTimeString() {
        if (timingMethod == TimingMethod.RTA_NS5 || timingMethod == TimingMethod.RTA_WS5) {
            String[] timeString = new String[4];
            timeString[0] = time[0] + " hours";
            timeString[1] = time[1] + " minutes";
            timeString[2] = time[2] + " seconds";

            if (time[3] > 0) {
                timeString[3] = time[3] + " milliseconds";
            }

            return timeString;
            //return time[0] + " hours\n " + time[1] + " minutes \n " + time[2] + " seconds" + ms;
        }
        else if (timingMethod == TimingMethod.IGT || timingMethod == TimingMethod.IGT_WSS) {
            String[] timeString = new String[4];
            timeString[0] = time[0] + " years";
            timeString[1] = time[1] + " months";
            timeString[2] = time[2] + " days";

            return timeString;
            //return time[0] + " years\n " + time[1] + "months\n " + time[2] + "days";
        }
        else {
            return null;
        }
    }
}

