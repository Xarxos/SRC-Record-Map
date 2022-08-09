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

    private User runner = new User("-1");
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
    }

    public void printAll(String prefixTabs) {
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
        System.out.println("");
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

    public void storeTime(double seconds) {
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
            valueThis = 100000d;
        }

        if(((Run)compRun).verified) {
            if(((Run)compRun).timingMethod != null) {
                methodComp = ((Run)compRun).timingMethod.ordinal() + 1;
            }
            valueComp = (methodComp + ((Run)compRun).pureTime);
        }
        else {
            valueComp = 100000d;
        }

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
        String[] timeString = new String[4];
        if(time[0]+time[1]+time[2]+time[3] == 0) {
            timeString[0] = "";
            timeString[1] = "";
            timeString[2] = "N/A";
            return timeString;
        }
        if (timingMethod == TimingMethod.RTA_NS5 || timingMethod == TimingMethod.RTA_WS5) {
            timeString[0] = time[0] + " hours";
            timeString[1] = time[1] + " minutes";
            timeString[2] = time[2] + " seconds";

            if (time[3] > 0) {
                timeString[3] = time[3] + " millis";
            }

            return timeString;
        }
        else if (timingMethod == TimingMethod.IGT || timingMethod == TimingMethod.IGT_WSS) {
            timeString[0] = time[0] + " years";
            timeString[1] = time[1] + " months";
            timeString[2] = time[2] + " days";

            return timeString;
        }
        else {
            return null;
        }
    }

    public Map<String, String> getVariableValues() {
        return variableValues;
    }

    public boolean isVerified() {
        return verified;
    }

    public void addVariableValue(String varId, String valId) {
        variableValues.put(varId, valId);
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRunner(User runner) {
        this.runner = runner;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setVerifier(User verifier) {
        this.verifier = verifier;
    }

    public void setTimingMethod(TimingMethod timingMethod) {
        this.timingMethod = timingMethod;
    }
}

